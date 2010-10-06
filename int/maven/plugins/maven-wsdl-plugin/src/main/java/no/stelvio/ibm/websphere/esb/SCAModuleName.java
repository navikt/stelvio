package no.stelvio.ibm.websphere.esb;

import no.stelvio.Version;

/**
 * Create a versioned module name like the "createVersionedSCAModule" wsadmin command
 * 
 * -Also supports uniqueCellId
 * 
 * @author Oystein Gisnas <test@example.com>
 *
 */
public class SCAModuleName {

	public static String createVersionedModuleName(String moduleName, Version version) {
		return createVersionedModuleName(moduleName, version, null);
	}

	public static String createVersionedModuleName(String moduleName,
			Version version, String unqiueCellId) {
		StringBuilder result = new StringBuilder(moduleName);
		if (!version.isSnapshot() && version.getMajor() != null) {
			result.append("_v");
			result.append(version.getMajor());
			if (version.getMinor() != null) {
				result.append("_");
				result.append(version.getMinor());
			}
			if (version.getRevision() != null) {
				result.append("_");
				result.append(version.getRevision());
			}
		}
		if (unqiueCellId != null) {
			result.append("_");
			result.append(unqiueCellId);
		}
		return result.toString();
	}

}
