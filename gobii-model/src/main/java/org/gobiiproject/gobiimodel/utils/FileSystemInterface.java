package org.gobiiproject.gobiimodel.utils;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;

public class FileSystemInterface {

	private static boolean keepAllFiles=false;
	private static boolean keepInTemp=false;
	/**
	 * As unix RM command.
	 * @param file
	 */
	public static void rm(String file){
		if(keepAllFiles){
			return;
		}
		HelperFunctions.tryExec("rm "+file);
	}

	public static void rmIfExist(File file){
		if(file==null)return;
		if(keepAllFiles){
			return;
		}
		if(keepInTemp){
			File tmpDir=new File(file.getParent(),"temp");
			tmpDir.mkdir(); // returns false if directory exists or is file (if it's a file, whoops).
			File tempFile=new File(tmpDir,file.getName());
			try {
				Files.move(file.toPath(),tempFile.toPath(),(CopyOption)StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				ErrorLogger.logError("FileSystemInterface",e);
			}
		}
		if(file.exists()){
			file.delete();
		}
	}
	public static void rmIfExist(String file){
		if(file==null)return;
		File f=new File(file);
		rmIfExist(f);
	}
	/**
	 * As unix MV command.
	 * @param from
	 * @param to
	 */
	public static void mv(String from, String to){
		HelperFunctions.tryExec("mv "+from + " " + to);
	}

	public static int lineCount(String file){
		if(!new File(file).exists()){
			ErrorLogger.logDebug("FileSystemInterface","File " + file + " being WC'd does not exist");
			return 0;
		}
		String [] exec={"wc","-l",file};
		int retVal=-1;
		try {
			ProcessBuilder builder = new ProcessBuilder(exec);
			Process p = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));//What terrible person makes 'InputStream' the type of the output of a process
			p.waitFor();
			retVal=Integer.parseInt(reader.readLine().split(" ")[0]);

		}
		catch(Exception e){
			ErrorLogger.logError("FileSystemInterface","Unable to call wc",e);
		}
		return retVal;
	}

	/**
	 * Determines whether or not to keep temporary files. Useful for debuggins
	 * @param keep
	 */
	public static void keepAllFiles(boolean keep){
		keepAllFiles=keep;
	}
	/**
	 * Determines whether 'deleted' temporary files will instead be stored in *path*\temp\*filename*
	 * Only matters if keepAllFiles = false.
	 * @param keep
	 */
	public static void keepTempFiles(boolean keep){
		keepInTemp=keep;
	}
}
