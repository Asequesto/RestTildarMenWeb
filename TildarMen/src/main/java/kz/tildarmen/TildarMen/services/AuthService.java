package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    public void checkPermission(User user, Long userId) {
        if(!(user.getId().equals(userId))) {
            throw new SecurityException("Permission denied, you can't use this service");
        }
    }

}
