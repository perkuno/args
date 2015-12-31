package uno.perk.args.apt;

import java.util.Objects;
import java.util.Optional;

import com.sun.source.doctree.AttributeTree;
import com.sun.source.doctree.AuthorTree;
import com.sun.source.doctree.CommentTree;
import com.sun.source.doctree.DeprecatedTree;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocRootTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.DocTreeVisitor;
import com.sun.source.doctree.EndElementTree;
import com.sun.source.doctree.EntityTree;
import com.sun.source.doctree.ErroneousTree;
import com.sun.source.doctree.IdentifierTree;
import com.sun.source.doctree.InheritDocTree;
import com.sun.source.doctree.LinkTree;
import com.sun.source.doctree.LiteralTree;
import com.sun.source.doctree.ParamTree;
import com.sun.source.doctree.ReferenceTree;
import com.sun.source.doctree.ReturnTree;
import com.sun.source.doctree.SeeTree;
import com.sun.source.doctree.SerialDataTree;
import com.sun.source.doctree.SerialFieldTree;
import com.sun.source.doctree.SerialTree;
import com.sun.source.doctree.SinceTree;
import com.sun.source.doctree.StartElementTree;
import com.sun.source.doctree.TextTree;
import com.sun.source.doctree.ThrowsTree;
import com.sun.source.doctree.UnknownBlockTagTree;
import com.sun.source.doctree.UnknownInlineTagTree;
import com.sun.source.doctree.ValueTree;
import com.sun.source.doctree.VersionTree;

import static java.util.Objects.requireNonNull;

/**
 * Gathers javadoc comments as plain text.
 */
class PlainTextCollector implements DocTreeVisitor<Optional<String>, Void> {

  /**
   * Renders a javadoc comment as plain text.
   *
   * @param docCommentTree The javadoc comment tree to convert to plain text.
   * @return The plain text rendering of the given javadoc comment.
   */
  public static String collectText(DocCommentTree docCommentTree) {
    requireNonNull(docCommentTree);

    PlainTextCollector collector = new PlainTextCollector();

    StringBuilder doc = new StringBuilder();
    for (DocTree docTree : docCommentTree.getFirstSentence()) {
      Optional<String> text = collector.collect(docTree);
      if (text.isPresent()) {
        doc.append(text.get());
      }
    }
    if (doc.length() > 0) {
      doc.append('\n');
    }

    if (!docCommentTree.getBody().isEmpty()) {
      doc.append('\n');
    }
    for (DocTree docTree : docCommentTree.getBody()) {
      Optional<String> text = collector.collect(docTree);
      if (text.isPresent()) {
        doc.append(text.get());
      }
    }

    return doc.toString();
  }

  private PlainTextCollector() {
    // Lifecycle is controlled via `collectText`.
  }

  private Optional<String> collect(DocTree docTree) {
    return docTree.accept(this, null);
  }

  @Override
  public Optional<String> visitAttribute(AttributeTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitAuthor(AuthorTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitComment(CommentTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitDeprecated(DeprecatedTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitDocComment(DocCommentTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitDocRoot(DocRootTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitEntity(EntityTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitErroneous(ErroneousTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitIdentifier(IdentifierTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitInheritDoc(InheritDocTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitLink(LinkTree node, Void aVoid) {
    StringBuilder linkText = new StringBuilder();
    for (DocTree docTree : node.getLabel()) {
      Optional<String> text = docTree.accept(this, null);
      if (text.isPresent()) {
        linkText.append(text.get());
      }
    }
    if (linkText.length() > 0) {
      return Optional.of(linkText.toString());
    } else {
      return node.getReference().accept(this, null);
    }
  }

  @Override
  public Optional<String> visitLiteral(LiteralTree node, Void aVoid) {
    return inlineCode(node.getBody().accept(this, null));
  }

  @Override
  public Optional<String> visitParam(ParamTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitReference(ReferenceTree node, Void aVoid) {
    return inlineCode(Optional.of(node.getSignature()));
  }

  @Override
  public Optional<String> visitReturn(ReturnTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitSee(SeeTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitSerial(SerialTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitSerialData(SerialDataTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitSerialField(SerialFieldTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitSince(SinceTree node, Void aVoid) {
    return Optional.empty();
  }

  // TODO(John Sirois): Handle nested lists.

  enum ListType {
    ORDERED, UNORDERED
  }

  private volatile ListType currentListType;
  private int listIndex;

  @Override
  public Optional<String> visitStartElement(StartElementTree node, Void aVoid) {
    switch (node.getName().toString()) {
      case "p":
        return Optional.of("\n");
      case "ol":
        currentListType = ListType.ORDERED;
        listIndex = 0;
        return Optional.empty();
      case "ul":
        currentListType = ListType.UNORDERED;
        System.out.printf("currentListType: %s for %s%n", currentListType, node);
        return Optional.empty();
      case "li":
        if (currentListType == null) {
          throw new IllegalStateException("Encountered a list item outside any list: " + node);
        }
        switch (currentListType) {
          case ORDERED:
            return Optional.of(String.valueOf(++listIndex) + " ");
          case UNORDERED:
            return Optional.of("* ");
          default:
            throw new IllegalStateException(
                "Encountered an unknown list type: " + currentListType);
        }
      default:
        return Optional.empty();
    }
  }

  @Override
  public Optional<String> visitEndElement(EndElementTree node, Void aVoid) {
    switch (node.getName().toString()) {
      case "ol":
        listIndex = 0;
      case "ul":
        currentListType = null;
        System.out.printf("currentListType: %s%n", currentListType);
        break;
    }
    return Optional.empty();
  }

  @Override
  public Optional<String> visitText(TextTree node, Void aVoid) {
    return Optional.of(node.getBody());
  }

  @Override
  public Optional<String> visitThrows(ThrowsTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitUnknownBlockTag(UnknownBlockTagTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitUnknownInlineTag(UnknownInlineTagTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitValue(ValueTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitVersion(VersionTree node, Void aVoid) {
    return Optional.empty();
  }

  @Override
  public Optional<String> visitOther(DocTree node, Void aVoid) {
    return Optional.empty();
  }

  private Optional<String> inlineCode(Optional<String> text) {
    return text.map(s -> String.format("`%s`", s));
  }
}
