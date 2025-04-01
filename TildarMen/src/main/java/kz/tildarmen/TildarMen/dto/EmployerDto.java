package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.model.Location;
import lombok.Data;

@Data
public class EmployerDto {

    private String introduction;
    private Location location;

}
