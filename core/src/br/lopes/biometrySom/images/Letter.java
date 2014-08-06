package br.lopes.biometrySom.images;

import com.badlogic.gdx.graphics.Pixmap;

public class Letter {

    private String name;

    private Pixmap sample;

    public Letter(Pixmap sample, String name) {
        this.sample = new Pixmap(sample.getWidth(), sample.getHeight(), sample.getFormat());
		this.sample.drawPixmap(sample, 0, 0);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Pixmap getSample() {
        return sample;
    }

	@Override
	public String toString() {
		return name;
	}

}
