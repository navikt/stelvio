package no.nav.maven.plugin.artifact.modifier.managers;

import java.io.File;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;

public interface IArchiveManager {

	File unArchive(File archive, String directory);
	File archive(File directory, String targetDirectory, String targetFileName);
}
