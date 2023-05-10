package ClassPackage;

import java.awt.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.io.*;
import java.io.File;

import javax.imageio.ImageIO;

public class PictureMix {
	String[] Filename = { "", "" };
	int len = 2, type = 2;
	BufferedImage[] images = new BufferedImage[len];
	JPanel ImgPanel = new JPanel();
	JScrollPane MixImg;
	JFrame frame;

	public static void solve() {
		new PictureMix();
	}

	public PictureMix() {
		SetTable();
	}

	public void SetTable() {
		frame = new JFrame("JAVA_EndTerm_project");
		frame.setLayout(new BorderLayout(10,10));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(350, 300);

		//top side
		JButton Filter = new JButton("Filter"), Mix = new JButton("Mix"), Cut = new JButton("Cut");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        panel.add(Filter);
        panel.add(Mix);
        panel.add(Cut);

        frame.add(panel, BorderLayout.PAGE_START);

        Filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageFilter.solve();
                frame.setVisible(false);
                frame.dispose();
            }
        });

		//left side
		JButton choose_first_picture = new JButton("Choose First Picture");
		JButton choose_second_picture = new JButton("Choose Second Picture");
		JButton picture_mix = new JButton("Picture Mix");
		JCheckBox Portrait = new JCheckBox("Portrait", true);
		JCheckBox Landscape = new JCheckBox("Landscape", false);

		JPanel TypePanel = new JPanel();
		TypePanel.setLayout(new GridLayout(1, 2));
		TypePanel.add(Portrait);
		TypePanel.add(Landscape);

		ButtonGroup TypeButton = new ButtonGroup(); // create ButtonGroup
		TypeButton.add(Portrait); // add  to group
		TypeButton.add(Landscape); // add  to group

		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setLayout(new GridLayout(4, 1));
		ButtonPanel.add(choose_first_picture);
		ButtonPanel.add(choose_second_picture);
		ButtonPanel.add(picture_mix);
		ButtonPanel.add(TypePanel);

		frame.add(ButtonPanel, BorderLayout.LINE_START);

		choose_first_picture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();// 宣告filechooser
				File CurrentDirectory;
				try {
					CurrentDirectory = new File((new File(".").getCanonicalPath()));
					fileChooser.setCurrentDirectory(CurrentDirectory);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
				int returnValue = fileChooser.showOpenDialog(null);// 叫出filechooser
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();// 指派給File
					Filename[0] = selectedFile.getName();
				}
				addpicture();
			}
		});

		choose_second_picture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();// 宣告filechooser
				File CurrentDirectory;
				try {
					CurrentDirectory = new File((new File(".").getCanonicalPath()));
					fileChooser.setCurrentDirectory(CurrentDirectory);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
				int returnValue = fileChooser.showOpenDialog(null);// 叫出filechooser
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();// 指派給File
					Filename[1] = selectedFile.getName();
				}
				addpicture();
			}
		});

		picture_mix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (Portrait.isSelected()) {
                    type = 2;
                }
				else if (Landscape.isSelected()) {
                    type = 1;
                }
				try {
					mergeImage(images, type, "PictureMix.png");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		//display
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	public void addpicture() {
		frame.remove(ImgPanel);
		ImgPanel.removeAll();
		JScrollPane scrollPane1, scrollPane2;
		ImgPanel.setLayout(new GridLayout(1, 3));
		if (Filename[0] != "") {
			try {
				images[0] = ImageIO.read(new File(Filename[0]));// 讀取檔案
			} catch (Exception e) {
				images[0] = null;
			}
			scrollPane1 = new JScrollPane(new JLabel(new ImageIcon(images[0])));// 把Image放進label裡
			ImgPanel.add(scrollPane1);
		}

		if (Filename[1] != "") {
			try {
				images[1] = ImageIO.read(new File(Filename[1]));// 讀取檔案
			} catch (Exception e) {
				images[1] = null;
			}
			scrollPane2 = new JScrollPane(new JLabel(new ImageIcon(images[1])));// 把Image放進label裡
			ImgPanel.add(scrollPane2);
		}
		if (Filename[0] != "" || Filename[1] != "") {
			frame.add(ImgPanel, BorderLayout.CENTER);
			frame.pack();
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
			int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
			frame.setLocation(x, y);
		}
	}

	public void mergeImage(BufferedImage[] images, int type, String targetFile) throws IOException {
		int len = images.length;
		int[][] ImageArrays = new int[len][];

		for (int i = 0; i < len; i++) {
			int width = images[i].getWidth();
			int height = images[i].getHeight();
			ImageArrays[i] = new int[width * height];
			ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
		}
		int newHeight = 0;
		int newWidth = 0;
		for (int i = 0; i < images.length; i++) {
			// 横向
			if (type == 1) {
				newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();
				newWidth += images[i].getWidth();
			} else if (type == 2) {// 纵向
				newWidth = newWidth > images[i].getWidth() ? newWidth : images[i].getWidth();
				newHeight += images[i].getHeight();
			}
		}
		if (type == 1 && newWidth < 1) {
			return;
		}
		if (type == 2 && newHeight < 1) {
			return;
		}

		try {
			BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			int height_i = 0;
			int width_i = 0;
			for (int i = 0; i < images.length; i++) {
				if (type == 1) {
					ImageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0,
							images[i].getWidth());
					width_i += images[i].getWidth();
				} else if (type == 2) {
					ImageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
					height_i += images[i].getHeight();
				}
			}
			// 输出想要的图片
			ImageIO.write(ImageNew, targetFile.split("\\.")[1], new File(targetFile));

			try {
				frame.remove(MixImg);
			} catch (Exception e) {
				e.printStackTrace();
			}

			MixImg = new JScrollPane(new JLabel(new ImageIcon(ImageIO.read(new File("PictureMix.png")))));
			frame.add(MixImg, BorderLayout.LINE_END);
			frame.pack();
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
			int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
			frame.setLocation(x, y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}