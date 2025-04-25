package kz.tildarmen.TildarMen.requests;

import lombok.Data;
import java.util.List;

@Data
public class SearchTranslatorsRequest {
    String availability;
    List<String> languages;
    List<String> serviceTypes;
    List<String> specializations;
    List<String> locations;
}
