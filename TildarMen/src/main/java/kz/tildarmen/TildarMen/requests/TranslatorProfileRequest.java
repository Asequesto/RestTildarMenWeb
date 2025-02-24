package kz.tildarmen.TildarMen.requests;

import lombok.Data;

@Data
public class TranslatorProfileRequest {

    private String title;
    private String basedIn;
    private String availability;
}
