package com.testvagrant.goodreads;

import com.sample.framework.uiautomation.DataProvider.TestDataProvider;
import com.sample.framework.uiautomation.base.TestBase;
import com.testvagrant.goodreads.pageObjects.LoginPage;
import com.testvagrant.goodreads.pageObjects.SearchPage;
import com.testvagrant.goodreads.pageObjects.SignInModal;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.HashMap;

public class WhatsTheBookName extends TestBase {
    LoginPage loginPage;
    SignInModal signInModal;
    SearchPage searchPage;

    @BeforeClass
    public void setup() {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @BeforeMethod
    public void initializeObjects() {
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        signInModal = PageFactory.initElements(driver, SignInModal.class);
        searchPage = PageFactory.initElements(driver, SearchPage.class);
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void whatsTheBookName(HashMap<String, String> searchParam) throws Exception {
        loginPage.loginAsGuestOrUser();
        signInModal.closeIfModalIsPresent();

        searchPage.navigateToSearchPage();
        searchPage.searchBook("bookName");
        searchPage.getFirstBookName();
        searchPage.getFirstBookLink();


    }


}
