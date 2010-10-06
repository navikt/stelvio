package no.stelvio;
import static org.junit.Assert.*;

import no.stelvio.Version;

import org.junit.Test;

public class VersionTest {

	@Test
	public void testVersion() {
		// Only major
		Version version1 = new Version("1");
		assertEquals(new Integer(1), version1.getMajor());
		assertNull(version1.getMinor());
		assertNull(version1.getRevision());
		assertFalse(version1.isSnapshot());

		// Full
		Version version123 = new Version("1.2.3");
		assertEquals(new Integer(1), version123.getMajor());
		assertEquals(new Integer(2), version123.getMinor());
		assertEquals(new Integer(3), version123.getRevision());
		assertFalse(version123.isSnapshot());

		// Snapshot
		Version versionSnapshot = new Version("1.0-SNAPSHOT");
		assertEquals(new Integer(1), versionSnapshot.getMajor());
		assertEquals(new Integer(0), versionSnapshot.getMinor());
		assertNull(versionSnapshot.getRevision());
		assertTrue(versionSnapshot.isSnapshot());
	}
}
