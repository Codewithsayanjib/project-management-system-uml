package com.pms.ui;

import com.pms.model.*;

import javax.swing.*;
import java.awt.*;

/** The dashboard: KPI stat cards + a compact overview of every project. */
final class DashboardView {
    private DashboardView() {}

    static JComponent build(Company company) {
        JPanel root = Theme.vbox();
        root.setBorder(Theme.pad(0, 0, 20, 0));

        // ---- stat cards row
        JPanel stats = new JPanel(new GridLayout(1, 4, 16, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        long active = company.getProjects().stream()
                .filter(p -> p.getStatus().name().equals("IN_PROGRESS")).count();
        stats.add(stat("Active clients", String.valueOf(company.getClients().size()),
                "across all engagements"));
        stats.add(stat("Employees", String.valueOf(company.getEmployees().size()),
                "in the staffing pool"));
        stats.add(stat("Projects", String.valueOf(company.getProjects().size()),
                active + " currently in progress"));
        stats.add(stat("Billed revenue", String.format("$%,.0f", company.totalRevenue()),
                "invoiced incl. tax"));
        root.add(stats);
        root.add(Box.createVerticalStrut(22));

        root.add(left(Theme.label("Projects overview", Theme.h2(), Theme.TEXT)));
        root.add(Box.createVerticalStrut(12));

        for (Project p : company.getProjects()) {
            root.add(overviewCard(p));
            root.add(Box.createVerticalStrut(12));
        }
        return root;
    }

    private static JComponent stat(String title, String value, String hint) {
        Theme.Card c = new Theme.Card(14);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.add(left(Theme.label(title.toUpperCase(), Theme.font(10, Font.BOLD), Theme.MUTED)));
        c.add(Box.createVerticalStrut(8));
        c.add(left(Theme.label(value, Theme.font(27, Font.BOLD), Theme.TEXT)));
        c.add(Box.createVerticalStrut(4));
        c.add(left(Theme.label(hint, Theme.font(11, Font.PLAIN), Theme.MUTED)));
        return c;
    }

    private static JComponent overviewCard(Project p) {
        Theme.Card c = new Theme.Card(16);
        c.setLayout(new BorderLayout(16, 0));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel leftBox = Theme.vbox();
        JPanel row = Theme.hbox();
        row.add(Theme.label(p.getName(), Theme.h3(), Theme.TEXT));
        row.add(Box.createHorizontalStrut(10));
        row.add(Theme.statusPill(p.getStatus().name()));
        row.add(Box.createHorizontalStrut(6));
        row.add(Theme.pill(p.projectType(), Theme.MUTED, Theme.SURFACE_2));
        leftBox.add(left(row));
        leftBox.add(Box.createVerticalStrut(6));
        leftBox.add(left(Theme.label(p.getClient().getName() + "  •  current phase: "
                + p.getTracker().getCurrentPhase().getLabel(), Theme.small(), Theme.MUTED)));
        c.add(leftBox, BorderLayout.CENTER);

        JPanel rightBox = Theme.vbox();
        rightBox.setPreferredSize(new Dimension(220, 60));
        double pct = p.getTracker().overallCompletion() / 100.0;
        JPanel pr = Theme.hbox();
        pr.add(Theme.label("Progress", Theme.small(), Theme.MUTED));
        pr.add(Box.createHorizontalGlue());
        pr.add(Theme.label((int) Math.round(pct * 100) + "%", Theme.font(12, Font.BOLD), Theme.TEXT));
        rightBox.add(pr);
        rightBox.add(Box.createVerticalStrut(6));
        Theme.Bar bar = new Theme.Bar(pct, Theme.statusColor(p.getStatus().name()));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        rightBox.add(bar);
        rightBox.add(Box.createVerticalStrut(8));
        JPanel inv = Theme.hbox();
        inv.add(Box.createHorizontalGlue());
        inv.add(Theme.label("Invoice", Theme.font(11, Font.PLAIN), Theme.MUTED));
        inv.add(Box.createHorizontalStrut(6));
        inv.add(Theme.label(String.format("$%,.0f", p.invoiceAmount()),
                Theme.font(12, Font.BOLD), Theme.TEXT));
        rightBox.add(inv);
        c.add(rightBox, BorderLayout.EAST);
        return c;
    }

    static JComponent left(JComponent c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height + 4));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(c);
        return p;
    }
    static JComponent right(JComponent c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(c);
        return p;
    }
}
