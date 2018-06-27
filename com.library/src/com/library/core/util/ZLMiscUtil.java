/*

 */

package com.library.core.util;

import java.util.List;
import java.util.Map;

public abstract class ZLMiscUtil {
	public static <T> boolean equals(T o0, T o1) {
		return (o0 == null) ? (o1 == null) : o0.equals(o1);
	}

	public static <T> boolean listsEquals(List<T> list1, List<T> list2) {
		if (list1 == null) {
			return list2 == null || list2.isEmpty();
		}
		if (list1.size() != list2.size()) {
			return false;
		}
		return list1.containsAll(list2);
	}

	public static <KeyT,ValueT> boolean mapsEquals(Map<KeyT,ValueT> map1, Map<KeyT,ValueT> map2) {
		if (map1 == null) {
			return map2 == null || map2.isEmpty();
		}
		if (map1.size() != map2.size()
				|| !map1.keySet().containsAll(map2.keySet())) {
			return false;
		}
		for (KeyT key: map1.keySet()) {
			final ValueT value1 = map1.get(key);
			final ValueT value2 = map2.get(key);
			if (!equals(value1, value2)) {
				return false;
			}
		}
		return true;
	}

	public static boolean matchesIgnoreCase(String text, String lowerCasePattern) {
		return (text.length() >= lowerCasePattern.length()) &&
			   (text.toLowerCase().indexOf(lowerCasePattern) >= 0);
	}
}
