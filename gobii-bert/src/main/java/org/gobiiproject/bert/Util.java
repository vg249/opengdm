package org.gobiiproject.bert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

	public static <T> Map<T, T> into(Map<T, T> map, T ... ts) {
		if (! (ts.length % 2 == 0)) {
			throw new RuntimeException("An uneven number of key/values provided to Util.map");
		}

		for (int i = 0 ; i < ts.length ; i++) {
			map.put(ts[i], ts[i+1]);
		}

		return map;
	}

	public static <T> Map<T,T> map(T ... ts) {
		return into(new HashMap<>(), ts);
	}

	public static <S,T> Map<S, T> zipMap(List<S> keys, List<T> vals) {
		if (keys.size() != vals.size()) {
			throw new RuntimeException("Size of keys and values must be the same");
		}

		Map<S, T> map = new HashMap<>(keys.size());

		for (int i = 0 ; i < keys.size() ; i++) {
			map.put(keys.get(i), vals.get(i));
		}

		return map;
	}

	public static <T> List<T> listOf(T ... ts) {
		List<T> list = new ArrayList<>();

		for (T t : ts) {
			list.add(t);
		}

		return list;
	}
}
