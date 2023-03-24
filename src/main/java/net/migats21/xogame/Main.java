package net.migats21.xogame;

import net.migats21.xogame.render.XOFrame;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        XOFrame xoFrame = new XOFrame();
        SwingUtilities.invokeLater(() -> {
            xoFrame.init();
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(xoFrame::loop, 0, 2, TimeUnit.MILLISECONDS);
        });
    }
}