package kz.tildarmen.TildarMen.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadCertificateRequest {

    private String title;
    private Long year;
    private MultipartFile file;

}
