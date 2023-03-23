package net.migats21.xogame.render;

import net.migats21.xogame.level.LevelState;
import net.migats21.xogame.util.GameMouseListener;
import net.migats21.xogame.util.Time;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public LevelState levelState;
    private static GamePanel instance;
    public static GamePanel getInstance() {
        return instance;
    }

    public GamePanel() {
        setPreferredSize(new Dimension(800, 800));
        setBackground(new Color(0x181818));
        levelState = new LevelState();
        addMouseListener(new GameMouseListener(this));
        instance = this;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(9));
        g2d.setColor(Color.WHITE);
        int x1 = (getWidth() + 10) / 3;
        int x2 = (getWidth() * 2 - 10) / 3;
        int x3 = getWidth()*5/16;
        int y1 = (getHeight() + 10) / 3;
        int y2 = (getHeight() * 2 - 10) / 3;
        int y3 = getHeight()*5/16;
        g2d.drawLine(x1, 30, x1, getHeight()-30);
        g2d.drawLine(x2, 30, x2, getHeight()-30);
        g2d.drawLine(getWidth()-30, y1, 30, y1);
        g2d.drawLine(getWidth()-30, y2, 30, y2);
        for (byte i=0;i<9;i++) {
            if (levelState.isEmpty(i)) continue;
            float f = levelState.getAnimationProgress(i);
            x1 = (i%3-1)*x3 + getWidth()/2;
            y1 = (i/3-1)*y3 + getHeight()/2;
            int s = Math.min(getWidth()+100,getHeight()+100)/12;
            if (levelState.isMoved(i, true)) {
                g2d.drawArc(x1-s, y1-s, s*2, s*2, 80, (int) (360.0f*f)+80);
            } else {
                g2d.drawLine(x1-s, y1-s, x1-s+Math.min((int)(f*s*4),s*2), y1-s+Math.min((int)(f*s*4),s*2));
                if (f >= 0.5f) {
                    g2d.drawLine(x1+s, y1-s, x1+s+Math.max((int) (s*2 - f * s*4), s*-2), y1 - s - Math.max((int) (s*2-f*s*4), s*-2));
                }
            }
        }
        if (levelState.hasStrike()) {
            byte b = levelState.getStrike();
            x1 = ((int)b%3-1)*x3 + getWidth()/2;
            y1 = ((int)b%3-1)*y3 + getHeight()/2;
            x2 = (int) (30 + levelState.getStrikeAnimationProgress() * (getWidth()-60));
            y2 = (int) (30 + levelState.getStrikeAnimationProgress() * (getHeight()-60));
            if (b < 3) {
                g2d.drawLine(x1,30, x1, y2);
            } else if (b < 6) {
                g2d.drawLine(getWidth()-30, y1, getWidth()-x2, y1);
            } else if (b == 6) {
                g2d.drawLine(getWidth()-30, 30, getWidth()-x2, y2);
            } else {
                g2d.drawLine(30, 30, x2, y2);
            }
        }
    }

    public boolean loop() {
        if (levelState.updateAnimationProgress(Time.getDeltaTime())) {
            repaint();
            return true;
        }
        return false;
    }
}
