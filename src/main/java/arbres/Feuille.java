package arbres;

import java.io.Serializable;
import java.util.LinkedList;

public class Feuille extends Element implements Serializable{

	private LinkedList<String> documents = new LinkedList<String>();
	
	public Feuille(Noeud noeudParent, String document) {
		super(noeudParent);
		this.documents.add(document);
	}
	
	public LinkedList<String> getDocuments() {
		return documents;
	}
	
}
