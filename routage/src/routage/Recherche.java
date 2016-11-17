package routage;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Recherche {
	
	/**
	 * 
	 * @param nbRedemarrage nombre de redemarrages de la recherche
	 * @param nbIteRechLocale nombre d'iterations sur la recherche locale
	 * @param choixVoisin meilleur parmis tous : 0, premier ameliorant : -1, meilleur parmis echantillon : taille de l'echantillon
	 * @return
	 */
	public Solution calculerSolution(int nbRedemarrage, int nbIteRechLocale, int choixVoisin){
		return null;
	}
	
	public static void main(String[] args){
		Path fichier = FileSystems.getDefault().getPath("ref","routage_a1.txt");
		try {
			Solution solution = new Lecture(fichier).lire();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
