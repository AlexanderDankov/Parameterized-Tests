package com.simbirsoft.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.google.common.base.CharMatcher;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.lang.Integer.valueOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OnlineTradePage {

    public static final String URL = "https://www.onlinetrade.ru/";

    private SelenideElement
            searchInput = $("input[name=query]"),
            searchResult = $(".content").$("h1"),
            loginButton = $(".huab__cell.huab__cell__member"),
            loginForm = $("#popup_login"),
            loginInput = $("#ajax_login_popup_email"),
            passwordInput = $("#ajax_login_popup_pass"),
            loginFormButton = $("input[value=Вход]"),
            loginErrorMessage = $(".coloredMessage.coloredMessage__red"),
            catalogButton = $(".header__buttonCatalog.js__catalogLine__mainLink"),
            catalogItemHeader = $(".content").$("h1"),
            priceRangeWrapper = $("div[data-spoiledcontent=price_active]"),
            minPrice = $("#price1"),
            maxPrice = $("#price2"),
            showFilterResults = $("#filterResult__sticker_ID").find(byText("показать"));

    private ElementsCollection
            catalogItems =  $$(".mCM__item"),
            catalogItemContent = $$(".drawCats__items"),
            searchCategories = $$(".sCM__item__link"),
            prices = $$(".js__actualPrice");

    public void doSearch(String searchQuery) {
        searchInput.setValue(searchQuery).pressEnter();
    }

    public void checkSearchResult(String searchQuery) {
        searchResult.shouldHave(text("Найденные категории по запросу «" + searchQuery + "»"));
    }

    public void openLoginForm() {
        loginButton.click();
        loginForm.shouldBe(Condition.visible);
    }

    public void fillLoginForm(String login, String password) {
        loginInput.setValue(login);
        passwordInput.setValue(password);
        loginFormButton.click();
    }

    public void checkIfLoginAndPassIncorrect() {
        loginErrorMessage.shouldHave(text("Указан неверный e-mail или пароль"));
    }

    public void switchToCatalogItem(Catalog catalogItem) {
        catalogButton.click();
        catalogItems.findBy(text(catalogItem.getDesc())).click();
    }

    public void checkCatalogItemContent(Catalog catalogItem) {
        catalogItemHeader.shouldHave(text(catalogItem.getDesc()));
        catalogItemContent.shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    public void openListOfSearchedItems(String item) {
        searchCategories.findBy(text(item)).click();
    }

    public void setPriceRange(List<Integer> priceRange) {
        priceRangeWrapper.click();
        minPrice.setValue(String.valueOf(priceRange.get(0))).pressEnter();
        maxPrice.setValue(String.valueOf(priceRange.get(1))).pressEnter();
        showFilterResults.shouldBe(visible);
        showFilterResults.click();

    }

    public void checkItemsPriceInSetRange(List<Integer> priceRange) {
        List<Integer> pricesOfItems = new ArrayList<>();

        prices.shouldHave(CollectionCondition.sizeGreaterThan(0));

        for (SelenideElement price : prices) {
            pricesOfItems.add(valueOf(CharMatcher.inRange('0', '9').retainFrom(price.getOwnText())));
        }

        for (Integer priceOfItem : pricesOfItems) {
            assertAll(
                    () -> assertTrue(priceOfItem >= priceRange.get(0)),
                    () -> assertTrue(priceOfItem <= priceRange.get(1))
            );
        }
    }
}
