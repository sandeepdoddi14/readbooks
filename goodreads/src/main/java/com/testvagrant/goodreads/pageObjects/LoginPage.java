package com.testvagrant.goodreads.pageObjects;

import com.sample.framework.uiautomation.base.TestBase;
import com.sample.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class LoginPage extends TestBase {
    GenericHelper genericHelper;
    @FindBy(id = "userSignInFormEmail")
    WebElement emailId;
    @FindBy(xpath = "//*[@id='user_password']")
    WebElement password;
    @FindBy(xpath = "//*[@id='sign_in']")
    WebElement signInButton;

    public LoginPage() {
        genericHelper = PageFactory.initElements(driver, GenericHelper.class);
    }

    public void signIn(String userEmailId, String userPassword) {
        genericHelper.setElementText(emailId, userEmailId, "Email Address");
        genericHelper.setElementText(password, userPassword, "Password");
        genericHelper.submitForm(signInButton, "Sign In");

        Assert.assertTrue(driver.getTitle().equals("Recent updates | Goodreads"), "Error in Logging In");
    }


    //read from config
    //logins as guest or user
    //based on settnig given in config.ini
    public void loginAsGuestOrUser() {
        if (Boolean.parseBoolean(data.get("@@isGuest"))) {
            Reporter("LoggedIn as Guest", "Info");
            genericHelper.navigateTo("/search");
        } else {
            signIn(data.get("@@username"), data.get("@@password"));
        }
    }


}
