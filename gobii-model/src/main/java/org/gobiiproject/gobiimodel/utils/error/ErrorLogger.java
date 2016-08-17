package org.gobiiproject.gobiimodel.utils.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Horribly simplistic error logger.
 * Allows for multiple errors to be logically distinguished, 
 * so I don't have to hear 'the log file says moo' complaints all the time.
 * @author jdl232
 * Hopefully HelperFunctions will just return Errors instead of true/false
 */


public class ErrorLogger {
	private static final Logger log = LoggerFactory.getLogger("Gobii Error Log");
	public static Set<Error> errors = new HashSet();

	/**
	 * Logs an error that has occurred in the process.
	 * @param e "Error" to be logged
	 */
	public static void logError(Error e){
		errors.add(e);
		log.error("Error in {}: {}",e.name,e.reason);
		if(e.file!=""){
			logErrorFile(new File(e.file));
		}
	}

	/**
	 * Adds an entire file to the error stream
	 * @param f file to add to error stream
	 */
	private static void logErrorFile(File f){
		if(!f.exists()) return; //No file
		if(f.length()==0) return; //No file
		BufferedReader br=null;
		try {
			br=new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			//This is... .very unlikely, as we check file existance a line ago.
			log.error("File Not Found in ErrorLogger.logErrorFile",e);
			return;
		}

		log.error("Contents of File {}",f.getAbsoluteFile());
		String s=null;
		try {
			while ((s = br.readLine())!=null) {
				log.error(s);
			}
		}catch(IOException e){

		}
	}

	/**
	 * Logs an error through ErrorLogger, as well as notifying ErrorLogger that an important error
	 * has occurred (see {@link ErrorLogger#success success})
	 * @param name Name of the component being logged
	 * @param reason Reason given for error
	 * @param file File containing additional error details (if exists) Nullable
	 * @throws InvalidErrorException
	 */
	public static void logError(String name, String reason, String file) throws InvalidErrorException{
		if(name==null || reason==null)throw new InvalidErrorException();
		logError(new Error(name,reason,file));
	}

	public static void logWarning(String name, String reason, String file) throws InvalidErrorException{
		if(name==null || reason==null)throw new InvalidErrorException();
	}
	public static void logDebug(String name, String reason, String file) throws InvalidErrorException{
		if(name==null || reason==null)throw new InvalidErrorException();
	}

	public static void logInfo(String name, String reason, String file) throws InvalidErrorException{

	}

	public static void logTrace(String name, String reason, String file) throws InvalidErrorException{

	}



	/**
	 * Determine if the current process is 'successful'.
	 * "Successful" means no errors were received by the process so far.
	 * @return true if there are no errors that have been logged
	 */
	public static boolean success(){
		return errors.isEmpty();
	}

	/**
	 * List of Error objects in the system.
	 */
	public static Set<Error> getAllErrors(){
		return errors;
	}

	/**
	 * List of errors in the system in printable string format
	 * @return A string composed of all toStrings for all errors in the system, deliniated by newlines.
	 */
	public static String getAllErrorStrings(){
		StringBuilder sb=new StringBuilder();
		for(Error e:errors){
			sb.append(e);
			sb.append("\n");
		}
		return sb.toString();
	}

}

/*Package*/ class Error{
	String name;
	String reason;
	String file;
	Error(String name, String reason, String file){
		this.name=name;
		this.reason=reason;
		this.file=file;
	}
	public String toString(){
		return name+": "+reason+(file!=null?"\nAn log for this error may be available at "+file:"");
	}
}
class InvalidErrorException extends Exception{}