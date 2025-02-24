package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.enums.ImageUsageType;
import lombok.Data;

@Data
public class ImageDto {

    private Long id;
    private String fileName;
    private String fileType;
    private String downloadUrl;
    private ImageUsageType usageType;

}
