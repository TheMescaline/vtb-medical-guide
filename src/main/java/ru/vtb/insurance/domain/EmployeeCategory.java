package ru.vtb.insurance.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EmployeeCategory {
    FIRST("1"),
    FIRST_NO_STOM("1бс"),
    SECOND("2"),
    SECOND_ONE("2.1"),
    SECOND_NO_STOM("2бс"),
    THIRD("3"),
    THIRD_NO_STOM("3бс"),
    FOURTH("4"),
    FOURTH_NO_STOM("4бс"),
    FIFTH("5"),
    FIFTH_NO_STOM("5бс"),
    SIXTH("6"),
    SIXTH_NO_STOM("6бс"),
    SEVENTH("7"),
    SEVENTH_NO_STOM("7бс"),
    EIGHTH("8"),
    EIGHTH_NO_STOM("8бс"),
    NINTH("9");

    private final String category;

    EmployeeCategory(String category) {
        this.category = category;
    }

    @JsonValue
    final public String getCategory() {
        return category;
    }
}
