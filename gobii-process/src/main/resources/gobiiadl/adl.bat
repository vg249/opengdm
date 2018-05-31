@echo off
rem example: http://localhost:8282/gobii-dev USER_READER READER -t 30 -d /path_to_parent_directory/  or -s /path_to_scenario_directory
java -jar gobiiadl.jar -h %1 -u %2 -p %3 %4 %5