@echo off


rem -----------------------------------------------------------------------------------------
rem This file is used to set up the necessary variables to run build automation.
rem Run build.bat from the same directory.
rem
rem Important:  Use 8.3 file and directory names (ie. "PROGRA~1", not "Program Files")
rem -----------------------------------------------------------------------------------------


rem WID installation directory - use back slashes
SET WID_HOME=C:\IBM\WID6

rem Root directory of the automation scripts and workspaces (usually /Automation) - use back slashes
SET AUTOMATION_HOME=.


rem Rational Software Development Platform Directory (install directory of the first RSDP product installed locally)
SET RSDP_HOME=%WID_HOME%

rem -----------------------------------------------------------------------------------------
rem You shouldn't have to change any of the following environment variables
rem -----------------------------------------------------------------------------------------

rem JAVA home directory (usually %WID_HOME%\runtimes\base_v5\java) - use back slashes
SET JAVA_HOME=%WID_HOME%\runtimes\bi_v6\java
rem SET JAVA_HOME=C:\apps\Java\jdk1.5.0_09

rem The directory where startup.jar is located
SET ECLIPSE_HOME=%RSDP_HOME%\eclipse

rem WID installation directory - use back slashes
SET WID_LIB=%WID_HOME%\runtimes\bi_v6\lib

rem We want a date in the format YYYYMMDD
rem SET TODAYS_DATE=%DATE:~6%%DATE:~0,2%%DATE:~3,2%%TIME:~0,2%%TIME:~3,2%%TIME:~6,2%
SET TODAYS_DATE=%DATE:~6%%DATE:~0,2%%DATE:~3,2%



rem The directory where the log files will be written - use back slashes
SET LOGDIR=%AUTOMATION_HOME%\dailybuilds\%TODAYS_DATE%\logs

rem Directory on local machine where EARs etc.. will be created and from where these
rem files will be sent to the FTP_SERVER machine - use forward slashes
SET OUTPUT_DIR_ZIP=%AUTOMATION_HOME%\dailybuilds\%TODAYS_DATE%\zip

SET OUTPUT_WSDL_DIR_ZIP=%AUTOMATION_HOME%\dailybuilds\%TODAYS_DATE%\wsdl-zip

rem Directory on local machine where EARs etc.. will be created and from where these
rem files will be sent to the FTP_SERVER machine - use forward slashes
SET OUTPUT_DIR_EAR=%AUTOMATION_HOME%\dailybuilds\%TODAYS_DATE%\ear

rem WID workspace directory - use back slashes
SET WORKSPACE=%AUTOMATION_HOME%\dailybuilds\%TODAYS_DATE%\workspace


rem serviceDeploy path and file name
SET SERVICEDEPLOY=%WID_HOME%\runtimes\bi_v6\bin\serviceDeploy.bat
