package com.x7ubi.kurswahl.error;

public class ErrorMessage {
    public static class Authentication {
        public static String USERNAME_EXITS = "Nutzername existiert bereits";
    }

    public static class Administration {
        public static String ADMIN_NOT_FOUND = "Admin konnte nicht gefunden werden!";

        public static String STUDENT_NOT_FOUND = "Schüler konnte nicht gefunden werden!";

        public static String TEACHER_NOT_FOUND = "Lehrer konnte nicht gefunden werden!";
    }
}
