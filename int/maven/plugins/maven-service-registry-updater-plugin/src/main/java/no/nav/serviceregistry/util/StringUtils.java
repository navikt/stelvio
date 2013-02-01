package no.nav.serviceregistry.util;

public class StringUtils {
	public static boolean empty(String... s){
		if (s == null) return true;
		for (String string : s) {
			if(string == null || string.isEmpty()){
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
	
	public static boolean notEmpty(String... s){
		if (s == null) return false;
		for (String string : s) {
			if(string != null && !string.isEmpty()){
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
}