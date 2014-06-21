package br.lopes.biometrySom.logic;

import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput;
import com.heatonresearch.book.jeffheatoncode.som.SelfOrganizingMap;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap;
import java.text.Normalizer;

public class WorkerThread implements Runnable {

    //References
    private final TrainSelfOrganizingMap trainer;
    private final SelfOrganizingMap som;

    //Thread flags
    private boolean stopThread = false;

    //Variables
    private final int MAX_ERROR_COUNT = 2000;
    private int retry = 0;
    private double totalError, bestError;

    public WorkerThread(TrainSelfOrganizingMap trainer, SelfOrganizingMap som) {
        this.trainer = trainer;
        this.som = som;
    }

    @Override
    public void run() {
        while (!stopThread) {

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

                System.out.println("LastError : " + lastError);

                System.out.println("Trainer.getBestError() " + trainer.getBestError());

                System.out.println("Error Count = " + errorCount);

            }
            System.out.println("\nFinished Thread!");

            if (errorCount >= MAX_ERROR_COUNT) {

            }

            stopThread = true;
            test();

        }
    }

    private void test() {
        System.out.println("\n\nTesting SOM!");
        double[] input = new double[6];

//        //First Sample will set the first outputNeuron
//        train[0][0] = 1;
//        train[0][1] = 2;
//        train[0][2] = 3;
//        train[0][3] = 4;
//        train[0][4] = 5;
//        train[0][5] = 6;
// 
//        //First Sample will set the first outputNeuron
//        train[1][0] = 80;
//        train[1][1] = 90;
//        train[1][2] = 100;
//        train[1][3] = 110;
//        train[1][4] = 120;
//        train[1][5] = 130;
// 
//        //First Sample will set the first outputNeuron
//        train[2][0] = 200;
//        train[2][1] = 210;
//        train[2][2] = 220;
//        train[2][3] = 230;
//        train[2][4] = 240;
//        train[2][5] = 250;
        input[0] = 1;
        input[1] = 2;
        input[2] = 3;
        input[3] = 4;
        input[4] = 5;
        input[5] = 6;

        int winner = som.winner(input);
        System.out.println("\nInput :" + printVector(input));
        System.out.println("Winner : " + winner);
        System.out.println("ValueR : " + som.getOutputWeights().get(winner, 0));

        input[0] = 80;
        input[1] = 90;
        input[2] = 100;
        input[3] = 110;
        input[4] = 120;
        input[5] = 130;

        winner = som.winner(input);
        System.out.println("\nInput :" + printVector(input));
        System.out.println("Winner : " + winner);
        System.out.println("ValueR : " + som.getOutputWeights().get(winner, 0));

        input[0] = 200;
        input[1] = 210;
        input[2] = 220;
        input[3] = 230;
        input[4] = 240;
        input[5] = 250;

        winner = som.winner(input);
        System.out.println("\nInput :" + printVector(input));
        System.out.println("Winner : " + winner);
        System.out.println("ValueR : " + som.getOutputWeights().get(winner, 0));

        input[0] = 0;
        input[1] = 2;
        input[2] = 2;
        input[3] = 5;
        input[4] = 5;
        input[5] = 6;

        winner = som.winner(input);
        System.out.println("\nInput :" + printVector(input));
        System.out.println("Winner : " + winner);
        System.out.println("ValueR : " + som.getOutputWeights().get(winner, 0));

    }

    private String printVector(double v[]) {
        String answer = "";

        for (int i = 0; i < v.length; i++) {
            answer += v[i] + ";";
        }
        return answer;
    }

}
