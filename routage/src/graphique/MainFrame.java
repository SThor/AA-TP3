package graphique;

import routage.Lecture;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by silmathoron on 17/11/2016.
 */
public class MainFrame {
	private JPanel panel1;
	private JButton chargerUnMondeButton;
	private JButton calculerUneSolutionButton;
	private JPanel panelmonde;
	private JFileChooser choixFichier;
	private static JFrame frame;
	private File fichier;


	public MainFrame() {
		choixFichier = new JFileChooser(new File("."));
		chargerUnMondeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int valeurRetour = choixFichier.showOpenDialog(frame);
				if(valeurRetour == JFileChooser.APPROVE_OPTION){
					fichier = choixFichier.getSelectedFile();
					chargerMonde();
				}
			}
		});
		calculerUneSolutionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
	}

	private void chargerMonde() {
		try {
			((JPanelMonde)panelmonde).setMonde(new Lecture(fichier).lire());
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.repaint();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame = new JFrame("Monde");
		frame.setContentPane(new MainFrame().panel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void createUIComponents() {
		panelmonde = new JPanelMonde();
	}
}
