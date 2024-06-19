package ClassPackage;
import java.awt.*;

public class oldFilter implements Filter {
    public int[][] getNewImageData(int[][] imageData, int w, int h){
        int[][] newImageData = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int rgb = imageData[i][j];
                Color color = new Color((int)rgb);
                int R = (int)(color.getRed() * 0.393 + color.getGreen() * 0.769 + color.getBlue() * 0.189);
                int G = (int)(color.getRed() * 0.349 + color.getGreen() * 0.686 + color.getBlue() * 0.168);
                int B = (int)(color.getRed() * 0.272 + color.getGreen() * 0.534 + color.getBlue() * 0.131);
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                Color newColor = new Color(R, G, B);
                newImageData[i][j] = newColor.getRGB();
            }
        }
        return newImageData;
    }
}
