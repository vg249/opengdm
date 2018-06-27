usage: GobiiAdl
 -d <directory>      Specifies the path to the directory where the files
                     are in
 -h <gobii server>   The URL of the gobii server to connect to
 -p <password>       Password of the user doing the load
 -s <scenario>       Specifies the path of one subdirectory under the main
                     directory. When specified, tool is run in
                     single-scenario mode
 -t <timeout>        Maximum waiting time in minutes
 -u <username>       Username of the user doing the load
 
 
 
/**
*	-h (host), -u (username), and -p (password) are required
*	either -d (directory) or -s should be specified. When both are specified, the adl will run with batch mode and ignore the specified scenario
*	-t (timeout) is optional
**/
 
Example usage:

-- with specified directory for batch mode
java -jar gobiiadl.jar -h http://localhost:8282/gobii-dev -u USER_READER -p reader -d /path_to_test_files

-- with specified directory for batch mode and timeout
java -jar gobiiadl.jar -h http://localhost:8282/gobii-dev -u USER_READER -p reader -d /path_to_test_files -t 30

-- with specified directory for single-scenario mode
java -jar gobiiadl.jar -h http://localhost:8282/gobii-dev -u USER_READER -p reader -s /path_to_test_files/scenario

-- with specified directory for single-scenario mode and timeout
java -jar gobiiadl.jar -h http://localhost:8282/gobii-dev -u USER_READER -p reader -s /path_to_test_files/scenario -t 30

