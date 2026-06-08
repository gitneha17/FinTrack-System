package com.fintrack.utils;

public class SessionManager {

    private static int userId;
    private static String currentUser;

    // SET USER ID
    public static void setUserId(int id) {
        userId = id;
    }

    // GET USER ID
    public static int getUserId() {
        return userId;
    }

    // SET CURRENT USERNAME
    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    // GET CURRENT USERNAME
    public static String getCurrentUser() {
        return currentUser;
    }
}