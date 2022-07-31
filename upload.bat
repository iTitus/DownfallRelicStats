@echo off

echo Building...
call mvn package
IF %ERRORLEVEL% NEQ 0 (
  echo Build failed...
  exit %ERRORLEVEL%
)

echo Uploading...
java -jar "C:/Program Files (x86)/Steam/steamapps/common/SlayTheSpire/mod-uploader.jar" upload -w steam_workshop
