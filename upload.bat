@echo off

mvn package
IF %ERRORLEVEL% NEQ 0 (
  echo "Build failed..."
  exit %ERRORLEVEL%
)

java -jar "C:/Program Files (x86)/Steam/steamapps/common/SlayTheSpire/mod-uploader.jar" upload -w steam_workshop
