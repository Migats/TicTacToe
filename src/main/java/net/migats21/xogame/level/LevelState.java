/*
 * Copyright (c) 2023 Migats21
 * net.migats21.xogame.level.LevelState (Obfuscated variables)
 */
package net.migats21.xogame.level;

public class LevelState {
    private float[] values;
    private boolean turn;
    private boolean starting;
    private float winning;
    private byte strike;
    private boolean hasChanged;
    private int scoreCross, scoreCircle;
    private CrossAI ai;

    public LevelState() {
        values = new float[9];
        winning = 0.0f;
        starting = turn = true;
        strike = -1;
    }

    public float getAnimationProgress(byte index) {
        return Math.max(Math.abs(values[index]) - 1.0f, 0.0f);
    }

    public boolean updateAnimationProgress(float deltaTime) {
        boolean hasChanged = false;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > 0.0f && values[i] <= 2.0f) {
                values[i] += deltaTime * 2;
                hasChanged = true;
                continue;
            }
            if (values[i] < 0.0f && values[i] >= -2.0f) {
                values[i] -= deltaTime * 2;
                hasChanged = true;
            }
        }
        if (!hasChanged && strike != -1 && winning <= 1.0f) {
            winning += deltaTime * 2;
            return true;
        }
        if (this.hasChanged) {
            this.hasChanged = false;
            return true;
        }
        return hasChanged;
    }

    public boolean toggleAI() {
        if (!isAiEnabled()) {
            ai = new CrossAI(this);
            System.out.println("Enabled AI");
            if (!isEmpty()) restart();
            if (!turn) ai.move();
            return true;
        }
        ai = null;
        System.out.println("Disabled AI");
        return false;
    }

    public boolean move(byte index) {
        if (strike != -1) {
            restart();
            return false;
        }
        if (values[index] == 0.0f) {
            values[index] = turn ? 1.0f : -1.0f;
            System.out.println((turn ? "Circle" : "Cross") + " moved at " + index);
            checkForWin(index, turn);
            turn = !turn;
            hasChanged = true;
            if (strike != -1) {
                System.out.println((turn ? "Cross" : "Circle") + " wins");
            } else if (!anySpace()) {
                System.out.println("Game ended with a draw");
            } else if (!userCanMove()) ai.respondToMove(index);
            return true;
        }
        if (anySpace()) return false;
        restart();
        return false;
    }

    private boolean anySpace() {
        for (float f : values) {
            if (f == 0.0f) return true;
        }
        return false;
    }

    private boolean isEmpty() {
        for (float f : values) {
            if (f != 0.0f) return false;
        }
        return true;
    }

    private void restart() {
        values = new float[9];
        winning = 0.0f;
        starting = turn = !starting;
        strike = -1;
        hasChanged = true;
        System.out.println("Game has been reset");
        if (isAiEnabled()) {
            ai.resetMemory();
            if (!turn) ai.move();
        }
    }

    private void checkForWin(byte move, boolean player) {
        byte x = (byte) (move % 3);
        wins:
        {
            if (isMoved(x, player) && isMoved((byte) (x + 3), player) && isMoved((byte) (x + 6), player)) {
                strike = x;
                break wins;
            }
            byte y = (byte) (move / 3);
            byte left = (byte) (y * 3);
            if (isMoved(left, player) && isMoved((byte) (left + 1), player) && isMoved((byte) (left + 2), player)) {
                strike = (byte) (y + 3);
                break wins;
            }
            if (isMoved((byte) 4, player) && (x == 1) == (y == 1)) {
                if (isMoved((byte) 2, player) && isMoved((byte) 6, player)) {
                    strike = 6;
                    break wins;
                }
                if (isMoved((byte) 0, player) && isMoved((byte) 8, player)) {
                    strike = 7;
                    break wins;
                }
            }
            return;
        }
        increaseScore();
    }

    public boolean isMoved(byte index, boolean player) {
        return player ? values[index] > 0.0f : values[index] < 0.0f;
    }

    public boolean isEmpty(byte index) {
        return values[index] == 0.0f;
    }

    public boolean hasStrike() {
        return strike != -1 && winning != 0;
    }

    public byte getStrike() {
        return strike;
    }

    public float getStrikeAnimationProgress() {
        return winning;
    }

    public String getTopText() {
        if (strike != -1) {
            return turn ? "Cross wins" : "Circle wins";
        }
        if (anySpace()) {
            return turn ? "Circle's turn" : "Cross' turn";
        }
        return "Draw";
    }

    public void increaseScore() {
        if (turn) {
            scoreCircle++;
        } else {
            scoreCross++;
        }
    }

    public float getScoreCross() {
        if (strike == -1 || !turn) return scoreCross;
        return scoreCross + Math.min(winning, 1.0f) - 1.0f;
    }

    public float getScoreCircle() {
        if (strike == -1 || turn) return scoreCircle;
        return scoreCircle + Math.min(winning, 1.0f) - 1.0f;
    }

    public boolean userCanMove() {
        return turn || !isAiEnabled() || !anySpace() || hasStrike();
    }

    public boolean isAiEnabled() {
        return ai != null;
    }

    public boolean getStarting() {
        return starting;
    }

    // For debug purposes
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(getClass().getName());
        stringBuilder.append("[");
        for (int i = 0; i < 9; i++) {
            stringBuilder.append(values[i] > 0.0f ? 'O' : values[i] < 0.0f ? 'X' : i);
            if (i < 8) stringBuilder.append(", ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
