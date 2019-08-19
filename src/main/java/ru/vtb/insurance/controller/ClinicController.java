package ru.vtb.insurance.controller;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.insurance.domain.Clinic;
import ru.vtb.insurance.exceptions.ClinicNotFoundException;
import ru.vtb.insurance.service.ClinicRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/v1")
public class ClinicController {
    private final ClinicRepository repository;
    private final ClinicResourceAssembler assembler;

    public ClinicController(ClinicRepository clinicRepository, ClinicResourceAssembler assembler) {
        this.repository = clinicRepository;
        this.assembler = assembler;
    }

    @GetMapping("/clinics")
    Resources<Resource<Clinic>> all() {
        List<Resource<Clinic>> clinics = repository
                .findAll()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(clinics,
                linkTo(methodOn(ClinicController.class).all()).withSelfRel());
    }

    @GetMapping("/clinics/{id}")
    Resource<Clinic> getOne(@PathVariable Long id) {
        Clinic clinic = repository.findById(id).orElseThrow(() -> new ClinicNotFoundException(id));
        return assembler.toResource(clinic);
    }

    @PostMapping("/clinics")
    ResponseEntity<?> newClinic(@RequestBody Clinic clinic) throws URISyntaxException {
        Resource<Clinic> resource = assembler.toResource(repository.save(clinic));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping("/clinics/{id}")
    ResponseEntity<?> replaceClinic(@RequestBody Clinic newClinic, @PathVariable Long id) throws URISyntaxException {
        Resource<Clinic> resource = assembler.toResource(repository
                .findById(id)
                .map(clinic -> {
                    clinic.setClinicName(newClinic.getClinicName());
                    clinic.setAddress(newClinic.getAddress());
                    clinic.setMedicalServices(newClinic.getMedicalServices());
                    clinic.setDescription(newClinic.getDescription());
                    clinic.setX(newClinic.getX());
                    clinic.setY(newClinic.getY());
                    clinic.setUrl(newClinic.getUrl());
                    clinic.setPhone(newClinic.getPhone());
                    return repository.save(clinic);
                }).orElseGet(() -> {
                    newClinic.setId(id);
                    return repository.save(newClinic);
                }));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/clinics/{id}")
    ResponseEntity<?> deleteClinic(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

