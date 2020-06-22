#!/bin/bash
set -e

TIME=$(TZ="Europe/Oslo" date +%Y.%m.%d-%H.%M)
COMMIT=$(git rev-parse --short=12 HEAD)
BASEVERSION=`mvn org.apache.maven.plugins:maven-help-plugin:3.1.1:evaluate -Dexpression=project.version -q -DforceStdout | sed 's/-SNAPSHOT//g'`
VERSION="$BASEVERSION-$COMMIT"
echo "Setting version $VERSION"

mvn -B versions:set -DnewVersion="$VERSION"
mvn -B versions:commit

echo "Running release"
mvn -B --settings maven-settings.xml -DskipTests deploy -Dmaven.wagon.http.pool=false
