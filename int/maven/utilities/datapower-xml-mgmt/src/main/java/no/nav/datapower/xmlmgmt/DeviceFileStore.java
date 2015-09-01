package no.nav.datapower.xmlmgmt;


public enum DeviceFileStore {

	CERT("cert"),
	PUBCERT("pubcert"),
	SHAREDCERT("sharedcert"),
	LOCAL("local"),
	TEMPORARY("temporary"),
	STORE("store");
	
	private final String location;
	
	DeviceFileStore(final String location) {
		this.location = location;
	}
	
	public String toString() {
		return location + ":///";
	}
	
	public String getDevicePath(String relativePath) {
		return toString() + relativePath;
	}
	
	public static DeviceFileStore fromString(String name) {
		for (DeviceFileStore candidate : DeviceFileStore.values()) {
			if (name.equals(candidate.location))
				return candidate;
		}
		return null;
	}	
}
