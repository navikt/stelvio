If the adapter is somehow updated, the following must be done (after replacing the rar file) in order to synchronize duplicated files:
	1. Replace jar-files that are updated/changed in the connectorModule folder (this will update the WID connector project)
	2. Update Maven artifacts (this will update the artifacts used when building with Maven):
		a. Upload the jar-files that are updated/changed to the Maven Repo using deploy-file (refer to deploy.txt)
		b. Update the dependencies in pom.xml to reflect the jar-files that are updated/changed.
		c. Upload the updated rar-file to the Maven Repo using deploy-file with the updated pom.xml (refer to deploy.txt)