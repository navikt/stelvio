package no.nav.maven.plugin.artifact.modifier.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.jst.j2ee.commonarchivecore.internal.CommonarchiveFactory;
import org.eclipse.jst.j2ee.commonarchivecore.internal.EARFile;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.OpenFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.ReopenException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.SaveFailureException;
import org.eclipse.jst.j2ee.commonarchivecore.internal.helpers.ArchiveOptions;
import org.eclipse.jst.j2ee.commonarchivecore.internal.impl.CommonarchiveFactoryImpl;

/** 
 * @author test@example.com 
 */
public final class EarFile {

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
			throw new RuntimeException("Unable to find files that need to be saved.", e);
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

	public static void copyFile(final File source, final File dest) {
		FileInputStream in = null;
		FileOutputStream out = null;
		
		try {
			in = new FileInputStream(source);
			out = new FileOutputStream(dest);
		   byte[] buffer = new byte[4096];         
		   int bytes_read;                         

		   while((bytes_read = in.read(buffer)) != -1) { 
			   out.write(buffer, 0, bytes_read);            
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("An error occured reading source file: " + source.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new RuntimeException("An error occured writing desctination file: " + dest.getAbsolutePath(), e);
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {}	
			}
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {}	
			}
		}		
	}
}
