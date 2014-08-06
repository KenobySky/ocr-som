package br.lopes.biometrySom;

import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput.NormalizationType;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap.LearningMethod;

public class Options {

    private static int DOWNSAMPLE_WIDTH, DOWNSAMPLE_HEIGHT;
    private static NormalizationType normalizeInput;
    private static LearningMethod trainLearningMethod;
    private static float learnRate;
    private static int MAX_ERROR_COUNT;
    private static boolean running = false;
    private static boolean REMOVE_BORDERS;

    static {
        DOWNSAMPLE_WIDTH = 10;
        DOWNSAMPLE_HEIGHT = 10;
        normalizeInput = NormalizationType.MULTIPLICATIVE;
        trainLearningMethod = LearningMethod.ADDITIVE;
        learnRate = 0.2f;
        MAX_ERROR_COUNT = 200;
        running = false;
        REMOVE_BORDERS = false;
    }

    public static boolean isREMOVE_BORDERS() {
        return REMOVE_BORDERS;
    }

    public static void setREMOVE_BORDERS(boolean REMOVE_BORDERS) {
        Options.REMOVE_BORDERS = REMOVE_BORDERS;
    }

    public static int getDOWNSAMPLE_WIDTH() {
        return DOWNSAMPLE_WIDTH;
    }

    public static void setDOWNSAMPLE_WIDTH(int downsample_width) {
        if (downsample_width <= 0) {
            throw new IllegalArgumentException("DownSample_Width cannot be <= 0 !");
        } else {
            Options.DOWNSAMPLE_WIDTH = downsample_width;
        }
    }

    public static int getDOWNSAMPLE_HEIGHT() {
        return DOWNSAMPLE_HEIGHT;
    }

    public static void setDOWNSAMPLE_HEIGHT(int downsample_height) {
        if (downsample_height <= 0) {
            throw new IllegalArgumentException("DownSample_Height cannot be <= 0 !");
        } else {
            Options.DOWNSAMPLE_HEIGHT = downsample_height;
        }
    }

    public static boolean isThreadRunning() {
        return running;
    }

    public static void setThreadRunning(boolean isRunning) {
        running = isRunning;
    }

    public static NormalizationType getNormalizeInput() {
        return normalizeInput;
    }

    public static void setNormalizeInput(NormalizationType normalizeInput) {
        Options.normalizeInput = normalizeInput;
    }

    public static LearningMethod getTrainLearningMethod() {
        return trainLearningMethod;
    }

    public static void setTrainLearningMethod(LearningMethod trainLearningMethod) {
        Options.trainLearningMethod = trainLearningMethod;
    }

    public static float getLearnRate() {

        return learnRate;
    }

    public static void setLearnRate(float learnRate) {
        if (learnRate <= 0) {
            throw new IllegalArgumentException("Learn Rate cannot be <= 0 !");
        } else {
            Options.learnRate = learnRate;
        }
    }

    public static int getMAX_ERROR_COUNT() {
        return MAX_ERROR_COUNT;
    }

    public static void setMAX_ERROR_COUNT(int maxErrorCount) {
        if (maxErrorCount < 0) {
            throw new IllegalArgumentException("Learn Rate cannot be <= 0 !");
        } else {
            Options.MAX_ERROR_COUNT = maxErrorCount;
        }
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Options.running = running;
    }

}
