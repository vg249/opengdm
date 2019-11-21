package org.gobiiproject.gobiiprocess.digester.HeplerFunctions;

import org.gobiiproject.gobiiprocess.digester.HelperFunctions.PGArray;
import org.junit.Assert;
import org.junit.Test;

public class PGArrayTest {

	@Test
	public void testDelimiterSeparatedStringToPgArrayString() {
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+/-/z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+|-|z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+\\-\\z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+,-,z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+;-;z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+:-:z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+/-\\z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("+:-;z"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("{+;-;z}"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("(+|-|z)"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("[+:-:z]"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("[+:::::-::\\|/,z}"));
		Assert.assertEquals("{\"\"+\"\",\"\"-\"\",\"\"z\"\"}", PGArray.delimiterSeparatedStringToPgArrayString("(+;-;z}"));
	}
}
