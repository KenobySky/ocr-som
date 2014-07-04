package br.lopes.biometrySom.logic;

import java.util.ArrayList;

import br.lopes.biometrySom.OcrSom;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput.NormalizationType;
import com.heatonresearch.book.jeffheatoncode.som.SelfOrganizingMap;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap.LearningMethod;

public class Logic {

	private OcrSom ocrSom;

	//SOM Variables
	public boolean runningThread;
	private SelfOrganizingMap som;
	private TrainSelfOrganizingMap trainer;
	//inner Som Variables
	private NormalizationType normalizationType;
	private LearningMethod learningMethod;

	//Som Thread
	private Thread workerThread;

	//
	private ArrayList<Map> map;

	public Logic(OcrSom mv) {
		this.ocrSom = mv;
		map = new ArrayList<Map>();
	}

	public void start(int inputCount, int outputCount, NormalizeInput.NormalizationType normalizationType, double[][] train, TrainSelfOrganizingMap.LearningMethod learnMethod, float learnRate) {
		if(!runningThread) {
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
		for(Map aMap : map)
			if(winnerNeuron == aMap.getWinnerNeuronIndex())
				return aMap.getLetter();
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
