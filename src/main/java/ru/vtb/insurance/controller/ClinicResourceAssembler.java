package ru.vtb.insurance.controller;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import ru.vtb.insurance.domain.Clinic;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ClinicResourceAssembler implements ResourceAssembler<Clinic, Resource<Clinic>> {

    @Override
    public Resource<Clinic> toResource(Clinic clinic) {
        return new Resource<>(clinic,
                linkTo(methodOn(ClinicController.class).getOne(clinic.getId())).withSelfRel(),
                linkTo(methodOn(ClinicController.class).all()).withRel("clinics"));
    }
}
