package com.fot.tms.util;

import java.awt.Color;
import java.awt.Font;

/**
 * Modern UI styling constants and themes for the Java Swing interface.
 */
public class UIConstants {
    // Primary Colors
    public static final Color PRIMARY_COLOR = new Color(24, 43, 73);      // Deep Navy
    public static final Color PRIMARY_LIGHT = new Color(37, 99, 235);     // Modern Royal Blue
    public static final Color ACCENT_COLOR = new Color(14, 165, 233);      // Sky Blue Accent
    public static final Color ACCENT_HOVER = new Color(2, 132, 199);

    // Status Colors
    public static final Color SUCCESS_COLOR = new Color(16, 185, 129);     // Emerald Green
    public static final Color WARNING_COLOR = new Color(245, 158, 11);     // Amber Warning
    public static final Color DANGER_COLOR = new Color(239, 68, 68);       // Crimson Red
    public static final Color INFO_COLOR = new Color(59, 130, 246);

    // Background & Surface Colors
    public static final Color BG_MAIN = new Color(248, 250, 252);          // Light Slate background
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BG_SIDEBAR = new Color(15, 23, 42);          // Dark Slate Navy
    public static final Color BG_SIDEBAR_ACTIVE = new Color(30, 41, 59);
    public static final Color BG_TABLE_HEADER = new Color(241, 245, 249);
    public static final Color BG_TABLE_ALTERNATE = new Color(248, 250, 252);
    public static final Color BORDER_COLOR = new Color(226, 232, 240);

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);
    public static final Color TEXT_LIGHT = new Color(241, 245, 249);
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;

    // Typography
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_CARD_VALUE = new Font("Segoe UI", Font.BOLD, 28);

    private UIConstants() {}
}
