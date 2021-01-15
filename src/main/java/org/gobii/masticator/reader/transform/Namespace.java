package org.gobii.masticator.reader.transform;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.gobii.Util;
import org.gobii.masticator.Transformers;
import org.gobii.masticator.reader.transform.compilers.Compilers;
import org.gobii.masticator.reader.transform.compilers.Transformation;


public class Namespace {

	private static Map<String, TransformerPrototype<Object, String>> transformers = new HashMap<>();

	public static TransformerPrototype<Object, String> resolve(String fname) {
		return transformers.get(fname);
	}

	public static void intern(String fname, TransformerPrototype<Object, String> transformer) {
		transformers.put(fname, transformer);
	}

	static {
		try {
			Compilers.resolve("clj").load(Util.slurpResource("transformations.clj"));
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}

		final Transformers t = new Transformers();

		Arrays.stream(Transformers.class.getMethods())
				.filter(m -> m.isAnnotationPresent(Transformation.class))
				.forEach(m -> transformers.put(m.getName(), args -> v -> {
					try {
						return m.invoke(t, v, args.toArray()) + "";
					} catch (Exception e) {
						throw new TransformException(e);
					}
				}));
	}
}
