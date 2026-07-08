package com.pms.ui;

import com.pms.model.*;

import javax.swing.*;
import java.awt.*;

/** Grid of employee cards showing designation, skills and current assignments. */
final class EmployeesView {
    private EmployeesView() {}

    static JComponent build(Company company) {
        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 16));
        grid.setOpaque(false);
        grid.setBorder(Theme.pad(0, 0, 20, 0));
        for (Employee e : company.getEmployees()) {
            grid.add(card(e));
        }
        // wrap so cards keep their height at the top
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(grid, BorderLayout.NORTH);
        return wrap;
    }

    private static JComponent card(Employee e) {
        Theme.Card c = new Theme.Card(16);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

        JPanel head = Theme.hbox();
        head.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel avatar = new JLabel(e.getName().substring(0, 1));
        avatar.setOpaque(false);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(Theme.font(18, Font.BOLD));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(44, 44));
        avatar.setBorder(BorderFactory.createEmptyBorder());
        JPanel av = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        av.setOpaque(false);
        av.setPreferredSize(new Dimension(44, 44));
        av.setMaximumSize(new Dimension(44, 44));
        av.add(avatar);

        JPanel nameBox = Theme.vbox();
        nameBox.add(Theme.label(e.getName(), Theme.h3(), Theme.TEXT));
        nameBox.add(Theme.label(e.getEmployeeId(), Theme.small(), Theme.MUTED));

        head.add(av);
        head.add(Box.createHorizontalStrut(12));
        head.add(nameBox);
        head.add(Box.createHorizontalGlue());
        c.add(head);
        c.add(Box.createVerticalStrut(12));

        c.add(DashboardView.left(Theme.pill(ProjectsView.titleCase(e.getDesignation().name()),
                Theme.ACCENT, Theme.ACCENT_SOFT)));
        c.add(Box.createVerticalStrut(12));

        c.add(DashboardView.left(Theme.label("SKILLS", Theme.font(10, Font.BOLD), Theme.MUTED)));
        c.add(Box.createVerticalStrut(6));
        JPanel skills = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        skills.setOpaque(false);
        skills.setAlignmentX(Component.LEFT_ALIGNMENT);
        skills.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        for (Skill s : e.getSkills()) {
            skills.add(Theme.pill(s.getName() + " · " + ProjectsView.titleCase(s.getLevel().name()),
                    Theme.TEXT, Theme.SURFACE_2));
        }
        c.add(skills);
        c.add(Box.createVerticalStrut(10));

        String projects = e.getAssignments().isEmpty() ? "Unassigned"
                : e.getAssignments().stream().map(a -> a.getProject().getName())
                    .reduce((a, b) -> a + ", " + b).orElse("");
        c.add(DashboardView.left(Theme.label("Working on: " + projects, Theme.small(), Theme.MUTED)));
        return c;
    }
}
