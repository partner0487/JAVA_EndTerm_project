package ClassPackage;
import java.awt.*;

public class castingFilter implements Filter {
    public int[][] getNewImageData(int[][] imageData, int w, int h){
        int[][] newImageData = new int[w][h];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                int rgb = imageData[i][j];
                Color color = new Color(rgb);
                int R = 128 * color.getRed() / (color.getGreen() + color.getBlue() + 1);
                int G = 128 * color.getGreen() / (color.getRed() + color.getBlue() + 1);
                int B = 128 * color.getBlue() / (color.getRed() + color.getGreen() + 1);
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                R = R < 0 ? 0 : R; G = G < 0 ? 0 : G; B = B < 0 ? 0 : B;
                Color newColor = new Color(R, G, B);
                newImageData[i][j] = newColor.getRGB();
            }
        }
        return newImageData;
    }
}
