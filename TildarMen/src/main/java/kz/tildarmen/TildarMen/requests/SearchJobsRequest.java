package kz.tildarmen.TildarMen.requests;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SearchJobsRequest {
    List<String> languages;
    List<String> serviceTypes;
    List<String> specializations;
    List<String> locations;
    LocalDate startDate;
    LocalDate endDate;
}
