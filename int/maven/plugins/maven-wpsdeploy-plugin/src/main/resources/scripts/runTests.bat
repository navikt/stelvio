@echo off 
set path=lib\WindowsCmdColors_x64;%path%

ansicon C:\IBM\WID7_WTE\runtimes\bi_v7\java\bin\java.exe -Dlogging.level=EXCEPTION -cp C:\IBM\WID7_WTE\runtimes\bi_v7\optionalLibraries\jython\jython.jar org.python.util.jython runTests.py

echo --
echo Deleting all *.class files
for /R %%i in (*.class) do del "%%i"

IF (%1)==(-exit) GOTO END
pause
:END