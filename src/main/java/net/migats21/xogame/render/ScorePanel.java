package net.migats21.xogame.render;

import net.migats21.xogame.util.GameMouseListener;

import javax.swing.JPanel;
import java.awt.*;

public class ScorePanel extends JPanel {
    public static final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, 24);
    public boolean hovered;
    public ScorePanel() {
        setPreferredSize(new Dimension(800, 50));
        setBackground(Color.BLACK);
        addMouseListener(new GameMouseListener(this));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.WHITE);
        g2d.drawOval(10, 10, 30, 30);
        g2d.drawLine(getWidth()-40, 10, getWidth()-10, 40);
        g2d.drawLine(getWidth()-10, 10, getWidth()-40, 40);
        g2d.setFont(FONT);
        FontMetrics fontMetrics = g2d.getFontMetrics(FONT);
        String turn;
        if (hovered) {
            turn = "Click to " + (GamePanel.getInstance().levelState.isAiEnabled() ? "disable" : "enable") + " AI";
        } else {
            turn = GamePanel.getInstance().levelState.getTopText();
        }
        g2d.drawString(turn, (getWidth() - fontMetrics.stringWidth(turn))/2, 32);
        g2d.setFont(FONT);
        float scoreCircle = GamePanel.getInstance().levelState.getScoreCircle();
        float scoreCross = GamePanel.getInstance().levelState.getScoreCross();
        if (scoreCircle % 1.0f == 0.0f) {
            g2d.drawString(Integer.toString((int) scoreCircle), 50, 35);
        } else {
            String scoreCircleText = Integer.toString((int) Math.ceil(scoreCircle));
            String prevScoreCircleText = Integer.toString((int) Math.floor(scoreCircle));
            g2d.drawString(prevScoreCircleText, 50, (1.0f - scoreCircle % 1.0f) * 35);
            g2d.drawString(scoreCircleText, 50, (1.0f - scoreCircle % 1.0f) * 35 + 35);
        }
        if (scoreCross % 1.0f == 0.0f) {
            String scoreCrossText = Integer.toString((int) scoreCross);
            g2d.drawString(scoreCrossText, getWidth() - 50 - fontMetrics.stringWidth(scoreCrossText), 35);
        } else {
            String scoreCrossText = Integer.toString((int) Math.ceil(scoreCross));
            String prevScoreCrossText = Integer.toString((int) Math.floor(scoreCross));
            g2d.drawString(prevScoreCrossText, getWidth() - 50 - fontMetrics.stringWidth(prevScoreCrossText), (1.0f - scoreCross % 1.0f) * 35);
            g2d.drawString(scoreCrossText, getWidth() - 50 - fontMetrics.stringWidth(scoreCrossText), (1.0f - scoreCross % 1.0f) * 35 + 35);
        }
    }
}
