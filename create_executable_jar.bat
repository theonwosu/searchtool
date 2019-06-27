@echo off
rem set JAVA_HOME=C:\bea\8.1.6\bea\jdk142_11
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_74
set path=%JAVA_HOME%\bin

jar cmf mainClass.txt searchApp.jar search

pause