package ClassPackage;
import java.awt.*;

public class comicsFilter implements Filter{
    public int[][] getNewImageData(int[][] imageData, int w, int h){
        int[][] newImageData = new int[w][h];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                int rgb = imageData[i][j];
                Color color = new Color(rgb);
                int R = Math.abs(2 * color.getGreen() - color.getBlue() + color.getRed()) * color.getRed() / 256;
                int G = Math.abs(2 * color.getBlue() - color.getGreen() + color.getRed()) * color.getRed() / 256;
                int B = Math.abs(2 * color.getBlue() - color.getGreen() + color.getRed()) * color.getGreen() / 256;
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                Color newColor = new Color(R, G, B);
                newImageData[i][j] = newColor.getRGB();
            }
        }
        return newImageData;
    }
}
