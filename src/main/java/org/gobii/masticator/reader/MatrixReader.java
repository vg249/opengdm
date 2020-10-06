package org.gobii.masticator.reader;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.gobii.masticator.reader.result.Break;
import org.gobii.masticator.reader.result.End;
import org.gobii.masticator.reader.result.Val;


public class MatrixReader implements Reader {


	private File file;
	private int row;
	private int col;

	private RandomAccessFile raf;

	private boolean lineBreak = false;

	public MatrixReader(File file, int row, int col) throws IOException {
		this.file = file;
		this.row = row;
		this.col = col;

		this.raf = new RandomAccessFile(file, "r");

		for (int i = 0 ; i < row ; i++) {
			raf.readLine();
		}

		skipLineBeginning();

	}

	private void skipLineBeginning() throws IOException {
		for (int i = 0 ; i < col ; i++) {
			while (raf.readByte() != '\t') ;
		}
	}

	@Override
	public int dimension() {
		return 2;
	}

	@Override
	public ReaderResult read() throws IOException {

		if (lineBreak) {
			lineBreak = false;
			return Break.inst;
		} else if (raf.getFilePointer() == raf.length() - 1) {
			return End.inst;
		}

		StringBuilder sb = new StringBuilder();

		for (;;) {
			char c;
			try {
				c = (char) raf.readByte();
			} catch (EOFException eof) {
				break;
			}

			if (c == '\t') {
				break;
			} else if (c == '\n') {
				skipLineBeginning();
				break;
			} else {
				sb.append(c);
			}
		}

		return Val.of(sb.toString());
	}
}
