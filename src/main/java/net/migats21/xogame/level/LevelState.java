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
    public float getAnimationProgress(byte i) {
        return Math.max(Math.abs(values[i]) - 1.0f, 0.0f);
    }
    public boolean updateAnimationProgress(float deltaTime) {
        boolean isDrawingTurn = false;
        for (int i=0;i<9;i++) {
            if (values[i] > 0.0f && values[i] <= 2.0f) {
                values[i]+=deltaTime*2;
                isDrawingTurn = true;
                continue;
            }
            if (values[i] < 0.0f && values[i] >= -2.0f) {
                values[i]-=deltaTime*2;
                isDrawingTurn = true;
            }
        }
        if (!isDrawingTurn && strike != -1 && winning <= 1.0f) {
            winning+=deltaTime*2;
            return true;
        }
        if (hasChanged) {
            hasChanged = false;
            return true;
        }
        return isDrawingTurn;
    }
    public boolean toggleAI() {
        if (!isAiEnabled()) {
            ai = new CrossAI(this);
            System.out.println("Enabled AI");
            if (!turn && strike == -1 && anySpace()) ai.move();
            return true;
        }
        ai = null;
        System.out.println("Disabled AI");
        return false;
    }
    public boolean move(byte i) {
        if (strike != -1) {
            restart();
            return false;
        }
        if (values[i] == 0.0f) {
            values[i] = turn ? 1.0f : -1.0f;
            System.out.println((turn ? "Circle" : "Cross") + " moved at " + i);
            checkForWin(i, turn);
            turn = !turn;
            hasChanged = true;
            if (strike != -1) {
                System.out.println((turn ? "Cross" : "Circle") + " wins");
            } else if (!anySpace()) {
                System.out.println("Game ended with a draw");
            } else if (!userCanMove()) ai.respondToMove(i);
            return true;
        }
        if (anySpace()) return false;
        restart();
        return false;
    }

    private boolean anySpace() {
        for(float value : values) {
            if (value == 0.0f) return true;
        }
        return false;
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

    private void checkForWin(byte i, boolean turn) {
        byte j = (byte) (i % 3);
        if (isMoved(j, turn) && isMoved((byte) (j+3), turn) && isMoved((byte) (j+6), turn)) {
            strike = j;
            increaseScore();
            return;
        }
        byte k = (byte) (i / 3);
        byte l = (byte) (k*3);
        if (isMoved(l, turn) && isMoved((byte) (l+1), turn) && isMoved((byte) (l+2), turn)) {
            strike = (byte)(k+3);
            increaseScore();
            return;
        }
        if (isMoved((byte) 4, turn) && (j == 1) == (k == 1)) {
            if (isMoved((byte) 2, turn) && isMoved((byte) 6, turn)) {
                strike = 6;
                increaseScore();
                return;
            }
            if (isMoved((byte) 0, turn) && isMoved((byte) 8, turn)) {
                strike = 7;
                increaseScore();
            }
        }
    }

    public boolean isMoved(byte i, boolean player) {
        return player ? values[i] > 0.0f : values[i] < 0.0f;
    }
    public boolean isEmpty(byte i) {
        return values[i] == 0.0f;
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
}
