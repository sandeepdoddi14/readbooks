package com.testvagrant.goodreads.pageObjects;

import com.sample.framework.uiautomation.Utility.UtilityHelper;
import com.sample.framework.uiautomation.base.TestBase;
import com.sample.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class SearchPage extends TestBase {
    GenericHelper genericHelper;
    @FindBy(className = "searchBox__input searchBox--large__input")
    WebElement searchBox;
    @FindBy(className = "searchBox__button searchBox--large__button")
    WebElement submitSearchButton;
    @FindBy(className = "/html/body/div[2]/div[3]/div[1]/div[2]/div[2]/table/tbody/tr")
    List<WebElement> books;

    public SearchPage() {
        genericHelper = PageFactory.initElements(driver, GenericHelper.class);
    }

    public void searchBook(String bookName) {
        genericHelper.setElementText(searchBox, bookName, "Search Book");
        genericHelper.elementClick(submitSearchButton, "Search Book Button");
    }

    public String getFirstBookLink() {
        return books.get(0).getText();
    }


    public String getFirstBookName() {
        return books.get(0).getAttribute("href");
    }


    public void navigateToSearchPage() {
        String url = new UtilityHelper().getProperty("urls.properties", "searchPage");
        genericHelper.navigateTo(url);
    }


}






