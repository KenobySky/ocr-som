package br.lopes.biometrySom;

import br.lopes.biometrySom.images.DownSample;
import br.lopes.biometrySom.images.LetterDrawn;
import br.lopes.biometrySom.logic.Logic;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput.NormalizationType;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap.LearningMethod;

public class OcrSom extends ApplicationAdapter {

    private Logic logic;
    private Array<LetterDrawn> lettersDrawn;

    public OcrSom() {
        lettersDrawn = new Array<>();
        logic = new Logic(this);
    }
    
    @Override
    public void render() {

    }

    //View Methods
    //Logic Methods
    private void addLetter(Pixmap letterDrawnImage, String letterName) {
        LetterDrawn ld = new LetterDrawn(letterDrawnImage, letterName);
        lettersDrawn.add(ld);

    }

    private void startTrain(NormalizationType normalizationType, LearningMethod learningMethod, float learnRate) {
        //double[][] train
        int inputCount = (LetterDrawn.DOWNSAMPLE_WIDTH * LetterDrawn.DOWNSAMPLE_HEIGHT);
        int outputCount = lettersDrawn.size;

        double[][] train = new double[lettersDrawn.size][inputCount];
        //Each Line is a letter representation in pixel
        //Each Column is a pixel
        for (int i = 0; i < lettersDrawn.size; i++) {
            Pixmap letterSample = lettersDrawn.get(i).getSample();
            Pixmap downSampled = DownSample.downSample(letterSample);

            //
            int index = 0;
            for (int x = 0; x < downSampled.getWidth(); x++) {
                for (int y = 0; y < downSampled.getHeight(); y++) {
                    int pixel = downSampled.getPixel(x, y);
                    train[i][index] = pixel;
                    index++;
                }
            }

        }

        logic.start(inputCount, outputCount, normalizationType, train, learningMethod, learnRate);
    }

    //Recognize Methods
    //Returns the Recognize Letter
    public String recognizeLetter(Pixmap letterDrawnImage) {
        Pixmap downSampled = DownSample.downSample(letterDrawnImage);

        double input[] = new double[downSampled.getWidth() * downSampled.getHeight()];
        int index = 0;
        for (int x = 0; x < downSampled.getWidth(); x++)
			for(int y = 0; y < downSampled.getHeight(); y++) {
				int pixel = downSampled.getPixel(x, y);
				input[index] = pixel;
				index++;
			}

        int winner = logic.getSom().winner(input);

        for (int i = 0; i < logic.getMap().size(); i++)
			if(logic.getMap().get(i).getWinnerNeuronIndex() == winner)
				return logic.getMap().get(i).getLetter();

        return "?";
    }

	/** @return the {@link #lettersDrawn} */
	public Array<LetterDrawn> getLettersDrawn() {
		return lettersDrawn;
	}

}
