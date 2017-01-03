package exemples;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

import arbres.*;

public class LectureArbres {


	public static void main(String[] args) throws IOException, ClassNotFoundException {

		// Pour créer les arbres
		LectureArbres lecture = new LectureArbres(true);
		lecture.lireTousLesDocument();

		//Pour ne pas les créer quand on les récupère de la sérialisation
		//LectureArbres lecture = new LectureArbres(false);
		
		//Pour sérialiser les arbres
		int compteur = 0;
		for (Arbre arbre : lecture.listeArbres) {
			FileOutputStream fos = new FileOutputStream("src/main/arbres/arbre"+ compteur +".serial");

			ObjectOutputStream oos= new ObjectOutputStream(fos);

			oos.writeObject(arbre); 
			oos.flush();

			oos.close();
			fos.close();
			compteur++;
		}

		//Pour lire les arbres
		/*int compteur = 0;
		for (int i=0;i<26;i++) {
			FileInputStream fis = new FileInputStream("src/main/arbres/arbre"+ compteur +".serial");

			ObjectInputStream ois= new ObjectInputStream(fis);

			Arbre arbre = (Arbre) ois.readObject();
			lecture.listeArbres.add(arbre);
			ois.close();
			fis.close();
			compteur++;
		}*/

		System.out.println("Lecture des arbres");
		for (Arbre arbre : lecture.listeArbres) {
			System.out.println(arbre.getInitNoeud().getValeur() + " : " + arbre.getInitNoeud().getListeNoeuds().size());
		}

		System.out.println("Liste des documents");
		LinkedList<String> docs = lecture.chercherMot("nouveau");
		for (String doc : docs) {
			System.out.println(doc);
		}
	}



	ArrayList<Arbre> listeArbres = new ArrayList<Arbre>();

	String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

	public LectureArbres(boolean charge) {
		if (charge == true){
			for (String c : alphabet){
				Arbre arbre = new Arbre(c);
				this.listeArbres.add(arbre);
			}
		} 
	}

	public String lireFichier(File file)  {

		String texte = "";

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);

			int content;
			while ((content = fis.read()) != -1) {
				texte = texte + (char) content;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return texte;
	}


	public HashMap<String, Integer> wraperTT(String texte) {
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
		texte = texte.replaceAll(",", " , ");
		texte = texte.replaceAll("'", " ' ");
		texte = texte.replaceAll("\"", " \" ");
		texte = texte.replaceAll("[(]", " ( ");
		texte = texte.replaceAll("[)]", " ) ");
		texte = texte.replaceAll("\\[|\\]" , "");
		texte = texte.replaceAll("[-+*/]", " ");
		texte = texte.replaceAll("[0123456789]", " ");
		texte = texte.replaceAll("[?!#$€%&'`;:/@...]", " ");
		//System.out.println(texte);
		String[] phrases = texte.split("[.]");
		System.setProperty("treetagger.home", "/home/francoise/Documents/ENSAI/WebDataMining");
		TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
		try {
			tt.setModel("/home/francoise/Documents/ENSAI/WebDataMining/lib/french-utf8.par:iso8859-1");

			tt.setHandler((token, pos, lemma) -> {
				if ( !pos.startsWith("NUM") && !pos.startsWith("KON") &&  !pos.startsWith("PRP") &&  !pos.startsWith("DET") &&  !pos.startsWith("PUN") &&  !pos.startsWith("PRO")){
					//System.out.println(token + " " + pos +" "+lemma);
					lemma = removeAccent(lemma.toLowerCase());
					if (hashmap.containsKey(lemma)) {
						hashmap.put(lemma,hashmap.get(lemma)+1);
					} else {
						hashmap.put(lemma, 1);
					}
				}
			}
					);

			for (String phrase : phrases) {
				String[] mots = phrase.split(" ");
				tt.process(asList(mots));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TreeTaggerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			tt.destroy();
		}
		return hashmap;
	}

	public void lireTousLesDocument() {
		File resources = new File("src/main/resources");
		String[] listeFichiers = resources.list();
		Arrays.sort(listeFichiers);

		for (String fichier : listeFichiers) {

			File file = new File("src/main/resources/" + fichier);
			String texte = this.lireFichier(file);
			//System.out.println(texte);
			HashMap<String,Integer> hashmap = this.wraperTT(texte);

			remplirArbre(hashmap, fichier);
		}
	}

	public void remplirArbre(HashMap<String,Integer> hashmap, String document) {
		System.out.println(document);
		for (String mot : hashmap.keySet()){
			int compteur = 0;
			int index = -1;
			for (String lettre : alphabet) {
				if (mot.startsWith(lettre)) {
					index = compteur;
				}
				compteur += 1;
			}
			System.out.println(mot);
			for (int i = 0; i<hashmap.get(mot);i++) {
				listeArbres.get(index).getInitNoeud().insererMots(mot, document);
			}
		}
	}

	public String removeAccent(String source) {
		return Normalizer.normalize(source, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
	}


	public LinkedList<String> chercherMot(String mot) {
		LinkedList<String> documents = null;
		int compteur = 0;
		int index = -1;
		for (String lettre : alphabet) {
			if (mot.startsWith(lettre)) {
				index = compteur;
			}
			compteur += 1;
		}
		Noeud noeud = listeArbres.get(index).getInitNoeud();

		for (int i = 1;i<mot.length();i++) {
			String lettre = mot.substring(i,i+1);
			boolean trouve = false;
			for (Noeud temp : noeud.getListeNoeuds()) {
				if (temp.getValeur().equals(lettre)) {
					noeud = temp;
					trouve = true;
				} 
			}
			if (trouve = false) {
				System.out.println("Le mot n'existe pas !"); 
				break;
			} 
		}
		if (noeud.getFeuille() == null) {
			System.out.println("Le mot n'existe pas !"); 
		} else {
			documents = noeud.getFeuille().getDocuments();
		}
		return documents;
	}
}
