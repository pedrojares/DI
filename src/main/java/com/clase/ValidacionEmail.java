package com.clase;

import java.util.regex.Pattern;

final class ValidacionEmail {
    private static final Pattern FORMATO_EMAIL = Pattern.compile(
            "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
                    + "@(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$");

    private ValidacionEmail() {
    }

    static boolean esValido(String email) {
        if (email == null) {
            return false;
        }

        String emailNormalizado = email.trim();
        int arroba = emailNormalizado.lastIndexOf('@');
        return emailNormalizado.length() <= 254
                && arroba > 0
                && arroba <= 64
                && FORMATO_EMAIL.matcher(emailNormalizado).matches();
    }
}
