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
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JobSeleniumIT {

    private static WebDriver driver;

    @BeforeAll
    static void beforeAllTests() {
        WebDriverManager.chromedriver().setup();
        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);

        // https://www.omgubuntu.co.uk/2022/04/how-to-install-firefox-deb-apt-ubuntu-22-04
//        WebDriverManager.firefoxdriver().setup();
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
        final String datePickerTextFieldId = String.format("%s_input", fieldId);
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
        var selectOneMenuItems = waitSelectOneMenu.until(ExpectedConditions.presenceOfElementLocated(By.id(selectOneMenuItemsId)));
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
                var pageNextLink = driver.findElement(By.className("ui-paginator-next"));
                if (pageNextLink != null && pageNextLink.isEnabled()) {
                    pageNextLink.click();
                }
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
    // TODO Change the test data below
    @CsvSource(value = {
            "field1Id, field1Value, field2Id, field2Value, field3Id, field3Value,",
            "field1Id, field1Value, field2Id, field2Value, field3Id, field3Value,",
            "field1Id, field1Value, field2Id, field2Value, field3Id, field3Value,",
    })
    void shouldCreate(
            String field1Id, String field1Value,
            String field2Id, String field2Value,
            String field3Id, String field3Value
    ) throws InterruptedException {

        driver.get("http://localhost:8080/jobs/create.xhtml");
        // Maximize the browser window to see the data being inputted
        driver.manage().window().maximize();

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Job - Create");

        // Set the value for each form field
        setTextValue(field1Id, field1Value);
        // setPrimeFacesSelectOneMenuValue(field2Id, field2Value);
        // setPrimeFacesDatePickerValue(field1Id, field1Value);
        setTextValue(field2Id, field2Value);
        setTextValue(field3Id, field3Value);

        Thread.sleep(1000);

        // Find the create button and click on it
        driver.findElement(By.id("createButton")).click();

        // Wait for 3 seconds and verify navigate has been redirected to the listing page
        var wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        var facesMessages = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-messages-info-summary")));
        // Verify the title of the page
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Job - List");
        // Verify the feedback message is displayed in the page
        String feedbackMessage = facesMessages.getText();
        assertThat(feedbackMessage)
                .containsIgnoringCase("Create was successful.");
        // The primary key of the entity is at the end of the feedback message after the period
        final int indexOfPrimaryKeyValue = feedbackMessage.indexOf(".") + 2;

    }

    @Order(2)
    @ParameterizedTest
    // TODO Change the test data below
    @CsvSource({
            "ExpectedColumn1Value, ExpectedColumn2Value, ExpectedColumn3Value",
            "ExpectedColumn1Value, ExpectedColumn2Value, ExpectedColumn3Value",
            "ExpectedColumn2Value, ExpectedColumn2Value, ExpectedColumn3Value",
    })
    void shouldList(
            String idValue,
            String expectedColumn1Value, String expectedColumn2Value, String expectedColumn3Value
    ) throws InterruptedException {
        // Open a browser and navigate to the page to list entities
        driver.get("http://localhost:8080/jobs/index.xhtml");
        // Maximize the browser window so we can see the data being inputted
        driver.manage().window().maximize();

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Job - List");

        // find the table element
        // xPath for table: /html/body/form/div/div[1]/table
        var tableElement = driver.findElement(By.xpath("//table[@role='grid']"));

        int rowNumber = clickOnActionLink(idValue, tableElement, null) + 1;

        final String rowXPath = String.format("tbody/tr[%d]", rowNumber);
        var rowElement = tableElement.findElement(By.xpath(rowXPath));
        var rowColumns = rowElement.findElements(By.xpath("td"));
        final String column1Value = rowColumns.get(0).getText();
        final String column2Value = rowColumns.get(1).getText();
        final String column3Value = rowColumns.get(2).getText();

        assertThat(column1Value)
                .isEqualToIgnoringCase(expectedColumn1Value);
        assertThat(column2Value)
                .isEqualToIgnoringCase(expectedColumn2Value);
        assertThat(column3Value)
                .isEqualToIgnoringCase(expectedColumn3Value);

        // Decrement rowNumber to be 0-based instead of 1 based
        rowNumber -= 1;

        // Verify that clicking on the edit link navigates to the Edit page
        driver.findElements(By.xpath("//a[contains(@id,'editLink')]")).get(rowNumber).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Job - Edit");
        Thread.sleep(1000);
        // Navigate back to the listing page
        driver.navigate().back();

        // Verify that clicking on the details link navigates to the Details page
        driver.findElements(By.xpath("//a[contains(@id,'detailsLink')]")).get(rowNumber).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Job - Details");
        Thread.sleep(1000);
        // Navigate back to the listing page
        driver.navigate().back();

        // Verify that clicking on the details link navigates to the Delete page
        driver.findElements(By.xpath("//a[contains(@id,'deleteLink')]")).get(rowNumber).click();
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Job - Delete");
        Thread.sleep(1000);
        // Navigate back to the listing page
        driver.navigate().back();
    }

    @Order(3)
    @ParameterizedTest
    // TODO Change the test data below
    @CsvSource({
            "editId, idValue, field2Id, expectedField2Value, field3Id, expectedField3Value",
            "editId, idValue, field2Id, expectedField2Value, field3Id, expectedField3Value",
            "editId, idValue, field2Id, expectedField2Value, field3Id, expectedField3Value",
    })
    void shouldDetails(
            String idField, String idValue,
            String field2Id, String expectedField2Value,
            String field3Id, String expectedField3Value
    ) throws InterruptedException {
        // Open a browser and navigate directly to the details page
        final String editUrl = String.format("http://localhost:8080/jobs/details.xhtml?editId=%s", idValue);
        driver.get(editUrl);
        // Maximize the browser window so we can see the data being inputted
        driver.manage().window().maximize();

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Job - Details");

        Thread.sleep(500);

        // TODO: change the form field names and values you are expecting
        var actualField1Value = driver.findElement(By.id(idField)).getText();
        var actualField2Value = driver.findElement(By.id(field2Id)).getText();
        var actualField3Value = driver.findElement(By.id(field3Id)).getText();
        assertThat(actualField1Value)
                .isEqualToIgnoringCase(idValue);
        assertThat(actualField2Value)
                .isEqualToIgnoringCase(expectedField2Value);
        assertThat(actualField3Value)
                .isEqualToIgnoringCase(expectedField3Value);

        Thread.sleep(500);

    }

    @Order(4)
    @ParameterizedTest
    // TODO Change the test data below
    @CsvSource({
            "editId, idValue, field1Id, expectedField1Value, field2Id, expectedField2Value, field3Id, expectedField3Value",
            "editId, idValue, field1Id, expectedField1Value, field2Id, expectedField2Value, field3Id, expectedField3Value",
            "editId, idValue, field1Id, expectedField1Value, field2Id, expectedField2Value, field3Id, expectedField3Value",
    })
    void shouldEdit(
            String idField, String expectedIdValue,
            String editableField1Id, String newEditableField1Value,
            String editableField2Id, String newEditableField2Value,
            String editableField3Id, String newEditableField3Value
    ) throws InterruptedException {

        // Open a browser and navigate directly to the edit page
        final String editUrl = String.format("http://localhost:8080/jobs/edit.xhtml?editId=%s", expectedIdValue);
        driver.get(editUrl);
        // Maximize the browser window so we can see the data being inputted
        driver.manage().window().maximize();


        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Job - Edit");

        // Verify ID of entity to edit
        var idFieldValue = driver.findElement(By.id(idField)).getAttribute("value");
        assertThat(idFieldValue)
                .isEqualToIgnoringCase(expectedIdValue);

        // Set the value for each form field
        setTextValue(editableField1Id, newEditableField1Value);
        // setPrimeFacesSelectOneMenuValue(field2Id, field2Value);
        // setPrimeFacesDatePickerValue(field2Id, field2Value);
        setTextValue(editableField2Id, newEditableField2Value);
        setTextValue(editableField3Id, newEditableField3Value);

        Thread.sleep(1000);

        driver.manage().window().maximize();
        driver.findElement(By.id("updateButton")).click();

        var wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        var feedbackMessages = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ui-messages-info-summary")));
        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Job - List");
        assertThat(feedbackMessages.getText())
                .containsIgnoringCase("Update was successful.");

        Thread.sleep(1000);
    }

    @Order(5)
    @ParameterizedTest
    // TODO Change the test data below
    @CsvSource({
            "editId,idFieldValue1",
            "editId,idFieldValue2",
            "editId,idFieldValue3",
    })
    void shouldDelete(
            String idField,
            String expectedIdValue
    ) throws InterruptedException {

        // Open a browser and navigate directly to the edit page
        final String editUrl = String.format("http://localhost:8080/jobs/delete.xhtml?editId=%s", expectedIdValue);
        driver.get(editUrl);
        // Maximize the browser window so we can see the data being inputted
        driver.manage().window().maximize();

        assertThat(driver.getTitle())
                .isEqualToIgnoringCase("Job - Delete");

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
                .isEqualToIgnoringCase("Job - List");
        assertThat(feedbackMessages.getText())
                .containsIgnoringCase("Delete was successful.");

        Thread.sleep(1000);

    }

}