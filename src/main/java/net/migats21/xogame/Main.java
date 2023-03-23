package net.migats21.xogame;

import net.migats21.xogame.render.XOFrame;

public class Main {
    public static void main(String[] args) {
        XOFrame xoFrame = new XOFrame();
        xoFrame.init(args);
        while (xoFrame.isVisible()) {
            xoFrame.loop();
        }
    }
}