package ClassPackage;
import java.awt.*;

public class mosaicFilter implements Filter {
    public int[][] getNewImageData(int[][] imageData, int w, int h){
        int[][] newImageData = new int[w][h];
        for(int i = 0; i < w; i += 10){
            for(int j = 0; j < h; j += 10){
                int rgb = imageData[i][j];
                Color newColor = new Color(rgb);
                for(int k = i; k < i + 10; k++)
                    for(int u = j; u < j + 10; u++)
                        newImageData[k][u] = newColor.getRGB();
            }
        }
        return newImageData;
    }
}
