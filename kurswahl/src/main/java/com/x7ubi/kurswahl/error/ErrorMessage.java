package com.x7ubi.kurswahl.error;

public class ErrorMessage {

    public static class General {
        public static String WRONG_OLD_PASSWORD = "Altes Passwort ist falsch";

        public static String USER_NOT_FOUND = "Nutzer konnte nicht gefunden werden";
    }

    public static class Administration {
        public static final String TAPE_ALREADY_EXISTS = "Ein Band mit diesem Namen existiert bereits";
        public static String ADMIN_NOT_FOUND = "Admin konnte nicht gefunden werden!";

        public static String STUDENT_NOT_FOUND = "Schüler konnte nicht gefunden werden!";

        public static String TEACHER_NOT_FOUND = "Lehrer konnte nicht gefunden werden!";

        public static String SUBJECT_AREA_ALREADY_EXISTS = "Ein Fachbereich mit diesem Namen existiert bereits";

        public static String SUBJECT_AREA_NOT_FOUND = "Fachbereich konnte nicht gefunden werden!";

        public static String SUBJECT_ALREADY_EXISTS = "Ein Fach mit diesem Namen existiert bereits";

        public static String SUBJECT_NOT_FOUND = "Fach konnte nicht gefunden werden!";

        public static String STUDENT_CLASS_ALREADY_EXISTS = "Eine Klasse mit diesem existiert bereits in diesem Jahr";

        public static String STUDENT_CLASS_NOT_FOUND = "Die Klasse konnte nicht gefunden werden!";
        public static String TEACHER_STUDENT_CLASS = "Lehrer ist noch Leiter eine(r) Klasse(n)";
    }
}
