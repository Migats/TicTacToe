package net.migats21.xogame.level;


import javax.swing.Timer;
import java.util.Arrays;

public class CrossAI {

    private final LevelState levelState;
    private int lastMove = -1;
    private int userLastMove;
    private Timer responseTimer = new Timer(1000, (ignored) -> move());
    private static final byte[] ROWS = {0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,2,4,6,0,4,8};
    private static final byte[] FINAL_ORDER = {4,0,8,2,6,1,5,3,7};

    public CrossAI(LevelState levelState) {
        this.levelState = levelState;
        responseTimer.setRepeats(false);
    }
    public void move() {
        byte i = -1;
        if (getWins(i)) return;
        if (levelState.getStarting()) {// getStarting returns true if the user is the starting player
            if (getTactics()) return;
        } else {
            if (playTactics()) return;
        }
        for (int j : FINAL_ORDER) {
            if (move((byte) j)) break;
        }
    }

    private boolean playTactics() {
        if (lastMove == -1) {
            move((byte) 0);
            return true;
        }
        if (lastMove == 0) {
            if (userLastMove == 6 || userLastMove == 2) {
                move((byte) 8);
                return true;
            }
            if (userLastMove == 8 || userLastMove == 1 || userLastMove == 7) {
                move((byte) 6);
                return true;
            }
            if (userLastMove == 3 || userLastMove == 5) {
                move((byte) 2);
                return true;
            }
        }
        if (lastMove == 6 && userLastMove == 3 && levelState.isMoved((byte) 8, true)) {
            if (move((byte) 2)) {
                return true;
            }
        }
        return false;
    }

    private boolean getTactics() {
        if ((levelState.isMoved((byte) 0, true) && levelState.isMoved((byte) 8, true)) || (levelState.isMoved((byte) 2, true) && levelState.isMoved((byte) 6, true))) {
            if (move((byte) 1)) {
                return true;
            }
        }
        if (levelState.isMoved((byte) 5, true) || levelState.isMoved((byte) 7, true)) {
            if (move((byte) 8)) {
                return true;
            }
        }
        return false;
    }

    private boolean getWins(byte i) {
        for(int j = 0; j < ROWS.length; j+=3) {
            byte[] row = Arrays.copyOfRange(ROWS, j, j+3);
            byte k = 0;
            byte l = -1;
            byte m = 0;
            for (byte n : row) {
                // isMoved's second parameter stands for what player is in that field. True for the user, false for the AI
                if (levelState.isMoved(n, false)) {
                    k++;
                    continue;
                }
                if (levelState.isMoved(n, true)) {
                    m++;
                    continue;
                }
                l = n;
            }
            if (l != -1) {
                if (k == 2) {
                    move(l);
                    return true;
                } else if (m == 2) {
                    i = l;
                }
            }
        }
        if (i != -1) {
            move(i);
            return true;
        }
        return false;
    }

    private boolean move(byte i) {
        if (levelState.move(i)) {
            lastMove = i;
            return true;
        }
        return false;
    }

    public void respondToMove(byte i) {
        userLastMove = i;
        responseTimer.start();
    }

    public void resetMemory() {
        lastMove = -1;
    }
}
