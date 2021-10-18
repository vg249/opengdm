package org.gobii.masticator.reader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import lombok.Data;

import org.gobii.masticator.AspectMapper;
import org.gobii.masticator.reader.result.End;
import org.gobii.masticator.reader.result.Val;

@Data
public class ColumnReader implements Reader {

	private String delimiter = "\t";

	private File file;
	private int row;
	private int col;

	private Iterator<String> lines;

	public ColumnReader(File file, int row, int col) throws IOException {
		this.delimiter = String.valueOf(AspectMapper.delimitter);
		this.file = file;
		this.row = row;
		this.col = col;
		this.lines = Files.lines(file.toPath()).iterator();
		for(int i = 0 ; i < row ; i++) {
			lines.next();
		}
	}

	@Override
	public ReaderResult read() throws IOException {

		if (! lines.hasNext()) {
			return End.inst;
		}

		return new Val(lines.next().split(delimiter)[col]);
	}
}
