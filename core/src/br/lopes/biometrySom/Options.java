package br.lopes.biometrySom;

import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput.NormalizationType;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap.LearningMethod;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class Options {

    public static final int DOWNSAMPLE_WIDTH = 10, DOWNSAMPLE_HEIGHT = 10;
    public static NormalizationType normalizeInput = NormalizationType.MULTIPLICATIVE;
    public static LearningMethod trainLearningMethod = LearningMethod.ADDITIVE;
    public static float learnRate = .2f;

}
