package kz.tildarmen.TildarMen.dto;


import kz.tildarmen.TildarMen.model.Language;
import kz.tildarmen.TildarMen.model.Location;
import kz.tildarmen.TildarMen.model.ServiceTypes;
import kz.tildarmen.TildarMen.model.Specialization;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class JobDto {

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    Set<Location> locations;
    Set<Language> languages;
    Set<ServiceTypes> serviceTypes;
    Set<Specialization> specializations;

}
