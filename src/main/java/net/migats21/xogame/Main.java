/*
 * Copyright (c) 2023 Migats21
 * net.migats21.xogame.Main
 */
package net.migats21.xogame;

import net.migats21.xogame.render.XOFrame;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final int FRAME_INTERVAL = 1000 / GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();

    public static void main(String[] args) {
        XOFrame xoFrame = new XOFrame();
        SwingUtilities.invokeLater(() -> {
            xoFrame.init();
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(xoFrame::loop, 0, FRAME_INTERVAL, TimeUnit.MILLISECONDS);
        });
    }
}