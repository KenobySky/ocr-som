package br.lopes.biometrySom;

import br.lopes.biometrySom.images.DownSample;
import br.lopes.biometrySom.images.Letter;
import br.lopes.biometrySom.logic.Logic;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput.NormalizationType;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap.LearningMethod;
import net.dermetfan.utils.libgdx.math.GeometryUtils;

public class OcrSom extends ApplicationAdapter {

	private Logic logic;

	private Stage stage;
	private Pixmap canvasPixmap, samplePixmap;
	private Texture canvasTexture, sampleTexture;
	private TextField name;
	private List<Letter> letters;

	public OcrSom() {
		logic = new Logic(this);
	}

	//View Methods
	@Override
	public void create() {
		Assets.manager.load(Assets.class);
		Assets.manager.finishLoading();

		Skin skin = Assets.manager.get(Assets.uiskin, Skin.class);
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		canvasPixmap = new Pixmap(400, 400, Format.RGBA8888);
		canvasPixmap.setColor(Color.WHITE);
		canvasPixmap.fill();
		canvasPixmap.setColor(Color.BLACK); // for drawing
		canvasTexture = new Texture(canvasPixmap);
		samplePixmap = new Pixmap(100, 100, Format.RGBA8888);
		samplePixmap.setColor(Color.WHITE);
		samplePixmap.fill();
		sampleTexture = new Texture(samplePixmap);

		final Image canvas = new Image(new TextureRegionDrawable(new TextureRegion(canvasTexture)));
		Button downsample = new TextButton("Downsample", skin), clear = new TextButton("Clear", skin);
		Image sample = new Image(new TextureRegionDrawable(new TextureRegion(sampleTexture)));

		name = new TextField("", skin);
		letters = new List<>(skin);
		Button train = new TextButton("Train", skin);
		Button addLetter = new TextButton("Add letter", skin);
		Button recognize = new TextButton("Recognize", skin);
		Button delete = new TextButton("Delete", skin);

		canvas.addListener(new InputListener() {
			float oldX;
			float oldY;

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				oldX = x;
				oldY = GeometryUtils.invertAxis(y, canvas.getHeight());
				return true;
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				y = GeometryUtils.invertAxis(y, canvas.getHeight());
				canvasPixmap.drawLine((int) oldX, (int) oldY, (int) x, (int) y);
				canvasTexture.draw(canvasPixmap, 0, 0);
				oldX = x;
				oldY = y;
			}
		});

		downsample.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Pixmap sampled = DownSample.downSample(sampleTexture.getWidth(), sampleTexture.getHeight(), canvasPixmap);
				sampleTexture.draw(sampled, 0, 0);
				sampled.dispose();
			}
		});

		clear.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				canvasPixmap.setColor(Color.WHITE);
				canvasPixmap.fill();
				canvasTexture.draw(canvasPixmap, 0, 0);
				canvasPixmap.setColor(Color.BLACK);
			}
		});

		train.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				startTrain(Options.normalizeInput, Options.trainLearningMethod, Options.learnRate);
				return false;
			}
		});

		addLetter.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				letters.getItems().add(new Letter(canvasPixmap, name.getText()));
				return true;
			}
		});

		recognize.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				String recognizeLetter = recognizeLetter(canvasPixmap);
				showMessage(recognizeLetter);
				return true;
			}
		});

		delete.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				int index = letters.getSelectedIndex();
				if(index != -1)
					letters.getItems().removeIndex(index).getSample().dispose();
				return true;
			}
		});

		Table table = new Table();
		table.setFillParent(true);
		table.defaults().expand().fillX();
		table.add(canvas).fill(false).colspan(2).row();
		table.add(downsample);
		table.add(clear).row();
		table.add(sample).fill(false);
		table.add(letters).row();
		table.add(train);

		Table addLetterTable = new Table();
		addLetterTable.defaults().expand().fillX();
		addLetterTable.add(name);
		addLetterTable.add(addLetter);
		table.add(addLetterTable).row();
		table.add(recognize);
		table.add(delete);

		stage.addActor(table);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();
		canvasPixmap.dispose();
		canvasTexture.dispose();
		samplePixmap.dispose();
		sampleTexture.dispose();
	}

	private void showMessage(String msg) {
		if(msg != null && !msg.isEmpty()) {
			//Show this in A console like in TSM project
		}
	}

	//Logic Methods

	private void startTrain(NormalizationType normalizationType, LearningMethod learningMethod, float learnRate) {

		int inputCount = (Options.DOWNSAMPLE_WIDTH * Options.DOWNSAMPLE_HEIGHT);
		int outputCount = letters.getItems().size;

		double[][] train = new double[letters.getItems().size][inputCount];
		//Each Line is a letter representation in pixel
		//Each Column is a pixel
		for(int i = 0; i < letters.getItems().size; i++) {
			Pixmap letterSample = letters.getItems().get(i).getSample();
			Pixmap downSampled = DownSample.downSample(letterSample);
			int index = 0;
			for(int x = 0; x < downSampled.getWidth(); x++) {
				for(int y = 0; y < downSampled.getHeight(); y++) {
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
		for(int x = 0; x < downSampled.getWidth(); x++) {
			for(int y = 0; y < downSampled.getHeight(); y++) {
				int pixel = downSampled.getPixel(x, y);
				input[index] = pixel;
				index++;
			}
		}

		int winner = logic.getSom().winner(input);

		for(int i = 0; i < logic.getMap().size(); i++) {
			if(logic.getMap().get(i).getWinnerNeuronIndex() == winner) {
				return logic.getMap().get(i).getLetter();
			}
		}

		return "?";
	}

	// getters and setters

	public Array<Letter> getLetters() {
		return letters.getItems();
	}

}
