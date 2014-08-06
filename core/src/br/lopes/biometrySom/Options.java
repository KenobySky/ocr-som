package br.lopes.biometrySom;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput.NormalizationType;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap.LearningMethod;

/**
 *
 * @author Andre Vinícius Lopes
 */
public abstract class Options {

    public static int DOWNSAMPLE_WIDTH = 10, DOWNSAMPLE_HEIGHT = 10;
    public static NormalizationType normalizeInput = NormalizationType.MULTIPLICATIVE;
    public static LearningMethod trainLearningMethod = LearningMethod.ADDITIVE;
    public static float learnRate = .2f;

	public static class OptionsGUI extends Table {

		public OptionsGUI() {
			super(Assets.manager.get(Assets.uiskin, Skin.class));
			Skin skin = Assets.manager.get(Assets.uiskin, Skin.class);

			TextField downsampleWidth = new TextField(String.valueOf(DOWNSAMPLE_WIDTH), skin), downsampleHeight = new TextField(String.valueOf(DOWNSAMPLE_HEIGHT), skin);
			TextFieldFilter numericFilter = new TextFieldFilter() {
				@Override
				public boolean acceptChar(TextField textField, char c) {
					return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
				}
			};
			downsampleWidth.setTextFieldFilter(numericFilter);
			downsampleHeight.setTextFieldFilter(numericFilter);
			downsampleWidth.setTextFieldListener(new TextFieldListener() {
				@Override
				public void keyTyped(TextField textField, char c) {
					DOWNSAMPLE_WIDTH = Integer.parseInt(zerofy(textField.getText()));
				}
			});
			downsampleHeight.setTextFieldListener(new TextFieldListener() {
				@Override
				public void keyTyped(TextField textField, char c) {
					DOWNSAMPLE_HEIGHT = Integer.parseInt(zerofy(textField.getText()));
				}
			});

			final SelectBox<NormalizationType> normalizationType = new SelectBox<>(skin);
			normalizationType.setItems(NormalizationType.values());
			normalizationType.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					normalizeInput = normalizationType.getSelected();
				}
			});

			final SelectBox<LearningMethod> learningMethod = new SelectBox<>(skin);
			learningMethod.setItems(LearningMethod.values());
			learningMethod.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					trainLearningMethod = learningMethod.getSelected();
				}
			});

			TextField learnRate = new TextField(String.valueOf(Options.learnRate), skin);
			learnRate.setTextFieldFilter(numericFilter);
			learnRate.setTextFieldListener(new TextFieldListener() {
				@Override
				public void keyTyped(TextField textField, char c) {
					Options.learnRate = Integer.parseInt(zerofy(textField.getText()));
				}
			});

			add("Downsample size:");
			add(downsampleWidth);
			add(downsampleHeight).row();
			add("Normalization type:");
			add(normalizationType).colspan(2).row();
			add("Learning method:");
			add(learningMethod).colspan(2).row();
			add("Learn rate:");
			add(learnRate).colspan(2);
		}

		private String zerofy(String num) {
			return num == null || num.isEmpty() ? "0" : num;
		}

	}

}
