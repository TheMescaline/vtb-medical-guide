package ru.vtb.insurance.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Clinic {
    @Id @GeneratedValue
    private long id;
    @OneToMany
    private Set<MedicalService> medicalServices;
    private String address;

    public Clinic(Set<MedicalService> medicalServices, String address) {
        this.medicalServices = medicalServices;
        this.address = address;
    }
}
