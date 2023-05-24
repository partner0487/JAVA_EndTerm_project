package ClassPackage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.ChangedCharSetException;

import ClassPackage.PictureMix;

import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.sql.rowset.FilteredRowSet;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class imageFilter extends JFrame {
    JButton JumpToFilter, JumpToMix, JumpToCut;
    JButton gray, old, mosaic, blackWhite, desaturate,
            opposite, comics, casting, relief, selectPicture, savePicture, SinCity;
    JPanel Filter, File, JumpTo;

    String fileName;
    JPanel ImgPanel = new JPanel(new GridLayout(1, 1));
    BufferedImage[] img = new BufferedImage[1];

    int[][] imageData;
    int[][] newImageData;
    int w, h;
    int rgb, R, G, B;

    public void init() {
        this.setTitle("image filter");
        this.setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JumpToFilter = new JButton("Filter"); 
        JumpToMix = new JButton("Mix"); 
        JumpToCut = new JButton("Cut");
        JumpTo = new JPanel(new GridLayout(1, 3));
        JumpTo.add(JumpToFilter); JumpTo.add(JumpToMix); JumpTo.add(JumpToCut);
        add(JumpTo, BorderLayout.PAGE_START);

        JumpToFilter.setEnabled(false);
        JumpToMix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                PictureMix.solve();
                setVisible(false);
                dispose();
            }
        });
        JumpToCut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

            }
        });

        {
            gray = new JButton("灰色"); old = new JButton("懷舊");
            mosaic = new JButton("馬賽克"); blackWhite = new JButton("黑白");
            desaturate = new JButton("去色"); opposite = new JButton("反色"); 
            comics = new JButton("連環畫");
            casting = new JButton("鎔鑄"); relief = new JButton("浮雕");
            SinCity = new JButton("SinCity");
            JPanel Filter = new JPanel(new GridLayout(10, 1));
            Filter.add(gray); Filter.add(old); Filter.add(mosaic);
            Filter.add(blackWhite); Filter.add(desaturate);
            Filter.add(opposite); Filter.add(comics);
            Filter.add(casting); Filter.add(relief); Filter.add(SinCity);
            add(Filter, BorderLayout.LINE_START);

            gray.setEnabled(false); old.setEnabled(false);
            mosaic.setEnabled(false); blackWhite.setEnabled(false);
            desaturate.setEnabled(false);
            opposite.setEnabled(false); comics.setEnabled(false);
            casting.setEnabled(false);
            relief.setEnabled(false); SinCity.setEnabled(false);

            File = new JPanel();

            selectPicture = new JButton("select a picture");
            File.add(selectPicture);
            
            savePicture = new JButton("save");
            File.add(savePicture);

            add(File, BorderLayout.EAST);
        }

        selectPicture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gray.setEnabled(true); old.setEnabled(true);
                mosaic.setEnabled(true); blackWhite.setEnabled(true);
                desaturate.setEnabled(true);
                opposite.setEnabled(true); comics.setEnabled(true);
                casting.setEnabled(true);
                relief.setEnabled(true); SinCity.setEnabled(true);

                JFileChooser fileChooser = new JFileChooser();
                File CurrentDirectory;
				try {
					CurrentDirectory = new File((new File(".").getCanonicalPath()));
					fileChooser.setCurrentDirectory(CurrentDirectory);
				} 
				catch (Exception ex) {
					ex.printStackTrace();
				}
                int ret = fileChooser.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileName = selectedFile.getName();
                }
                addPicture();
                imageData = getImageData(fileName);
            }
        });
        savePicture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                for(int i = 0; i < w; i++){
                    for(int j = 0; j < h; j++){
                        newImage.setRGB(i, j, newImageData[i][j]);
                    }
                }
                try{
                    File output = new File("Filer.jpg");
                    ImageIO.write(newImage, "jpg", output);
                }
                catch(IOException ex){
                    System.out.println("error");
                }
            }
        });

        gray.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                gray(getGraphics());
                displayFilterImage();
            }
        });
        old.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                old(getGraphics());
                displayFilterImage();
            }
        });
        mosaic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                mosaic(getGraphics());
                displayFilterImage();
            }
        });
        blackWhite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                blackWhite(getGraphics());
                displayFilterImage();
            }
        });
        desaturate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                desaturate(getGraphics());
                displayFilterImage();
            }
        });
        opposite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                opposite(getGraphics());
                displayFilterImage();
            }
        });
        comics.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                comics(getGraphics());
                displayFilterImage();
            }
        });
        casting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                casting(getGraphics());
                displayFilterImage();
            }
        });
        relief.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                relief(getGraphics());
                displayFilterImage();
            }
        });
        SinCity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                SinCity(getGraphics());
                displayFilterImage();
            }
        });
    }
    public void displayFilterImage(){
        try{
            ImgPanel.remove(1);
        }
        catch(ArrayIndexOutOfBoundsException ex){
            System.out.printf("還沒有圖片\n");
        }
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                newImage.setRGB(i, j, newImageData[i][j]);
            }
        }
        ImageIcon displayImage = new ImageIcon(newImage);
        JScrollPane displayPane = new JScrollPane(new JLabel(displayImage));
        ImgPanel.add(displayPane, 1);
        add(ImgPanel);
        setSize(1000, 1000);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public void addPicture() {
        remove(ImgPanel);
        ImgPanel.removeAll();
        JScrollPane scrollPane1;

        try{
            img[0] = ImageIO.read(new File(fileName));
        }
        catch(Exception e){
            img[0] = null;
        }
        ImageIcon displayedImage = new ImageIcon(img[0]);
        Image image = displayedImage.getImage();
        Image resizeImage;
        if(displayedImage.getIconWidth() >= displayedImage.getIconHeight()){
            resizeImage = image.getScaledInstance(displayedImage.getIconWidth() / (displayedImage.getIconHeight() / 500)
                                                    , 500, java.awt.Image.SCALE_SMOOTH);
        }
        else{
            resizeImage = image.getScaledInstance(500
                                                , displayedImage.getIconHeight() / (displayedImage.getIconWidth() / 500), java.awt.Image.SCALE_SMOOTH);
        }
        displayedImage = new ImageIcon(resizeImage);

        scrollPane1 = new JScrollPane(new JLabel(displayedImage));// 把Image放進label裡
        ImgPanel.add(scrollPane1);
        add(ImgPanel);
        setSize(1000, 1000);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public static void solve() {
        imageFilter imageFilter = new imageFilter();
        imageFilter.init();
    }

    public int[][] getImageData(String path) {
        File now = new File(path);
        BufferedImage img = null;

        try {
            img = ImageIO.read(now);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ImageIcon displayedImage = new ImageIcon(img);
        Image image = displayedImage.getImage();
        Image resizeImage;
        if(displayedImage.getIconWidth() >= displayedImage.getIconHeight()){
            resizeImage = image.getScaledInstance(displayedImage.getIconWidth() / (displayedImage.getIconHeight() / 500)
                                                    , 500, java.awt.Image.SCALE_SMOOTH);
        }
        else{
            resizeImage = image.getScaledInstance(500
                                                , displayedImage.getIconHeight() / (displayedImage.getIconWidth() / 500), java.awt.Image.SCALE_SMOOTH);
        }
        displayedImage = new ImageIcon(resizeImage);

        Graphics2D draw = img.createGraphics();
        draw.drawImage(resizeImage, 0, 0, null);

        image = displayedImage.getImage();

        w = displayedImage.getIconWidth();
        h = displayedImage.getIconHeight();
        int[][] ret = new int[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++)
                ret[i][j] = img.getRGB(i, j);
        }
        return ret;
    }

    public void gray(Graphics g) {
        super.paint(g);
        newImageData = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = color.getRed();
                G = color.getGreen();
                B = color.getBlue();
                int gray = (R + G + B) / 3;
                Color newColor = new Color(gray, gray, gray);
                newImageData[i][j] = newColor.getRGB();
            }
        }
    }
    public void old(Graphics g) {
        super.paint(g);
        newImageData = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                rgb = imageData[i][j];
                Color color = new Color((int)rgb);
                R = (int)(color.getRed() * 0.393 + color.getGreen() * 0.769 + color.getBlue() * 0.189);
                G = (int)(color.getRed() * 0.349 + color.getGreen() * 0.686 + color.getBlue() * 0.168);
                B = (int)(color.getRed() * 0.272 + color.getGreen() * 0.534 + color.getBlue() * 0.131);
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                Color newColor = new Color(R, G, B);
                newImageData[i][j] = newColor.getRGB();
            }
        }
    }
    public void mosaic(Graphics g){
        super.paint(g);
        newImageData = new int[w][h];
        for(int i = 0; i < w; i += 10){
            for(int j = 0; j < h; j += 10){
                rgb = imageData[i][j];
                Color newColor = new Color(rgb);
                for(int k = i; k < i + 10; k++)
                    for(int u = j; u < j + 10; u++)
                        newImageData[k][u] = newColor.getRGB();
            }
        }
    }
    public void blackWhite(Graphics g){
        super.paint(g);
        newImageData = new int[w][h];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = color.getRed();
                G = color.getGreen();
                B = color.getBlue();
                int blackWhite = (R + G + B) / 3;
                blackWhite = (blackWhite >= 100 ? 255 : 0);
                Color newColor = new Color(blackWhite, blackWhite, blackWhite);
                newImageData[i][j] = newColor.getRGB();
            }
        }
    }
    public void desaturate(Graphics g){
        super.paint(g);
        newImageData = new int[w][h];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = color.getRed();
                G = color.getGreen();
                B = color.getBlue();
                
                int Max = Math.max(R, Math.max(G, B));
                int Min = Math.min(R, Math.min(G, B));
                int desaturate = (Max + Min) / 2;
                Color newColor = new Color(desaturate, desaturate, desaturate);
                newImageData[i][j] = newColor.getRGB();
            }
        }
    }
    public void opposite(Graphics g){
        super.paint(g);
        newImageData = new int[w][h];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = 255 - color.getRed();
                G = 255 - color.getGreen();
                B = 255 - color.getBlue();
                Color newColor = new Color(R, G, B);
                newImageData[i][j] = newColor.getRGB();
            }
        }
    }
    public void comics(Graphics g){
        super.paint(g);
        newImageData = new int[w][h];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = Math.abs(2 * color.getGreen() - color.getBlue() + color.getRed()) * color.getRed() / 256;
                G = Math.abs(2 * color.getBlue() - color.getGreen() + color.getRed()) * color.getRed() / 256;
                B = Math.abs(2 * color.getBlue() - color.getGreen() + color.getRed()) * color.getGreen() / 256;
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                Color newColor = new Color(R, G, B);
                newImageData[i][j] = newColor.getRGB();
            }
        }
    }
    public void casting(Graphics g){
        super.paint(g);
        newImageData = new int[w][h];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = 128 * color.getRed() / (color.getGreen() + color.getBlue() + 1);
                G = 128 * color.getGreen() / (color.getRed() + color.getBlue() + 1);
                B = 128 * color.getBlue() / (color.getRed() + color.getGreen() + 1);
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                R = R < 0 ? 0 : R; G = G < 0 ? 0 : G; B = B < 0 ? 0 : B;
                Color newColor = new Color(R, G, B);
                newImageData[i][j] = newColor.getRGB();
            }
        }
    }
    public void relief(Graphics g){
        super.paint(g);
        newImageData = new int[w][h];
        for(int i = 0; i < w - 1; i++){
            for(int j = 0; j < h - 1; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                int r0 = color.getRed(), g0 = color.getGreen(), b0 = color.getBlue();

                rgb = imageData[i + 1][j + 1];
                color = new Color(rgb);
                int r1 = color.getRed(), g1 = color.getGreen(), b1 = color.getBlue();

                R = r0 - r1 + 128;
                G = g0 - g1 + 128;
                B = b0 - b1 + 128;
                R = R > 255 ? 255 : R; G = G > 255 ? 255 : G; B = B > 255 ? 255 : B;
                R = R < 0 ? 0 : R; G = G < 0 ? 0 : G; B = B < 0 ? 0 : B;
                Color newColor = new Color(R, G, B);
                newImageData[i][j] = newColor.getRGB();
            }
        }
    }
    public void SinCity(Graphics g){
        super.paint(g);
        Color minColor = new Color(255, 0, 0, 255);
        double threshold = 200;
        newImageData = new int[w][h];

        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                rgb = imageData[i][j];
                Color color = new Color(rgb);
                R = color.getRed(); G = color.getGreen(); B = color.getBlue();
                int gray = (int)(0.299 * R + 0.587 * G + 0.114 * B);
                double dis = getDis(R, G, B, minColor);
                Color newColor;
                if(dis < threshold){
                    double k = dis / threshold;
                    R = (int)(R * k + gray * (1.0f - k));
                    G = (int)(G * k + gray * (1.0f - k));
                    B = (int)(B * k + gray * (1.0f - k));
                    newColor = new Color(R, G, B);
                }
                else newColor = new Color(gray, gray, gray);
                newImageData[i][j] = newColor.getRGB();
            }
        }
    }
    public double getDis(int R, int G, int B, Color minColor){
        int [] SQRT_LUT = new  int [] { 
            0 , 1 , 4 , 9 , 16 , 25 , 36 , 49 , 64 , 81 , 100 , 121 , 144 , 169 , 196 , 225 , 256 , 289 , 324 ,
            361 , 400 , 441 , 484 , 529 , 576 , 625 , 676 , 729 , 784 , 841 , 900 , 961 , 1024 , 1089 , 1156 ,
            1225 , 1296 , 1369 , 1444 , 1521 , 1600 , 1681 , 1764 , 1849 , 1936 , 2025 , 2116 , 2209 , 2304 ,
            2401 , 2500 , 2601 , 2704 , 2809 , 2916 , 3025 , 3136 , 3249 , 3364 , 3481 , 3600 , 3721 , 3844 ,
            3969 , 4096 , 4225 , 4356 , 4489 , 4624 , 4761 , 4900 , 5041 , 5184 , 5329 , 5476 , 5625 , 5776 ,
            5929 , 6084 , 6241 , 6400 , 6561 , 6724 , 6889 , 7056 , 7225 , 7396 , 7569 , 7744 , 7921 , 8100 ,
            8281 , 8464 , 8649 , 8836 , 9025 , 9216 , 9409 , 9604 , 9801 , 10000 , 10201 , 10404 , 10609 ,
            10816 , 11025 , 11236 , 11449 , 11664 , 11881 , 12100 , 12321 , 12544 , 12769 , 12996 , 13225 ,
            13456 , 13689 , 13924 , 14161 , 14400 , 14641 , 14884 , 15129 , 15376 , 15625 , 15876 , 16129 ,
            16384 , 16641 , 16900 , 17161 , 17424 , 17689 , 17956 , 18225 , 18496 , 18769 , 19044 , 19321 ,
            19600 , 19881 , 20164 , 20449 , 20736 , 21025 , 21316 , 21609 , 21904 , 22201 , 22500 , 22801 ,
            23104 , 23409 , 23716 , 24025 , 24336 , 24649 , 24964 , 25281 , 25600 , 25921 , 26244 , 26569 ,
            26896 , 27225 , 27556 , 27889 , 28224 , 28561 , 28900 , 29241 , 29584 , 29929 , 30276 , 30625 ,
            30976 , 31329 , 31684 , 32041 , 32400 , 32761 , 33124 , 33489 , 33856 , 34225 , 34596 , 34969 ,
            35344 , 35721 , 36100 , 36481 , 36864 , 37249 , 37636 , 38025 , 38416 , 38809 , 39204 , 39601 ,
            40000 , 40401 , 40804 , 41209 , 41616 , 42025 , 42436 , 42849 , 43264 , 43681 , 44100 , 44521 ,
            44944 , 45369 , 45796 , 46225 , 46656 , 47089 , 47524 , 47961 , 48400 , 48841 , 49284 , 49729 ,
            50176 , 50625 , 51076 , 51529 , 51984 , 52441 , 52900 , 53361 , 53824 , 54289 , 54756 , 55225 ,
            55696 , 56169 , 56644 , 57121 , 57600 , 58081 , 58564 , 59049 , 59536 , 60025 , 60516 , 61009 ,
            61504 , 62001 , 62500 , 63001 , 63504 , 64009 , 64516 , 65025 ,
        };
        R = R - minColor.getRed();
        G = G - minColor.getGreen();
        B = B - minColor.getBlue();
		int distance = SQRT_LUT[Math.abs(R)] +
				SQRT_LUT[Math.abs(G)] +
				SQRT_LUT[Math.abs(B)];
		return Math.sqrt(distance);		
    }
}
