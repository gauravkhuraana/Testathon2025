@echo off
REM Load BrowserStack credentials from .env file
REM Run this script before executing tests to set environment variables

if not exist ".env" (
    echo .env file not found!
    echo Please create .env file with your BrowserStack credentials
    echo Example:
    echo BROWSERSTACK_USERNAME=your_username
    echo BROWSERSTACK_ACCESS_KEY=your_access_key
    pause
    exit /b 1
)

echo Loading BrowserStack credentials from .env file...

for /f "usebackq tokens=1,2 delims==" %%a in (".env") do (
    if not "%%a"=="" if not "%%a"=="#" (
        set %%a=%%b
        echo Set %%a
    )
)

echo.
echo Environment variables loaded successfully!
echo You can now run your tests with:
echo   mvn clean test -Penvironment=browserstack
echo   or
echo   mvn clean test -Penvironment=local
echo.
pause
