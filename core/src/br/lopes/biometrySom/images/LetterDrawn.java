package br.lopes.biometrySom.images;

import com.badlogic.gdx.graphics.Pixmap;

public class LetterDrawn {

    private String letterName = "?";

    private Pixmap sample;

    public LetterDrawn(Pixmap letterDrawn, String name) {
        this.letterName = name;
        this.sample = letterDrawn;
    }

    public String getLetterName() {
        return letterName;
    }

    public Pixmap getSample() {
        return sample;
    }

    public void dispose() {
        sample.dispose();
    }

}
