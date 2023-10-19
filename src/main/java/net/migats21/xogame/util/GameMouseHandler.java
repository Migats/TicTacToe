/*
 * Copyright (c) 2023 Migats21
 * net.migats21.xogame.util.GameMouseListener (Obfuscated variables)
 */
package net.migats21.xogame.util;

import net.migats21.xogame.render.GamePanel;
import net.migats21.xogame.render.ScorePanel;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameMouseHandler implements MouseListener {

    private final Container target;

    public GameMouseHandler(Container container) {
        this.target = container;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (target instanceof GamePanel gamePanel) {
            if (gamePanel.levelState.userCanMove()) {
                byte tileX = (byte) ((mouseEvent.getX() - 10.0f) * 3.0f / (gamePanel.getWidth() - 20.0f));
                byte tileY = (byte) ((mouseEvent.getY() - 10.0f) * 3.0f / (gamePanel.getHeight() - 20.0f));
                if (tileX < 0) tileX = 0;
                if (tileY < 0) tileY = 0;
                if (tileX > 2) tileX = 2;
                if (tileY > 2) tileY = 2;
                gamePanel.levelState.move((byte) (tileX + tileY * 3));
            }
            return;
        }
        if (target instanceof ScorePanel scorePanel) {
            GamePanel.getInstance().levelState.toggleAI();
            scorePanel.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if (target instanceof ScorePanel scorePanel) {
            scorePanel.hovered = true;
            scorePanel.repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (target instanceof ScorePanel scorePanel) {
            scorePanel.hovered = false;
            scorePanel.repaint();
        }
    }
}
