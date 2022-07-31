package com.simbirsoft.pages;

public enum Catalog {

    ELECTRONIC("Электроника"),
    COMPUTERS("Компьютеры и периферия"),
    APPLIANCES("Бытовая техника"),
    REPAIRS("Строительство и ремонт"),
    HOUSEHOLDS("Товары для дома");

    private String desc;

    Catalog(String desc) {
        this.desc =desc;
    }

    public String getDesc() {
        return desc;
    }
}
