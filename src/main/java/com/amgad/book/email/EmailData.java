package com.amgad.book.email;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailData {
    private String from;
    private String to;
    private String username;
    private String subject;
    private String confirmationUrl;
    private String activationCode;
    private EmailTemplateName emailTemplateName;
}
