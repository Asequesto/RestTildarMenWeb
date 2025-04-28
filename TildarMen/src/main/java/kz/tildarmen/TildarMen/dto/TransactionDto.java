package kz.tildarmen.TildarMen.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDto {

    private LocalDateTime date;
    private Long price;
    private String description;
    private String translatorFirstName;
    private String translatorLastName;

}
