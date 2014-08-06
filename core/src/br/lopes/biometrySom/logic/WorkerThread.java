package br.lopes.biometrySom.logic;

import br.lopes.biometrySom.Options;
import br.lopes.biometrySom.images.DownSample;
import br.lopes.biometrySom.images.Letter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.heatonresearch.book.jeffheatoncode.som.SelfOrganizingMap;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap;
import java.util.ArrayList;

public class WorkerThread implements Runnable {

    //References
    private final TrainSelfOrganizingMap trainer;
    private final SelfOrganizingMap som;
    private Logic logic;

    //Variables
    private int MAX_ERROR_COUNT = 2000;
    private int retry = 0;
    private double totalError, bestError;

    public WorkerThread(Logic logic, TrainSelfOrganizingMap trainer, SelfOrganizingMap som, int max_error_count) {
        this.trainer = trainer;
        this.som = som;
        this.MAX_ERROR_COUNT = max_error_count;
        this.logic = logic;
    }

    @Override
    public void run() {
        while (Options.isThreadRunning()) {

            trainer.initialize();

            double lastError = Double.MAX_VALUE;
            int errorCount = 0;

            while (errorCount < MAX_ERROR_COUNT) {
                trainer.iteration();
                retry++;
                this.totalError = trainer.getTotalError();
                this.bestError = trainer.getBestError();

                if (this.bestError < lastError) {
                    lastError = this.bestError;
                    errorCount = 0;
                } else {
                    errorCount++;
                }

                logic.printInfo("Training : Last Error = " + lastError);

                System.out.println("Trainer.getBestError() " + trainer.getBestError());

                System.out.println("Error Count = " + errorCount);

                if (!Options.isThreadRunning()) {
                    System.out.println("Warning : Breaking Thread during inner loop!");
                    break;
                }

            }

            logic.printInfo("Finished Training SOM - Commencing Mapping...");
            mapNeurons(logic.getMap());
            logic.printInfo("Finished Mapping! Ready To Recognize!");
            Options.setThreadRunning(false);

        }
    }

    public ArrayList<Map> mapNeurons(ArrayList<Map> map) {

        Array<Letter> lettersDrawn = logic.getOcrSom().getLetters();

        double[] input = new double[(Options.getDownsampleWidth() * Options.getDownsampleHeight())];

        for (int i = 0; i < lettersDrawn.size; i++) {

            Pixmap downSampled = DownSample.downSample(lettersDrawn.get(i).getSample());

            int index = 0;

            for (int x = 0; x < downSampled.getWidth(); x++) {
                for (int y = 0; y < downSampled.getHeight(); y++, index++) {

                    int pixel = downSampled.getPixel(x, y);
                    input[index] = pixel;

                }
            }
            map.add(new Map(som.winner(input), lettersDrawn.get(i).getName()));
        }

        return logic.getMap();
    }

}
