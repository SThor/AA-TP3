/**
 * 
 */
package routage;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * @author Solen
 *
 * Classe de lecture du fichier et creation objets
 */
public class Lecture {
	private File fichier ;				// Fichier contenant le Solution
	
	/**
	 * Constructeur
	 * @param fichier contenant les informations
	 */
	public Lecture(File fichier) {
		this.fichier = fichier ;
	}
	
	/**
	 * Cree la liste de clients a livrer
	 * @return liste de clients a livrer
	 * @throws IOException 
	 */
	public List<Client> lire() throws IOException {
		String id ;
		float x ;
		float y ;
		float quantite ;
		float tmin ;
		float tmax ;
		float duree ;
		
		Scanner scanner = null ;
		Solution Solution = new Solution() ;
		
		// Creation scanner
		try {
			scanner = new Scanner(fichier) ;
		} catch (FileNotFoundException e) {
			System.out.println("Echec creation scanner");
			e.printStackTrace();
		}
		
		// Lecture fichier et creation clients
		List<Client> listeClients = new ArrayList<>() ;
		// Decalage de la chaîne "Cap"
		scanner.next() ;
		// Recuperation capacite
		Solution.setCapacite(scanner.nextInt());
		// Decalage de la seconde ligne 
		while (!scanner.hasNextInt()) {
			scanner.next() ;
		}
		// Infos du depot
		
		// Parcours de chaque ligne
		while (scanner.hasNext()) {
			// Sauvegarde des caracteristiques du client
			id = (String)scanner.next() ;
			x = Float.parseFloat(scanner.next()) ;
			y = Float.parseFloat(scanner.next()) ;
			quantite = Float.parseFloat(scanner.next()) ;
			tmin = Float.parseFloat(scanner.next()) ;
			tmax = Float.parseFloat(scanner.next()) ;
			duree = Float.parseFloat(scanner.next()) ;
			System.out.println(id+" "+x+" "+y+" "+quantite+" "+tmin+" "+tmax+" "+duree);
			listeClients.add(new Client(id,new Point2D.Double(x,y),tmin,tmax,quantite,duree)) ;
		}

		return listeClients ;
	}
}

