package ClassPackage;
import javax.swing.*;
import ClassPackage.imageFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PhotoMagician {
    public static void main(String[] args){
        JFrame application = new JFrame("Photo Magician");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(1000, 1000);
        application.setVisible(true);

        JButton Filter = new JButton("Filter"),
                Mix = new JButton("Mix"), Cut = new JButton("Cut");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        panel.add(Filter);
        panel.add(Mix);
        panel.add(Cut);

        application.add(panel, BorderLayout.CENTER);

        Filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                imageFilter.solve();
                application.setVisible(false);
                application.dispose();
            }
        });
        Mix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                PictureMix.solve();
                application.setVisible(false);
                application.dispose();
            }
        });
    }
}
