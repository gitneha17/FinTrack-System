package com.fintrack;

import javax.swing.SwingUtilities;
import com.fintrack.ui.auth.RoleSelectionUI;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new RoleSelectionUI();
        });

    }
}