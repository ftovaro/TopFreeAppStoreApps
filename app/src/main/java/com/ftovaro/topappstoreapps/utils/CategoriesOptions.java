package com.ftovaro.topappstoreapps.utils;

/**
 * Enum that represents the different categories.
 * Created by FelipeTovar on 10-Apr-16.
 */
public enum CategoriesOptions {
    TOP20APPS(0), EDUCATION(1), ENTERTAINMENT(2), GAMES(3), MUSIC(4), NAVIGATION(5),
    PHOTO_AND_VIDEO(6), SOCIAL_NETWORKING(7), TRAVEL(8);

    private int value;

    CategoriesOptions(int value) {
        this.value = value;
    }

    public static CategoriesOptions getStatusFromInt(int value) {
        return CategoriesOptions.values()[value];
    }
}
