/*
 * Copyright (c) 2023 Migats21
 * net.migats21.xogame.render.XOFrame (Obfuscated variables)
 */
package net.migats21.xogame.render;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class XOFrame extends JFrame {
    GamePanel gamePanel;
    ScorePanel scorePanel;

    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        // setResizable(false); The program can now be resized
        setTitle("Tic-tac-toe");
        setLayout(new BorderLayout());
        gamePanel = new GamePanel();
        add(gamePanel);
        scorePanel = new ScorePanel();
        add(scorePanel, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void loop() {
        if (gamePanel.loop()) scorePanel.repaint();
    }

}
