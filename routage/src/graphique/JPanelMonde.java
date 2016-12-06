package graphique;

import routage.Camion;
import routage.Client;
import routage.Solution;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Created by silmathoron on 17/11/2016.
 */
public class JPanelMonde extends JPanel {
	private static final Color DEFAULT_COULEUR_DEPOT = Color.RED;
	private static final Color DEFAULT_COLOR = Color.DARK_GRAY;
	private static final Color DEFAULT_LINE_COLOR = Color.LIGHT_GRAY;
	private Graphics2D g2;
	private List<Client> monde;
	private Solution solution;

	private final static int DEFAULT_DIAMETRE = 5;
	private final static int DEFAULT_MARGIN = 10;
	private Point2D.Double min;
	private Point2D.Double max;
	private double echelleX;
	private double echelleY;

	public JPanelMonde() {
		super();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		if(monde != null){
			if(solution != null && solution.getMonde().equals(monde)){
				paintSolution();
			}
			paintMonde();
		}
	}

	private void paintMonde() {
		for (int i = 0; i < monde.size(); i++) {
			Client client = monde.get(i);
			Point2D.Double coordonnees = client.getCoordonnees();
			calculerEchelles();
			int x = (int) (coordonnees.x * echelleX) - DEFAULT_DIAMETRE / 2 + DEFAULT_MARGIN;
			int y = (int) (coordonnees.y * echelleY) - DEFAULT_DIAMETRE / 2 + DEFAULT_MARGIN;
			if(i==0){
				g2.setColor(DEFAULT_COULEUR_DEPOT);
			}else{
				g2.setColor(DEFAULT_COLOR);
			}
			g2.fillOval(x, y, DEFAULT_DIAMETRE, DEFAULT_DIAMETRE);
		}
	}
	
	private void paintSolution(){

		System.out.println("solution.tempsTotalDeParcours() = " + solution.tempsTotalDeParcours());
		for (Camion camion: solution.getCamions()) {
			g2.setColor(camion.getColor());
			List<Client> circuit = camion.getCircuit();
			paintRoute(monde.get(0),circuit.get(0));
			for (int i = 0; i < circuit.size()-1; i++) {
				paintRoute(circuit.get(i),circuit.get(i+1));
			}
			paintRoute(circuit.get(circuit.size()-1),monde.get(0));
		}
	}

	private void paintRoute(Client client1, Client client2) {
		int x1 = (int) (client1.getCoordonnees().getX()*echelleX + DEFAULT_MARGIN);
		int y1 = (int) (client1.getCoordonnees().getY()*echelleY + DEFAULT_MARGIN);
		int x2 = (int) (client2.getCoordonnees().getX()*echelleX + DEFAULT_MARGIN);
		int y2 = (int) (client2.getCoordonnees().getY()*echelleY + DEFAULT_MARGIN);
		g2.drawLine(x1,y1,x2,y2);
	}

	public void setMonde(List<Client> monde) {
		this.monde = monde;
		calculerMinMax();
	}

	private void calculerEchelles() {
		echelleX = (getWidth()-2*DEFAULT_MARGIN)/max.x ;
		echelleY = (getHeight()-2*DEFAULT_MARGIN)/max.y;
	}

	private void calculerMinMax() {
		min = new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);
		max = new Point2D.Double(Double.MIN_VALUE, Double.MIN_VALUE);
		if(monde != null){
			for (Client client: monde) {
				Point2D.Double coordonnees = client.getCoordonnees();
				min.x = Math.min(coordonnees.getX(),min.getX());
				min.y = Math.min(coordonnees.getY(),min.getY());
				max.x = Math.max(coordonnees.getX(),max.getX());
				max.y = Math.max(coordonnees.getY(),max.getY());
			}
		}
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}
}
