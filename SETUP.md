# Setup Guide - Selenium BrowserStack Framework

## Prerequisites Installation

### 1. Java 17 Installation

#### Windows:
1. Download OpenJDK 17 from [Microsoft OpenJDK](https://www.microsoft.com/openjdk)
2. Install and set JAVA_HOME environment variable
3. Verify installation:
   ```cmd
   java -version
   javac -version
   ```

#### macOS:
```bash
# Using Homebrew
brew install openjdk@17

# Set JAVA_HOME in ~/.zshrc or ~/.bash_profile
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

#### Linux:
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# CentOS/RHEL
sudo yum install java-17-openjdk-devel
```

### 2. Maven Installation

#### Windows:
1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to C:\Program Files\Apache\maven
3. Add to PATH: C:\Program Files\Apache\maven\bin
4. Verify: `mvn -version`

#### macOS:
```bash
# Using Homebrew
brew install maven
```

#### Linux:
```bash
# Ubuntu/Debian
sudo apt install maven

# CentOS/RHEL
sudo yum install maven
```

### 3. Git Installation

#### Windows:
Download from [Git for Windows](https://git-scm.com/download/win)

#### macOS:
```bash
# Usually pre-installed, or use Homebrew
brew install git
```

#### Linux:
```bash
# Ubuntu/Debian
sudo apt install git

# CentOS/RHEL
sudo yum install git
```

## BrowserStack Account Setup

### 1. Create BrowserStack Account
1. Go to [BrowserStack](https://www.browserstack.com/)
2. Sign up for a free account or log in
3. Navigate to **Account Settings** → **API**
4. Copy your **Username** and **Access Key**

### 2. Set Environment Variables

#### Windows (Command Prompt):
```cmd
set BROWSERSTACK_USERNAME=your_username_here
set BROWSERSTACK_ACCESS_KEY=your_access_key_here
```

#### Windows (PowerShell):
```powershell
$env:BROWSERSTACK_USERNAME="your_username_here"
$env:BROWSERSTACK_ACCESS_KEY="your_access_key_here"
```

#### macOS/Linux:
```bash
export BROWSERSTACK_USERNAME="your_username_here"
export BROWSERSTACK_ACCESS_KEY="your_access_key_here"

# Add to ~/.bashrc or ~/.zshrc for persistence
echo 'export BROWSERSTACK_USERNAME="your_username_here"' >> ~/.bashrc
echo 'export BROWSERSTACK_ACCESS_KEY="your_access_key_here"' >> ~/.bashrc
source ~/.bashrc
```

## Project Setup

### 1. Clone/Download Project
```bash
# If using Git
git clone <repository-url>
cd Testathon2025

# If downloaded as ZIP
# Extract to desired location and navigate to folder
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Verify Setup
```bash
# Compile project
mvn clean compile

# Run a quick test
mvn clean test -Dtest=LoginTest -Penvironment=local
```

## IDE Configuration (Optional)

### IntelliJ IDEA
1. Open project as Maven project
2. File → Project Structure → Project → Set SDK to Java 17
3. Install TestNG plugin if not already installed
4. Configure run configurations for different test suites

### Eclipse
1. Import as Existing Maven Project
2. Right-click project → Properties → Java Build Path → Set JRE to Java 17
3. Install TestNG plugin from Eclipse Marketplace

### VS Code
1. Install Java Extension Pack
2. Install TestNG Runner extension
3. Configure Java path in settings

## Running Tests

### Quick Start Commands

#### 1. Run All Tests on BrowserStack
```bash
mvn clean test -Penvironment=browserstack
```

#### 2. Run Smoke Tests Only
```bash
mvn clean test -Penvironment=browserstack -Dgroups=smoke
```

#### 3. Run Tests Locally
```bash
mvn clean test -Penvironment=local -Dbrowser=chrome
```

#### 4. Run Specific Test Class
```bash
mvn clean test -Dtest=LoginTest -Penvironment=browserstack
```

### Using Script Runners

#### Windows:
```cmd
# Command Prompt
run-tests.bat

# PowerShell
.\run-tests.ps1
```

#### macOS/Linux:
```bash
chmod +x run-tests.sh
./run-tests.sh
```

## Troubleshooting Common Issues

### Issue 1: Maven not found
**Solution:**
- Verify Maven installation: `mvn -version`
- Check PATH variable includes Maven bin directory
- Restart terminal after setting PATH

### Issue 2: Java version issues
**Solution:**
- Verify Java 17: `java -version`
- Set JAVA_HOME environment variable
- Use `update-alternatives` on Linux to switch Java versions

### Issue 3: BrowserStack connection failed
**Solution:**
- Verify credentials are set correctly
- Check internet connectivity
- Ensure BrowserStack account has available sessions
- Try logging into BrowserStack web dashboard

### Issue 4: Chrome driver issues (local execution)
**Solution:**
- Selenium 4+ includes built-in driver management
- Ensure Chrome browser is installed and up to date
- Driver binaries are automatically downloaded when needed

### Issue 5: Tests fail to find elements
**Solution:**
- Application might be slow to load
- Increase timeout values in config.properties
- Check if application URL is accessible

### Issue 6: Permission denied on script files
**Solution:**
```bash
# macOS/Linux
chmod +x run-tests.sh

# Windows PowerShell (run as administrator)
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

## Directory Structure After Setup
```
Testathon2025/
├── src/
│   ├── main/java/          # Framework source code
│   └── test/
│       ├── java/           # Test classes
│       └── resources/      # Configuration files
├── target/                 # Maven build output
├── test-output/           # Test results and reports
│   ├── extent-reports/    # HTML reports
│   └── screenshots/       # Test screenshots
├── pom.xml               # Maven configuration
├── browserstack.yml      # BrowserStack configuration
├── README.md            # Documentation
└── run-tests.*          # Execution scripts
```

## Configuration Files

### config.properties
Main configuration file for framework settings:
- Timeouts and waits
- Screenshot settings
- Application URLs
- Reporting configuration

### browserstack.yml
BrowserStack-specific configuration:
- Browser and OS combinations
- BrowserStack capabilities
- Parallel execution settings

### testng.xml
TestNG suite configuration:
- Test grouping
- Parallel execution
- Test parameters

## Next Steps

1. **Customize Test Data**: Update `src/test/resources/testdata/testdata.md`
2. **Add New Tests**: Create new test classes in `src/test/java/tests/`
3. **Add New Pages**: Create page objects in `src/main/java/pages/`
4. **Configure Browsers**: Update `browserstack.yml` for additional browsers
5. **Integrate with CI/CD**: Use Maven commands in your CI/CD pipeline

## Support Resources

- **BrowserStack Documentation**: https://www.browserstack.com/docs
- **TestNG Documentation**: https://testng.org/doc/
- **Selenium Documentation**: https://selenium-python.readthedocs.io/
- **Maven Documentation**: https://maven.apache.org/guides/

For framework-specific questions, refer to the README.md file in the project root.
