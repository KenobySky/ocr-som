package z.test;

import br.lopes.biometrySom.OcrSom;
import br.lopes.biometrySom.logic.Logic;
import br.lopes.biometrySom.logic.WorkerThread;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput;
import com.heatonresearch.book.jeffheatoncode.som.SelfOrganizingMap;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class TestExample {

	//Reference to View
	private OcrSom view;

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
		TestExample logic = new TestExample(null);
		int sample_count = 5;
		int inputCount = 6;

		double[][] train = new double[sample_count][inputCount];

		//First Sample will set the first outputNeuron
		train[0][0] = 0;
		train[0][1] = 1;
		train[0][2] = 0;
		train[0][3] = 1;
		train[0][4] = 0;
		train[0][5] = 1;

		//Second Sample will set the 2nd outputNeuron
		train[1][0] = 1;
		train[1][1] = 1;
		train[1][2] = 1;
		train[1][3] = 1;
		train[1][4] = 1;
		train[1][5] = 1;

		//Third Sample will set the 3rd outputNeuron
		train[2][0] = 500;
		train[2][1] = 600;
		train[2][2] = 500;
		train[2][3] = 600;
		train[2][4] = 500;
		train[2][5] = 400;

		//Fourth Sample will set the 2nd outputNeuron
		train[3][0] = 1000;
		train[3][1] = 99;
		train[3][2] = 0;
		train[3][3] = 0;
		train[3][4] = 2;
		train[3][5] = -8000;

		//Fifth Sample will set the 2nd outputNeuron
		train[4][0] = -500;
		train[4][1] = -500;
		train[4][2] = -500;
		train[4][3] = -500;
		train[4][4] = -500;
		train[4][5] = -500;

		logic.normalizationType = NormalizeInput.NormalizationType.MULTIPLICATIVE;
		logic.learningMethod = TrainSelfOrganizingMap.LearningMethod.ADDITIVE;

		logic.start(inputCount, sample_count, logic.normalizationType, train, logic.learningMethod, logic.learnRate);
	}

	public TestExample(OcrSom view) {
		this.view = view;

	}

	public void start(int inputCount, int outputCount, NormalizeInput.NormalizationType normalizationType, double[][] train, TrainSelfOrganizingMap.LearningMethod learnMethod, double learnRate) {
		if(!started) {
			init(inputCount, outputCount, normalizationType, train, learnMethod, learnRate);
			started = true;
		}
		System.out.println("Commencing Worker Thread!!");
		workerThread = new Thread(new WorkerThread(new Logic(view), trainer, som, 2000));
		workerThread.start();

	}

	public void init(int inputCount, int outputCount, NormalizeInput.NormalizationType normalizationType, double[][] train, TrainSelfOrganizingMap.LearningMethod learnMethod, double learnRate) {
		//Debug
		System.out.println("[0;2] : " + train[0][2]);
		//

		System.out.println("Initializing SOM");
		som = new SelfOrganizingMap(inputCount, outputCount, normalizationType);

		System.out.println("Initializing Train Self Organizing Map");
		trainer = new TrainSelfOrganizingMap(som, train, learnMethod, learnRate);

		System.out.println("Finished Initializing TSOM and SOM");
	}

}
