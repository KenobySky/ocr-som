package br.lopes.biometrySom.Images;

import com.badlogic.gdx.graphics.Pixmap;


public class LetterDrawn {

    private String letterName = "?";

    //Texture or Pixmap?
    private Pixmap sample;

    public static final int DOWNSAMPLE_WIDTH = 10;
    public static final int DOWNSAMPLE_HEIGHT = 10;

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
    
    

}
