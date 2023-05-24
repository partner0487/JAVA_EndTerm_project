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
            opposite, comics, casting, relief, selectPicture, savePicture;
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
        JumpToCut = new JButton("Gif");
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
                GifProcessor.solve();
                setVisible(false);
                dispose();
            }
        });

        {
            gray = new JButton("灰色"); old = new JButton("懷舊");
            mosaic = new JButton("馬賽克"); blackWhite = new JButton("黑白");
            desaturate = new JButton("去色"); opposite = new JButton("反色"); 
            comics = new JButton("連環畫");
            casting = new JButton("鎔鑄"); relief = new JButton("浮雕");
            JPanel Filter = new JPanel(new GridLayout(9, 1));
            Filter.add(gray); Filter.add(old); Filter.add(mosaic);
            Filter.add(blackWhite); Filter.add(desaturate);
            Filter.add(opposite); Filter.add(comics);
            Filter.add(casting); Filter.add(relief);
            add(Filter, BorderLayout.LINE_START);

            gray.setEnabled(false); old.setEnabled(false);
            mosaic.setEnabled(false); blackWhite.setEnabled(false);
            desaturate.setEnabled(false);
            opposite.setEnabled(false); comics.setEnabled(false);
            casting.setEnabled(false);
            relief.setEnabled(false);

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
                relief.setEnabled(true);

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
                imageData = getImageData(fileName);
                gray(getGraphics());
            }
        });
        old.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                old(getGraphics());
            }
        });
        mosaic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                mosaic(getGraphics());
            }
        });
        blackWhite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                blackWhite(getGraphics());
            }
        });
        desaturate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                desaturate(getGraphics());
            }
        });
        opposite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                opposite(getGraphics());
            }
        });
        comics.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                comics(getGraphics());
            }
        });
        casting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                casting(getGraphics());
            }
        });
        relief.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = getImageData(fileName);
                relief(getGraphics());
            }
        });
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
        Image resizeImage = image.getScaledInstance(displayedImage.getIconWidth() / (displayedImage.getIconHeight() / 500)
                                                    , 500, java.awt.Image.SCALE_SMOOTH);
        displayedImage = new ImageIcon(resizeImage);

        scrollPane1 = new JScrollPane(new JLabel(displayedImage));// 把Image放進label裡
        ImgPanel.add(scrollPane1);
        add(ImgPanel, BorderLayout.LINE_START);
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
        Image resizeImage = image.getScaledInstance(displayedImage.getIconWidth() / (displayedImage.getIconHeight() / 500),
                                                    500, java.awt.Image.SCALE_SMOOTH);
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
                g.setColor(newColor);
                g.fillRect(1000 + i, 325 + j, 1, 1);
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
                g.setColor(newColor);
                g.fillRect(1000 + i, 325 + j, 1, 1);
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
                g.setColor(newColor);
                g.fillRect(1000 + i, 325 + j, 10, 10);
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
                g.setColor(newColor);
                g.fillRect(1000 + i, 325 + j, 1, 1);
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
                g.setColor(newColor);
                g.fillRect(1000 + i, 325 + j, 1, 1);
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
                g.setColor(newColor);
                g.fillRect(1000 + i, 325 + j, 1, 1);
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
                g.setColor(newColor);
                g.fillRect(1000 + i, 325 + j, 1, 1);
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
                g.setColor(newColor);
                g.fillRect(1000 + i, 325 + j, 1, 1);
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
                g.setColor(newColor);
                g.fillRect(1000 + i, 325 + j, 1, 1);
                newImageData[i][j] = newColor.getRGB();
            }
        }
    }
}
