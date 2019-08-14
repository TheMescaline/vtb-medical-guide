package ru.vtb.insurance.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "clinics")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "medical_services")
    @ElementCollection(targetClass = MedicalService.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "medical_services", joinColumns = @JoinColumn(name = "id_clinic"))
    @Enumerated(EnumType.STRING)
    private Set<MedicalService> medicalServices;

    @Column(name = "clinic_name", nullable = false)
    private String clinicName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "x_coordinate")
    private Double x;

    @Column(name = "y_coordinate")
    private Double y;

    public Clinic(String clinicName, Set<MedicalService> medicalServices, String address) {
        this(clinicName, medicalServices, address, null, null);
    }

    public Clinic(String clinicName, Set<MedicalService> medicalServices, String address, Double x, Double y) {
        this.clinicName = clinicName;
        this.medicalServices = medicalServices;
        this.address = address;
        this.x = x;
        this.y = y;
    }
}
