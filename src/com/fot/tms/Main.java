package com.fot.tms;

import com.fot.tms.database.DatabaseInitializer;
import com.fot.tms.ui.LoginFrame;

import javax.swing.*;

/**
 * Main Entry Point for Faculty of Technology Management System (FoT-TMS).
 * Initializes Look & Feel, checks database connectivity, and launches LoginFrame.
 */
public class Main {
    public static void main(String[] args) {
        // Disable native window decoration loading to prevent Java 24 console warnings
        System.setProperty("flatlaf.useWindowDecorations", "false");
        System.setProperty("flatlaf.nativeLibrary", "false");

        // Setup Look and Feel
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // System default look and feel
            }
        }

        // Initialize database tables & baseline data if required
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseInitializer.initializeDatabaseIfNeeded();
            } catch (Exception e) {
                System.err.println("Database auto-check notification: " + e.getMessage());
            }
            
            // Launch Login Window
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
