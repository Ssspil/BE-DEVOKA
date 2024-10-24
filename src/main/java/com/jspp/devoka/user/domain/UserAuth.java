package com.jspp.devoka.user.domain;

public enum UserAuth {
    ADMIN("ADMIN", "관리자"),
    USER("USER", "사용자");


    private final String key;
    private final String value;

    UserAuth(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
