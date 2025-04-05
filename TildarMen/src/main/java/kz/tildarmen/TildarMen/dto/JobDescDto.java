package kz.tildarmen.TildarMen.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobDescDto {

    private String title;
    private Long price;
    private LocalDateTime endDate;

}
