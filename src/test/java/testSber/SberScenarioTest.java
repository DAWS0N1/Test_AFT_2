package testSber;

import base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;



public class SberScenarioTest extends BaseTest {

    @ParameterizedTest(name = "testSber [{index}]")
    @CsvSource({"Первый,Иван,Петрович,12.12.1998,example@mail.ru,(999) 999-99-99",
            "Второй,Иван,Петрович,12.12.1998,example2@mail.ru,(999) 666-99-99",
            "Третий,Иван,Петрович,12.12.1998,example3@mail.ru,(999) 999-66-99"})
    void sberScenarion(String lName, String fName, String mName, String bDate, String email, String phone) throws InterruptedException {
        // выбрать пункт меню - "Карты"
        String insuranceButtonXPath = "//a[@aria-label='Меню  Карты']";
        WebElement insuranceButton = driver.findElement(By.xpath(insuranceButtonXPath));
        insuranceButton.click();

        // выбрать пункт подменю - "Дебетовые карты"
        String sberInsuranceButtonXPath = "//a[text()='Дебетовые карты' and contains(@class, 'link_second')]";
        WebElement travellersInsuranceButton = driver.findElement(By.xpath(sberInsuranceButtonXPath));
        travellersInsuranceButton.click();

        // проверка заголовка
        String headerInsuranceTextXPath = "//h1[@data-test-id='Product_catalog_header']";
        WebElement headerInsuranceText = driver.findElement(By.xpath(headerInsuranceTextXPath));
        waitUtilElementToBeVisible(headerInsuranceText);
        Assertions.assertEquals("Дебетовые карты", headerInsuranceText.getText(),
                "Заголовок отсутствует/не соответствует требуемому");


        // выбрать кнопку - "Заказать онлайн" молодёжной карты
        String insuranceCardButtonXPath = "//a[@data-product='Молодёжная карта' and contains(@class, 'button')]";
        WebElement insuranceCardButton = driver.findElement(By.xpath(insuranceCardButtonXPath));
        scrollToElementJs(insuranceCardButton);
        waitToVisibility();
        insuranceCardButton.click();


        // заполнить поля данными
        String fieldXPath = "//input[@data-name='%s']";
        fillInputFirstField(driver.findElement(By.xpath(String.format(fieldXPath, "lastName"))), lName);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "firstName"))), fName);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "middleName"))), mName);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "birthDate"))), bDate);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "email"))), email);
        fillInputFieldPhone(driver.findElement(By.xpath(String.format(fieldXPath, "phone"))), phone);


        String insuranceButtonNextXPath = "//button[@data-step='2']";
        WebElement insuranceButtonNext = driver.findElement(By.xpath(insuranceButtonNextXPath));
        waitToVisibility();
        insuranceButtonNext.click();

        String errorFieldXPath = "//label[text()='%s']/following-sibling::div[@class='odcui-error__text' and text()]";
        checkErrorMessageAtField(driver.findElement(By.xpath(String.format(errorFieldXPath, "Серия"))), "Обязательное поле");
        checkErrorMessageAtField(driver.findElement(By.xpath(String.format(errorFieldXPath, "Номер"))), "Обязательное поле");
        checkErrorMessageAtField(driver.findElement(By.xpath(String.format(errorFieldXPath, "Дата выдачи"))), "Обязательное поле");
    }

    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    private void waitUtilElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private void fillInputFirstField(WebElement element, String value) throws InterruptedException {
        scrollToElementJs(element);
        waitToVisibility();
        element.click();
        element.sendKeys(value);
        Assertions.assertEquals(value, element.getAttribute("value"), "Поле было заполнено некорректно");
    }
    private void fillInputField(WebElement element, String value) throws InterruptedException {
        scrollToElementJs(element);
        waitUtilElementToBeVisible(element);
        element.click();
        element.sendKeys(value);
        Assertions.assertEquals(value, element.getAttribute("value"), "Поле было заполнено некорректно");
    }

    private void fillInputFieldPhone(WebElement element, String value) throws InterruptedException {
        scrollToElementJs(element);
        waitToVisibility();
        element.click();
        element.sendKeys(value);
        Assertions.assertEquals("+7 " + value, element.getAttribute("value"),
                "Поле было заполнено некорректно");
    }

    private void waitToVisibility() throws InterruptedException {
        Thread.sleep(1000);
    }

    private void checkErrorMessageAtField(WebElement element, String errorMessage) {
        Assertions.assertEquals(errorMessage, element.getText(),
                "Проверка ошибки у поля не была пройдена");
    }
}
