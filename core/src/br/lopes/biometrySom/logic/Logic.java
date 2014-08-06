package br.lopes.biometrySom.logic;

import br.lopes.biometrySom.OcrSom;
import br.lopes.biometrySom.Options;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput;
import com.heatonresearch.book.jeffheatoncode.som.SelfOrganizingMap;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap;
import java.util.ArrayList;

public class Logic {

    private OcrSom ocrSom;

    //SOM Variables
    //public boolean runningThread;
    private SelfOrganizingMap som;
    private TrainSelfOrganizingMap trainer;

    //Som Thread
    private Thread workerThread;
    private ArrayList<Map> map;

    public Logic(OcrSom mv) {
        this.ocrSom = mv;
        map = new ArrayList<Map>();
    }

    public void start(int inputCount, int outputCount, NormalizeInput.NormalizationType normalizationType, double[][] train, TrainSelfOrganizingMap.LearningMethod learnMethod, float learnRate) {
        if (!Options.isThreadRunning()) {
            init(inputCount, outputCount, normalizationType, train, learnMethod, learnRate);
            Options.setThreadRunning(true);
        }

        workerThread = new Thread(new WorkerThread(this, trainer, som, Options.getMAX_ERROR_COUNT()));
        workerThread.start();

    }

    private void init(int inputCount, int outputCount, NormalizeInput.NormalizationType normalizationType, double[][] train, TrainSelfOrganizingMap.LearningMethod learnMethod, float learnRate) {
        som = new SelfOrganizingMap(inputCount, outputCount, normalizationType);
        trainer = new TrainSelfOrganizingMap(som, train, learnMethod, learnRate);
    }

    public void stopThread() {
        Options.setThreadRunning(false);
    }

    public void printInfo(String info) {
        ocrSom.showMessage(info);
    }

    public String getLetter(int winnerNeuron) {
        for (Map aMap : map) {
            if (winnerNeuron == aMap.getWinnerNeuronIndex()) {
                return aMap.getLetter();
            }
        }
        return "?";
    }

    public ArrayList<Map> getMap() {
        return map;
    }

    public void setMap(ArrayList<Map> map) {
        this.map = map;
    }

    public SelfOrganizingMap getSom() {
        return som;
    }

    public OcrSom getOcrSom() {
        return ocrSom;
    }

}
