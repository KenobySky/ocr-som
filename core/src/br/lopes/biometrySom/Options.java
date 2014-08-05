package br.lopes.biometrySom;

import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput.NormalizationType;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap.LearningMethod;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class Options {

    public static int DOWNSAMPLE_WIDTH, DOWNSAMPLE_HEIGHT;
    public static NormalizationType normalizeInput;
    public static LearningMethod trainLearningMethod;
    public static float learnRate;

    static {
        normalizeInput = NormalizationType.MULTIPLICATIVE;
        trainLearningMethod = LearningMethod.ADDITIVE;
        learnRate = 0.2f;
        DOWNSAMPLE_WIDTH = 10;
        DOWNSAMPLE_HEIGHT = 10;
    }

}
