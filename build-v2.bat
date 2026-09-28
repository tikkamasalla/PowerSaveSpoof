@echo off
set JAVA_HOME=E:\android-sdk\jdk21
cd /d E:\powersave-spoof
call gradlew assembleDebug --console=plain > build-v2.log 2>&1
echo EXITCODE=%ERRORLEVEL% >> build-v2.log
