annotation_processor(
  name='args',
  sources=rglobs('*.java'),
  processors=[
    'uno.perk.args.apt.ArgsProcessor',
  ],
  dependencies=[
    '3rdparty:jdk-tools'
  ],
  provides=artifact(
    org='uno.perk',
    name='args',
    repo=repository(
      name='maven-central',
      url='https://oss.sonatype.org/#stagingRepositories',
      push_db_basedir=os.path.join(get_buildroot(), 'build-support', 'publish', 'pushdb')
    ),
    publication_metadata=ossrh(
      description='Command line argument parsing for Java 1.8.',
      url='https://github.com/perkuno/args',
      licenses=[
        license(
          name='Apache License, Version 2.0',
          url='http://www.apache.org/licenses/LICENSE-2.0'
        ),
      ],
      developers=[
        developer(
          name='John Sirois',
          email='john.sirois@gmail.com'
        )
      ],
      scm=github(
        user='perkuno',
        repo='args'
      )
    )
  ),
)