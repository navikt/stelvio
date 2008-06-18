package no.nav.maven.plugins.common.utils;

import java.util.Date;

public class Random {
	public static String getUniqueId(){
		return new Date().getTime() + new java.util.Random().nextInt(10000) + "";
	}
	
}
