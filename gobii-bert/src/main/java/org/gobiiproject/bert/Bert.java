package org.gobiiproject.bert;

import ernie.core.Ernie;

public class Bert {

	public static void main(String[] args) throws Exception {

		Ernie ernie = new Ernie();

		ernie.loadComponents(Components.class);

		for (String s : args) {
			ernie.runFile(s);
		}

		ernie.runFile("/Users/ljc237-admin/gobii/bert/preamble.bert");
		ernie.runFile("/Users/ljc237-admin/gobii/bert/tests.bert");

//		ComponentsUtil.ssh("cbsugobiixvm16.biohpc.cornell.edu", "gadm", "ls > asdf");
//		ComponentsUtil.ssh("cbsugobiixvm16.biohpc.cornell.edu", "gadm", "cat asdf");

		System.exit(0);
	}
}
