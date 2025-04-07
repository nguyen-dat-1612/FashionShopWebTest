package org.example.utils;

import org.example.pages.HeaderComponent;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class LoginHelper {
    private WebDriver driver;
    private HeaderComponent header;

    public LoginHelper(WebDriver driver) {
        this.driver = driver;
        this.header = new HeaderComponent(driver);
    }

    public void login(String email, String password) {
        header.clickLoginButton();
        header.fillLoginForm(email, password);
        header.submitLoginForm();

        // Giả lập đăng nhập thành công
        ((JavascriptExecutor) driver).executeScript(
                "localStorage.setItem('isLoggedIn', 'true');" +
                        "document.getElementById('profileIcon').classList.remove('d-none');" +
                        "document.getElementById('cartIcon').classList.remove('d-none');"
        );
    }

    public void logout() {
        // Giả lập đăng xuất
        ((JavascriptExecutor) driver).executeScript(
                "localStorage.removeItem('isLoggedIn');" +
                        "document.getElementById('profileIcon').classList.add('d-none');" +
                        "document.getElementById('cartIcon').classList.add('d-none');"
        );
    }
}