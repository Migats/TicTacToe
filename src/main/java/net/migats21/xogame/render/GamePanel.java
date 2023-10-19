/*
 * Copyright (c) 2023 Migats21
 * net.migats21.xogame.render.GamePanel
 */
package net.migats21.xogame.render;

import net.migats21.xogame.level.LevelState;
import net.migats21.xogame.util.GameMouseHandler;
import net.migats21.xogame.util.Time;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GamePanel extends JPanel {

    private static GamePanel instance;
    public LevelState levelState;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 800));
        setBackground(new Color(0x181818));
        levelState = new LevelState();
        addMouseListener(new GameMouseHandler(this));
        instance = this;
    }

    public static GamePanel getInstance() {
        return instance;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setStroke(new BasicStroke(9));
        graphics2D.setColor(Color.WHITE);
        int leftLineX = (getWidth() + 10) / 3;
        int rightLineX = (getWidth() * 2 - 10) / 3;
        int symbolMarginX = getWidth() * 5 / 16;
        int topLineY = (getHeight() + 10) / 3;
        int bottomLineY = (getHeight() * 2 - 10) / 3;
        int symbolMarginY = getHeight() * 5 / 16;
        graphics2D.drawLine(leftLineX, 30, leftLineX, getHeight() - 30);
        graphics2D.drawLine(rightLineX, 30, rightLineX, getHeight() - 30);
        graphics2D.drawLine(getWidth() - 30, topLineY, 30, topLineY);
        graphics2D.drawLine(getWidth() - 30, bottomLineY, 30, bottomLineY);
        for (byte b = 0; b < 9; b++) {
            if (levelState.isEmpty(b)) continue;
            float frameTime = levelState.getAnimationProgress(b);
            int symbolX = (b % 3 - 1) * symbolMarginX + getWidth() / 2;
            int symbolY = (b / 3 - 1) * symbolMarginY + getHeight() / 2;
            int symbolSize = Math.min(getWidth() + 100, getHeight() + 100) / 12;
            if (levelState.isMoved(b, true)) {
                graphics2D.drawArc(symbolX - symbolSize, symbolY - symbolSize, symbolSize * 2, symbolSize * 2, 80, (int) (360.0f * frameTime) + 80);
            } else {
                graphics2D.drawLine(symbolX - symbolSize, symbolY - symbolSize, symbolX - symbolSize + Math.min((int) (frameTime * symbolSize * 4), symbolSize * 2), symbolY - symbolSize + Math.min((int) (frameTime * symbolSize * 4), symbolSize * 2));
                if (frameTime >= 0.5f) {
                    graphics2D.drawLine(symbolX + symbolSize, symbolY - symbolSize, symbolX + symbolSize + Math.max((int) (symbolSize * 2 - frameTime * symbolSize * 4), symbolSize * -2), symbolY - symbolSize - Math.max((int) (symbolSize * 2 - frameTime * symbolSize * 4), symbolSize * -2));
                }
            }
        }
        if (levelState.hasStrike()) {
            byte strike = levelState.getStrike();
            int lineStartX = ((int) strike % 3 - 1) * symbolMarginX + getWidth() / 2;
            int lineStartY = ((int) strike % 3 - 1) * symbolMarginY + getHeight() / 2;
            int lineEndX = (int) (30 + levelState.getStrikeAnimationProgress() * (getWidth() - 60));
            int lineEndY = (int) (30 + levelState.getStrikeAnimationProgress() * (getHeight() - 60));
            if (strike < 3) {
                graphics2D.drawLine(lineStartX, 30, lineStartX, lineEndY);
            } else if (strike < 6) {
                graphics2D.drawLine(getWidth() - 30, lineStartY, getWidth() - lineEndX, lineStartY);
            } else if (strike == 6) {
                graphics2D.drawLine(getWidth() - 30, 30, getWidth() - lineEndX, lineEndY);
            } else {
                graphics2D.drawLine(30, 30, lineEndX, lineEndY);
            }
        }
    }

    public boolean loop() {
        // Optimised rendering
        if (levelState.updateAnimationProgress(Time.getDeltaTime())) {
            repaint();
            return true;
        }
        return false;
    }
}
