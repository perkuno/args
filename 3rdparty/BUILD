import os

jar_library(
  name='javapoet',
  jars=[
    jar('com.squareup', 'javapoet', '1.4.0'),
  ]
)

# TODO(John Sirois): Add pants support for this.
jar_library(
  name='jdk-tools',
  jars=[
    jar('jdk', 'tools', '1.8',
        url='file:{java_home}/lib/tools.jar'.format(java_home=os.environ['JAVA_HOME'])),
  ]
)

jar_library(
  name='junit',
  jars=[
    jar('junit', 'junit', '4.12'),
  ]
)
