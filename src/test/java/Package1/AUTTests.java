package Package1;
import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.Assert;
import io.github.bonigarcia.wdm.WebDriverManager;





public class AUTTests {

	static WebDriver driver = null;
	//static String appURL = "http://3.13.172.238:8080/DevOpsDemoApplication";
	//static String browserName = "Chrome";
	//static String chromeDriverPath = "./drivers/chromedriver.exe";
	static int tcserialnumber;
	static String tcid;
	static String tcname;
	static String tcstatus;
	static String tcerrdesc;
	static String username = "admin";
	static String password = "admin";
	static String extentbasedirectory = "./reports/";
	static ExtentHtmlReporter htmlreporter = null;
	static ExtentReports extentreports = null;
	
	
	@Parameters({"Browser", "AppUrl"})
	@BeforeTest
	public static void SetUp(String browserName, String appURL) throws Exception
	{
		//System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		/*ChromeOptions options = new ChromeOptions();
		options.addArguments("enable-automation");
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--disable-extensions");
		options.addArguments("--dns-prefetch-disable");
		options.addArguments("--disable-gpu");
		options.setPageLoadStrategy(PageLoadStrategy.NORMAL);*/
		
		tcserialnumber = 0;
		htmlreporter = new ExtentHtmlReporter(extentbasedirectory +"extentreports.html");
		htmlreporter.config().setTheme(Theme.DARK);
		htmlreporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlreporter.config().setChartVisibilityOnOpen(true);
		htmlreporter.config().setDocumentTitle("Kovair Demo Application Test Report");
		htmlreporter.config().setReportName("Kovair Demo Application Test Report");
		
		extentreports = new ExtentReports();
		extentreports.attachReporter(htmlreporter);
		
		System.out.println("Parameter value for Browser is "+browserName);
		System.out.println("Parameter value AppUrl is "+appURL);
		
		if(browserName.equals("Chrome"))
		{
			WebDriverManager.chromedriver().setup();
			 driver=new ChromeDriver();
		}
		
		else if(browserName.equals("Firefox"))
		{
			WebDriverManager.firefoxdriver().setup();
			driver=new FirefoxDriver();
		}
		
		else if(browserName.equals("Edge"))
		{
			WebDriverManager.edgedriver().setup();
			driver=new EdgeDriver();
		}
		
		else 
		{
			System.out.println("We do not support this browser");
		}
		
		driver.manage().window().maximize();
		driver.get(appURL);
		Thread.sleep(3000);
	}
	
	
	@BeforeMethod
	public static void bmethod()
	{
		tcserialnumber++;
		tcid = "";
		tcname = "";
		tcstatus = "";
		tcerrdesc = "";
		
	}
	
	@Test(priority = 1)
	public static void logintest()
	{
		tcid = "TestCase_" + tcserialnumber;
		tcname = "Verify that user can log in to the application with proper credentials";
		ExtentTest test = extentreports.createTest(tcname);
		try
		{
			WebElement usernametxt = driver.findElement(By.id("txtUserId"));
			WebElement passwordtxt = driver.findElement(By.id("txtPassword"));
			WebElement loginbtn = driver.findElement(By.id("btnClick"));
			usernametxt.sendKeys(username);
			Thread.sleep(2000);
			passwordtxt.sendKeys(password);
			Thread.sleep(2000);
			loginbtn.click();
			Thread.sleep(6000);
			
			WebElement kovairlogo = driver.findElement(By.className("navbar-brand"));
			if(kovairlogo.isDisplayed())
			{
				tcstatus = "Passed";
				tcerrdesc = "";
				test.pass("");
                                
			}
			else
			{
				tcstatus = "Failed";
				tcerrdesc = "User not logged in successfully";
				//String imgpath = capture(driver,"scr1");
				test.fail(tcerrdesc);
                Assert.fail();
				//test.addScreenCaptureFromPath(imgpath);
			}
		}
		catch(Exception e)
		{
			tcstatus = "Not executed";
			tcerrdesc = e.getMessage();
		}
	}
	
	@Test(priority = 2)
	public static void empbasicdetailstest()
	{
		tcid = "TestCase_" + tcserialnumber;
		tcname = "Verify that name of the logged in employee is properly displayed under Employee Basic Details";
		String expectedname = "Patty Brown";
		String actualname = "";
		ExtentTest test = extentreports.createTest(tcname);
		try
		{
			actualname = driver.findElement(By.xpath("//div[@id='emp-basic-info']/h2")).getText();
			if(expectedname.equals(actualname))
			{
				tcstatus = "Passed";
				tcerrdesc = "";
				test.pass("");
			}
			else
			{
				tcstatus = "Failed";
				tcerrdesc = "Logged in Employee name is not getting displayed";
				//String imgpath = capture(driver,"scr2");
				test.fail(tcerrdesc);
				//test.addScreenCaptureFromPath(imgpath);
                Assert.fail();
			}
		}
		catch(Exception e)
		{
			tcstatus = "Not executed";
			tcerrdesc = e.getMessage();
		}
	}
	
	@Test(priority = 3)
	public static void findEmployeetest()
	{
		tcid = "TestCase_" + tcserialnumber;
		tcname = "Verify that a specific employee: Somesh Roy is already present in the list of all employees";
		ExtentTest test = extentreports.createTest(tcname);
		String employeename = "Somesh Roy";
		String actualfirstname = "";
		String actuallastname = "";
		String actualemployeename = "";
		try
		{
			int totalempsize = driver.findElements(By.xpath("//div[@class='ui-grid-canvas']/div")).size();
			int flag = 0;
			for(int i = 1; i<=totalempsize;i++)
			{
				actualfirstname = driver.findElement(By.xpath("//div[@class='ui-grid-canvas']/div["+i+"]/div/div[1]/div")).getText();
				actuallastname = driver.findElement(By.xpath("//div[@class='ui-grid-canvas']/div["+i+"]/div/div[2]/div")).getText();
				
				actualemployeename = actualfirstname.trim() + " " + actuallastname.trim();
				
				if(actualemployeename.trim().equals(employeename))
				{
					flag = 1;
					break;
				}
			}
			
			if(flag == 1)
			{
				tcstatus = "Passed";
				tcerrdesc = "";
				test.pass("");
			}
			else
			{
				tcstatus = "Failed";
				tcerrdesc = employeename + " not found in all employee list";
				//String imgpath = capture(driver,"scr3");
				test.fail(tcerrdesc);
				//test.addScreenCaptureFromPath(imgpath);
                Assert.fail();
			}
			
		}
		catch(Exception e)
		{
			tcstatus = "Not executed";
			tcerrdesc = e.getMessage();
		}
	}
	
	@AfterMethod
	public static void amethod()
	{
		System.out.println("TC ID: " + tcid);
		System.out.println("TC Name: " + tcname);
		System.out.println("TC Status: " + tcstatus);
		System.out.println("TC Error Description: " + tcerrdesc);
	}
	
	@AfterTest
	public static void teardown()
	{
		driver.quit();
		extentreports.flush();
	}
	
	
	public static String capture(WebDriver driver, String name) throws IOException
	{
            String imagepath = "./reports/";
            TakesScreenshot ts = (TakesScreenshot)driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            File destination = new File((String) imagepath + name + ".png");

            FileUtils.copyFile(source,destination);

            return "./" +name + ".png";
	}
	
}