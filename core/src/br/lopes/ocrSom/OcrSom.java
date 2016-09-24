package br.lopes.ocrSom;

import br.lopes.ocrSom.Options.OptionsGUI;
import br.lopes.ocrSom.images.DownSample;
import br.lopes.ocrSom.images.Letter;
import br.lopes.ocrSom.logic.Logic;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput.NormalizationType;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap.LearningMethod;
import net.dermetfan.gdx.math.GeometryUtils;

public class OcrSom extends ApplicationAdapter {

    private Logic logic = new Logic(this);

    private Stage stage;
    private Pixmap canvasPixmap, samplePixmap;
    private TextureRegion canvasTextureRegion, sampleTextureRegion;
    private TextField name;
    private List<Letter> letters;
    private Label log;
    private ScrollPane logPane;
    private Image sample;

    /**
     * Determines if a downsampling is required or not. True if
     * {@link #canvasPixmap} changed since the last time {@link #downSample()}
     * was called.
     */
    private boolean sampleDirty = true;

    //View Methods
    @Override
    public void create() {
        Assets.manager.load(Assets.class);
        Assets.manager.finishLoading();

        final Skin skin = Assets.manager.get(Assets.uiskin, Skin.class);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        log = new Label("", skin);

        canvasPixmap = new Pixmap(400, 400, Format.RGBA8888);
        canvasPixmap.setColor(Color.WHITE);
        canvasPixmap.fill();
        canvasPixmap.setColor(Color.BLACK); // for drawing
        samplePixmap = new Pixmap(Options.getDownsampleWidth(), Options.getDownsampleHeight(), Format.RGBA8888);

        canvasTextureRegion = new TextureRegion(new Texture(canvasPixmap));
        sampleTextureRegion = new TextureRegion(new Texture(samplePixmap));

        final Image canvas = new Image(new TextureRegionDrawable(canvasTextureRegion));
        sample = new Image(new TextureRegionDrawable(sampleTextureRegion));


        Button downsample = new TextButton("Downsample", skin), clear = new TextButton("Clear", skin), options = new TextButton("Options", skin);
        name = new TextField("", skin);
        letters = new List<>(skin);
        Button train = new TextButton("Train", skin);
        Button addLetter = new TextButton("Add letter", skin);
        Button recognize = new TextButton("Recognize", skin);
        Button delete = new TextButton("Delete", skin);

        /*
         * Add ToolTips
         */
        //TrainButton ToolTip
        train.addListener(new Tooltip<>(new Label("Train The Kohonen Map", skin, "tooltip")));

        //AddLetter
        addLetter.addListener(new Tooltip<>(new Label("Add letter to be trained", skin, "tooltip")));

        //Recognize
        recognize.addListener(new Tooltip<>(new Label("Recognize the letter drawn", skin, "tooltip")));

        //Delete
        delete.addListener(new Tooltip<>(new Label("Delete the selected letter", skin, "tooltip")));

        //Downsample
        downsample.addListener(new Tooltip<>(new Label("Downsample the letter", skin, "tooltip")));

        //Downsample
        letters.addListener(new Tooltip<>(new Label("Array of letters to be trained", skin, "tooltip")));

        //Options
        options.addListener(new Tooltip<>(new Label("SOM-Algorithm Options", skin, "tooltip")));

        //Name
        name.addListener(new Tooltip<>(new Label("Insert letter which was drawn", skin, "tooltip")));

        //Clear
        clear.addListener(new Tooltip<>(new Label("Clear the canvas", skin, "tooltip")));

        //Add Listeners
        canvas.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                y = GeometryUtils.invertAxis(y, canvas.getHeight());
                canvasPixmap.fillCircle((int) x, (int) y, 20);
                canvasTextureRegion.getTexture().draw(canvasPixmap, 0, 0);
                sampleDirty = true;
            }
        });

        downsample.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                downSample();
            }
        });

        clear.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                canvasPixmap.setColor(Color.WHITE);
                canvasPixmap.fill();
                canvasTextureRegion.getTexture().draw(canvasPixmap, 0, 0);
                canvasPixmap.setColor(Color.BLACK);
                sampleDirty = true;
            }
        });

        options.addListener(new ClickListener() {
            Window window = new Window("Options", skin);
            Button close = new TextButton("close", skin);

            {
                window.setModal(true);
                window.setColor(1, 1, 1, 0);
                window.add(new OptionsGUI()).fill().row();
                window.add(close).fill();
                window.pack();


                window.setPosition(stage.getWidth() / 2, stage.getHeight() / 2, Align.center);
                //window.setCenterPosition(stage.getWidth() / 2, stage.getHeight() / 2);

                close.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        window.addAction(Actions.sequence(Actions.fadeOut(.4f), Actions.removeActor()));

                    }
                });
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(window);
                window.addAction(Actions.fadeIn(.4f));
            }
        });

        train.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMessage("Training...");
                startTrain(Options.getNormalizeInput(), Options.getTrainLearningMethod(), Options.getLearnRate());
            }
        });

        addLetter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (name.getText().isEmpty()) {
                    showMessage("Please enter a name for the letter.");
                    return;
                }
                if (sampleDirty) {
                    downSample();
                }
                letters.getItems().add(new Letter(canvasPixmap, samplePixmap, name.getText()));
                name.setText("");
            }
        });

        recognize.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String recognizeLetter = recognizeLetter(canvasPixmap);
                showMessage(recognizeLetter);
            }
        });

        delete.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int index = letters.getSelectedIndex();
                if (index != -1) {
                    letters.getItems().removeIndex(index).dispose();
                }
            }
        });

        letters.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int index = letters.getSelectedIndex();
                if (index != -1) {
                    Pixmap selectedSample = letters.getItems().get(index).getSample();
                    canvasPixmap.drawPixmap(selectedSample, 0, 0);
                    canvasTextureRegion.getTexture().draw(canvasPixmap, 0, 0);
                }
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().expand().fill();
        table.add(canvas).fill(false).colspan(3).row();
        table.add(downsample);
        table.add(clear);
        table.add(options).row();
        table.add(sample).fill(false).size(100);
        table.add(new ScrollPane(letters)).colspan(2).row();
        table.add(train);
        table.add(name);
        table.add(addLetter).row();
        table.add(recognize);
        table.add(delete).colspan(2).row();
        table.add(logPane = new ScrollPane(log)).height(log.getStyle().font.getLineHeight() * 3).colspan(3);

        stage.addActor(table);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (scrollLog) {
            logPane.setScrollPercentY(1);
            scrollLog = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void resume() {
        canvasTextureRegion.setTexture(new Texture(canvasPixmap));
        sampleTextureRegion.setTexture(new Texture(samplePixmap));
    }

    @Override
    public void dispose() {
        try {
            Assets.manager.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        stage.dispose();
        canvasPixmap.dispose();
        canvasTextureRegion.getTexture().dispose();
        samplePixmap.dispose();
        sampleTextureRegion.getTexture().dispose();
    }

    /**
     * Downsamples {@link #canvasPixmap} and saves the result to
     * {@link #samplePixmap} and {@link #sampleTextureRegion}. Also sets
     * {@link #sampleDirty} to {@code false}.
     */
    private void downSample() {

        // Recreate sampleTexture if necessary
        if (Options.getDownsampleWidth() != samplePixmap.getWidth() || Options.getDownsampleHeight() != samplePixmap.getHeight()) {
            samplePixmap = new Pixmap(Options.getDownsampleWidth(), Options.getDownsampleHeight(), Format.RGBA8888);
            sampleTextureRegion.setTexture(new Texture(samplePixmap));
            sample = new Image(new TextureRegionDrawable(sampleTextureRegion));
        }

        Pixmap sampled = DownSample.downSample(canvasPixmap);
        assert sampled.getWidth() == samplePixmap.getWidth() && sampled.getHeight() == samplePixmap.getHeight();
        samplePixmap.drawPixmap(sampled, 0, 0);
        sampleTextureRegion.getTexture().draw(sampled, 0, 0);
        sampled.dispose();
        sampleDirty = false;
        System.out.println("sampled.getWidth() " + sampled.getWidth());
        System.out.println("samplePixmap.getWidth() " + samplePixmap.getWidth());

        System.out.println("sampled.getHeight() " + sampled.getHeight());
        System.out.println("samplePixmap.getHeight() " + samplePixmap.getHeight());
    }

    /**
     * if {@link #logPane} should scroll all the way down
     */
    private boolean scrollLog;

    public void showMessage(String msg) {
        if (msg != null && !msg.isEmpty()) {
            Gdx.app.log("Message", msg);
            log.setText(log.getText() + "\n" + msg);
            scrollLog = true;
        }
    }

    //Logic Methods
    private void startTrain(NormalizationType normalizationType, LearningMethod learningMethod, float learnRate) {
        if (letters.getItems().size == 0) {
            showMessage("No letters to train!");
            return;
        }

        int inputCount = Options.getDownsampleWidth() * Options.getDownsampleHeight();
        int outputCount = letters.getItems().size;

        double[][] train = new double[letters.getItems().size][inputCount];
        //Each Line is a letter representation in pixel
        //Each Column is a pixel
        Pixmap downSampled;
        for (int i = 0; i < letters.getItems().size; i++) {
            downSampled = letters.getItems().get(i).getDownSample();
            for (int x = 0, index = 0; x < downSampled.getWidth(); x++) {
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
        if (logic.getSom() == null) {
            showMessage("Training is required before recognizing");
            return "Impossible to proceed";
        }

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
                return "Recognized : " + logic.getMap().get(i).getLetter();
            }
        }

        return "Couldn't recognize! *Unknown*";
    }

    // getters and setters
    public Array<Letter> getLetters() {
        return letters.getItems();
    }

}
