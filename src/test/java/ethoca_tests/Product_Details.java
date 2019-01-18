package ethoca_tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ExtentManager;

public class Product_Details extends TestBase{

	WebDriver ob;
	String[][] arr;
	
	@BeforeMethod
	public void beforeMethod() throws Exception {
		
		extent = ExtentManager.getReporter(filePath);
		configureExtentReport(extent);
		test = extent.startTest(this.getClass().getSimpleName(), "Validate that all product prices get displayed correctly in ACCESSORIES page").assignCategory(
				"Regression");
		
		arr=readExcel(System.getProperty("user.dir") + "\\src\\test\\resources\\xlsx\\Product_Details.xlsx");
	}
	

	@Test
	public void test2() throws InterruptedException, IOException {
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		ob=openBrowser();
		moveToParticularElement(ob,OR.getProperty("product_category_menu_bar_link"),"product_category_menu_bar_link");
		moveToParticularElementAndClick(ob, OR.getProperty("accessories_drop_down_menu_link"),"accessories_drop_down_menu_link");
		
		List<WebElement> prod_names=ob.findElements(By.xpath(OR.getProperty("product_title_link")));
		List<WebElement> prices=ob.findElements(By.xpath(OR.getProperty("price_label")));

		HashMap<String,String> m=new HashMap<String, String>();
		String prod_name_text,price_text,temp;
		for(int i=0;i<prod_names.size();i++) {
			
			prod_name_text=prod_names.get(i).getText();
			temp=prices.get(i).getText();
			price_text=temp.substring(1,temp.length()-1);
			m.put(prod_name_text, price_text);
		}
		
		int count=0;
		
		for(String[] x:arr) {
			
			if(!m.get(x[0]).equals(x[1])) {
				
				count++;
				test.log(LogStatus.FAIL, "Price of "+x[0]+" is "+m.get(x[0])+".But it should be "+x[1]);
				
			}
				
		}
		
		compareNumbers(count,0,"All prices getting displayed correctly in ACCESSORIES page");
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(LogStatus.FAIL, result.getThrowable().toString());
			test.log(
					LogStatus.INFO,
					"Snapshot below: "
							+ test.addScreenCapture(getScreenshot()));// screenshot
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(LogStatus.SKIP, result.getThrowable().toString());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(LogStatus.PASS, "All assertions have passed");
		}
		
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");

		extent.endTest(test);
		
		ob.quit();
	}
	
	
	
}
