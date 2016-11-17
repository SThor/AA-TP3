package routage;

import java.awt.geom.Point2D;

public class Client {
	private Point2D.Double coordonnees;
	private float tMin;
	private float tMax;
	private float quantite;
	private float tDechargement;
	private String id;	
	
	public Client(String id, Point2D.Double coordonnees, float tMin, float tMax, float quantite, float tDechargement) {
		super();
		this.id = id;
		this.coordonnees = coordonnees;
		this.tMin = tMin;
		this.tMax = tMax;
		this.quantite = quantite;
		this.tDechargement = tDechargement;
	}
	public Point2D.Double getCoordonnees() {
		return coordonnees;
	}
	public float gettMin() {
		return tMin;
	}
	public float gettMax() {
		return tMax;
	}
	public float getQuantite() {
		return quantite;
	}
	public float gettDechargement() {
		return tDechargement;
	}
	
	
}
