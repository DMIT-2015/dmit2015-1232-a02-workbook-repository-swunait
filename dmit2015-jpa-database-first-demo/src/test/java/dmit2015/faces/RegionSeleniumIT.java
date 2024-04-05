package dmit2015.faces;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegionSeleniumIT {

    private static WebDriver driver;

    static List<String> sharedEditIds = new ArrayList<>();

    @BeforeAll
    static void beforeAllTests() {
        // You can download the latest version of Selenium Manager from https://github.com/SeleniumHQ/selenium/tree/trunk/common/manager
        // Use the command `./selenium-manager --browser chrome` to download the webdriver for Chrome browser
        // Use the command `./selenium-manager --browser firefox` to download the webdriver for Firefox browser
        // System.setProperty("webdriver.chrome.driver", "/home/user2015/.cache/selenium/chromedriver/linux64/119.0.6045.105/chromedriver");
        // System.setProperty("webdriver.gecko.driver", "/snap/bin/geckodriver");
        WebDriverManager.chromedriver().setup();
        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);
//        driver = new FirefoxDriver();

    }

    @AfterAll
    static void afterAllTests() {
        driver.quit();
    }

    @BeforeEach
    void beforeEachTestMethod() {

    }

    @AfterEach
    void afterEachTestMethod() throws InterruptedException {
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        Thread.sleep(1000);
    }

    private void setTextValue(String fieldId, String fieldValue) {
        WebElement element = driver.findElement(By.id(fieldId));
        element.clear();
        element.sendKeys(fieldValue);
    }

    private void setPrimeFacesDatePickerValue(String fieldId, String fieldValue) {
        // The text field for the p:datePicker component has a suffix of "_input" appended to the end of the field id.
        final String datePickerTextFieldId = String.format("%_input", fieldId);
        WebElement element = driver.findElement(By.id(datePickerTextFieldId));
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.chord(Keys.BACK_SPACE));
        element.clear();
        element.sendKeys(fieldValue);
        element.sendKeys(Keys.chord(Keys.TAB));
    }

    private void setPrimeFacesSelectOneMenuValue(String fieldId, String fieldValue) {
        // The id of the element to click for p:selectOneMenu has a suffix of "_label" appended to the id of the p:selectOneMenu
        String selectOneMenuId = String.format("%s_label", fieldId);
        driver.findElement(By.id(selectOneMenuId)).click();
        // Wait until 3 seconds for select items to pop up
        var waitSelectOneMenu = new WebDriverWait(driver, Duration.ofSeconds(3));
        // The id of the items for p:selectOneMenu has a suffix of "_items" appended to the id of the p:selectOneMenu
        String selectOneMenuItemsId = String.format("%s_items", fieldId);
        var selectOneMenuItems = waitSelectOneMenu.until(ExpectedConditions.visibilityOfElementLocated(By.id(selectOneMenuItemsId)));
        // The value for each item is stored a attribute named "data-label"
        String selectItemXPath = String.format("*[@data-label=\"%s\"]", fieldValue);
        var selectItem = selectOneMenuItems.findElement(By.xpath(selectItemXPath));
        selectItem.click();
    }

    private int clickOnActionLink(String idValue, WebElement tableElement, String actionLinkId) {
        // Find the total number of pages in the table paginator
        int totalPages = driver.findElements(By.className("ui-paginator-page")).size();
        // Set the current page to 1
        int currentPage = 1;

        boolean found = false;
        final String xPathQueryForIdValue = String.format("tbody/tr[td='%s']", idValue);

        while (!found && currentPage <= totalPages) {
            try {
                var tdWithIdValue = tableElement.findElement(By.xpath(xPathQueryForIdValue));
                found = true;
            } catch (Exception ex) {
                currentPage += 1;
                // Click on the next page link
                driver.findElement(By.className("ui-paginator-next")).click();
//                driver.findElement(By.xpath("/html/body/form/div/div[2]/a[3]")).click();
//                driver.findElement(By.xpath("//a[aria-label='Next Page']")).click();
            }
        }


        // Check if the table contains a row with the primary key id value.
        var rows = tableElement.findElements(By.xpath("./tbody/tr"));
        var rowIndex = -1;
        for (int index = 0; index < rows.size(); index++) {
            WebElement row = rows.get(index);
            var idCell = row.findElement(By.xpath(".//td[1]"));
            var idCellText = idCell.getText();
            if (Objects.equals(idCellText, idValue)) {
                rowIndex = index;
                index = rows.size(); // use to exit loop
            }
        }

        if (rowIndex != -1 && actionLinkId != null) {
            String xpathExpressionForActionLink = String.format(".//a[contains(@id,'%s')]", actionLinkId);
            var detailsLink = rows.get(rowIndex).findElement(By.xpath(xpathExpressionForActionLink));
            detailsLink.click();
        }

        return rowIndex;
    }

    @Order(1)
    @ParameterizedTest
    @CsvSource(value = {
            "regionName, Canada",
            "regionName, Alberta",
            "regionName, Edmonton",
    })
    void shouldCreate(
            String regionNameId, String regionNameValue
    ) throws InterruptedException {

        driver.get("http://localhost:8080/regions/create.xhtml");
        // Maximize the browser window to see the data being inputted
        driver.manage().window().maximize();

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - Create");

        // Set the value for each form field
        setTextValue(regionNameId, regionNameValue);
        // setPrimeFacesSelectOneMenuValue(field2Id, field2Value);
        // setPrimeFacesDatePickerValue(field1Id, field1Value);
//        setTextValue(field2Id, field2Value);
//        setTextValue(field3Id, field3Value);

        Thread.sleep(1000);

        // Find the create button and click on it
        driver.findElement(By.id("createButton")).click();

        // Wait for 3 seconds and verify navigate has been redirected to the listing page
        var wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        var facesMessages = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ui-messages-info-summary")));
        // Verify the title of the page
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - List");
        // Verify the feedback message is displayed in the page
        String feedbackMessage = facesMessages.getText();
        assertThat(feedbackMessage)
                .containsIgnoringCase("Create was successful.");
        // The primary key of the entity is at the end of the feedback message after the period
        final int indexOfPrimaryKeyValue = feedbackMessage.indexOf(".") + 2;
        // Extract the primary key for re-use if we need to edit or delete the entity
        sharedEditIds.add(feedbackMessage.substring(indexOfPrimaryKeyValue).replaceAll(",",""));

    }

    @Order(2)
    @ParameterizedTest
    // TODO Change the test data below
    @CsvSource({
            "0, Canada",
            "1, Alberta",
            "2, Edmonton",
    })
    void shouldList(
            int idIndex,
            String expectedRegionName
    ) throws InterruptedException {
        String expectedIdValue = sharedEditIds.get(idIndex);
        // Open a browser and navigate to the index page
        driver.get("http://localhost:8080/regions/index.xhtml");
        // Maximize the browser window so we can see the data being inputted
        driver.manage().window().maximize();

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - List");

        // find the table element
        // xPath for table: /html/body/form/div/div[1]/table
        var tableElement = driver.findElement(By.xpath("/html/body/form/div/div[1]/table"));

        int rowNumber = clickOnActionLink(expectedIdValue, tableElement, null) + 1;

        // /html/body/form/div/div[1]/table/tbody/tr[1]
        final String rowXPath = String.format("tbody/tr[%d]", rowNumber);
        var rowElement = tableElement.findElement(By.xpath(rowXPath));
        var rowColumns = rowElement.findElements(By.xpath("td"));
        final String regionIdValue = rowColumns.get(0).getText();
        final String regionNameValue = rowColumns.get(1).getText();

        assertThat(regionIdValue)
                .isEqualToIgnoringCase(expectedIdValue);
        assertThat(regionNameValue)
                .isEqualToIgnoringCase(expectedRegionName);

        // Verify that clicking on the edit link navigates to the Edit page
        rowNumber -= 1;
        driver.findElements(By.xpath("//a[contains(@id,'editLink')]")).get(rowNumber).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - Edit");
        Thread.sleep(1000);
        // Navigate back to the listing page
        driver.navigate().back();

        // Verify that clicking on the details link navigates to the Details page
        driver.findElements(By.xpath("//a[contains(@id,'detailsLink')]")).get(rowNumber).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - Details");
        Thread.sleep(1000);
        // Navigate back to the listing page
        driver.navigate().back();

        // Verify that clicking on the details link navigates to the Delete page
        driver.findElements(By.xpath("//a[contains(@id,'deleteLink')]")).get(rowNumber).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - Delete");
        Thread.sleep(1000);
        // Navigate back to the listing page
        driver.navigate().back();
    }

    @Order(3)
    @ParameterizedTest
    @CsvSource({
            "0, editId, regionName, Canada",
            "1, editId, regionName, Alberta",
            "2, editId, regionName, Edmonton"
    })
    void shouldDetails(
            int idIndex,
            String editId,
            String regionNameId, String expectedRegionName
    ) throws InterruptedException {
        String expectedIdValue = sharedEditIds.get(idIndex);
        // Open a browser and navigate to the index page
        driver.get("http://localhost:8080/regions/index.xhtml");
        // Maximize the browser window so we can see the data being inputted
        driver.manage().window().maximize();

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - List");

        // find the table element
        // xPath for table: /html/body/form/div/div[1]/table
        var tableElement = driver.findElement(By.xpath("/html/body/form/div/div[1]/table"));

        clickOnActionLink(expectedIdValue, tableElement, "detailsLink");
        Thread.sleep(1000);

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - Details");

        var actualRegionId = driver.findElement(By.id(editId)).getText();
        var actualRegionName = driver.findElement(By.id(regionNameId)).getText();

        assertThat(actualRegionId)
                .isEqualToIgnoringCase(expectedIdValue);
        assertThat(actualRegionName)
                .isEqualToIgnoringCase(expectedRegionName);


    }

    @Order(4)
    @ParameterizedTest
    @CsvSource({
            "0, editId, regionName, New Canada",
            "1, editId, regionName, New Alberta",
            "2, editId, regionName, New Edmonton"
    })
    void shouldEdit(
            int idIndex,
            String editId,
            String regionNameId, String regionNameValue
    ) throws InterruptedException {

        String expectedIdValue = sharedEditIds.get(idIndex);
        // Open a browser and navigate to the index page
        driver.get("http://localhost:8080/regions/index.xhtml");
        // Maximize the browser window so we can see the data being inputted
        driver.manage().window().maximize();

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - List");

        // find the table element
        // xPath for table: /html/body/form/div/div[1]/table
        var tableElement = driver.findElement(By.xpath("/html/body/form/div/div[1]/table"));

        clickOnActionLink(expectedIdValue, tableElement, "editLink");
        Thread.sleep(1000);

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - Edit");

        // Verify ID of entity to edit
        var idFieldValue = driver.findElement(By.id(editId)).getAttribute("value");
        assertThat(idFieldValue)
                .isEqualToIgnoringCase(expectedIdValue);

        // Set the value for each form field
        setTextValue(regionNameId, regionNameValue);

        Thread.sleep(1000);

        driver.manage().window().maximize();
        driver.findElement(By.id("updateButton")).click();

        var wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        var feedbackMessages = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-messages-info-summary")));
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - List");
        assertThat(feedbackMessages.getText())
                .containsIgnoringCase("Update was successful.");
    }


    @Order(5)
    @ParameterizedTest
    @CsvSource({
            "0,editId",
            "1,editId",
            "2,editId",
    })
    void shouldDelete(
            int idIndex,
            String idField
    ) throws InterruptedException {
        String expectedIdValue = sharedEditIds.get(idIndex);
        // Open a browser and navigate to the index page
        driver.get("http://localhost:8080/regions/index.xhtml");
        // Maximize the browser window so we can see the data being inputted
        driver.manage().window().maximize();

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - List");

        // find the table element
        // xPath for table: /html/body/form/div/div[1]/table
        var tableElement = driver.findElement(By.xpath("/html/body/form/div/div[1]/table"));

        clickOnActionLink(expectedIdValue, tableElement, "deleteLink");
        Thread.sleep(1000);

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - Delete");

        // Verify ID of entity to edit
        var idFieldValue = driver.findElement(By.id(idField)).getText();
        assertThat(idFieldValue)
                .isEqualToIgnoringCase(expectedIdValue);

        driver.findElement(By.id("deleteButton")).click();

        var wait = new WebDriverWait(driver, Duration.ofSeconds(1));

        var yesConfirmationButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-confirmdialog-yes")));
        Thread.sleep(1000);
        yesConfirmationButton.click();

        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        var feedbackMessages = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-messages-info-summary")));
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Region - List");
        assertThat(feedbackMessages.getText())
                .containsIgnoringCase("Delete was successful.");

    }

}