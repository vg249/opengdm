package org.gobiiproject.gobiimodel.utils;

import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;

public class FileSystemInterface {

	/**
	 * As unix RM command.
	 * @param file
	 */
	public static void rm(String file){
		HelperFunctions.tryExec("rm "+file);
	}
	/**
	 * As unix MV command.
	 * @param from
	 * @param to
	 */
	public static void mv(String from, String to){
		HelperFunctions.tryExec("mv "+from + " " + to);
	}
}
