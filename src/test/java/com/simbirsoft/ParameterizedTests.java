package com.simbirsoft;

import org.junit.jupiter.params.provider.*;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.open;
import static com.simbirsoft.OnlineTradePage.URL;

public class ParameterizedTests extends TestBase{

    OnlineTradePage onlineTradePage = new OnlineTradePage();

    @ValueSource(strings = {"Телефоны", "Бытовая техника", "Посудомойка"})
    @ParameterizedTest(name = "Проверка результата поиска для значения: {0}")
    void onlineTradeSearch(String searchQuery) {
        open(URL);
        onlineTradePage.doSearch(searchQuery);
        onlineTradePage.checkSearchResult(searchQuery);

    }

    @CsvSource({
            "123445678@mail.ru, 1234",
            "Unknown-mail@gmail.com, 4567",
            "343-567-999@yandex.ru, 0000"
    })
    @ParameterizedTest(name = "Проверка текста ошибки при вводе некорректного логина и пароля: {0}")
    void loginFormValidation(String login, String password) {
        open(URL);
        onlineTradePage.openLoginForm();
        onlineTradePage.fillLoginForm(login, password);
        onlineTradePage.checkIfLoginAndPassIncorrect();
    }

    @EnumSource(value = Catalog.class)
    @ParameterizedTest(name = "Проверка контента элемента каталога: {0}")
    void catalogPagesContent(Catalog catalogItem) {
        open(URL);
        onlineTradePage.switchToCatalogItem(catalogItem);
        onlineTradePage.checkCatalogItemContent(catalogItem);
    }

    @MethodSource("argumentsStream")
    @ParameterizedTest(name = "Проверка элементов на соответствие указанному диапазону цен для запроса: {0}")
    void checkPriceRangeForItem(String item, List<Integer> priceRange) {
        open(URL);
        onlineTradePage.doSearch(item);
        onlineTradePage.openListOfSearchedItems(item);
        onlineTradePage.setPriceRange(priceRange);
        onlineTradePage.checkItemsPriceInSetRange(priceRange);
    }
    static Stream<Arguments> argumentsStream() {
        return Stream.of(
                Arguments.of(
                        "Видеокарты", List.of(50000, 100000)
                ),
                Arguments.of(
                        "Процессоры", List.of(10000, 30000)
                ),
                Arguments.of(
                        "Оперативная память", List.of(5000, 15000)
                )
        );
    }
}
