#!/bin/bash
set -e

TIME=$(TZ="Europe/Oslo" date +%Y.%m.%d-%H.%M)
COMMIT=$(git rev-parse --short=12 HEAD)
VERSION="6.0.0-beta-6-$COMMIT"
echo "Setting version $VERSION"

mvn -B versions:set -DnewVersion="$VERSION"
mvn -B versions:commit

echo "Running release"
mvn -B --settings maven-settings.xml deploy -Dmaven.wagon.http.pool=false
