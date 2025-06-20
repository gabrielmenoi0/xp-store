package com.xp.store.utils.Validations;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {
    public static boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static boolean validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static boolean validateFullName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return false;
        }
        if (fullName.length() < 3) {
            return false;
        }

        String[] names = fullName.split(" ");
        if (names.length < 2) {
            return false;
        }

        if (!fullName.matches("[A-Za-zÀ-ÿ ]+")) {
            return false;
        }

        return true;
    }

    public static boolean validateRole(String role) {
        if (role == null || role.isEmpty()) {
            return false;
        }
        if (!role.equalsIgnoreCase("ADMIN") &&
                !role.equalsIgnoreCase("CORRETOR") &&
                !role.equalsIgnoreCase("PROPRIETARIO") &&
                !role.equalsIgnoreCase("INQUILINO") &&
                !role.equalsIgnoreCase("USUARIO")) {
            return false;
        }
        return true;
    }

    public static boolean validateCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }
        String cpfRegex = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$";
        Pattern pattern = Pattern.compile(cpfRegex);
        Matcher matcher = pattern.matcher(cpf);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static boolean validateDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }
        String dateRegex = "^([0-2][0-9]|(3)[0-1])/(0[1-9]|1[0-2])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(date);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static boolean validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        String phoneRegex = "^\\(\\d{2}\\) \\d{5}-\\d{4}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static boolean validateRequiredField(String field, String fieldName) {
        if (field == null || field.isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean validateCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || !cnpj.matches("\\d+")) {
            return false;
        }

        int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        try {
            int digit1 = calculateDigit(cnpj.substring(0, 12), weight1);
            int digit2 = calculateDigit(cnpj.substring(0, 12) + digit1, weight2);

            return cnpj.equals(cnpj.substring(0, 12) + digit1 + digit2);
        } catch (InputMismatchException e) {
            return false;
        }
    }

    private static int calculateDigit(String str, int[] weight) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += (str.charAt(i) - '0') * weight[i];
        }
        int remainder = sum % 11;
        return (remainder < 2) ? 0 : (11 - remainder);
    }
    public static boolean validatePositiveNumber(int number, String fieldName) {
        if (number <= 0) {
            return false;
        }
        return true;
    }

}
