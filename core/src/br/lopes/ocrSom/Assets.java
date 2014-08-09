package br.lopes.ocrSom;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.dermetfan.utils.libgdx.AnnotationAssetManager;
import net.dermetfan.utils.libgdx.AnnotationAssetManager.Asset;

public abstract class Assets {

	public static final AnnotationAssetManager manager = new AnnotationAssetManager();

	@Asset(type = Skin.class)
	public static final String uiskin = "uiskin.json";

}
