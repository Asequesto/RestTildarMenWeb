package kz.tildarmen.TildarMen.dto;

import lombok.Data;

@Data
public class CertificateDto {

    private Long id;
    private String title;
    private String certificateUrl;
    private Long year;

}
