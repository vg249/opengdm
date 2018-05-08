@echo off
rem example: dominant_test http://localhost:8282/gobii-dev USER_READER READER
java -jar gobiiadl.jar -wxml test_profiles/dominant_test/dominant_test.xml -h %1 -u %2 -p %3