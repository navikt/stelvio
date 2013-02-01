package no.nav.serviceregistry.util;

public class StringUtils {
	public static boolean empty(String s){
		if(s == null || "".equals(s)){
			return true;
		}
		return false;
	}
	
	public static boolean notEmpty(String s){
		return !empty(s);
	}
}
