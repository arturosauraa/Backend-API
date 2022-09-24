package com.network.crud.service;

import com.network.crud.entity.User;
import com.network.crud.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public User registerUser(User user) {
        if (checkIfUserExist(user.getEmail())) {
            try {
                throw new Exception("User already exists with this email");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //Password validation needed


        User user_create = new User();
        user_create.setEmail(user.getEmail());
        user_create.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user_create);

        return user_create;
    }

    public void activateUserAccount(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    public boolean checkIfUserExist(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

/*    public void sendRegistrationConfirmationEmail(String sendTo, String token) {
        EmailService emailService = new EmailService(token);
        emailService.sendRegistrationMail(sendTo);
    }*/

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("user with email %s not found", email)));
    }


    ///Extra Testing methods///
    public User save(User appUser) {
        return userRepository.save(appUser);
    }

    //Retrieves a user by using its id
    public User getAppUser(Long id) {
        return userRepository.findById(id).get();
    }

    //Updates the user by using its id, so it can change the name, password or email
    public User update(User appUser) {
        User user = userRepository.findById(appUser.getId()).get();
        user.setPassword(appUser.getPassword());
        user.setEmail(appUser.getEmail());

        return userRepository.save(user);

    }

    //uses the id to delete a user
    public String delete(Long id) {
        userRepository.deleteById(id);
        return "Entity deleted" + id;
    }
}

