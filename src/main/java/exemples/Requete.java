package exemples;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;

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

	public HashMap<String, Integer> wraperTT(String texte) {
		HashMap<String,Integer> hashmap = new HashMap<String,Integer>();
		texte = texte.replaceAll(",", " ,");
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
	public String removeAccent(String source) {
		return Normalizer.normalize(source, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
	}



}

