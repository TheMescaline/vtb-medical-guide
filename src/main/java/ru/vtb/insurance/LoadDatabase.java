package ru.vtb.insurance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.vtb.insurance.domain.Clinic;
import ru.vtb.insurance.domain.MedicalService;
import ru.vtb.insurance.service.ClinicRepository;

import java.util.Arrays;
import java.util.HashSet;

@Slf4j
@Configuration
public class LoadDatabase {

    @Bean
    @Profile("dbInitialization")
    CommandLineRunner initDataBase(ClinicRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Clinic("ГлавКлиника", new HashSet<MedicalService>(Arrays.asList(MedicalService.STOMATOLOGY, MedicalService.AMBULATORY)), "Москва, Пушкинская", "\"Амбулаторно-поликлиническая помощь\" без стоматологии, вызов врача на дом в пределах МКАД")));
            log.info("Preloading " + repository.save(new Clinic("НОВА-МЕД", new HashSet<MedicalService>(Arrays.asList(MedicalService.STATIONARY)), "Москва, Красная Пресня", "\"Амбулаторно-поликлиническая помощь\" без стоматологии, вызов врача на дом в пределах МКАД")));
            log.info("Preloading " + repository.save(new Clinic("Больничка № 4", new HashSet<MedicalService>(Arrays.asList(MedicalService.AMBULATORY)), "Москва, Тверская", "\"Амбулаторно-поликлиническая помощь\" без стоматологии, вызов врача на дом в пределах МКАД")));
            log.info("Preloading " + repository.save(new Clinic("СМ-Клиника", new HashSet<MedicalService>(Arrays.asList(MedicalService.AMBULATORY)), "Москва, Октябрьская", "\"Экстренная и плановая стационарная помощь\", размещение в одноместной палате повышенной комфортности")));
            log.info("Preloading " + repository.save(new Clinic("VIP-LEPILA", new HashSet<MedicalService>(Arrays.asList(MedicalService.AMBULATORY, MedicalService.STATIONARY, MedicalService.STOMATOLOGY)), "Москва, Наташинская", "\"Амбулаторно-поликлиническая помощь\" без стоматологии, вызов врача на дом в пределах МКАД")));
            log.info("Preloading " + repository.save(new Clinic("S-kulap", new HashSet<MedicalService>(Arrays.asList(MedicalService.AMBULATORY, MedicalService.STATIONARY)), "Люберцы, Жулебинский бульвар", "Амбулаторно-поликлиническая помощь без стоматологии, без помощи на дому")));
        };
    }
}
