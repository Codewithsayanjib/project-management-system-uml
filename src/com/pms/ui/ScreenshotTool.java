package com.pms.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/** Renders each page of the GUI to a PNG (for the report). Dev utility. */
public class ScreenshotTool {
    public static void main(String[] args) throws Exception {
        String outDir = args.length > 0 ? args[0] : "docs";
        new File(outDir).mkdirs();

        final MainFrame[] holder = new MainFrame[1];
        SwingUtilities.invokeAndWait(() -> {
            MainFrame f = new MainFrame();
            f.setVisible(true);
            holder[0] = f;
        });
        MainFrame f = holder[0];
        Thread.sleep(700); // let peers realize & fonts load

        for (boolean darkMode : new boolean[]{true, false}) {
            final boolean dm = darkMode;
            SwingUtilities.invokeAndWait(() -> f.setDarkMode(dm));
            Thread.sleep(400);
            for (String page : new String[]{"Dashboard", "Projects", "Employees", "Clients"}) {
                SwingUtilities.invokeAndWait(() -> {
                    f.showPage(page);
                    f.getContentPane().validate();
                    f.repaint();
                });
                Thread.sleep(400);
                BufferedImage img = new BufferedImage(f.getContentPane().getWidth(),
                        f.getContentPane().getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = img.createGraphics();
                final Graphics2D gg = g;
                SwingUtilities.invokeAndWait(() -> f.getContentPane().printAll(gg));
                g.dispose();
                String suffix = darkMode ? "" : "_light";
                File out = new File(outDir, "gui_" + page.toLowerCase() + suffix + ".png");
                ImageIO.write(img, "png", out);
                System.out.println("wrote " + out.getPath() + " (" + img.getWidth() + "x" + img.getHeight() + ")");
            }
        }
        System.exit(0);
    }
}
