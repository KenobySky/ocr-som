package br.lopes.biometrySom;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.heatonresearch.book.jeffheatoncode.som.NormalizeInput.NormalizationType;
import com.heatonresearch.book.jeffheatoncode.som.TrainSelfOrganizingMap.LearningMethod;

public class Options {

	public static class OptionsGUI extends Table {

		public OptionsGUI() {
			super(Assets.manager.get(Assets.uiskin, Skin.class));
			Skin skin = Assets.manager.get(Assets.uiskin, Skin.class);

			TextField downsampleWidth = new TextField(String.valueOf(Options.downsampleWidth), skin), downsampleHeight = new TextField(String.valueOf(Options.downsampleHeight), skin);
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
					Options.setDownsampleWidth(Math.max(1, Integer.parseInt(zerofy(textField.getText()))));
				}
			});
			downsampleHeight.setTextFieldListener(new TextFieldListener() {
				@Override
				public void keyTyped(TextField textField, char c) {
					Options.setDownsampleHeight(Math.max(1, Integer.parseInt(zerofy(textField.getText()))));
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
					Options.setLearnRate(Math.max(Float.MIN_VALUE, Float.parseFloat(zerofy(textField.getText()))));
				}
			});

			final CheckBox stripWhitespace = new CheckBox("", skin);
			stripWhitespace.setChecked(Options.isStripWhitespace());
			stripWhitespace.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					Options.setStripWhitespace(stripWhitespace.isChecked());
				}
			});

			defaults().fill();
			add("Downsample size:");
			add(downsampleWidth).width(downsampleWidth.getHeight());
			add("x");
			add(downsampleHeight).width(downsampleHeight.getHeight()).row();
			add("Normalization type:");
			add(normalizationType).colspan(3).row();
			add("Learning method:");
			add(learningMethod).colspan(3).row();
			add("Learn rate:");
			add(learnRate).colspan(3).row();
			add("Strip whitespace:");
			add(stripWhitespace).colspan(3);
		}

		private String zerofy(String num) {
			return num == null || num.isEmpty() ? "0" : num;
		}

	}

	private static int downsampleWidth = 10, downsampleHeight = 10;
	private static NormalizationType normalizeInput = NormalizationType.MULTIPLICATIVE;
	private static LearningMethod trainLearningMethod = LearningMethod.ADDITIVE;
	private static float learnRate = .2f;
	private static int maxErrorCount = 200;
	private static boolean running;
	private static boolean stripWhitespace = true;

	public static boolean isStripWhitespace() {
		return stripWhitespace;
	}

	public static void setStripWhitespace(boolean stripWhitespace) {
		Options.stripWhitespace = stripWhitespace;
	}

	public static int getDownsampleWidth() {
		return downsampleWidth;
	}

	public static void setDownsampleWidth(int downsample_width) {
		if(downsample_width <= 0) {
			throw new IllegalArgumentException("DownSample_Width cannot be <= 0 !");
		} else {
			Options.downsampleWidth = downsample_width;
		}
	}

	public static int getDownsampleHeight() {
		return downsampleHeight;
	}

	public static void setDownsampleHeight(int downsample_height) {
		if(downsample_height <= 0) {
			throw new IllegalArgumentException("DownSample_Height cannot be <= 0 !");
		} else {
			Options.downsampleHeight = downsample_height;
		}
	}

	public static boolean isThreadRunning() {
		return running;
	}

	public static void setThreadRunning(boolean isRunning) {
		running = isRunning;
	}

	public static NormalizationType getNormalizeInput() {
		return normalizeInput;
	}

	public static void setNormalizeInput(NormalizationType normalizeInput) {
		Options.normalizeInput = normalizeInput;
	}

	public static LearningMethod getTrainLearningMethod() {
		return trainLearningMethod;
	}

	public static void setTrainLearningMethod(LearningMethod trainLearningMethod) {
		Options.trainLearningMethod = trainLearningMethod;
	}

	public static float getLearnRate() {
		return learnRate;
	}

	public static void setLearnRate(float learnRate) {
		if(learnRate <= 0) {
			throw new IllegalArgumentException("Learn Rate cannot be <= 0 !");
		} else {
			Options.learnRate = learnRate;
		}
	}

	public static int getMaxErrorCount() {
		return maxErrorCount;
	}

	public static void setMaxErrorCount(int maxErrorCount) {
		if(maxErrorCount < 0) {
			throw new IllegalArgumentException("Learn Rate cannot be <= 0 !");
		} else {
			Options.maxErrorCount = maxErrorCount;
		}
	}

	public static boolean isRunning() {
		return running;
	}

	public static void setRunning(boolean running) {
		Options.running = running;
	}

}
