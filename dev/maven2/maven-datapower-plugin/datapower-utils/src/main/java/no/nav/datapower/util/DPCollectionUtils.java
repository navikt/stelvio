package no.nav.datapower.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DPCollectionUtils {

	private DPCollectionUtils() {}
	
	public static <T> List<T> newLinkedList() {
		return new LinkedList<T>();
	}

	public static <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}

	public static <T> Set<T> newHashSet() {
		return new HashSet<T>();
	}

	public static <K,V> Map<K,V> newHashMap() {
		return new HashMap<K,V>();
	}
	
	public static <T> void printLines(Collection<T> collection, PrintStream out) {
		printLines(collection, out, "");
	}

	public static <T> void printLines(Collection<T> collection, PrintStream out, String linePrefix) {
		for (T element : collection) {
			out.println(linePrefix + element);
		}
	}

	public static <T> void printLines(Collection<T> collection, Logger log, Level level, String linePrefix) {
		for (T element : collection) {
			log.log(level, linePrefix + element);
		}
	}

	
	public static <K,V> void printLines(Map<K,V> map, PrintStream out, CharSequence separator) {
		for (K key : map.keySet()) {
			out.println(key.toString() + separator + map.get(key));
		}
	}
	/**
	 *	Syntax:	
	 *	
	 *	{"mapEntry1Key":"mapEntry1Value","mapEntry2key":"mapEntry2Value"}
	 *
	 */
	public static Map<String, String> mapFromString(String mapString) {
		Map<String,String> map = newHashMap();
		Validate.isTrue(mapString.startsWith("{"), "Syntax error in map string: missing enclosing start character '{'");
		Validate.isTrue(mapString.endsWith("}"), "Syntax error in map string: missing enclosing end character '}'");
		String[] mapEntries = StringUtils.split(trimStartEnd(mapString, "{", "}"), ',');
		for (String entry : mapEntries) {
			Validate.isTrue(entry.contains(":"), "Sytax error in map entry string: missing character ':'");
			String[] keyValue = StringUtils.split(entry, ':');
			String key = trimStartEnd(keyValue[0],"'", "'"); 
			String value = trimStartEnd(keyValue[1],"'", "'");
			map.put(key, value);
		}		
		return map;
	}
	
	private static String trimStartEnd(String str, String start, String end) {
		if (str.startsWith(start) && str.endsWith(end));
		 	str = StringUtils.substringBetween(str, start, end);
		 return str.trim();
	}
	
	/**
	 * Syntax:
	 * 
	 *	["listEntry1", "listEntry2"]
	 *
	 */
	public static List<String> listFromString(String listString) {
		List<String> list = newArrayList();
		Validate.isTrue(listString.startsWith("["), "Syntax error in list string: missing enclosing start character '['");
		Validate.isTrue(listString.endsWith("]"), "Syntax error in list string: missing enclosing end character ']'");
		String[] listEntries = StringUtils.split(trimStartEnd(listString, "[", "]"), ',');
		for (String entry : listEntries) {
			entry = entry.trim();
			if (entry.startsWith("'"))
				list.add(trimStartEnd(entry, "'", "'"));
			else if (entry.startsWith("\""))
				list.add(trimStartEnd(entry, "\"", "\""));		
			else
				list.add(entry);
		}
		return list;
	}
}
