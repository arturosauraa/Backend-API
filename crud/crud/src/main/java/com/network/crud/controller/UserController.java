package com.network.crud.controller;

import com.network.crud.entity.Email;
import com.network.crud.entity.User;
import com.network.crud.entity.VerificationToken;
import com.network.crud.service.EmailService;
import com.network.crud.service.UserService;
import com.network.crud.service.VerificationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;

    private final EmailService emailService;

    @PostMapping(path = "/register")
    public String registerUser(@RequestBody User user) {
        //Email validator

        User new_user = userService.registerUser(user);
        VerificationToken newToken = verificationTokenService.createVerificationToken(new_user);
        emailService.sendSimpleMail(new Email(user.getEmail(), "Your registration is: http://localhost:8080/user/verifyRegistration?token="+newToken.getToken(), "Registration Email", null));

        System.out.println("Your registration is: http://localhost:8080/user/verifyRegistration?token=" + newToken.getToken());
        return "Success";
    }

    @GetMapping(path = "/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        User validated_user = verificationTokenService.validateVerificationToken(token);
        userService.activateUserAccount(validated_user);
        verificationTokenService.removeTokenByToken(token);
        return "User verified successfully!";
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken) {
        VerificationToken newVerificationToken = verificationTokenService.generateNewVerificationToken(oldToken);
        User user = newVerificationToken.getUser();

        emailService.sendSimpleMail(new Email(user.getEmail(), "Your registration is: http://localhost:8080/user/verifyRegistration?token="+newVerificationToken.getToken(), "Registration Email", null));

        return "Verification Link sent";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/login")
    //@PreAuthorize("hasAuthority('USER_WRITE')")
    public String terminalLogin() {
        /*TODO: We cant just return an id here. Implementation of login tokens are required. We can save the token in
        cookies with expiration time etc.*/
        return "Logged in!";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/login")
    //@PreAuthorize("hasAuthority('USER_WRITE')")
    public String browserLogin() {
        /*TODO: We cant just return an id here. Implementation of login tokens are required. We can save the token in
        cookies with expiration time etc.*/
        return "Logged in!";
    }


    ///Extra Testing Methods///

    @GetMapping(path = "/{id}")
    public User getAppUser(@PathVariable Long id) {
        return userService.getAppUser(id);
    }

    //Updates a user from the database
    //To identify the user that you want to update, you must know its "id"
    @PutMapping
    public User updateAppUser(@RequestBody User user) {
        return userService.update(user);
    }

    //Deletes a user from the database
    //To identify the user that you want to delete, you must know its "id"
    @DeleteMapping(path = "/{id}")
    public String deleteAppUser(@PathVariable Long id) {
        return userService.delete(id);
    }
}

