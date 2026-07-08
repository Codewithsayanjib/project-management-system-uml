package com.pms.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Theme system for the application: two hand-tuned palettes (light and dark),
 * shared typography, and small reusable widgets (cards, pills, progress bars,
 * flat buttons). Pure JDK Swing - no external dependencies.
 *
 * Colours are static fields that are re-pointed when the mode changes; the
 * main frame rebuilds its component tree on toggle so every view picks up the
 * new palette.
 */
public final class Theme {
    private Theme() {}

    private static boolean dark = true;

    // ---- palette (assigned by apply()) --------------------------------------
    public static Color BG;
    public static Color SURFACE;
    public static Color SURFACE_2;
    public static Color SIDEBAR;
    public static Color SIDEBAR_TEXT;
    public static Color SIDEBAR_MUTED;
    public static Color ACCENT;
    public static Color ACCENT_SOFT;   // subtle accent background (badges)
    public static Color SUCCESS;
    public static Color SUCCESS_SOFT;
    public static Color WARNING;
    public static Color TEXT;
    public static Color MUTED;
    public static Color BORDER;
    public static Color TRACK;         // empty part of progress bars

    public static boolean isDark() { return dark; }

    public static void apply(boolean darkMode) {
        dark = darkMode;
        if (dark) {
            BG            = new Color(0x11151D);
            SURFACE       = new Color(0x191F2B);
            SURFACE_2     = new Color(0x222A39);
            SIDEBAR       = new Color(0x0D1117);
            SIDEBAR_TEXT  = new Color(0xE6EAF0);
            SIDEBAR_MUTED = new Color(0x7D8590);
            ACCENT        = new Color(0x5B6EE8);
            ACCENT_SOFT   = new Color(0x2A3352);
            SUCCESS       = new Color(0x3FB950);
            SUCCESS_SOFT  = new Color(0x1C3229);
            WARNING       = new Color(0xD29922);
            TEXT          = new Color(0xE6EAF0);
            MUTED         = new Color(0x8B95A5);
            BORDER        = new Color(0x2A3140);
            TRACK         = new Color(0x2A3140);
        } else {
            BG            = new Color(0xF5F6F8);
            SURFACE       = Color.WHITE;
            SURFACE_2     = new Color(0xF0F2F5);
            SIDEBAR       = new Color(0xFFFFFF);
            SIDEBAR_TEXT  = new Color(0x1F2430);
            SIDEBAR_MUTED = new Color(0x8A94A6);
            ACCENT        = new Color(0x4F5BD5);
            ACCENT_SOFT   = new Color(0xE9EBFB);
            SUCCESS       = new Color(0x1F8A4C);
            SUCCESS_SOFT  = new Color(0xE4F3EA);
            WARNING       = new Color(0xB07D10);
            TEXT          = new Color(0x1F2430);
            MUTED         = new Color(0x6C7686);
            BORDER        = new Color(0xE3E6EB);
            TRACK         = new Color(0xE9ECF1);
        }
        UIManager.put("ToolTip.background", SURFACE_2);
        UIManager.put("ToolTip.foreground", TEXT);
    }

    static { apply(true); }

    // ---- typography ---------------------------------------------------------
    public static final String FAMILY = pickFamily();
    public static Font font(int size, int style) { return new Font(FAMILY, style, size); }
    public static Font h1()      { return font(24, Font.BOLD); }
    public static Font h2()      { return font(16, Font.BOLD); }
    public static Font h3()      { return font(14, Font.BOLD); }
    public static Font body()    { return font(13, Font.PLAIN); }
    public static Font small()   { return font(12, Font.PLAIN); }
    public static Font caption() { return font(11, Font.BOLD); }

    private static String pickFamily() {
        String[] prefs = {"SF Pro Text", "Helvetica Neue", "Segoe UI", "Inter", "Arial"};
        java.util.List<String> avail = java.util.Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String p : prefs) if (avail.contains(p)) return p;
        return Font.SANS_SERIF;
    }

    // ---- widgets --------------------------------------------------------------

    /** A rounded, bordered panel used as a card. */
    public static class Card extends JPanel {
        private final int radius;
        private Color fill = SURFACE;
        public Card(int radius) { this(radius, new BorderLayout()); }
        public Card(int radius, LayoutManager lm) {
            super(lm);
            this.radius = radius;
            setOpaque(false);
            setBorder(new EmptyBorder(18, 20, 18, 20));
        }
        public Card fill(Color c) { this.fill = c; return this; }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** A slim rounded progress bar. */
    public static class Bar extends JComponent {
        private double value; // 0..1
        private final Color color;
        public Bar(double value, Color color) {
            this.value = Math.max(0, Math.min(1, value));
            this.color = color;
            setPreferredSize(new Dimension(120, 6));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int h = getHeight();
            g2.setColor(TRACK);
            g2.fillRoundRect(0, 0, getWidth(), h, h, h);
            int w = (int) Math.round(getWidth() * value);
            if (w > 0) {
                g2.setColor(color);
                g2.fillRoundRect(0, 0, Math.max(w, h), h, h, h);
            }
            g2.dispose();
        }
    }

    /** A rounded "pill" label used for statuses and tags. */
    public static JComponent pill(String text, Color fg, Color bg) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setForeground(fg);
        l.setFont(font(11, Font.BOLD));
        l.setBorder(new EmptyBorder(4, 11, 4, 11));
        l.setOpaque(false);
        return l;
    }

    /** Status pill using soft backgrounds (reads better than saturated fills). */
    public static JComponent statusPill(String status) {
        String label = status.replace('_', ' ');
        switch (status) {
            case "COMPLETED":   return pill(label, SUCCESS, SUCCESS_SOFT);
            case "IN_PROGRESS": return pill(label, ACCENT, ACCENT_SOFT);
            case "ON_HOLD":     return pill(label, WARNING, SURFACE_2);
            default:            return pill(label, MUTED, SURFACE_2);
        }
    }

    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    /** Flat, rounded, hover-aware button. */
    public static JButton button(String text, Color bg, Color fg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? bg.brighter() : bg;
                if (getModel().isPressed()) c = bg.darker();
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 9, 9);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(fg);
        b.setFont(font(12, Font.BOLD));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(9, 18, 9, 18));
        return b;
    }

    /** Bordered "secondary" button (outline style). */
    public static JButton ghostButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(SURFACE_2);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 9, 9);
                }
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 9, 9);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(TEXT);
        b.setFont(font(12, Font.BOLD));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        return b;
    }

    public static Border pad(int t, int l, int b, int r) { return new EmptyBorder(t, l, b, r); }

    public static Color statusColor(String status) {
        switch (status) {
            case "COMPLETED":   return SUCCESS;
            case "IN_PROGRESS": return ACCENT;
            case "ON_HOLD":     return WARNING;
            default:            return MUTED;
        }
    }

    public static JPanel vbox() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        return p;
    }
    public static JPanel hbox() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setOpaque(false);
        return p;
    }
}
