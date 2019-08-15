package ru.vtb.insurance.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MedicalService {
    AMBULATORY("Амбулаторное лечение"),
    STATIONARY("Стационарное лечение"),
    HOMECARE("Вызов врача на дом"),
    STOMATOLOGY("Стоматология"),
    EMERGENCY("Скорая помощь"),
    VIP("VIP-обслуживание");


    private final String service;

    MedicalService(String service) {
        this.service = service;
    }

    @JsonValue
    final public String getService() {
        return service;
    }
}
