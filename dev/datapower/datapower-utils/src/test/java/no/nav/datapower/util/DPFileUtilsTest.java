package no.nav.datapower.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import junit.framework.TestCase;

public class DPFileUtilsTest extends TestCase {

	
	public DPFileUtilsTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateSubDirectories() {
//		fail("Not yet implemented");
	}

	public void testAppend() {
//		fail("Not yet implemented");
	}

	public void testGetRelativePath() {
//		fail("Not yet implemented");
	}

	public void testGetFileList() {
//		fail("Not yet implemented");
	}

	public void testGetFolderList() {
//		fail("Not yet implemented");
	}

	public void testGetFolderListExcludeRoot() {
//		fail("Not yet implemented");
	}

	public void testGetFolderListRelativeTo() {
//		fail("Not yet implemented");
	}

	public void testGetRelativePathList() {
//		fail("Not yet implemented");
	}

	public void testGetResource() {
		File resource = DPFileUtils.getResource(getClass(), "/files");
		assertNotNull("Should be able to get resource", resource);
	}

	public void testCreateZipArchive() {
//		fail("Not yet implemented");
	}

	public void testExtractArchive() {
//		fail("Not yet implemented");
	}

	public void testReplaceSeparator() {
//		fail("Not yet implemented");
	}

	public void testScanFolders() {
//		fail("Not yet implemented");
	}

	public void testGetRelativeFolderPathsFileFile() {
//		fail("Not yet implemented");
	}

	public void testGetRelativeFolderPathsFile() {
//		fail("Not yet implemented");
	}

	public void testBase64EncodeFile() {
//		fail("Not yet implemented");
	}

	public void testFindDirectory() {
		File root = getFile("/files");
		File a = DPFileUtils.append(root, "a");
		File b = DPFileUtils.append(root, "b");
		try {
			File findA = DPFileUtils.findDirectory(root, "a");
			File findB = DPFileUtils.findDirectory(root, "b");
			assertNotNull(findA);
			assertNotNull(findB);
			assertEquals(a, findA);
			assertEquals(b, findB);
		} catch (IOException e) {
			fail("Caught IOException while searching for directory");
		}
	}
	
	public void testExtractArchiveFiltered() {
		File archive = FileUtils.toFile(getClass().getClassLoader().getResource("nav-cons-pen-pen-navorgenhet.zip"));
		System.out.println("Archive = " + archive);
		String archiveStr = archive.getPath();
		System.out.println("Archive Str= " + archiveStr);
//		File outDir = new File(archiveStr.substring(0, archiveStr.lastIndexOf(File.separatorChar)));
		File outDir = new File("E:\\data\\builds\\wsdl");
		System.out.println("OutDir = " + outDir);
		try {
			DPFileUtils.extractArchiveFiltered(archive, outDir, new OrFileFilter(
									new WildcardPathFilter("*nav-cons-pen-pen-navorgenhet\\*.wsdl"),
									new WildcardPathFilter("*nav-lib-cons*.xsd")));
//			DPFileUtils.extractArchiveFiltered(archive, outDir, new NotFileFilter(new WildcardFileFilter("*\\.*")));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Caught IOException while extracting ZIP archive");
		}
	}

	private File getFile(String resource) {
		return FileUtils.toFile(getClass().getResource(resource));
	}
	
//	public void testWildcardFileFilter() {
//		File absoluteFilePath1 = new File("C:/folder1/file1.txt");
//		File relativeFilePath1 = new File("folder1/file1.txt");
//		System.out.println("absoluteFile.getName() = " + absoluteFilePath1.getName());
//		IOFileFilter filter = new WildcardFileFilter("folder1/*.txt");
//		assertTrue("Relative path failed", filter.accept(relativeFilePath1));		
//		assertTrue("Absolute path failed", filter.accept(absoluteFilePath1));
//	}
	
	public void testWildcardMatch() {
		String absoluteFilename = "C:/wsdl/InterfaceWSEXP.wsdl";
		String relativeFilename1 = "C:/wsdl/no/nav/inf/Interface.wsdl";
		String relativeFilename2 = "C:/wsdl/no/nav/lib/asbo/ASBOEntity1.xsd";
		String relativeFilename3 = "C:/wsdl/no/nav/lib/asbo/ASBOEntity2.xsd";
		assertTrue("Relative path1 failed", FilenameUtils.wildcardMatch(relativeFilename1, "*wsdl/*.wsdl"));		
		assertTrue("Relative path2 failed", FilenameUtils.wildcardMatch(relativeFilename2, "*wsdl/no/*.xsd"));		
//		assertTrue("Absolute path failed", FilenameUtils.wildcardMatch(absoluteFilename, "*folder1/*.txt"));
	}
}
