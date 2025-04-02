package kz.tildarmen.TildarMen.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JobDescDto {

    private String title;
    private Long price;
    private LocalDate endDate;

}
