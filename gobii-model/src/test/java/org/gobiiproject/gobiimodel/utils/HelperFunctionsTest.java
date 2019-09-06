package org.gobiiproject.gobiimodel.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class HelperFunctionsTest {

	@Test
	public void testReadFile() {


		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test_file_read.txt").getFile());


		String test = HelperFunctions.readFile(file.getAbsolutePath());

		assertEquals("asdfqwerzxcv\n", test);
	}
}
