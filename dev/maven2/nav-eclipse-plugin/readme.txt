Endret av person59d660d1d1a2 Gustavsen
07.08.07

Denne pluginen er basert på maven-eclipse-plugin versjon 2.4.

Modifikasjoner:

AddMavenRepoMojo.java

- Lagt inn at den skal lete seg fram til hvor base directory til workspacet ditt er. Dette vi
ekstisterer fordi man henter ut koden fra clearcase via et workspace.

EclipseClasspathWriter.java

- lagt inn eksludering av .db filer i classpath filene som blir generert ved eclipse:eclipse. Dette fordi
det skaper problemer i RAD ved bygging.
