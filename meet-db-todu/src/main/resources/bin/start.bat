@echo off & setlocal enabledelayedexpansion

set LIB_JARS=""
cd ..\lib
for %%i in (*) do set LIB_JARS=!LIB_JARS!;..\lib\%%i
cd ..\bin


java -Xms64m -Xmx256m -XX:MaxPermSize=64M -classpath ..\conf;%LIB_JARS% com.sunline.dbscript.GenerateDbAndInitExcelMain
goto end

:end
pause