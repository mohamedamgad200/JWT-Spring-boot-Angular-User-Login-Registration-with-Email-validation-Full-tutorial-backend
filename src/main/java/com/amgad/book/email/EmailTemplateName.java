package com.amgad.book.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACCOUNT_ACTIVATION("account-activation");
    private final String name;
    EmailTemplateName(String name) {
        this.name = name;
    }
}
