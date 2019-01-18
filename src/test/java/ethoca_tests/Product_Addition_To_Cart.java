package ethoca_tests;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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

public class Product_Addition_To_Cart extends TestBase{

	WebDriver ob;
	
	@BeforeMethod
	public void beforeMethod() {
		
		extent = ExtentManager.getReporter(filePath);
		configureExtentReport(extent);
		test = extent.startTest(this.getClass().getSimpleName(), "Validate that user is able to add MAGIC MOUSE product to cart successfully").assignCategory(
				"Smoke");
		
	}
	

	@Test
	public void test1() throws InterruptedException, IOException {
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		ob=openBrowser();
		moveToParticularElement(ob,OR.getProperty("product_category_menu_bar_link"),"product_category_menu_bar_link");
		moveToParticularElementAndClick(ob, OR.getProperty("accessories_drop_down_menu_link"),"accessories_drop_down_menu_link");
		clickElement(ob,OR.getProperty("magic_mouse_add_to_cart_button"),"magic_mouse_add_to_cart_button");
		customisedWait(ob, OR.getProperty("product_added_notification"), "product_added_notification");
		String cart_icon_text=getTextOfElement(ob, OR.getProperty("cart_icon"), "cart_icon");
		validateThatStringContains(cart_icon_text, "1 item", "Cart icon message getting displayed correctly");
		clickElement(ob, OR.getProperty("cart_icon"), "cart_icon");
		String cart_message=getTextOfElement(ob, OR.getProperty("cart_message_label"), "cart_message_label");
		validateThatTextIsNotEqualTo(cart_message, "Oops, there is nothing in your cart.");
		
		
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
