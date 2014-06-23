package br.lopes.biometrySom.logic;

import br.lopes.biometrySom.MainView;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput;
import com.heatonresearch.book.jeffheatoncode.som.SelfOrganizingMap;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap;
import java.util.ArrayList;

public class Logic {

    private MainView mainView;

    //SOM Variables
    public boolean runningThread;
    private SelfOrganizingMap som;
    private TrainSelfOrganizingMap trainer;
    //inner Som Variables
    private NormalizeInput.NormalizationType normalizationType;
    private TrainSelfOrganizingMap.LearningMethod learningMethod;

    //Som Thread
    private Thread workerThread;

    //
    private ArrayList<Map> map;

    public Logic(MainView mv) {
        this.mainView = mv;
        map = new ArrayList<Map>();
    }

    public void start(int inputCount, int outputCount, NormalizeInput.NormalizationType normalizationType, double[][] train, TrainSelfOrganizingMap.LearningMethod learnMethod, float learnRate) {
        if (!runningThread) {
            init(inputCount, outputCount, normalizationType, train, learnMethod, learnRate);
            runningThread = true;
        }

        workerThread = new Thread(new WorkerThread(this, trainer, som, 2000));
        workerThread.start();

    }

    private void init(int inputCount, int outputCount, NormalizeInput.NormalizationType normalizationType, double[][] train, TrainSelfOrganizingMap.LearningMethod learnMethod, float learnRate) {
        som = new SelfOrganizingMap(inputCount, outputCount, normalizationType);
        trainer = new TrainSelfOrganizingMap(som, train, learnMethod, learnRate);
    }

    public void stopThread() {
        runningThread = false;
    }

    public void printInfo(String info) {
        System.out.println(info);
    }

    public String getLetter(int winnerNeuron) {
        for (int i = 0; i < map.size(); i++) {
            if (winnerNeuron == map.get(i).getWinnerNeuronIndex()) {
                return map.get(i).getLetter();
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

    public MainView getMainView() {
        return mainView;
    }
    
    

    
    
}
