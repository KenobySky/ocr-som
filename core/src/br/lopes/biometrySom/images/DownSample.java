package br.lopes.biometrySom.images;

import static br.lopes.biometrySom.images.LetterDrawn.DOWNSAMPLE_HEIGHT;
import static br.lopes.biometrySom.images.LetterDrawn.DOWNSAMPLE_WIDTH;
import com.badlogic.gdx.graphics.Pixmap;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class DownSample {

    public static Pixmap downSample(Pixmap sample) {
        Pixmap findBorders = findBorders(sample);
        Pixmap downSample = downSample(DOWNSAMPLE_WIDTH, DOWNSAMPLE_HEIGHT, findBorders);
        return downSample;
    }

    //Eliminate White Space around the drawn letter
    private static Pixmap findBorders(Pixmap letterDrawn) {

        return null;
    }

    //Reduce Size of drawn Letter
    private static Pixmap downSample(int width, int height, Pixmap letterDrawn) {
        return null;
    }

}
