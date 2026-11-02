package com.fot.tms;

import com.fot.tms.database.DatabaseInitializer;
import com.fot.tms.ui.LoginFrame;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Entry Point for Faculty of Technology Management System (FoT-TMS).
 * Features intelligent automatic FlatLaf Look & Feel resolution with
 * complete ContextClassLoader binding for seamless Swing ComponentUI rendering.
 */
public class Main {
    private static volatile ClassLoader flatLafLoader = null;

    public static void main(String[] args) {
        // Disable native window decoration loading to prevent Java console warnings
        System.setProperty("flatlaf.useWindowDecorations", "false");
        System.setProperty("flatlaf.nativeLibrary", "false");

        // Setup Look and Feel with automated FlatLaf fallback loader
        setupLookAndFeel();

        // Initialize database tables & baseline data if required
        SwingUtilities.invokeLater(() -> {
            // Ensure EventDispatchThread has the FlatLaf ClassLoader context
            if (flatLafLoader != null) {
                Thread.currentThread().setContextClassLoader(flatLafLoader);
            }

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

    private static void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception ex) {
            // Intelligent Fallback: Dynamically load FlatLaf from lib directory
            try {
                File flatLafJar = findFlatLafJar();
                if (flatLafJar != null && flatLafJar.exists()) {
                    flatLafLoader = new URLClassLoader(
                        new URL[]{flatLafJar.toURI().toURL()},
                        Main.class.getClassLoader()
                    );
                    
                    Thread.currentThread().setContextClassLoader(flatLafLoader);
                    
                    Class<?> lafClass = Class.forName("com.formdev.flatlaf.FlatLightLaf", true, flatLafLoader);
                    LookAndFeel laf = (LookAndFeel) lafClass.getDeclaredConstructor().newInstance();
                    
                    // Essential for Swing UIDefaults to find FlatLaf ComponentUI classes:
                    UIManager.getDefaults().put("ClassLoader", flatLafLoader);
                    UIManager.getLookAndFeelDefaults().put("ClassLoader", flatLafLoader);
                    UIManager.setLookAndFeel(laf);
                    UIManager.getDefaults().put("ClassLoader", flatLafLoader);
                    UIManager.getLookAndFeelDefaults().put("ClassLoader", flatLafLoader);
                    
                    System.out.println("[FoT-TMS] SUCCESS: FlatLaf Look and Feel fully configured!");
                    return;
                }
            } catch (Throwable t) {
                System.err.println("FlatLaf dynamic load note: " + t.getMessage());
            }

            // Fallback to system look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }
    }

    private static File findFlatLafJar() {
        List<File> roots = new ArrayList<>();

        File cur = new File("").getAbsoluteFile();
        while (cur != null) {
            roots.add(cur);
            cur = cur.getParentFile();
        }

        try {
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            path = java.net.URLDecoder.decode(path, "UTF-8");
            File codeDir = new File(path);
            while (codeDir != null) {
                if (!roots.contains(codeDir)) {
                    roots.add(codeDir);
                }
                codeDir = codeDir.getParentFile();
            }
        } catch (Exception ignored) {}

        for (File root : roots) {
            File found = searchDirRecursively(root, 0);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static File searchDirRecursively(File dir, int depth) {
        if (dir == null || !dir.exists() || depth > 3) return null;
        String dirName = dir.getName().toLowerCase();
        if (dirName.equals(".git") || dirName.equals(".idea") || dirName.equals("out")) return null;

        File[] files = dir.listFiles();
        if (files == null) return null;

        if (dirName.equals("lib") || dirName.equals("libs")) {
            for (File f : files) {
                if (f.isFile() && f.getName().toLowerCase().contains("flatlaf") && f.getName().toLowerCase().endsWith(".jar")) {
                    return f;
                }
            }
        }

        for (File f : files) {
            if (f.isDirectory()) {
                File found = searchDirRecursively(f, depth + 1);
                if (found != null) return found;
            }
        }
        return null;
    }
}
