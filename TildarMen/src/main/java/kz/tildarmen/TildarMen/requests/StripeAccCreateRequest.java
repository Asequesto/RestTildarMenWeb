package kz.tildarmen.TildarMen.requests;

import lombok.Data;

@Data
public class StripeAccCreateRequest {

    String accountId;
    String onboardingUrl;

}
