package kz.tildarmen.TildarMen.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDto {

    private Long id;
    private String type;
    private String reason;
    private String title;
    private String details;
    private String fileUrl;
    private LocalDateTime createdAt;

    private String translatorFirstName;
    private String translatorLastName;

    private String jobTitle;

}
