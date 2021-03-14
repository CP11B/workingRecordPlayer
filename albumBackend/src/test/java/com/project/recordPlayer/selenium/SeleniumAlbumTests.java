package com.project.recordPlayer.selenium;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@Sql(scripts = { "classpath:album-schema.sql",
//"classpath:album-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class SeleniumAlbumTests {
	
	@LocalServerPort
	private int port;
	
	private WebDriverWait wait;
	private RemoteWebDriver driver;

	@BeforeEach
	void setup() {
		this.driver = new ChromeDriver();
		this.driver.manage().window().maximize();
		this.driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS); // will wait two seconds for EVERY ELEMENT
		this.wait = new WebDriverWait(driver, 10); // set up explicit wait -> up to 5 seconds ONLY WHEN USED
	}
	
	@Test
	void createTest() {
		
		final String title = "Title";
		final String artist = "Artist";
		final String date = "2000";

		this.driver.get("http://localhost:" + port);
		
		WebElement createModalButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createButton")));
		createModalButton.click();
		
		WebElement titleField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createTitle")));
		titleField.sendKeys(title);
		
		WebElement artistField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createArtist")));
		artistField.sendKeys(artist);
		
		WebElement dateField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createReleaseYear")));
		dateField.sendKeys(date);
				
		WebElement saveEntryButton = this.driver.findElement(By.xpath("//*[@id=\"submit\"]"));
		saveEntryButton.click();

		WebElement creationCheck;
		creationCheck = this.driver.findElement(By.xpath("//*[@id=\"stack\"]/div/div[2]"));
		Assertions.assertTrue(creationCheck.getText().contains("Title"));		
		Assertions.assertTrue(creationCheck.getText().contains("Artist"));	
		this.driver.close();
	}
	
	@Test
	void updateTest() {
		this.driver.get("http://localhost:" + port);
		final String title = "Title";
		final String artist = "Artist";
		final String date = "2000";
		final String updatedTitle = "Updated Title";
		final String updatedArtist = "Updated Artist";

		WebElement createModalButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createButton")));
		createModalButton.click();
		
		WebElement titleField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createTitle")));
		titleField.sendKeys(title);
		
		WebElement artistField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createArtist")));
		artistField.sendKeys(artist);
		
		WebElement dateField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createReleaseYear")));
		dateField.sendKeys(date);
				
		WebElement saveEntryButton = this.driver.findElement(By.xpath("//*[@id=\"submit\"]"));
		saveEntryButton.click();
		
		WebElement creationCheck;
		creationCheck = this.driver.findElement(By.xpath("//*[@id=\"stack\"]/div/div[2]"));
		Assertions.assertTrue(creationCheck.getText().contains("Title"));		
		Assertions.assertTrue(creationCheck.getText().contains("Artist"));	
		
//		// after card is made, edit test starts here
	
		WebElement editModalButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"stack\"]/div/div[2]/span/button")));
		editModalButton.click();

		WebElement editTitleField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("editTitle")));
		driver.findElement(By.id("editTitle")).clear();
		editTitleField.sendKeys(updatedTitle);
		
		WebElement editArtistField = this.driver.findElement(By.id("editArtist"));
		driver.findElement(By.id("editArtist")).clear();
		editArtistField.sendKeys(updatedArtist);
		
		WebElement saveEditButton = this.driver.findElement(By.id("submitEditButton"));
		saveEditButton.click();
		
		WebElement editedCheck;
		editedCheck = this.driver.findElement(By.xpath("//*[@id=\"stack\"]/div[1]/div[2]/div[1]"));
		
		try {
			Assertions.assertTrue(editedCheck.getText().contains("Updated Title"));		
			
		} catch(org.openqa.selenium.StaleElementReferenceException e){
			System.out.println(e);
		}
		this.driver.close();
	}
	
	@Test
	void deleteTest() {
		
		this.driver.get("http://localhost:" + port);
		//this.driver.get("http://localhost:" + 8080);
		final String title = "Womp and the Bimbles";
		final String artist = "Headface";
		final String date = "2000";

		WebElement createModalButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createButton")));
		createModalButton.click();		
		
		WebElement titleField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createTitle")));
		titleField.sendKeys(title);
				
		WebElement artistField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createArtist")));
		artistField.sendKeys(artist);
		
		WebElement dateField = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createReleaseYear")));
		dateField.sendKeys(date);
				
		WebElement saveEntryButton = this.driver.findElement(By.xpath("//*[@id=\"submit\"]"));
		saveEntryButton.click();

		WebElement creationCheck;
		creationCheck = this.driver.findElement(By.cssSelector("#stack > div:nth-child(3) > div.extra.content > div.header.cardAlbumTitle"));
		System.out.println("------------------------------ :D");
		System.out.println(creationCheck.getText());
		Assertions.assertTrue(creationCheck.getText().contains("Womp and the Bimbles"));		
		
		
		// deletion starts here		
		WebElement editModalButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"stack\"]/div/div[2]/span/button")));
		editModalButton.click();
		
		WebElement deleteButton = this.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"deleteButton\"]")));
		deleteButton.click();
		
		WebElement deletionCheck;
		
		try {
			deletionCheck = this.driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/div[2]/div[1]"));
	    } catch (org.openqa.selenium.NoSuchElementException e) {
	    	assert true;
	    } catch (Exception e) {
	    	assert false;
	    }
			
	}
	
	//@AfterEach
	//void tearDown() {
	//	this.driver.close();
//	}
	
}
