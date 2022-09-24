package com.network.crud.service;

import com.network.crud.entity.User;
import com.network.crud.entity.VerificationToken;
import com.network.crud.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public void removeTokenByToken(String token) {
        verificationTokenRepository.removeByToken(token);
    }

    public VerificationToken createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, token);

        verificationTokenRepository.save(verificationToken);

        return verificationToken;
    }

    public User validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            try {
                throw new Exception("Token is Invalid or used");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            try {
                throw new Exception("Token is expired");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return verificationToken.getUser();
    }


    public VerificationToken generateNewVerificationToken(String oldToken) {
        //finding the old token provided
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        //generating and assigning new token
        verificationToken.setToken(UUID.randomUUID().toString());
        //saving it to token db
        verificationTokenRepository.save(verificationToken);

        return verificationToken;
    }
}
