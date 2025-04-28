package kz.tildarmen.TildarMen.services;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.Stripe;
import com.stripe.model.Account;
import com.stripe.model.LoginLink;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.AccountCreateParams;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.LoginLinkCreateOnAccountParams;
import com.stripe.param.checkout.SessionCreateParams;
import kz.tildarmen.TildarMen.model.Job;
import kz.tildarmen.TildarMen.model.StripeAccount;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.repository.StripeAccountRepository;
import kz.tildarmen.TildarMen.requests.StripeAccCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StripeService {

    private final TranslatorService translatorService;
    private final StripeAccountRepository stripeAccountRepository;
    private final JobService jobService;
    @Value("${stripe.secretKey}")
    private String stripeSecretKey;


    public String getAccount(Long id) throws StripeException {

        Stripe.apiKey = stripeSecretKey;
        Translator translator = translatorService.getTranslatorById(id);
        StripeAccount stripe = stripeAccountRepository.findByUser(translator);
        LoginLinkCreateOnAccountParams params = LoginLinkCreateOnAccountParams.builder().build();
        LoginLink loginLink = LoginLink.createOnAccount(stripe.getStripeId(), params, null);
        return loginLink.getUrl();
    }

    public StripeAccCreateRequest createAccount(Long id) throws StripeException {

        Stripe.apiKey = stripeSecretKey;
        Translator translator = translatorService.getTranslatorById(id);
        AccountCreateParams params = AccountCreateParams.builder()
                .setCapabilities(
                AccountCreateParams.Capabilities.builder()
                        .setCardPayments(
                                AccountCreateParams.Capabilities.CardPayments.builder()
                                        .setRequested(true)
                                        .build()
                        )
                        .setTransfers(
                                AccountCreateParams.Capabilities.Transfers.builder()
                                        .setRequested(true)
                                        .build()
                        )
                        .build()
                )
                .setController(AccountCreateParams.Controller.builder()
                        .setStripeDashboard(AccountCreateParams.Controller.StripeDashboard.builder()
                                .setType(AccountCreateParams.Controller.StripeDashboard.Type.EXPRESS)
                                .build())
                        .setFees(AccountCreateParams.Controller.Fees.builder()
                                .setPayer(AccountCreateParams.Controller.Fees.Payer.APPLICATION)
                                .build())
                        .setLosses(AccountCreateParams.Controller.Losses.builder()
                                .setPayments(AccountCreateParams.Controller.Losses.Payments.APPLICATION)
                                .build())
                        .build())
                .build();

        Account account = Account.create(params);
        AccountLinkCreateParams linkParams = AccountLinkCreateParams.builder()
                .setAccount(account.getId())
                .setRefreshUrl("http://localhost:3000/fail/")
                .setReturnUrl("http://localhost:3000/success/")
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();
        AccountLink link = AccountLink.create(linkParams);

        StripeAccCreateRequest request = new StripeAccCreateRequest();
        request.setAccountId(account.getId());
        request.setOnboardingUrl(link.getUrl());

        StripeAccount stripeAccount = new StripeAccount();
        stripeAccount.setUser(translator);
        stripeAccount.setStripeId(account.getId());
        stripeAccountRepository.save(stripeAccount);

        return request;

    }

    public String payment(Long id, Long jobId) throws StripeException {
        StripeClient client = new StripeClient(stripeSecretKey);
        Translator translator = translatorService.getTranslatorById(id);
        Job job = jobService.getJobById(jobId);
        StripeAccount stripeAccount = stripeAccountRepository.findByUser(translator);

        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("kzt")
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(job.getTitle())
                                                                .build()
                                                )
                                                .setUnitAmount(job.getPrice() * 100L)
                                                .build()
                                )
                                .setQuantity(1L)
                                .build()
                )
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                                .setApplicationFeeAmount(123L)
                                .build()
                )
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/")
                .setCancelUrl("http://localhost:3000/payment-cancel/")
                .setClientReferenceId(translator.getId().toString())
                .putMetadata("jobId", jobId.toString())
                .build();
        RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(stripeAccount.getStripeId()).build();
        Session session = client.checkout().sessions().create(params, requestOptions);

        return session.getUrl();
    }
}
