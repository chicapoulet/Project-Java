package com.inventory.util;

import com.inventory.model.User;

public class SessionManager {

    private static User currentUser;

    private SessionManager() {
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        // sauvegarde user en cours
        currentUser = user;
    }

    public static void clearSession() {
        // deconnexion
        currentUser = null;
    }

    public static boolean isAdmin() {
        // verif role admin
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }
}
