package ru.vtb.insurance.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.insurance.domain.Clinic;
import ru.vtb.insurance.domain.MedicalService;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findAllByMedicalServicesContains(MedicalService medicalService);
}
