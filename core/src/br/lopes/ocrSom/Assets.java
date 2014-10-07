package br.lopes.ocrSom;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;



public abstract class Assets {

    public static final AnnotationAssetManager manager = new AnnotationAssetManager();

    @Asset(Skin.class)
    public static final String uiskin = "uiskin.json";

}
