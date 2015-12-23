package uno.perk.args.apt;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.TextTree;
import com.sun.source.util.DocTrees;
import com.sun.source.util.SimpleDocTreeVisitor;
import com.sun.source.util.Trees;

import uno.perk.args.Optionable;

public class ArgsProcessor extends AbstractProcessor {

  private Trees trees;
  private DocTrees docTrees;

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.RELEASE_8;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(Optionable.class.getName());
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    trees = Trees.instance(processingEnv);
    docTrees = DocTrees.instance(processingEnv);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element element : roundEnv.getElementsAnnotatedWith(Optionable.class)) {
      if (!(element instanceof TypeElement)) {
        error(element, "Should only find types annotated with @Optionable.");
      }
      process((TypeElement) element);
    }
    return true;
  }

  private void process(TypeElement typeElement) {
    DocCommentTree classDoc = docTrees.getDocCommentTree(trees.getPath(typeElement));
    if (classDoc != null) {
      note(typeElement, "Found class doc for %s: %s",
          typeElement.getQualifiedName(), extractDoc(classDoc));
    }
    for (Element element : typeElement.getEnclosedElements()) {
      if (element instanceof ExecutableElement) {
        DocCommentTree methodDoc = docTrees.getDocCommentTree(trees.getPath(element));
        if (methodDoc != null) {
          note(typeElement, "Found method doc for %s.%s: %s",
              typeElement.getQualifiedName(), element.getSimpleName(), extractDoc(methodDoc));
        }
      }
    }
  }

  private String extractDoc(DocCommentTree docCommentTree) {
    StringBuilder doc = new StringBuilder();
    for (DocTree docTree : docCommentTree.getFirstSentence()) {
      doc.append(docTree.toString());
    }
    if (!docCommentTree.getBody().isEmpty() && doc.length() > 0) {
      doc.append('\n');
      doc.append('\n');
    }
    for (DocTree docTree : docCommentTree.getBody()) {
      doc.append(docTree.toString());
    }
    return doc.toString();
  }

  private void error(Element element, String message, Object... args) {
    log(Diagnostic.Kind.ERROR, element, message, args);
  }

  private void note(Element element, String message, Object... args) {
    log(Diagnostic.Kind.NOTE, element, message, args);
  }

  private void log(Diagnostic.Kind kind, Element element, String message, Object... args) {
    processingEnv.getMessager().printMessage(kind, String.format(message, args), element);
  }
}


