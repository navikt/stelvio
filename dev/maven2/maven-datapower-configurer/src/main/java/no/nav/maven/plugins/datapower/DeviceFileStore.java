package no.nav.maven.plugins.datapower;

import java.lang.reflect.Field;

public class DeviceFileStore {

	private final String location;
	
	private DeviceFileStore(final String location) {
		this.location = location;
	}
	
	public static final DeviceFileStore CERT = new DeviceFileStore("cert");
	public static final DeviceFileStore PUBCERT = new DeviceFileStore("pubcert");
	public static final DeviceFileStore SHAREDCERT = new DeviceFileStore("sharedcert");
	public static final DeviceFileStore LOCAL = new DeviceFileStore("local");
	public static final DeviceFileStore TEMPORARY = new DeviceFileStore("temporary");
	
	public String toString() {
		return location + ":///";
	}
	
	public String getDevicePath(String relativePath) {
		return toString() + relativePath;
	}
	
	public static DeviceFileStore fromString(String name) throws IllegalAccessException {
		Field[] fields = DeviceFileStore.class.getFields();
		
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (field.getType() == DeviceFileStore.class) {
				DeviceFileStore fileStore = (DeviceFileStore) field.get(null);
				if (fileStore.location.equals(name))
					return fileStore;
			}
		}
		return null;
	}	
}
