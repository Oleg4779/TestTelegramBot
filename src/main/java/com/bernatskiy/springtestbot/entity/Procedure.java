package com.bernatskiy.springtestbot.entity;

public enum Procedure {
    ЧИСТКА("Чистка"), ПИЛИНГ("Пилинг"), РЕЛАКС_УХОДЫ("Релакс уходы"), УХОДЫ("Уходы");

    private final String name;

    Procedure(String name) {
        this.name = name;
    }

    public String getName() {
        return name;

    }
}
