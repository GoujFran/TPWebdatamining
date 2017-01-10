package exemples;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

public class Requete {

	private String phrase ;

	public Requete(String requete){
		this.phrase = requete;
	}

	public String getPhrase(){
		return this.phrase;
	}

	public LinkedList<String>  wraperTT() {
		LinkedList<String> liste = new LinkedList<String>();
		phrase = phrase.replaceAll(",", " ,");
		phrase = phrase.replaceAll("'", " ' ");
		phrase = phrase.replaceAll("\"", " \" ");
		phrase = phrase.replaceAll("[(]", " ( ");
		phrase = phrase.replaceAll("[)]", " ) ");
		phrase = phrase.replaceAll("\\[|\\]" , "");
		phrase = phrase.replaceAll("[-+*/]", " ");
		phrase = phrase.replaceAll("[0123456789]", " ");
		phrase = phrase.replaceAll("[?!#$€%&'`;:/@...]", " ");
		//System.out.println(texte);
		System.setProperty("treetagger.home", "/home/francoise/Documents/ENSAI/WebDataMining");
		TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
		try {
			tt.setModel("/home/francoise/Documents/ENSAI/WebDataMining/lib/french-utf8.par:iso8859-1");

			tt.setHandler((token, pos, lemma) -> {
				if ( !pos.startsWith("NUM") && !pos.startsWith("KON") &&  !pos.startsWith("PRP") &&  !pos.startsWith("DET") &&  !pos.startsWith("PUN") &&  !pos.startsWith("PRO")){
					//System.out.println(token + " " + pos +" "+lemma);
					lemma = removeAccent(lemma.toLowerCase());
					if (!liste.contains(lemma)) {
						liste.add(lemma);
					} 
				}
			}
					);

			String[] mots = phrase.split(" ");
			tt.process(asList(mots));


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
		return liste;
	}

	public String removeAccent(String source) {
		return Normalizer.normalize(source, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
	}


	public HashMap<String, Double> calculCos(LectureArbres lecture) {
		Calcul calcul = new Calcul();
		calcul.setMoyenne(lecture);
		HashMap<String,Double> resultat = new HashMap<String,Double> ();

		LinkedList<String> liste = this.wraperTT();
		HashSet<String> listeDocuments = new HashSet<String>();
		for (String mot : liste) {
			listeDocuments.addAll(lecture.chercherMot(mot));
		}

		for (String doc : listeDocuments) {
			double res = 0;
			for (String mot : liste) {
				res += calcul.calculOkapi(lecture, mot, doc);
			}
			resultat.put(doc,res);
		}
		HashMap<String,Double> resultatTrie = resultat.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
				.collect(Collectors.toMap(
						Map.Entry::getKey, 
						Map.Entry::getValue, 
						(e1, e2) -> e1, 
						LinkedHashMap::new
						));
		return resultatTrie;
	}

}

