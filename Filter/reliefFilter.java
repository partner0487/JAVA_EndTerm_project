package ClassPackage;
import java.awt.*;

public class reliefFilter implements Filter {
    public int[][] getNewImageData(int[][] imageData, int w, int h){
        int[][] newImageData = new int[w][h];
        for(int i = 0; i < w - 1; i++){
            for(int j = 0; j < h - 1; j++){
                int rgb = imageData[i][j];
                Color color = new Color(rgb);
                int r0 = color.getRed(), g0 = color.getGreen(), b0 = color.getBlue();

                rgb = imageData[i + 1][j + 1];
                color = new Color(rgb);
                int r1 = color.getRed(), g1 = color.getGreen(), b1 = color.getBlue();

                int R = r0 - r1 + 128;
                int G = g0 - g1 + 128;
                int B = b0 - b1 + 128;
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                R = R < 0 ? 0 : R; G = G < 0 ? 0 : G; B = B < 0 ? 0 : B;
                Color newColor = new Color(R, G, B);
                newImageData[i][j] = newColor.getRGB();
            }
        }
        return newImageData;
    }
}
