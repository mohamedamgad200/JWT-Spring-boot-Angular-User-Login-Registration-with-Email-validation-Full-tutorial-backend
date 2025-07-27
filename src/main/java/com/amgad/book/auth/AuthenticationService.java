package com.amgad.book.auth;

import com.amgad.book.email.EmailData;
import com.amgad.book.email.EmailService;
import com.amgad.book.email.EmailTemplateName;
import com.amgad.book.role.Role;
import com.amgad.book.role.RoleRepository;
import com.amgad.book.user.Token;
import com.amgad.book.user.TokenRepository;
import com.amgad.book.user.User;
import com.amgad.book.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    @Value("${mailing.frontend.activation-url}")
    private String activationUrl;

    public RegisterResponse register(RegisterRequest registerRequest) throws MessagingException {
        Role userRole = roleRepository.findRoleByName("USER")
                //todo -better exception handling
                .orElseThrow(() -> new IllegalStateException("Role User was not initialized in the database"));
        User user = User.builder().
                firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
        return RegisterResponse.builder()
                .message("User registered successfully")
                .status("200")
                .build();
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String token = generateAndSaveActivationToken(user);
        EmailData emailData =EmailData.builder()
                .from("Contact@Amgad.com")
                .to(user.getEmail())
                .username(user.fullName())
                .emailTemplateName(EmailTemplateName.ACCOUNT_ACTIVATION)
                .activationCode(token)
                .confirmationUrl(activationUrl)
                .subject("Account Activation")
                .build();
        emailService.sendEmail(emailData);
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationToken(6);
        Token token=Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationToken(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());//0 to 9 index
            codeBuilder.append(characters.charAt(randomIndex));//append the character at the random index
        }
        return codeBuilder.toString();
    }
}
