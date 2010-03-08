package no.nav.maven.commons.managers;

import java.io.File;

/** 
 * @author test@example.com 
 */
public interface IArchiveManager {

	File unArchive(File archive, File directory);
	File archive(File directory, String targetDirectory, String targetFileName);
}
