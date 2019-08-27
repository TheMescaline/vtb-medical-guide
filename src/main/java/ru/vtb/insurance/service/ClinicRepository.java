package ru.vtb.insurance.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.insurance.domain.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
}
