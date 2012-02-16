Steps to import project in Eclipse/RAD:
-Run 'mvn eclipse:eclipse' from the parent directory (the context root will be different otherwise)
-Choose File->Import...->'Existing Projects Into Workspace'

Note: Because of a bug in maven-eclipse-plugin (documented in pom.xml),
application files must be duplicated between the project root and src/main/application