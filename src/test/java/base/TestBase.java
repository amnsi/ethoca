package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;

public class TestBase {

	private WebDriver ob = null;
	public static Properties CONFIG = null;
	public static Properties OR = null;

	public ExtentReports extent;
	public final String filePath = System.getProperty("user.dir") + "\\reporting\\extent_report.html";
	public ExtentTest test;

	public SoftAssert sa;

	public static final String USERNAME = "aveersingh999";
	public static final String ACCESS_KEY = "f9a1033e-f3d9-4889-b93f-374b08ff2b4a";
	public static final String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";

	@BeforeSuite
	public void beforeSuite() throws IOException {

		initialize();

	}

	@AfterSuite
	public void afterSuite() {

		extent.flush();
	}

	public void initialize() throws IOException {

		CONFIG = new Properties();
		FileInputStream ip = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\config.properties");
		CONFIG.load(ip);

		OR = new Properties();
		ip = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\OR.properties");
		OR.load(ip);

	}

	public WebDriver openBrowser() throws MalformedURLException {

		if (CONFIG.getProperty("remoteRun").equals("false")) {

			if (CONFIG.getProperty("browser").equals("Firefox")) {

				System.setProperty("webdriver.gecko.driver",
						System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\geckodriver.exe");
				ob = new FirefoxDriver();

				test.log(LogStatus.INFO, "Opened Firefox browser locally on Windows 10 machine");

			} else if (CONFIG.getProperty("browser").equals("Chrome")) {

				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\chromedriver.exe");
				ob = new ChromeDriver();

				test.log(LogStatus.INFO, "Opened Chrome browser locally on Windows 10 machine");

			} else {

				System.out.println("This browser is not supported");
				test.log(LogStatus.INFO, "This browser is not currently supported");

			}
		} else {

			DesiredCapabilities caps = null;

			if (CONFIG.getProperty("os").equals("Windows 10") && CONFIG.getProperty("browser").equals("Chrome")) {

				caps = DesiredCapabilities.chrome();
				caps.setCapability("platform", "Windows 10");
				caps.setCapability("version", "71.0");
			} else if (CONFIG.getProperty("os").equals("Windows 10")
					&& CONFIG.getProperty("browser").equals("Firefox")) {
				caps = DesiredCapabilities.firefox();
				caps.setCapability("platform", "Windows 10");
				caps.setCapability("version", "64.0");

			} else {

				System.out.println("This combination is not currently supported");
				test.log(LogStatus.INFO, "This combination is not currently supported");
			}

			ob = new RemoteWebDriver(new URL(URL), caps);
			test.log(LogStatus.INFO, "Opened " + CONFIG.getProperty("browser") + " browser remotely in "
					+ CONFIG.getProperty("os") + " machine using Sauce Labs");

		}

		ob.manage().window().maximize();
		test.log(LogStatus.INFO, "Maximised browser window");
		ob.get(CONFIG.getProperty("url"));
		test.log(LogStatus.INFO, "Opened URL");
		ob.manage().timeouts().implicitlyWait(Long.parseLong(CONFIG.getProperty("implicitWaitTime")), TimeUnit.SECONDS);
		return ob;
	}

	public void customisedWait(WebDriver ob, String xpath, String element_name) throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(ob, Long.parseLong(CONFIG.getProperty("explicitWaitTime")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		Thread.sleep(2000);

		test.log(LogStatus.INFO, "Selenium is able to find " + element_name + " element successfully");
	}

	public void moveToParticularElement(WebDriver ob, String xpath, String element_name) throws InterruptedException {

		customisedWait(ob, xpath, element_name);

		Actions myAction = new Actions(ob);
		WebElement myE = ob.findElement(By.xpath(xpath));
		myAction.moveToElement(myE).build().perform();
		test.log(LogStatus.INFO, "Moved to " + element_name + " element");

	}

	public void moveToParticularElementAndClick(WebDriver ob, String xpath, String element_name)
			throws InterruptedException {

		customisedWait(ob, xpath, element_name);

		Actions myAction = new Actions(ob);
		WebElement myE = ob.findElement(By.xpath(xpath));
		myAction.moveToElement(myE).build().perform();
		Thread.sleep(2000);
		test.log(LogStatus.INFO, "Moved to " + element_name + " element");
		myAction.click().build().perform();
		test.log(LogStatus.INFO, "Clicked " + element_name + " element");

	}

	public void clickElement(WebDriver ob, String xpath, String element_name) throws InterruptedException {

		customisedWait(ob, xpath, element_name);
		ob.findElement(By.xpath(xpath)).click();
		test.log(LogStatus.INFO, "Clicked " + element_name + " element");
	}

	public String getTextOfElement(WebDriver ob, String xpath, String element_name) throws InterruptedException {

		customisedWait(ob, xpath, element_name);
		String text = ob.findElement(By.xpath(xpath)).getText();
		test.log(LogStatus.INFO, "Text of " + element_name + " element retrieved");
		return text;
	}

	public void validateThatTextIsNotEqualTo(String actualText, String undesiredText) {

		Assert.assertFalse(actualText.equals(undesiredText));
		test.log(LogStatus.PASS, "Undesired text is not present");
	}

	public void compareStrings(String actual, String expected, String message) {

		Assert.assertEquals(actual, expected);
		test.log(LogStatus.PASS, message);
	}

	public void compareNumbers(int actual, int expected, String message) {

		Assert.assertEquals(actual, expected);
		test.log(LogStatus.PASS, message);
	}

	public void validateThatStringContains(String master_string, String child_string, String message) {

		Assert.assertTrue(master_string.contains(child_string));
		test.log(LogStatus.PASS, message);
	}

	public void validatePresenceOfElement(WebDriver ob, String xpath, String element_name) {

		List<WebElement> l = ob.findElements(By.xpath(xpath));
		int count = l.size();
		Assert.assertEquals(count, 1);
		test.log(LogStatus.PASS, "Element " + element_name + " is present");

	}

	public String[][] readExcel(String path) throws Exception {
		File myxl = new File(path);
		FileInputStream myStream = new FileInputStream(myxl);

		XSSFWorkbook myWB = new XSSFWorkbook(myStream);
		XSSFSheet mySheet = myWB.getSheetAt(0);
		int xRows = mySheet.getLastRowNum() + 1;
		int xCols = mySheet.getRow(0).getLastCellNum();
		System.out.println("Rows are " + xRows);
		System.out.println("Cols are " + xCols);
		String[][] xData = new String[xRows][xCols];
		for (int i = 0; i < xRows; i++) {
			XSSFRow row = mySheet.getRow(i);
			for (int j = 0; j < xCols; j++) {
				XSSFCell cell = row.getCell(j); // To read value from each col in each row
				String value = cell.toString();
				xData[i][j] = value;
			}

		}
		return xData;
	}

	public String getScreenshot() throws IOException {

		File myFile = ((TakesScreenshot) ob).getScreenshotAs(OutputType.FILE);
		String myPath = System.getProperty("user.dir") + "\\reporting\\" + this.getClass().getSimpleName() + ".png";
		FileUtils.copyFile(myFile, new File(myPath));
		return myPath;
	}

	public void configureExtentReport(ExtentReports extent) {

		extent.config().reportName("ETHOCA CHALLENGE");
		extent.config()
				.reportHeadline("[OS=" + CONFIG.getProperty("os") + ",BROWSER=" + CONFIG.getProperty("browser") + "]");
		extent.config().documentTitle("ETHOCA CHALLENGE");
	}

}
