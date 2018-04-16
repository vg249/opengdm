@echo off
rem example: codominant_test http://localhost:8282/gobii-dev USER_READER READER
java -jar gobiiadl.jar -wxml test_profiles/codominant_test/codominant_test.xml -h %1 -u %2 -p %3