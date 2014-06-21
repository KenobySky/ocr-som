package br.lopes.biometrySom.logic;

import br.lopes.biometrySom.MainView;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput;
import com.heatonresearch.book.jeffheatoncode.som.SelfOrganizingMap;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class Logic {

    //Reference to View
    private MainView view;

    //SOM Variables
    private boolean started = false;
    private SelfOrganizingMap som;
    private TrainSelfOrganizingMap trainer;

    private Thread workerThread;

    //private SOM Variables
    private float learnRate = 0.4f;
    private NormalizeInput.NormalizationType normalizationType;
    private TrainSelfOrganizingMap.LearningMethod learningMethod;

    //
    public static void main(String args[]) {
        Logic logic = new Logic(null);
        int sample_count = 3;
        int inputCount = 6;
        
        int outputCount = 3;

        double[][] train = new double[sample_count][inputCount];

        //First Sample will set the first outputNeuron
        train[0][0] = 1;
        train[0][1] = 2;
        train[0][2] = 3;
        train[0][3] = 4;
        train[0][4] = 5;
        train[0][5] = 6;
 
        //First Sample will set the first outputNeuron
        train[1][0] = 80;
        train[1][1] = 90;
        train[1][2] = 100;
        train[1][3] = 110;
        train[1][4] = 120;
        train[1][5] = 130;
 
        //First Sample will set the first outputNeuron
        train[2][0] = 200;
        train[2][1] = 210;
        train[2][2] = 220;
        train[2][3] = 230;
        train[2][4] = 240;
        train[2][5] = 250;
 
        
        logic.normalizationType = NormalizeInput.NormalizationType.MULTIPLICATIVE;
        logic.learningMethod = TrainSelfOrganizingMap.LearningMethod.ADDITIVE;

        logic.start(inputCount, outputCount, logic.normalizationType, train, logic.learningMethod, logic.learnRate);
    }

    public Logic(MainView view) {
        this.view = view;

    }

    public void start(int inputCount, int outputCount, NormalizeInput.NormalizationType normalizationType, double[][] train, TrainSelfOrganizingMap.LearningMethod learnMethod, double learnRate) {
        if (!started) {
            init(inputCount, outputCount, normalizationType, train, learnMethod, learnRate);
            started = true;
        }
        System.out.println("Commencing Worker Thread!!");
        workerThread = new Thread(new WorkerThread(trainer, som));
        workerThread.start();

    }

    public void init(int inputCount, int outputCount, NormalizeInput.NormalizationType normalizationType, double[][] train, TrainSelfOrganizingMap.LearningMethod learnMethod, double learnRate) {
        //Debug
        System.out.println("[0;2] : "+train[0][2]);
        //
        
        System.out.println("Initializing SOM");
        som = new SelfOrganizingMap(inputCount, outputCount, normalizationType);
        
        System.out.println("Initializing Train Self Organizing Map");
        trainer = new TrainSelfOrganizingMap(som, train, learnMethod, learnRate);

        System.out.println("Finished Initializing TSOM and SOM");
    }

}
