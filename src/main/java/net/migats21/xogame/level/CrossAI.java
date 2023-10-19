/*
 * Copyright (c) 2023 Migats21
 * net.migats21.xogame.level.CrossAI
 */
package net.migats21.xogame.level;

import javax.swing.Timer;
import java.util.Arrays;

public class CrossAI {
    private static final byte[] ROWS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 0, 3, 6, 1, 4, 7, 2, 5, 8, 2, 4, 6, 0, 4, 8};
    private static final byte[] FINAL_ORDER = {4, 0, 8, 2, 6, 1, 5, 3, 7};
    private final LevelState levelState;
    private int lastMove = -1;
    private int userLastMove;
    private final Timer responseTimer = new Timer(1000, (ignored) -> move());

    public CrossAI(LevelState levelState) {
        this.levelState = levelState;
        responseTimer.setRepeats(false);
    }

    public void move() {
        if (getWins()) return;
        // getStarting returns true if the user is the starting player
        if (levelState.getStarting()) {
            if (getForks()) return;
        } else {
            if (buildFork()) return;
        }
        for (byte move : FINAL_ORDER) {
            if (move(move)) break;
        }
    }

    private boolean buildFork() {
        if (lastMove == -1) {
            return move((byte) 0);
        }
        if (lastMove == 0) {
            if (userLastMove == 6 || userLastMove == 2) {
                return move((byte) 8);
            }
            if (userLastMove == 8 || userLastMove == 1 || userLastMove == 7) {
                return move((byte) 6);
            }
            if (userLastMove == 3 || userLastMove == 5) {
                return move((byte) 2);
            }
        }
        if (lastMove == 6 && userLastMove == 3 && levelState.isMoved((byte) 8, true)) {
            return move((byte) 2);
        }
        return false;
    }

    private boolean getForks() {
        if ((levelState.isMoved((byte) 0, true) && levelState.isMoved((byte) 8, true)) || (levelState.isMoved((byte) 2, true) && levelState.isMoved((byte) 6, true))) {
            if (move((byte) 1)) return true;
        }
        if (levelState.isMoved((byte) 5, true) || levelState.isMoved((byte) 7, true)) {
            return move((byte) 8);
        }
        return false;
    }

    private boolean getWins() {
        byte win = -1;
        for (int i = 0; i < ROWS.length; i += 3) {
            byte[] row = Arrays.copyOfRange(ROWS, i, i + 3);
            byte crossCount = 0;
            byte block = -1;
            byte circleCount = 0;
            for (byte b : row) {
                // isMoved's second parameter stands for what player is in that field. True for the user, false for the AI
                if (levelState.isMoved(b, false)) {
                    crossCount++;
                    continue;
                }
                if (levelState.isMoved(b, true)) {
                    circleCount++;
                    continue;
                }
                block = b;
            }
            if (block != -1) {
                if (crossCount == 2) {
                    win = block;
                    break;
                } else if (circleCount == 2) {
                    win = block;
                }
            }
        }
        if (win != -1) {
            move(win);
            return true;
        }
        return false;
    }

    private boolean move(byte index) {
        if (levelState.move(index)) {
            lastMove = index;
            return true;
        }
        return false;
    }

    public void respondToMove(byte index) {
        userLastMove = index;
        responseTimer.start();
    }

    public void resetMemory() {
        lastMove = -1;
    }
}
