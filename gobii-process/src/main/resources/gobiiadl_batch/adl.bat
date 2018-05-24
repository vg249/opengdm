@echo off
rem example: codominant_test http://localhost:8282/gobii-dev USER_READER READER -t 30 -d /path_to_parent_directory/
java -jar gobiiadl.jar -h %1 -u %2 -p %3 %4 %5