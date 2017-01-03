package exemples;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Calcul {

    
    public double calculTF(HashMap<String, HashMap<String,Integer>> hashmap,String mot, String document){
        double result = 0;
        if(hashmap.containsKey(document)){
            HashMap<String,Integer> temp = hashmap.get(document);
                    if (temp.containsKey(mot)){
                        result = temp.get(mot);
                    }
        }
        
        return result;
    }

    public double calculTF( LectureArbres lectureArbre,String mot, String document){
        double result = 0;
        LinkedList<String> listDoc = lectureArbre.chercherMot(mot);
        for (String doc : listDoc){
        	if (doc.equals(document)){
        		result++ ;
        	}
        }
        return result;
    }

    public double calculIDF(String mot, LectureArbres lectureArbre){
        double result = 0;
        Set<String> listDoc = new HashSet<String>(lectureArbre.chercherMot(mot));
        result = Math.log10(listDoc.size()/result);
        return result;
    }


    public double calculIDF(HashMap<String, HashMap<String,Integer>> hashmap,String mot){
        double result = 0;
        Set<String> listDoc = hashmap.keySet();
        for (String fichier :listDoc){
            if (hashmap.get(fichier).containsKey(mot)){
                result = result + 1;
            }
        }
        result = Math.log10(listDoc.size()/result);
        return result;
    }
    
    public double calculTFIDF(HashMap<String, HashMap<String,Integer>> hashmap,String mot, String document){
        double result ;
        result = calculTF(hashmap,mot,document)*calculIDF(hashmap, mot);
        return result;
    }
    
}
