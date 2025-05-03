package kz.tildarmen.TildarMen.services;

import kz.tildarmen.TildarMen.repository.EmailVerifyTokenRepository;
import kz.tildarmen.TildarMen.repository.ResetPasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduledService {

    private final EmailVerifyTokenRepository emailVerifyTokenRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void deleteExpiredTokens(){
        emailVerifyTokenRepository.deleteAllExpiredTokens(LocalDateTime.now());
        resetPasswordTokenRepository.deleteAllExpiredTokens(LocalDateTime.now());
        System.out.println("Expired tokens deleted");
    }

}
