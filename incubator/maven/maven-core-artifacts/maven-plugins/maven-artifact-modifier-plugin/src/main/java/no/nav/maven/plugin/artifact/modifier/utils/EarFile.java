package no.nav.maven.plugin.artifact.modifier.utils;

import java.io.IOException;

import org.eclipse.jst.j2ee.commonarchivecore.internal.CommonarchiveFactory;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.OpenFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.ReopenException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.SaveFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveOptions;
import org.eclipse.jst.j2ee.commonarchivecore.internal.impl.CommonarchiveFactoryImpl;

public class EarFile {

	public static EARFile openEarFile(String absolutePath)  {
		CommonarchiveFactory archiveFactory = CommonarchiveFactoryImpl.getActiveFactory();
		EARFile earFile = null;
		ArchiveOptions arcOpts = new ArchiveOptions();
		arcOpts.setSaveOnlyDirtyMofResources(false);
		try {
			earFile = archiveFactory.openEARFile(arcOpts, absolutePath);
		} catch (OpenFailureException e) {
			throw new RuntimeException("Unable to open ear file.", e);
		}
		return earFile;	
	}
	
	public static void closeEarFile(EARFile earFile) {	
		try {
			earFile.getFilesForSave();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			earFile.save();
		} catch (SaveFailureException e) {
			throw new RuntimeException("Unable to save ear file.", e);
		} catch (ReopenException e) {
			throw new RuntimeException("Unable to reopen ear file.", e);
		}
		
		earFile.close();
	}
}
