package no.nav.datapower.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DPCollectionUtils {

	private DPCollectionUtils() {}
	
	public static <T> List<T> newLinkedList() {
		return new LinkedList<T>();
	}

	public static <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}

	public static <K,V> Map<K,V> newHashMap() {
		return new HashMap<K,V>();
	}
	
	public <T> void printList(List<T> list, PrintStream out) {
		for (T element : list) {
			out.println(element);
		}
	}
}
