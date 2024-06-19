package ClassPackage;
import java.awt.*;

public class desaturateFilter implements Filter{
    public int[][] getNewImageData(int[][] imageData, int w, int h){
        int[][] newImageData = new int[w][h];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                int rgb = imageData[i][j];
                Color color = new Color(rgb);
                int R = color.getRed();
                int G = color.getGreen();
                int B = color.getBlue();
                
                int Max = Math.max(R, Math.max(G, B));
                int Min = Math.min(R, Math.min(G, B));
                int desaturate = (Max + Min) / 2;
                Color newColor = new Color(desaturate, desaturate, desaturate);
                newImageData[i][j] = newColor.getRGB();
            }
        }
        return newImageData;
    }
}
