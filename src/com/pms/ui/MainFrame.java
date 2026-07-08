package com.pms.ui;

import com.pms.model.Company;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Main application window: a sidebar-navigated dashboard for the Project
 * Management System with switchable light / dark appearance.
 * Built with pure Swing and custom painting (see {@link Theme}).
 */
public class MainFrame extends JFrame {

    private final Company company = SampleData.build();

    private CardLayout cards;
    private JPanel content;
    private JLabel pageTitle;
    private JLabel pageSubtitle;
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();
    private String current = "Dashboard";

    private static final String[] PAGES = {"Dashboard", "Projects", "Employees", "Clients"};

    public MainFrame() {
        setTitle("Acme PMS — Project Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 740));
        setSize(1320, 820);
        setLocationRelativeTo(null);
        buildUI();
    }

    /** Builds (or rebuilds, after a theme change) the entire component tree. */
    private void buildUI() {
        navButtons.clear();
        cards = new CardLayout();
        content = new JPanel(cards);
        content.setOpaque(false);
        pageTitle = Theme.label(current, Theme.h1(), Theme.TEXT);
        pageSubtitle = Theme.label(subtitleFor(current), Theme.small(), Theme.MUTED);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMain(), BorderLayout.CENTER);
        setContentPane(root);

        rebuildViews();
        navigate(current);
        revalidate();
        repaint();
    }

    // ---------------------------------------------------------------- sidebar
    private JComponent buildSidebar() {
        JPanel side = new JPanel(new BorderLayout());
        side.setBackground(Theme.SIDEBAR);
        side.setPreferredSize(new Dimension(232, 0));
        side.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER),
                Theme.pad(22, 18, 20, 18)));

        JPanel top = Theme.vbox();

        // brand
        JPanel brand = Theme.hbox();
        brand.setAlignmentX(LEFT_ALIGNMENT);
        JComponent mark = logoMark();
        JPanel bt = Theme.vbox();
        bt.add(Theme.label("Acme PMS", Theme.font(15, Font.BOLD), Theme.SIDEBAR_TEXT));
        bt.add(Theme.label("Project management", Theme.font(11, Font.PLAIN), Theme.SIDEBAR_MUTED));
        brand.add(mark);
        brand.add(Box.createHorizontalStrut(10));
        brand.add(bt);
        brand.add(Box.createHorizontalGlue());
        top.add(brand);
        top.add(Box.createVerticalStrut(28));

        JLabel section = Theme.label("WORKSPACE", Theme.font(10, Font.BOLD), Theme.SIDEBAR_MUTED);
        section.setAlignmentX(LEFT_ALIGNMENT);
        section.setBorder(Theme.pad(0, 12, 8, 0));
        top.add(section);

        for (String name : PAGES) {
            JButton b = navButton(name);
            navButtons.put(name, b);
            top.add(b);
            top.add(Box.createVerticalStrut(4));
        }
        side.add(top, BorderLayout.NORTH);

        JPanel bottom = Theme.vbox();
        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER);
        sep.setBackground(Theme.SIDEBAR);
        sep.setAlignmentX(LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        bottom.add(sep);
        bottom.add(Box.createVerticalStrut(12));
        bottom.add(Theme.label("Acme Software Pvt. Ltd.", Theme.font(11, Font.PLAIN), Theme.SIDEBAR_MUTED));
        bottom.add(Box.createVerticalStrut(2));
        bottom.add(Theme.label("Version 1.0", Theme.font(11, Font.PLAIN), Theme.SIDEBAR_MUTED));
        side.add(bottom, BorderLayout.SOUTH);
        return side;
    }

    /** Small rounded-square brand mark with the company initial. */
    private JComponent logoMark() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        Dimension d = new Dimension(36, 36);
        p.setPreferredSize(d);
        p.setMaximumSize(d);
        p.setMinimumSize(d);
        JLabel a = new JLabel("A", SwingConstants.CENTER);
        a.setForeground(Color.WHITE);
        a.setFont(Theme.font(17, Font.BOLD));
        p.add(a);
        return p;
    }

    private JButton navButton(String name) {
        JButton b = new JButton(name) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = name.equals(current);
                if (sel) {
                    g2.setColor(Theme.ACCENT_SOFT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 9, 9);
                    g2.setColor(Theme.ACCENT);
                    g2.fillRoundRect(0, 8, 3, getHeight() - 16, 3, 3);
                } else if (getModel().isRollover()) {
                    g2.setColor(Theme.isDark() ? Theme.SURFACE : Theme.SURFACE_2);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 9, 9);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setHorizontalAlignment(SwingConstants.LEFT);
        boolean sel = name.equals(current);
        b.setForeground(sel ? Theme.ACCENT : Theme.SIDEBAR_TEXT);
        b.setFont(Theme.font(13, Font.PLAIN));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(Theme.pad(10, 14, 10, 14));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.addActionListener(e -> navigate(name));
        return b;
    }

    private void navigate(String name) {
        current = name;
        navButtons.forEach((n, btn) -> {
            btn.setForeground(n.equals(current) ? Theme.ACCENT : Theme.SIDEBAR_TEXT);
            btn.setFont(Theme.font(13, n.equals(current) ? Font.BOLD : Font.PLAIN));
            btn.repaint();
        });
        pageTitle.setText(name);
        pageSubtitle.setText(subtitleFor(name));
        cards.show(content, name);
    }

    private static String subtitleFor(String name) {
        switch (name) {
            case "Projects":  return "Life cycle, team, billing and client feedback per project";
            case "Employees": return "Staff pool with designations, skills and current assignments";
            case "Clients":   return "Clients and the projects they own";
            default:          return "Portfolio overview across all clients and projects";
        }
    }

    // ------------------------------------------------------------------- main
    private JComponent buildMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BG);
        main.setBorder(Theme.pad(24, 32, 24, 32));

        JPanel header = Theme.hbox();
        header.setAlignmentX(LEFT_ALIGNMENT);
        JPanel titles = Theme.vbox();
        titles.add(pageTitle);
        titles.add(Box.createVerticalStrut(3));
        titles.add(pageSubtitle);
        header.add(titles);
        header.add(Box.createHorizontalGlue());

        JButton themeToggle = Theme.ghostButton(Theme.isDark() ? "Light mode" : "Dark mode");
        themeToggle.addActionListener(e -> {
            Theme.apply(!Theme.isDark());
            buildUI();
        });
        header.add(themeToggle);

        content.setOpaque(false);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(Theme.pad(22, 0, 0, 0));
        wrap.add(content, BorderLayout.CENTER);

        main.add(header, BorderLayout.NORTH);
        main.add(wrap, BorderLayout.CENTER);
        return main;
    }

    /** (Re)build every view from the current company state. */
    private void rebuildViews() {
        content.removeAll();
        content.add(scroll(DashboardView.build(company)), "Dashboard");
        content.add(scroll(ProjectsView.build(company, () -> {
            rebuildViews();
            cards.show(content, current);
            content.revalidate();
            content.repaint();
        })), "Projects");
        content.add(scroll(EmployeesView.build(company)), "Employees");
        content.add(scroll(ClientsView.build(company)), "Clients");
    }

    /** Wrapper that forces its child to the viewport width (so cards never
     *  overflow horizontally) while scrolling vertically. */
    private static class WidthTracking extends JPanel implements Scrollable {
        WidthTracking(JComponent child) {
            super(new BorderLayout());
            setOpaque(false);
            add(child, BorderLayout.NORTH);
        }
        public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 18; }
        public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 200; }
        public boolean getScrollableTracksViewportWidth() { return true; }
        public boolean getScrollableTracksViewportHeight() { return false; }
    }

    static JScrollPane scroll(JComponent c) {
        JScrollPane sp = new JScrollPane(new WidthTracking(c));
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(18);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return sp;
    }

    /** Package-private hooks used by the screenshot tool. */
    void showPage(String name) { navigate(name); }
    void setDarkMode(boolean dark) { Theme.apply(dark); buildUI(); }

    public static void main(String[] args) {
        System.setProperty("apple.awt.application.appearance", "system");
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
