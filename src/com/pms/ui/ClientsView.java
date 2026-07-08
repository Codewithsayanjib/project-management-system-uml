package com.pms.ui;

import com.pms.model.*;

import javax.swing.*;
import java.awt.*;

/** Client cards, each listing the projects that client owns and their status. */
final class ClientsView {
    private ClientsView() {}

    static JComponent build(Company company) {
        JPanel root = Theme.vbox();
        root.setBorder(Theme.pad(0, 0, 20, 0));
        for (Client cl : company.getClients()) {
            root.add(card(cl));
            root.add(Box.createVerticalStrut(14));
        }
        return root;
    }

    private static JComponent card(Client cl) {
        Theme.Card c = new Theme.Card(16);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel head = Theme.hbox();
        head.setAlignmentX(Component.LEFT_ALIGNMENT);
        head.add(Theme.label(cl.getName(), Theme.h2(), Theme.TEXT));
        head.add(Box.createHorizontalStrut(10));
        head.add(Theme.pill(cl.getClientId(), Theme.MUTED, Theme.SURFACE_2));
        head.add(Box.createHorizontalGlue());
        head.add(Theme.label(cl.getContactEmail(), Theme.small(), Theme.MUTED));
        c.add(head);
        c.add(Box.createVerticalStrut(6));
        c.add(DashboardView.left(Theme.label(cl.getProjects().size() + " project(s)",
                Theme.small(), Theme.MUTED)));
        c.add(Box.createVerticalStrut(12));

        for (Project p : cl.getProjects()) {
            JPanel line = Theme.hbox();
            line.setBorder(Theme.pad(8, 12, 8, 12));
            line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            line.add(Theme.label(p.getName(), Theme.font(13, Font.BOLD), Theme.TEXT));
            line.add(Box.createHorizontalStrut(10));
            line.add(Theme.label(p.projectType(), Theme.small(), Theme.MUTED));
            line.add(Box.createHorizontalGlue());
            line.add(Theme.statusPill(p.getStatus().name()));
            line.add(Box.createHorizontalStrut(12));
            line.add(Theme.label(String.format("$%,.0f", p.invoiceAmount()),
                    Theme.font(12, Font.BOLD), Theme.TEXT));

            JPanel rowWrap = new JPanel(new BorderLayout());
            rowWrap.setBackground(Theme.SURFACE_2);
            rowWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
            rowWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            rowWrap.add(line, BorderLayout.CENTER);
            c.add(rowWrap);
            c.add(Box.createVerticalStrut(8));
        }
        return c;
    }
}
