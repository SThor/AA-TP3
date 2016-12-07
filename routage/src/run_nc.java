import routage.Client;
import routage.Recherche;
import routage.Solution;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by silmathoron on 07/12/2016.
 */
public class run_nc {
	public static void main(String[] args) {
		Solution solution = new Solution();
		String nomFichier = args[1];
		File fichier = new File(nomFichier);
		try {
			List<Client> monde = solution.lireMonde(fichier);
		} catch (IOException e) {
			e.printStackTrace();
		}
		solution = Recherche.calculerSolution(solution,2,1000,20);
		solution.imprimerFichierDot();
	}
}
