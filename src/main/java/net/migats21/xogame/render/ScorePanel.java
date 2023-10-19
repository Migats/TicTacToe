/*
 * Copyright (c) 2023 Migats21
 * net.migats21.xogame.render.ScorePanel (Obfuscated variables)
 */
package net.migats21.xogame.render;

import net.migats21.xogame.util.GameMouseHandler;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ScorePanel extends JPanel {
    public static final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, 24);
    public boolean hovered;

    public ScorePanel() {
        setPreferredSize(new Dimension(800, 50));
        setBackground(Color.BLACK);
        addMouseListener(new GameMouseHandler(this));
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setStroke(new BasicStroke(5));
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawOval(10, 10, 30, 30);
        graphics2D.drawLine(getWidth() - 40, 10, getWidth() - 10, 40);
        graphics2D.drawLine(getWidth() - 10, 10, getWidth() - 40, 40);
        graphics2D.setFont(FONT);
        FontMetrics fontMetrics = graphics2D.getFontMetrics(FONT);
        String topText;
        if (hovered) {
            topText = "Click to " + (GamePanel.getInstance().levelState.isAiEnabled() ? "disable" : "enable") + " AI";
        } else {
            topText = GamePanel.getInstance().levelState.getTopText();
        }
        graphics2D.drawString(topText, (getWidth() - fontMetrics.stringWidth(topText)) / 2, 32);
        graphics2D.setFont(FONT);
        float score = GamePanel.getInstance().levelState.getScoreCircle();
        if (score % 1.0f == 0.0f) {
            graphics2D.drawString(Integer.toString((int) score), 50, 35);
        } else {
            String scoreAboveText = Integer.toString((int) Math.ceil(score));
            String scoreBelowText = Integer.toString((int) Math.floor(score));
            graphics2D.drawString(scoreBelowText, 50, (1.0f - score % 1.0f) * 35);
            graphics2D.drawString(scoreAboveText, 50, (1.0f - score % 1.0f) * 35 + 35);
        }
        score = GamePanel.getInstance().levelState.getScoreCross();
        if (score % 1.0f == 0.0f) {
            String scoreText = Integer.toString((int) score);
            graphics2D.drawString(scoreText, getWidth() - 50 - fontMetrics.stringWidth(scoreText), 35);
        } else {
            String scoreAboveText = Integer.toString((int) Math.ceil(score));
            String scoreBelowText = Integer.toString((int) score);
            graphics2D.drawString(scoreBelowText, getWidth() - 50 - fontMetrics.stringWidth(scoreBelowText), (1.0f - score % 1.0f) * 35);
            graphics2D.drawString(scoreAboveText, getWidth() - 50 - fontMetrics.stringWidth(scoreAboveText), (1.0f - score % 1.0f) * 35 + 35);
        }
    }
}
