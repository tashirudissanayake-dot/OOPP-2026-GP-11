@echo off
echo ====================================================================
echo Faculty of Technology Management System (FoT-TMS)
echo Compiling and Running Application...
echo ====================================================================

if not exist "bin" mkdir bin

echo Compiling Java source files...
javac -cp "lib/*" -d bin src/com/fot/tms/config/*.java src/com/fot/tms/database/*.java src/com/fot/tms/model/*.java src/com/fot/tms/dao/*.java src/com/fot/tms/service/*.java src/com/fot/tms/util/*.java src/com/fot/tms/ui/common/*.java src/com/fot/tms/ui/admin/*.java src/com/fot/tms/ui/lecturer/*.java src/com/fot/tms/ui/to/*.java src/com/fot/tms/ui/undergraduate/*.java src/com/fot/tms/ui/*.java src/com/fot/tms/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful! Launching FoT-TMS...
    java -cp "bin;lib/*" com.fot.tms.Main
) else (
    echo [ERROR] Compilation failed. Please inspect errors above.
    pause
)
