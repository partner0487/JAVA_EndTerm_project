package ClassPackage;
import java.awt.*;

public class oppositeFilter implements Filter {
    public int[][] getNewImageData(int[][] imageData, int w, int h){
        int[][] newImageData = new int[w][h];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                int rgb = imageData[i][j];
                Color color = new Color(rgb);
                int R = 255 - color.getRed();
                int G = 255 - color.getGreen();
                int B = 255 - color.getBlue();
                Color newColor = new Color(R, G, B);
                newImageData[i][j] = newColor.getRGB();
            }
        }
        return newImageData;
    }
}
