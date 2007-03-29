@echo off
setlocal

rem ==============================================================================
rem This file is used to invoke build tasks
rem build.bat
rem
rem  Available targets:
rem
rem      init               - displays the currently set variables and displays this help message
rem      build-all-projects - Task to build all WID projects
rem
rem   To run a full build:
rem
rem    $AUTOMATION_HOME/build.bat init build-all-projects
rem ==============================================================================

rem ==============================================================================
rem Load properties file setupBuild.bat
rem ==============================================================================

call setupBuild.bat

rem ==============================================================================
rem perform check of environment variable WID_HOME.
rem ==============================================================================

if exist %WID_HOME% goto continue3
echo Error: Please set WID_HOME
goto done
  
:continue3  
echo WID_HOME= "%WID_HOME%"

rem ==============================================================================
rem perform check of environment variable WID_LIB.
rem ==============================================================================

if exist %WID_LIB% goto continue3
echo Error: Please set WID_LIB
goto done
  
:continue3  
echo WID_LIB= "%WID_LIB%"


rem ==============================================================================
rem perform check of environment variable JAVA_HOME.
rem ==============================================================================

if exist %JAVA_HOME% goto continue4
echo Error: Please set JAVA_HOME
goto done
  
:continue4  
echo JAVA_HOME= "%JAVA_HOME%"

rem ==============================================================================
rem perform check of environment variable AUTOMATION_HOME.
rem ==============================================================================

if exist %AUTOMATION_HOME% goto continue5
echo Error: Please set AUTOMATION_HOME
goto done

:continue5
echo AUTOMATION_HOME= "%AUTOMATION_HOME%"

rem ==============================================================================
rem perform check of environment variable SERVICEDEPLOY.
rem ==============================================================================

if defined SERVICEDEPLOY goto continue6
echo Error: Please set SERVICEDEPLOY
goto done

:continue6
echo SERVICEDEPLOY= "%SERVICEDEPLOY%"

rem ==============================================================================
rem call ant.
rem ==============================================================================

:run
rmdir /S /Q %WORKSPACE%\.metadata
%JAVA_HOME%\bin\java -Dwid.home=%WID_HOME% -Dworkspace=%WORKSPACE% -Dlog.dir=%LOGDIR% -Doutput.dir.wsdl.zip=%OUTPUT_WSDL_DIR_ZIP% -Doutput.dir.ear=%OUTPUT_DIR_EAR% -Doutput.dir.zip=%OUTPUT_DIR_ZIP% -Dbuildscripts.dir=%AUTOMATION_HOME% -Dservice.deploy=%ServiceDeploy% -cp %WID_LIB%\sca-style.jar;%WID_LIB%\wsanttasks.jar;%WID_LIB%\wsprofile.jar;%ECLIPSE_HOME%\startup.jar org.eclipse.core.launcher.Main -logfile eclipse.log  -application com.ibm.etools.j2ee.ant.RunAnt  -buildfile %AUTOMATION_HOME%\masterBuild.xml -data "%WORKSPACE%" %*


echo RunAnt returned %ERRORLEVEL%

if "%ERRORLEVEL%"=="23" goto run
if "%ERRORLEVEL%"=="15" echo ERROR WORKSPACE "%WORKSPACE%" is already BEING USED
if "%ERRORLEVEL%"=="13" echo ERROR runAnt BUILD FAILED
if NOT "%ERRORLEVEL%"=="0" echo ERROR runAnt BUILD FAILED. ErrorCode = "%ERRORLEVEL%"

:done
exit "%ERRORLEVEL%"


