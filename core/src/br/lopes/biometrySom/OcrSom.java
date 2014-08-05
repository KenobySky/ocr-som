package br.lopes.biometrySom;

import br.lopes.biometrySom.images.DownSample;
import br.lopes.biometrySom.images.LetterDrawn;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput.NormalizationType;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap.LearningMethod;

public class OcrSom extends ApplicationAdapter {

    private Logic logic;
    private Array<LetterDrawn> lettersDrawn;

    private Stage stage;
    private Pixmap canvasPixmap, samplePixmap;
    private Texture canvasTexture, sampleTexture;
    private TextField letter;
    private List<String> letters;

    public OcrSom() {
        lettersDrawn = new Array<>();
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

        canvasPixmap = new Pixmap(400, 400, Format.RGBA4444);
        canvasPixmap.setColor(Color.WHITE);
        canvasPixmap.fill();
        canvasTexture = new Texture(canvasPixmap);
        samplePixmap = new Pixmap(100, 100, Format.RGBA4444);
        samplePixmap.setColor(Color.WHITE);
        samplePixmap.fill();
        sampleTexture = new Texture(samplePixmap);

        Image canvas = new Image(new TextureRegionDrawable(new TextureRegion(canvasTexture)));
        Button downsample = new TextButton("Downsample", skin), clear = new TextButton("Clear", skin);

        Image sample = new Image(new TextureRegionDrawable(new TextureRegion(sampleTexture)));

        letter = new TextField("", skin);
        letters = new List<>(skin);
        Button train = new TextButton("Train", skin);
        Button addLetter = new TextButton("Add letter", skin);
        Button recognize = new TextButton("Recognize", skin);
        Button delete = new TextButton("Delete", skin);

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
                addLetter(canvasPixmap, getLetter());
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
                int selectedIndex = letters.getSelectedIndex();

                if (selectedIndex != -1) {
                    lettersDrawn.get(selectedIndex).dispose();
                    lettersDrawn.removeIndex(selectedIndex);
                }

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
        addLetterTable.add(letter);
        addLetterTable.add(addLetter);
        table.add(addLetterTable).row();
        table.add(recognize);
        table.add(delete);
        table.debug();

        stage.addActor(table);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        //Table.drawDebug(stage);
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
        if (msg != null) {
            //Show this in A console like in TSM project
        }

    }

    private String getLetter() {
        String string = letter.getText();

        if (string == null) {
            return "?";
        }

        string = string.trim();

        return string;
    }

    //Logic Methods
    private void addLetter(Pixmap letterDrawnImage, String letterName) {
        LetterDrawn ld = new LetterDrawn(letterDrawnImage, letterName);
        lettersDrawn.add(ld);

    }

    private void startTrain(NormalizationType normalizationType, LearningMethod learningMethod, float learnRate) {

        int inputCount = (LetterDrawn.DOWNSAMPLE_WIDTH * LetterDrawn.DOWNSAMPLE_HEIGHT);
        int outputCount = lettersDrawn.size;

        double[][] train = new double[lettersDrawn.size][inputCount];
        //Each Line is a letter representation in pixel
        //Each Column is a pixel
        for (int i = 0; i < lettersDrawn.size; i++) {
            Pixmap letterSample = lettersDrawn.get(i).getSample();
            Pixmap downSampled = DownSample.downSample(letterSample);
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

    /**
     * @return the {@link #lettersDrawn}
     */
    public Array<LetterDrawn> getLettersDrawn() {
        return lettersDrawn;
    }

}
