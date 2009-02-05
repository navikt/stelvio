package no.nav.busconfiguration.managers;

import java.io.File;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;

public interface IArchiveManager {

	File unArchive(File archive, File directory);
	File archive(File directory, String targetDirectory, String targetFileName);
}
