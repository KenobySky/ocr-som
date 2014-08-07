package br.lopes.biometrySom.images;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Disposable;

public class Letter implements Disposable {

    private String name;
    private Pixmap sample;
    private Pixmap downSample;

    public Letter(Pixmap sample, Pixmap downSample, String name) {
        this.sample = new Pixmap(sample.getWidth(), sample.getHeight(), sample.getFormat());
        this.sample.drawPixmap(sample, 0, 0);
        this.name = name;
        this.downSample = downSample;
    }

    /**
     * disposes the {@link #sample}
     */
    @Override
    public void dispose() {
        sample.dispose();
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public Pixmap getDownSample() {
        return downSample;
    }

    public Pixmap getSample() {
        return sample;
    }

    @Override
    public String toString() {
        return name;
    }

}
