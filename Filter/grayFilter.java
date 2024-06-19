package ClassPackage;
import java.awt.*;

public class grayFilter implements Filter {
    public int[][] getNewImageData(int[][] imageData, int w, int h){
        int[][] newImageData = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int rgb = imageData[i][j];
                Color color = new Color(rgb);
                int R = color.getRed();
                int G = color.getGreen();
                int B = color.getBlue();
                int gray = (R + G + B) / 3;
                Color newColor = new Color(gray, gray, gray);
                newImageData[i][j] = newColor.getRGB();
            }
        }
        return newImageData;
    }
}
