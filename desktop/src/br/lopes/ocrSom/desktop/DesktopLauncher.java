package br.lopes.ocrSom.desktop;

import br.lopes.ocrSom.OcrSom;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "OCR with Kohonen Map";
        cfg.width = 405;
        cfg.height = 670;
        new LwjglApplication(new OcrSom(), cfg);
    }

}
