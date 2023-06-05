package ClassPackage;

import com.AnimatedGifEncoder;
import com.GifDecoder;


import javax.imageio.ImageIO;

import java.net.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.FlowLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.ChangedCharSetException;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import ClassPackage.PictureMix;
import ClassPackage.imageFilter;

import java.io.*;
import java.io.File;
import java.io.IOException;

public class GifProcessor extends JFrame{
    JButton JumpToFilter, JumpToMix, JumpToCut;
    JButton reverse , resolve , mix, selectGif, saveGif;
    JPanel Move, File, JumpTo;

    List<String> fileName = new ArrayList<>();
    JPanel ImgPanel = new JPanel(new GridLayout(1, 1));
    BufferedImage[] img = new BufferedImage[1];

    String imageData;
    int[][] newImageData;
    int w, h;

    String dirPath = "\\GifToImages\\";

    public static void solve() {
        GifProcessor GifProcessor = new GifProcessor();
        GifProcessor.init();
    }

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

        JumpToCut.setEnabled(false);
        JumpToMix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                PictureMix.solve();
                setVisible(false);
                dispose();
            }
        });
        JumpToFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageFilter.solve();
                setVisible(false);
                dispose();
            }
        });

        {
            reverse = new JButton("倒帶"); resolve = new JButton("逐幀轉圖片");
            mix = new JButton("圖片合成GIF");
            JPanel Move = new JPanel(new GridLayout(3, 1));
            Move.add(reverse); Move.add(resolve); Move.add(mix);
            add(Move, BorderLayout.LINE_START);

            reverse.setEnabled(false); 
            resolve.setEnabled(false);
            mix.setEnabled(false);

            File = new JPanel();

            selectGif = new JButton("select a gif");
            
            File.add(selectGif);

            add(File, BorderLayout.EAST);
        }

        selectGif.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                reverse.setEnabled(true); 
                resolve.setEnabled(true);
                mix.setEnabled(true);
                fileName.clear();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
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
                    File files[] = fileChooser.getSelectedFiles();
                    for(File file : files){
                        fileName.add(file.getName());
                    }
                }
                for (int i = 0; i < fileName.size(); i++) {
                    System.out.printf("%s\n", fileName.get(i));
                }
                addPicture();
            }
        });

        reverse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = fileName.get(0);
                String outputPath = "ReverseGif.gif";
                try{
                    reverseGif(imageData,outputPath);
                }
                catch(IOException ex){
                    System.out.println("error");
                }
            }
        });

        resolve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageData = fileName.get(0);
                try{
                    File directory = new File(dirPath);
            
                    for (File file: Objects.requireNonNull(directory.listFiles())) {
                        if (!file.isDirectory()) {
                            file.delete();
                        }
                    }
                    gifToImages(imageData,dirPath);
                }
                catch(IOException ex){
                    System.out.println("error");
                }
            }
        });
        
        mix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                List<BufferedImage> images = new ArrayList<>();
                String outputPath = "ImagesToGif.gif";
                try{
                    for (int i = 0; i < fileName.size(); i++) {
                        BufferedImage image = ImageIO.read(new File(dirPath + fileName.get(i)));
                        images.add(image);
                    }
                } 
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                try{
                    imagesToGif(images, outputPath);
                }
                catch(IOException ex){
                    System.out.println("error");
                }
            }
        });
    }

    public void addPicture() {
        remove(ImgPanel);
        ImgPanel.removeAll();
        JScrollPane scrollPane1;
        System.out.printf("%s\n",fileName.get(0));
        Icon icon = null;
        try{
            icon = new ImageIcon(getClass().getClassLoader().getResource(fileName.get(fileName.size()-1)));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try{
            icon = new ImageIcon(getClass().getClassLoader().getResource(dirPath + fileName.get(fileName.size()-1)));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        scrollPane1 = new JScrollPane(new JLabel(icon));// 把Image放進label裡
        ImgPanel.add(scrollPane1);
        add(ImgPanel, BorderLayout.CENTER);
        setSize(1000, 1000);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void reverseGif(String imagePath,String outputPath) throws IOException {
        GifDecoder decoder = new GifDecoder();
        int status = decoder.read(imagePath);
        if (status != GifDecoder.STATUS_OK) {
            throw new IOException("read image " + imagePath + " error!");
        }
        // 拆分一帧一帧的压缩之后合成
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outputPath);
        encoder.setRepeat(decoder.getLoopCount());
        for (int i = decoder.getFrameCount() -1; i >= 0; i--) {
            encoder.setDelay(decoder.getDelay(i));// 设置播放延迟时间
            BufferedImage bufferedImage = decoder.getFrame(i);// 获取每帧BufferedImage流
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            BufferedImage zoomImage = new BufferedImage(width, height, bufferedImage.getType());
            Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            Graphics gc = zoomImage.getGraphics();
            gc.setColor(Color.WHITE);
            gc.drawImage(image, 0, 0, null);
            encoder.addFrame(zoomImage);
        }
        encoder.finish();
        File outFile = new File(outputPath);
        BufferedImage image = ImageIO.read(outFile);
        ImageIO.write(image, outFile.getName(), outFile);
        
        remove(ImgPanel);
        ImgPanel.removeAll();
        JScrollPane scrollPane1,scrollPane2;
        System.out.printf("%s\n",fileName.get(0));
        Icon icon = null;
        try{
            icon = new ImageIcon(getClass().getClassLoader().getResource(fileName.get(fileName.size()-1)));
            scrollPane1 = new JScrollPane(new JLabel(icon));// 把Image放進label裡
            ImgPanel.add(scrollPane1);
            icon = new ImageIcon(getClass().getClassLoader().getResource(outFile.getName()));
            scrollPane2 = new JScrollPane(new JLabel(icon));// 把Image放進label裡
            ImgPanel.add(scrollPane2);
            add(ImgPanel, BorderLayout.CENTER);
            setSize(1000, 1000);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void imagesToGif(List<BufferedImage> imageList, String outputPath) throws IOException {
        int len = imageList.size();
        imageList.clear();
        for (int i = 0; i < len; i++) {
            File outFile = new File(dirPath + i + ".png");
            BufferedImage image = ImageIO.read(outFile);
            imageList.add(image);
        }
        // 拆分一帧一帧的压缩之后合成
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outputPath);
        encoder.setRepeat(0);
        for (BufferedImage bufferedImage : imageList) {
            encoder.setDelay(100);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            BufferedImage zoomImage = new BufferedImage(width, height, 3);
            Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            Graphics gc = zoomImage.getGraphics();
            gc.setColor(Color.WHITE);
            gc.drawImage(image, 0, 0, null);
            encoder.addFrame(zoomImage);
        }
        encoder.finish();
        File outFile = new File(outputPath);
        BufferedImage image = ImageIO.read(outFile);
        ImageIO.write(image, outFile.getName(), outFile);

        remove(ImgPanel);
        ImgPanel.removeAll();
        JScrollPane scrollPane1,scrollPane2;
        System.out.printf("%s\n",fileName.get(0));
        Icon icon = null;
        try{
            icon = new ImageIcon(getClass().getClassLoader().getResource(dirPath + fileName.get(fileName.size()-1)));
            scrollPane1 = new JScrollPane(new JLabel(icon));// 把Image放進label裡
            ImgPanel.add(scrollPane1);
            icon = new ImageIcon(getClass().getClassLoader().getResource(outFile.getName()));
            scrollPane2 = new JScrollPane(new JLabel(icon));// 把Image放進label裡
            ImgPanel.add(scrollPane2);
            add(ImgPanel, BorderLayout.CENTER);
            setSize(1000, 1000);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void gifToImages(String imagePath,String outputDirPath) throws IOException {
        GifDecoder decoder = new GifDecoder();
        int status = decoder.read(imagePath);
        if (status != GifDecoder.STATUS_OK) {
            throw new IOException("read image " + imagePath + " error!");
        }
        for (int i = 0; i < decoder.getFrameCount();i++) {
            BufferedImage bufferedImage = decoder.getFrame(i);// 获取每帧BufferedImage流
            File outFile = new File(outputDirPath + i + ".png");
            ImageIO.write(bufferedImage, "png", outFile);
        }

        remove(ImgPanel);
        ImgPanel.removeAll();
        JScrollPane scrollPane1,scrollPane2;
        System.out.printf("%s\n",fileName.get(0));
        Icon icon = null;
        try{
            icon = new ImageIcon(getClass().getClassLoader().getResource(fileName.get(fileName.size()-1)));
            scrollPane1 = new JScrollPane(new JLabel(icon));// 把Image放進label裡
            ImgPanel.add(scrollPane1);

            JPanel tmp = new JPanel();
            tmp.setLayout(new FlowLayout());
            for (int i = 0; i < decoder.getFrameCount(); i++){
                icon = new ImageIcon(getClass().getClassLoader().getResource(outputDirPath + i + ".png"));
                tmp.add(new JLabel(icon));
            }
            scrollPane2 = new JScrollPane(tmp);// 把Image放進label裡
            ImgPanel.add(scrollPane2);

            add(ImgPanel, BorderLayout.CENTER);
            setSize(1000, 1000);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
