package br.lopes.biometrySom.images;

import br.lopes.biometrySom.Options;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class DownSample {

    public static Pixmap downSample(Pixmap sample) {
        return downSample(Options.DOWNSAMPLE_WIDTH, Options.DOWNSAMPLE_HEIGHT, findBorders(sample));
    }

    //Eliminate White Space around the drawn letter
    private static Pixmap findBorders(Pixmap px) {

        int y0 = -1;
        int yf = 0;
        int x0 = -1;
        int xf = 0;

        int WHITE = 0;
        int BLACK = 255;

        for (int x = 0; x < px.getWidth(); x++) {
            for (int y = 0; y < px.getHeight(); y++) {

                int pixel = px.getPixel(x, y);

                if (pixel == BLACK) {
                    //Y_AXIS
                    if (y0 == -1 || y < y0) {
                        y0 = y;
                    }

                    if (yf == 0 || y > yf) {
                        yf = y;
                    }

                    //X_AXIS
                    if (x0 == -1 || x < x0) {
                        x0 = x;
                    }

                    if (xf == 0 || x > xf) {
                        xf = x;
                    }

                }
            }
        }

        int dx = xf - x0;
        int dy = yf - y0;

        Pixmap rbPixmap = new Pixmap(dx, dy, px.getFormat());

        for (int x = x0, scaledX = 0; x < xf; x++, scaledX++) {
            for (int y = y0, scaledY = 0; y < yf; y++, scaledY++) {

                if (px.getPixel(x, y) == BLACK) {
                    rbPixmap.drawPixel(scaledX, scaledY, BLACK);
                }

            }
        }

        px.dispose();
        return rbPixmap;
    }

    //Reduce Size of drawn Letter
    private static Pixmap downSample(int width, int height, Pixmap letterDrawn) {
        Texture texture = new Texture(letterDrawn);
        Sprite sprite = new Sprite(texture);

        //DownSample
        sprite.setSize(width, height);
        texture = sprite.getTexture();

        TextureData textureData = texture.getTextureData();
        textureData.prepare();
        Pixmap consumePixmap = textureData.consumePixmap();

        texture.dispose();
        return consumePixmap;
    }

    //Reduce Size of drawn Letter
    private static Pixmap downSample(float width, float height, Pixmap letterDrawn) {
        Image img = new Image(new Texture(letterDrawn));
        img.setSize(width, height);

        throw new UnsupportedOperationException("????");
    }

}
