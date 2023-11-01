package com.example.bank.core;

public enum Permission {
    ACCOUNTS_READ("accounts.read"),
    ACCOUNTS_WRITE("accounts.write"),
    TTRANSACTIONS_READ("transactions.read"),
    TTRANSACTIONS_WRITE("transactions.write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
