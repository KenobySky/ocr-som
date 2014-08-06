package br.lopes.biometrySom.desktop;

import br.lopes.biometrySom.OcrSom;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "OCR With Self Organizing Map";
        cfg.width = 410;
        cfg.height = 630;

        new LwjglApplication(new OcrSom(), cfg);
    }

}
