package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {
    private WebDriver driver;
    private final WebDriverWait wait;

    //Locator
    private final By avatarImg = By.id("avatar-img");
    private final By fullName = By.id("fullname");
    private final By genderSelect = By.id("gender");
    private final By email = By.id("email");
    private final By phone = By.id("phone");
    private final By address = By.id("address");
    private final By birthday = By.id("birthday");
    private final By saveBtn = By.className("save-btn");


    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(avatarImg));
    }
    public ProfilePage open() {
        driver.get("http://localhost:5500/src/main/webapp/pages/profile.html");
        return this;
    }


    public boolean isAvatarDisplayed() {
        return driver.findElement(avatarImg).isDisplayed();
    }
    public By getFullName() {
        return fullName;
    }

    public By getGenderSelect() {
        return genderSelect;
    }

    // Lấy text của giới tính đang chọn ("Nam" hoặc "Nữ")
    public String getSelectedGenderText() {
        WebElement genderElement = driver.findElement(genderSelect);
        Select select = new Select(genderElement);
        return select.getFirstSelectedOption().getText();
    }

    public By getEmail() {
        return email;
    }

    public By getPhone() {
        return phone;
    }

    public By getAddress() {
        return address;
    }

    public By getBirthday() {
        return birthday;
    }

    public boolean isFullNameDisplayed() {
        return driver.findElement(fullName).isDisplayed();
    }

    public boolean isGenderDisplayed() {
        return driver.findElement(genderSelect).isDisplayed();
    }

    public boolean isEmailDisplayed() {
        return driver.findElement(email).isDisplayed();
    }

    public boolean isPhoneDisplayed() {
        return driver.findElement(phone).isDisplayed();
    }

    public boolean isAddressDisplayed() {
        return driver.findElement(address).isDisplayed();
    }

    public boolean isBirthdayDisplayed() {
        return driver.findElement(birthday).isDisplayed();
    }
    public boolean isSaveBtnDisplayed() {
        return driver.findElement(saveBtn).isDisplayed();
    }

    public void fillFullName(String fullNameChange) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(fullName));
        input.clear();
        input.sendKeys(fullNameChange);
    }
    public void fillGender(String genderChange) {
        WebElement genderElement = wait.until(ExpectedConditions.visibilityOfElementLocated(genderSelect));

        // Dùng Select để chọn giá trị
        Select select = new Select(genderElement);

        if (genderChange.equalsIgnoreCase("Nam") || genderChange.equalsIgnoreCase("true")) {
            select.selectByValue("true");
        } else if (genderChange.equalsIgnoreCase("Nữ") || genderChange.equalsIgnoreCase("false")) {
            select.selectByValue("false");
        } else {
            throw new IllegalArgumentException("Giới tính không hợp lệ: " + genderChange);
        }
    }
    public void fillEmail(String emailChange) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(email));
        input.clear();
        input.sendKeys(emailChange);
    }
    public void fillPhone(String phoneChange) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(phone));
        input.clear();
        input.sendKeys(phoneChange);
    }
    public void fillAddress(String addressChange) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(address));
        input.clear();
        input.sendKeys(addressChange);
    }
    public void fillBirthday(String birthdayChange) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(birthday));
        input.clear();
        input.sendKeys(birthdayChange);
    }

    public boolean isAlertMessageDisplayed(String expectedMessage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        By loginAlertLocator = By.id("loginAlert"); // Giả định locator là id="loginAlert"
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginAlertLocator));

        WebElement alert = driver.findElement(loginAlertLocator);
        String actualMessage = alert.getText().trim();

        // Kiểm tra thêm lớp d-none để chắc chắn thông báo hiển thị
        boolean isVisible = !alert.getAttribute("class").contains("d-none");
        return isVisible && actualMessage.equals(expectedMessage);
    }

    public void clickSaveBtn() {
        driver.findElement(saveBtn).click();
    }
}
