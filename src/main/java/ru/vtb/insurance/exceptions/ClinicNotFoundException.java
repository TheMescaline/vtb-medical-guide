package ru.vtb.insurance.exceptions;

public class ClinicNotFoundException extends RuntimeException {
    public ClinicNotFoundException(Long id) {
        super("Can't find clinic " + id);
    }
}
