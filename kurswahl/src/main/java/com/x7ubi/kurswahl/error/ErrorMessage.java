package com.x7ubi.kurswahl.error;

public class ErrorMessage {
    public static class Authentication {
        public static String USERNAME_EXITS = "Nutzername existiert bereits";
    }

    public static class Administration {
        public static String ADMIN_NOT_FOUND = "Admin konnte nicht gefunden werden!";

        public static String STUDENT_NOT_FOUND = "Schüler konnte nicht gefunden werden!";

        public static String TEACHER_NOT_FOUND = "Lehrer konnte nicht gefunden werden!";

        public static String SUBJECT_AREA_ALREADY_EXISTS = "Fachbereich existiert bereits";

        public static String SUBJECT_AREA_NOT_FOUND = "Fachbereich konnte nicht gefunden werden!";

        public static String SUBJECT_ALREADY_EXISTS = "Fach existiert bereits";

        public static String SUBJECT_NOT_FOUND = "Fach konnte nicht gefunden werden!";
    }
}
