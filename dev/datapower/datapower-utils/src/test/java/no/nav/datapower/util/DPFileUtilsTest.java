package no.nav.datapower.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

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

	private File getFile(String resource) {
		return FileUtils.toFile(getClass().getResource(resource));
	}
}
