package net.migats21.xogame.util;

import net.migats21.xogame.render.GamePanel;
import net.migats21.xogame.render.ScorePanel;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameMouseListener implements MouseListener {

    private final Container container;

    public GameMouseListener(Container container) {
        this.container = container;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (container instanceof GamePanel gamePanel) {
            if (gamePanel.levelState.userCanMove()) {
                byte i = (byte) ((e.getX() - 10.0f) * 3.0f / (gamePanel.getWidth() - 20.0f));
                byte j = (byte) ((e.getY() - 10.0f) * 3.0f / (gamePanel.getHeight() - 20.0f));
                if (i < 0) i = 0;
                if (j < 0) j = 0;
                if (i > 2) i = 2;
                if (j > 2) j = 2;
                gamePanel.levelState.move((byte) (i + j * 3));
            }
            return;
        }
        if (container instanceof ScorePanel scorePanel) {
            GamePanel.getInstance().levelState.toggleAI();
            scorePanel.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (container instanceof ScorePanel scorePanel) {
            scorePanel.hovered = true;
            scorePanel.repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (container instanceof ScorePanel scorePanel) {
            scorePanel.hovered = false;
            scorePanel.repaint();
        }
    }
}
