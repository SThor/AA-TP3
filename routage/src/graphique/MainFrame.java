package graphique;

import routage.Client;
import routage.Solution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by silmathoron on 17/11/2016.
 */
public class MainFrame {
	private JPanel panel1;
	private JButton chargerUnMondeButton;
	private JButton calculerUneSolutionButton;
	private JPanel panelmonde;
	private JLabel coutSolutionLabel;
	private JFileChooser choixFichier;
	private static JFrame frame;
	private File fichier;
	private Solution solution = new Solution();
	private List<Client> monde;
	private JMenuBar menuBar;
	private JMenuItem menuItemVoisinAleatoire;
	private JMenuItem menuItemVoisinAjouteCamion;
	private JMenuItem menuItemVoisinBougeClient;


	public MainFrame() {
		choixFichier = new JFileChooser(new File("."));
		setupMenuBar();
	}

	private void updateSolution() {
		((JPanelMonde) panelmonde).setSolution(solution);
		if (solution.isSet()) {
			coutSolutionLabel.setText("Temps total de parcours : " + solution.tempsTotalDeParcours());
		} else {
			coutSolutionLabel.setText("Pas de solution générée.");
		}

		System.out.println("solution.valide() = " + solution.valide());

		frame.repaint();
	}

	private void setupMenuBar() {
		menuBar = new JMenuBar();

		JMenu menuVoisin = setupMenuVoisin();
		menuBar.add(menuVoisin);

		JMenu menuSolution = setupMenuSolution();
		menuBar.add(menuSolution);


		frame.setJMenuBar(menuBar);
	}

	private JMenu setupMenuSolution() {
		JMenu menuSolution = new JMenu("Solution");

		JMenuItem menuItemChargerMonde = new JMenuItem("Charger un monde");
		JMenuItem menuItemCalculerUneSolution = new JMenuItem("Calculer une solution");
		JMenuItem menuItemCreerSolutionUnCamion = new JMenuItem("Créer une solution à un camion");
		JMenuItem menuItemCreerSolutionPleinDeCamions = new JMenuItem("Créer une solution à un camion par client");
		JMenuItem menuItemCreerSolutionAleatoire = new JMenuItem("Créer une solution aléatoire");

		menuItemChargerMonde.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int valeurRetour = choixFichier.showOpenDialog(frame);
				if (valeurRetour == JFileChooser.APPROVE_OPTION) {
					fichier = choixFichier.getSelectedFile();
					chargerMonde();
					updateSolution();
				}
			}
		});

		menuItemCalculerUneSolution.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				solution = solution.calculerSolution(3,10,10);
				updateSolution();
			}
		});

		menuItemCreerSolutionUnCamion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				solution.genSolutionUnCamion();
				updateSolution();
			}
		});

		menuItemCreerSolutionPleinDeCamions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				solution.genSolutionPleinDeCamions();
				updateSolution();
			}
		});

		menuItemCreerSolutionAleatoire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				solution.random();
				updateSolution();
			}
		});

		menuSolution.add(menuItemChargerMonde);
		menuSolution.add(menuItemCalculerUneSolution);
		menuSolution.add(menuItemCreerSolutionUnCamion);
		menuSolution.add(menuItemCreerSolutionPleinDeCamions);
		menuSolution.add(menuItemCreerSolutionAleatoire);

		return menuSolution;
	}

	private JMenu setupMenuVoisin() {
		JMenu menuVoisin = new JMenu("Voisin");

		menuItemVoisinAleatoire = new JMenuItem("Générer un voisin aléatoire");
		menuItemVoisinAjouteCamion = new JMenuItem("Générer un voisin en ajoutant un camion");
		menuItemVoisinBougeClient = new JMenuItem("Générer un voisin en déplaçant un client");
		menuItemVoisinAleatoire.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, Event.ALT_MASK));
		menuItemVoisinAjouteCamion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, Event.ALT_MASK));
		menuItemVoisinBougeClient.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, Event.ALT_MASK));
		menuItemVoisinAleatoire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				solution = solution.genRandomVoisin();
				updateSolution();
			}
		});
		menuItemVoisinAjouteCamion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				solution = solution.genRandomVoisinAjouteCamion();
				updateSolution();
			}
		});
		menuItemVoisinBougeClient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				solution = solution.genRandomVoisinBougeClient();
				updateSolution();
			}
		});

		menuVoisin.add(menuItemVoisinAleatoire);
		menuVoisin.add(menuItemVoisinAjouteCamion);
		menuVoisin.add(menuItemVoisinBougeClient);

		return menuVoisin;
	}

	private void chargerMonde() {
		try {
			monde = solution.lireMonde(fichier);
			((JPanelMonde) panelmonde).setMonde(monde);
			updateSolution();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		frame.setPreferredSize(new Dimension(700,600));
	}
}
