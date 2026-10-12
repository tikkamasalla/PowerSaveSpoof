@echo off
set JAVA_HOME=E:\android-sdk\jdk21
cd /d E:\powersave-spoof
echo sdk.dir=E:\\android-sdk> local.properties
call gradlew assembleDebug --console=plain > build-v12.log 2>&1
echo EXITCODE=%ERRORLEVEL% >> build-v12.log
