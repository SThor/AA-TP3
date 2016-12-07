package routage;

import java.util.Random;
import java.util.TreeSet;

/**
 * Created by silmathoron on 06/12/2016.
 */
public class Recherche {
	public static final int MEILLEUR_PARMIS_TOUS = 0;
	public static final int PREMIER_AMELIORANT = -1;
	private static final int NOUVEAU_CAMION = -1;
	Solution solutionCourante;

	/**
	 * @param nbRedemarrage   nombre de redemarrages de la recherche
	 * @param nbIteRechLocale nombre d'iterations sur la recherche locale
	 * @param choixVoisin     meilleur parmis tous : 0, premier ameliorant : -1, meilleur parmis echantillon : taille de l'echantillon
	 * @return
	 */
	public static Solution calculerSolution(Solution solution, int nbRedemarrage, int nbIteRechLocale, int choixVoisin) {
		TreeSet<Solution> solutions = new TreeSet<>();
		for (int i = 0; i < nbRedemarrage; i++) {
			solution.random();
			solutions.add(recherche(solution, nbIteRechLocale, choixVoisin));
		}
		return solutions.first();
	}

	public static Solution recherche(Solution solution, int nbIteRechLocale, int choixVoisin) {
		TreeSet<Solution> solutions = new TreeSet<>();
		for (int i = 0; i < nbIteRechLocale; i++) {
			solutions.add(iteration(choixVoisin, solution));
		}
		return solutions.first();
	}

	private static Solution iteration(int choixVoisin, Solution solution) {
		Solution resultat;
		switch (choixVoisin) {
			case MEILLEUR_PARMIS_TOUS:
				resultat = genAllVoisins(solution).first();
				break;
			case PREMIER_AMELIORANT:
				Solution voisin = genRandomVoisin(solution);
				while (solution.compareTo(voisin) >= 0) { //tant que le voisin généré n'améliore pas la solution actuelle
					voisin = genRandomVoisin(solution);
				}
				resultat = voisin;
				break;
			default:
				resultat = genRandomVoisins(solution, choixVoisin).first();
				break;
		}
		return resultat;
	}

	public static Solution genRandomVoisin(Solution solution) {
		Random rand = new Random();
		int n = rand.nextInt(2);
		switch (n) {
			case 0:
				return genRandomVoisinAjouteCamion(solution);
			case 1:
				return genRandomVoisinBougeClient(solution);
			default:
				return null;
		}
	}

	public static Solution genRandomVoisinAjouteCamion(Solution solution) {
		Random rand = new Random();
		System.err.println("ajoute Camion");
		return genVoisinAjouteCamion(solution, rand.nextInt(solution.getMonde().size() - 1) + 1);
	}

	public static Solution genRandomVoisinBougeClient(Solution solution) {
		Random rand = new Random();
		return genVoisinBougeClient(solution, rand.nextInt(solution.getMonde().size() - 1) + 1, rand.nextInt(solution.getCamions().size()));
	}

	private static Solution genVoisinAjouteCamion(Solution solution, int idClient) {
		Solution voisin = new Solution(solution);
		voisin.bougeClient(idClient, NOUVEAU_CAMION);
		return voisin;
	}

	private static Solution genVoisinBougeClient(Solution solution, int idClient, int idCamionDestination) {
		Solution voisin = new Solution(solution);
		voisin.bougeClient(idClient, idCamionDestination);
		return voisin;
	}

	public static TreeSet<Solution> genRandomVoisins(Solution solution, int nbVoisins) {
		TreeSet<Solution> voisins = new TreeSet<>();

		while (voisins.size() < nbVoisins) {
			Solution voisin = genRandomVoisin(solution);
			voisins.add(voisin);
		}

		return voisins;
	}

	public static TreeSet<Solution> genAllVoisins(Solution solution) {
		TreeSet<Solution> voisins = new TreeSet<>();

		for (int i = 1; i < solution.getMonde().size(); i++) {
			voisins.add(genVoisinAjouteCamion(solution, i));
		}
		for (int i = 0; i < solution.getCamions().size(); i++) {
			for (int j = 0; j < solution.getCamions().size(); j++) {
				if (i != j) {
					voisins.add(genVoisinBougeClient(solution, i, j));
				}
			}
		}

		return voisins;
	}
}
