package com.pms.ui;

import com.pms.enums.LifeCyclePhase;
import com.pms.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/** Detailed, interactive per-project cards: requirement, life cycle, team,
 *  feedback, billing, and an action to advance the life cycle. */
final class ProjectsView {
    private ProjectsView() {}

    static JComponent build(Company company, Runnable onChange) {
        JPanel root = Theme.vbox();
        root.setBorder(Theme.pad(0, 0, 20, 0));
        for (Project p : company.getProjects()) {
            root.add(card(p, onChange));
            root.add(Box.createVerticalStrut(16));
        }
        return root;
    }

    private static JComponent card(Project p, Runnable onChange) {
        Theme.Card c = new Theme.Card(18);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);

        // header
        JPanel head = Theme.hbox();
        head.setAlignmentX(Component.LEFT_ALIGNMENT);
        head.add(Theme.label(p.getName(), Theme.h2(), Theme.TEXT));
        head.add(Box.createHorizontalStrut(10));
        head.add(Theme.statusPill(p.getStatus().name()));
        head.add(Box.createHorizontalStrut(6));
        head.add(Theme.pill(p.projectType(), Theme.MUTED, Theme.SURFACE_2));
        head.add(Box.createHorizontalGlue());
        head.add(Theme.label(p.getProjectId(), Theme.font(12, Font.BOLD), Theme.MUTED));
        c.add(head);
        c.add(Box.createVerticalStrut(4));
        c.add(DashboardView.left(Theme.label("Client: " + p.getClient().getName()
                + "   •   " + p.getRequirement().getSummary(), Theme.small(), Theme.MUTED)));
        c.add(Box.createVerticalStrut(16));

        // life cycle phases
        c.add(DashboardView.left(Theme.label("DEVELOPMENT LIFE CYCLE", Theme.font(11, Font.BOLD), Theme.MUTED)));
        c.add(Box.createVerticalStrut(10));
        c.add(phaseTrack(p));
        c.add(Box.createVerticalStrut(16));

        // two columns: team + requirement | feedback + billing
        JPanel cols = new JPanel(new GridLayout(1, 2, 20, 0));
        cols.setOpaque(false);
        cols.setAlignmentX(Component.LEFT_ALIGNMENT);
        cols.add(teamAndReq(p));
        cols.add(feedbackAndBilling(p));
        c.add(cols);
        c.add(Box.createVerticalStrut(16));

        // actions
        JPanel actions = Theme.hbox();
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        boolean done = p.getStatus().name().equals("COMPLETED");
        JButton advance = Theme.button(done ? "Life cycle complete" : "Advance life cycle",
                done ? Theme.SURFACE_2 : Theme.ACCENT, done ? Theme.MUTED : Color.WHITE);
        advance.setEnabled(!done);
        advance.addActionListener(e -> {
            p.advanceLifeCycle();
            p.getTracker().updateProgress(100);
            onChange.run();
        });
        actions.add(advance);
        actions.add(Box.createHorizontalGlue());
        actions.add(Theme.label("Invoice (incl. tax)", Theme.small(), Theme.MUTED));
        actions.add(Box.createHorizontalStrut(8));
        actions.add(Theme.label(String.format("$%,.2f", p.invoiceAmount()), Theme.h3(), Theme.SUCCESS));
        actions.add(Box.createHorizontalStrut(4));
        c.add(actions);
        return c;
    }

    private static JComponent phaseTrack(Project p) {
        JPanel row = new JPanel(new GridLayout(1, LifeCyclePhase.values().length, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        LifeCyclePhase currentPhase = p.getTracker().getCurrentPhase();
        boolean completed = p.getStatus().name().equals("COMPLETED");
        for (Map.Entry<LifeCyclePhase, Integer> e : p.getTracker().getPhaseCompletion().entrySet()) {
            LifeCyclePhase ph = e.getKey();
            int pct = e.getValue();
            boolean isCurrent = ph == currentPhase && !completed;
            Color col = pct >= 100 ? Theme.SUCCESS : isCurrent ? Theme.ACCENT : Theme.SURFACE_2;
            JPanel cell = Theme.vbox();
            JLabel name = Theme.label(ph.getLabel(), Theme.font(11, isCurrent ? Font.BOLD : Font.PLAIN),
                    pct >= 100 ? Theme.TEXT : isCurrent ? Theme.TEXT : Theme.MUTED);
            cell.add(name);
            cell.add(Box.createVerticalStrut(6));
            Theme.Bar bar = new Theme.Bar(pct / 100.0, col);
            bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
            cell.add(bar);
            row.add(cell);
        }
        return row;
    }

    private static JComponent teamAndReq(Project p) {
        JPanel col = Theme.vbox();
        col.add(DashboardView.left(Theme.label("TEAM  (who handles this project)",
                Theme.font(11, Font.BOLD), Theme.MUTED)));
        col.add(Box.createVerticalStrut(8));
        for (Assignment a : p.getAssignments()) {
            JPanel line = Theme.hbox();
            line.add(dot(Theme.ACCENT));
            line.add(Box.createHorizontalStrut(8));
            line.add(Theme.label(a.getEmployee().getName(), Theme.font(13, Font.BOLD), Theme.TEXT));
            line.add(Box.createHorizontalStrut(8));
            String desig = titleCase(a.getEmployee().getDesignation().name());
            String detail = desig.equalsIgnoreCase(a.getRoleOnProject())
                    ? a.getRoleOnProject()
                    : a.getRoleOnProject() + "  ·  " + desig;
            line.add(Theme.label(detail, Theme.small(), Theme.MUTED));
            line.add(Box.createHorizontalGlue());
            col.add(DashboardView.left(line));
            col.add(Box.createVerticalStrut(6));
        }
        col.add(Box.createVerticalStrut(6));
        col.add(DashboardView.left(Theme.label("REQUIREMENT  (dept: " + p.getRequirement().getDepartment()
                + ")", Theme.font(11, Font.BOLD), Theme.MUTED)));
        col.add(Box.createVerticalStrut(6));
        for (String item : p.getRequirement().getItems()) {
            col.add(DashboardView.left(Theme.label("•  " + item, Theme.small(), Theme.TEXT)));
        }
        return col;
    }

    private static JComponent feedbackAndBilling(Project p) {
        JPanel col = Theme.vbox();
        JPanel fh = Theme.hbox();
        fh.add(Theme.label("CLIENT FEEDBACK", Theme.font(11, Font.BOLD), Theme.MUTED));
        fh.add(Box.createHorizontalStrut(8));
        fh.add(Theme.label(String.format("★ %.1f avg", p.averageFeedbackRating()),
                Theme.font(11, Font.BOLD), Theme.WARNING));
        col.add(DashboardView.left(fh));
        col.add(Box.createVerticalStrut(8));
        if (p.getFeedbackLog().isEmpty()) {
            col.add(DashboardView.left(Theme.label("No feedback yet", Theme.small(), Theme.MUTED)));
        }
        for (ClientFeedback f : p.getFeedbackLog()) {
            JPanel line = Theme.hbox();
            line.add(Theme.label(stars(f.getRating()), Theme.small(), Theme.WARNING));
            line.add(Box.createHorizontalStrut(8));
            line.add(Theme.label(f.getPhase().getLabel() + ": ", Theme.font(12, Font.BOLD), Theme.TEXT));
            line.add(Theme.label("“" + f.getComments() + "”", Theme.small(), Theme.MUTED));
            line.add(Box.createHorizontalGlue());
            col.add(DashboardView.left(line));
            col.add(Box.createVerticalStrut(5));
        }
        col.add(Box.createVerticalStrut(8));
        col.add(DashboardView.left(Theme.label("BILLING", Theme.font(11, Font.BOLD), Theme.MUTED)));
        col.add(Box.createVerticalStrut(6));
        Billing b = p.getBilling();
        col.add(DashboardView.left(Theme.label(String.format(
                "Logged %.0f h  •  complexity ×%.1f  •  +18%% tax",
                b.getLoggedHours(), p.complexityFactor()), Theme.small(), Theme.TEXT)));
        return col;
    }

    private static JComponent dot(Color c) {
        JLabel l = new JLabel("●");
        l.setForeground(c);
        l.setFont(Theme.font(9, Font.PLAIN));
        return l;
    }
    private static String stars(int r) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 5; i++) s.append(i < r ? "★" : "☆");
        return s.toString();
    }

    /** PROJECT_MANAGER -> Project Manager */
    static String titleCase(String enumName) {
        String[] parts = enumName.toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();
        for (String w : parts) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
        }
        return sb.toString();
    }
}
