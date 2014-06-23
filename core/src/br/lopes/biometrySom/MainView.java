package br.lopes.biometrySom;

import br.lopes.biometrySom.Images.DownSample;
import br.lopes.biometrySom.Images.LetterDrawn;
import br.lopes.biometrySom.logic.Logic;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap;
import java.util.ArrayList;


public class MainView {

    private Logic logic;
    private ArrayList<LetterDrawn> lettersDrawn;

    public MainView() {
        lettersDrawn = new ArrayList<LetterDrawn>();
        logic = new Logic(this);
    }

    //View Methods
    //Logic Methods
    private void addLetter(Pixmap letterDrawnImage, String letterName) {
        LetterDrawn ld = new LetterDrawn(letterDrawnImage, letterName);
        lettersDrawn.add(ld);

    }

    private void startTrain(NormalizeInput.NormalizationType normalizationType, TrainSelfOrganizingMap.LearningMethod learningMethod, float learnRate) {

        //double[][] train
        int inputCount = (LetterDrawn.DOWNSAMPLE_WIDTH * LetterDrawn.DOWNSAMPLE_HEIGHT);
        int outputCount = lettersDrawn.size();

        double[][] train = new double[lettersDrawn.size()][inputCount];
        //Each Line is a letter representation in pixel
        //Each Column is a pixel
        for (int i = 0; i < lettersDrawn.size(); i++) {
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

    //Recognize Methids
    //Returns the Recognize Letter
    public String recognizeLetter(Pixmap letterDrawnImage) {

        Pixmap downSampled = DownSample.downSample(letterDrawnImage);

        //
        double input[] = new double[downSampled.getWidth() * downSampled.getHeight()];
        int index = 0;
        for (int x = 0; x < downSampled.getWidth(); x++) {
            for (int y = 0; y < downSampled.getHeight(); y++) {
                int pixel = downSampled.getPixel(x, y);
                input[index] = pixel;
                index++;
            }
        }

        int winner = logic.getSom().winner(input);

        for (int i = 0; i < logic.getMap().size(); i++) {
            if (logic.getMap().get(i).getWinnerNeuronIndex() == winner) {
                return logic.getMap().get(i).getLetter();
            }
        }

        return "?";
    }   

    public ArrayList<LetterDrawn> getLettersDrawn() {
        return lettersDrawn;
    }
    
    

}
