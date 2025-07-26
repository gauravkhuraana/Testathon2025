# BrowserStack Test Execution Scripts
# Optimized for BrowserStack Pro Plan (5 parallel sessions max)

# Run Desktop Browser Tests (4 parallel sessions)
Write-Host "Running Desktop Browser Cross-Platform Tests..."
mvn clean test -Pbrowserstack "-Dtestng.suite.file=src/test/resources/testng-cross-platform-limited.xml"

Write-Host "`nTest execution completed. Check results above."
Write-Host "To run other test suites:"
Write-Host "Mobile devices: mvn clean test -Pbrowserstack `"-Dtestng.suite.file=src/test/resources/testng-mobile-devices.xml`""
Write-Host "Tablet devices: mvn clean test -Pbrowserstack `"-Dtestng.suite.file=src/test/resources/testng-tablet-devices.xml`""
Write-Host "Browser compatibility: mvn clean test -Pbrowserstack `"-Dtestng.suite.file=src/test/resources/testng-browser-compatibility.xml`""
