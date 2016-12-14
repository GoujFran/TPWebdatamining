package arbres;

import java.io.Serializable;
import java.util.LinkedList;

public class Noeud extends Element implements Serializable{

	private String valeur;
	private LinkedList<Noeud> listeNoeuds = new LinkedList<Noeud>();
	private Feuille feuille;
	
		
	public Noeud(String valeur, Noeud noeudParent) {
		super(noeudParent);
		this.valeur = valeur;
	}
	
	public String getValeur() {
		return valeur;
	}
	
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
	
	public LinkedList<Noeud> getListeNoeuds() {
		return listeNoeuds;
	}
	
	public Feuille getFeuille() {
		return feuille;
	}

	public void insererMots(String mot, String document) {
		if (mot.length()== 1) {
			if (feuille == null) {
				feuille = new Feuille(this,document);
			} else {
				feuille.getDocuments().add(document);
			}
		} else {
			String lettreSuivante = mot.substring(1,2);
			mot = mot.substring(1);
			boolean existe = false;
			for (Noeud noeud : listeNoeuds) {
				if (noeud.valeur.equals(lettreSuivante)) {
					noeud.insererMots(mot, document);
					existe = true;
				}
			}
			if (!existe) {
				Noeud noeud = new Noeud(lettreSuivante, this);
				listeNoeuds.add(noeud);
				listeNoeuds.getLast().insererMots(mot, document);
			}
		}
	}
	
}
