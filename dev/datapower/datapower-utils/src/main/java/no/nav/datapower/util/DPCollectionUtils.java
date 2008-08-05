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
	
	public static <K,V> void printLines(Map<K,V> map, PrintStream out, CharSequence separator) {
		for (K key : map.keySet()) {
			out.println(key.toString() + separator + map.get(key));
		}
	}
}
