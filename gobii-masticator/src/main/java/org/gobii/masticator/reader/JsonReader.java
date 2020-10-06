package org.gobii.masticator.reader;

import java.io.IOException;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class JsonReader implements Reader {

	private TableReader reader;

	@Override
	public ReaderResult read() throws IOException {

		return reader.read()
				.map(s -> {
					String[] toks = s.split("\t", -1);
					StringJoiner joiner = new StringJoiner(",");
					for (int i = 0; i < toks.length; i++) {
						joiner.add(String.format("\\\"%s\\\": \\\"%s\\\"", reader.getHeader().get(i), toks[i]));
					}

					return String.format("\"{%s}\"", joiner.toString());
				});
	}
}
