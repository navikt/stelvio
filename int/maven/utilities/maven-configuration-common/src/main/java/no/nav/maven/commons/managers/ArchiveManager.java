package no.nav.maven.commons.managers;

import java.io.File;
import java.io.IOException;

import no.nav.maven.commons.constants.Constants;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.ear.EarArchiver;

/**
 * @author test@example.com
 */
public final class ArchiveManager implements IArchiveManager {

	private final Archiver archiver;
	private final UnArchiver unArchiver;

	public ArchiveManager(final Archiver archiver, final UnArchiver unArchiver) {
		this.archiver = archiver;
		this.unArchiver = unArchiver;
	}

	public final File archive(final File directory, final String targetDirectory, final String targetFileName) {
		try {
			archiver.addDirectory(directory);
		} catch (ArchiverException e) {
			throw new RuntimeException("An error occured during archiving", e);
		}

		archiver.setDestFile(new File(targetDirectory, targetFileName));

		/* http://jira.codehaus.org/browse/MASSEMBLY-345 */
		if (archiver instanceof EarArchiver) {
			try {
				((EarArchiver) archiver).setAppxml(new File(new File(directory.getAbsolutePath(),
						Constants.J2E_MANIFEST_DIRECTORY), Constants.J2E_APPLICATION_XML_FILE));
			} catch (ArchiverException e) {
				throw new RuntimeException("An error occured when setting the deployment descriptor", e);
			}
		}

		try {
			archiver.createArchive();
		} catch (ArchiverException e) {
			throw new RuntimeException("An error occured during archiving", e);
		} catch (IOException e) {
			throw new RuntimeException("An error occured during archiving", e);
		}

		return archiver.getDestFile();
	}

	public final File unArchive(final File archive, final File directory) {
		File tmpDir = null;

		try {
			if (directory == null) {
				tmpDir = File.createTempFile(archive.getName(), null);
			} else {
				tmpDir = directory;
			}
		} catch (IOException e) {
			throw new RuntimeException("An error occured creating temporary directory", e);
		}

		deleteDirectory(tmpDir);
		tmpDir.mkdirs();
		tmpDir.deleteOnExit();

		unArchiver.setSourceFile(archive);
		unArchiver.setDestDirectory(tmpDir);

		try {
			unArchiver.extract();
		} catch (ArchiverException e) {
			throw new RuntimeException("An archiver error occured extracting the archive", e);
		} catch (IOException e) {
			throw new RuntimeException("An archiver error occured extracting the archive", e);
		}

		return tmpDir;
	}

	private void deleteDirectory(File directory) {
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					deleteDirectory(file);
				} else {
					file.delete();
				}
			}
			directory.delete();
		}
	}
}
