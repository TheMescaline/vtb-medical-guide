package ru.vtb.insurance.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "clinics")
public class Clinic {
    @Id
    private long id;

    @Column(name = "medical_services")
    @ElementCollection(targetClass = MedicalService.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "medical_services", joinColumns = @JoinColumn(name = "id_clinic"))
    @Enumerated(EnumType.STRING)
    @BatchSize(size = 200)
    private Set<MedicalService> medicalServices;

    @Column(name = "employee_categories")
    @ElementCollection(targetClass = EmployeeCategory.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_categories", joinColumns = @JoinColumn(name = "id_clinic"))
    @Enumerated(EnumType.STRING)
    @BatchSize(size = 200)
    private Set<EmployeeCategory> employeeCategories;

    @Column(name = "clinic_name", nullable = false)
    private String clinicName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "x_coordinate")
    private double x;

    @Column(name = "y_coordinate")
    private double y;

    @Column(name = "url")
    private String url;

    @Column(name = "phone")
    private String phone;

    public Clinic(long id, Set<MedicalService> medicalServices, Set<EmployeeCategory> employeeCategories, String clinicName, String address, String description) {
        this(id, medicalServices, employeeCategories, clinicName, address, description, 0, 0, null, null);
    }

    public Clinic(long id, Set<MedicalService> medicalServices, Set<EmployeeCategory> employeeCategories, String clinicName, String address, String description, double x, double y, String url, String phone) {
        this.id = id;
        this.medicalServices = medicalServices;
        this.employeeCategories = employeeCategories;
        this.clinicName = clinicName;
        this.address = address;
        this.description = description;
        this.x = x;
        this.y = y;
        this.url = url;
        this.phone = phone;
    }
}
