package br.lopes.biometrySom.images;

import br.lopes.biometrySom.Options;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class DownSample {

	private static int WHITE = Color.rgba8888(Color.WHITE);
	private static int BLACK = Color.rgba8888(Color.BLACK);

	/** @param sample the Pixmap to downsample
	 *  @return a new Pixmap of the size defined in {@link Options} containing the given pixmap, sampled down */
	public static Pixmap downSample(Pixmap sample) {
		if(Options.isStripWhitespace())
			sample = findBorders(sample);
		return downSample(Options.getDownsampleWidth(), Options.getDownsampleHeight(), sample);
	}

	/** Reduce size of drawn letter */
	private static Pixmap downSample(int width, int height, Pixmap pm) {
		Pixmap ds = new Pixmap(width, height, pm.getFormat());
		Pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
		ds.drawPixmap(pm, 0, 0, pm.getWidth(), pm.getHeight(), 0, 0, width, height);
		return ds;
	}

	/** Eliminate whitespace around the drawn letter */
	private static Pixmap findBorders(Pixmap px) {
		int y0 = -1;
		int yf = 0;
		int x0 = -1;
		int xf = 0;

		for(int x = 0; x < px.getWidth(); x++) {
			for(int y = 0; y < px.getHeight(); y++) {
				int pixel = px.getPixel(x, y);

				if(pixel == BLACK) {
					Gdx.app.debug("DownSample#findBorders(Pixmap)", "Pixel == " + BLACK + " at " + x + ";" + y);
					//Y_AXIS
					if(y0 == -1 || y < y0) {
						y0 = y;
					}

					if(yf == 0 || y > yf) {
						yf = y;
					}

					//X_AXIS
					if(x0 == -1 || x < x0) {
						x0 = x;
					}

					if(xf == 0 || x > xf) {
						xf = x;
					}
				}
			}
		}

		int dx = xf - x0;
		int dy = yf - y0;

		Pixmap rbPixmap = new Pixmap(dx, dy, px.getFormat());
		rbPixmap.setColor(Color.WHITE);
		rbPixmap.fill();
		rbPixmap.setColor(Color.BLACK);

		for(int x = x0, scaledX = 0; x < xf; x++, scaledX++) {
			for(int y = y0, scaledY = 0; y < yf; y++, scaledY++) {
				if(px.getPixel(x, y) == BLACK) {
					rbPixmap.drawPixel(scaledX, scaledY, BLACK);
				}
			}
		}

		Gdx.app.debug("DownSample#findBorders(Pixmap)", "Original Pixmap Size : " + px.getWidth() + ";" + px.getHeight());
		Gdx.app.debug("DownSample#findBorders(Pixmap)", "New Pixmap Size : " + rbPixmap.getWidth() + ";" + rbPixmap.getHeight());

		return rbPixmap;
	}

}
