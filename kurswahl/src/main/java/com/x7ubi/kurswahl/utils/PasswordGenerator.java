package com.x7ubi.kurswahl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PasswordGenerator {
    public static String generatePassword() {
        int length = 12;
        StringBuilder password = new StringBuilder();
        Random random = new Random(System.nanoTime());
        final String lowerLetters = "abcdefghikmnpqrstuvwxyz";
        final String upperLetters = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
        final String digits = "0123456789";

        List<String> charCategories = new ArrayList<>(List.of(lowerLetters, upperLetters, digits));

        for (int i = 0; i < length; i++) {
            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }
        return new String(password);
    }
}
