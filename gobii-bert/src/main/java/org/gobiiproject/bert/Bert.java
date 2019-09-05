package org.gobiiproject.bert;

import ernie.core.Ernie;

public class Bert {

	public static void main(String[] args) throws Exception {

		Ernie ernie = new Ernie();

		ernie.loadComponents(Components.class);

		for (String s : args) {
			ernie.runFile(args[0]);
		}

		ComponentsUtil.scp("cbsugobiixvm16.biohpc.cornell.edu", "gadm", "/tmp/asdf", "/tmp/asdf");
	}
}
