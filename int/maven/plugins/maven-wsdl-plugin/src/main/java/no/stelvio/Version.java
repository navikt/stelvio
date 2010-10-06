package no.stelvio;

/**
 * Represent a version
 * 
 * Limitations:
 * -Supports version string on the form M.m.r where m and r are optional
 * -SNAPSHOT"-postfix is also supported for all three numbers
 * 
 * @author Oystein Gisnas <test@example.com>
 *
 */
public class Version {

	Integer major;
	Integer minor;
	Integer revision;
	boolean snapshot = false;

	public Version(String versionString) {
		String[] versionParts = versionString.split("\\.");
		switch (versionParts.length) {
		case 3:
			if (versionParts[2].contains("-")) {
				snapshot = true;
				revision = new Integer(versionParts[2].substring(0, versionParts[2].indexOf("-")));
			} else {
				revision = new Integer(versionParts[2]);
			}
		case 2:
			if (versionParts[1].contains("-")) {
				snapshot = true;
				minor = new Integer(versionParts[1].substring(0, versionParts[1].indexOf("-")));
			} else {
				minor = new Integer(versionParts[1]);
			}
		case 1:
			if (versionParts[0].contains("-")) {
				snapshot = true;
				major = new Integer(versionParts[0].substring(0, versionParts[0].indexOf("-")));
			} else {
				major = new Integer(versionParts[0]);
			}
			break;
		default:
			throw new NumberFormatException("Version string '" + versionString + "' is invalid");
		}
	}

	public Integer getMajor() {
		return major;
	}

	public Integer getMinor() {
		return minor;
	}

	public Integer getRevision() {
		return revision;
	}

	public boolean isSnapshot() {
		return snapshot;
	}

	/**
	 * Remove minor and revision version
	 * Does not change the snapshot state
	 */
	public void keepMajorVersionOnly() {
		minor = null;
		revision = null;
	}

}