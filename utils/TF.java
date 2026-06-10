package utils;

import aed3.ElementoLista;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class TF {
    ArrayList<String> stopWords = new ArrayList<>(List.of("de", "a", "o", "que", "e", "do", "da", "em", "um", "para", "é", "com", "não", "uma", "os", "no", "se", "na", "por", "mais", "as", "dos", "como", "mas", "foi", "ao", "ele", "das", "tem", "à", "seu", "sua", "ou", "ser", "quando", "muito", "há", "nos", "já", "está", "eu", "também", "só", "pelo", "pela", "até", "isso", "ela", "entre", "era", "depois", "sem", "mesmo", "aos", "ter", "seus", "quem", "nas", "me", "esse", "eles", "estão", "você", "tinha", "foram", "essa", "num", "nem", "suas", "meu", "às", "minha", "têm", "numa", "pelos", "elas", "havia", "seja", "qual", "será", "nós", "tenho", "lhe"));
    public TF() {

    }

    //calcula a frequencia das palavras
    public HashMap<String, ElementoLista> calcularTf(String palavras, int id) {
        palavras = removerAcentoStringEDeixarMinusculo(palavras);
        //quebra a string e remove as stop words
        ArrayList<String> termos = quebrarString(palavras);
        termos = removerStopWords(termos);

        HashMap<String, ElementoLista> frequencias = new HashMap<>();
        for(int i = 0; i < termos.size(); i++) {
            if(!frequencias.containsKey(termos.get(i))) {
                frequencias.put(termos.get(i), new ElementoLista(id, 1/(float)(termos.size())));
            } else {
                ElementoLista atual = frequencias.get(termos.get(i));
                atual.setFrequencia(atual.getFrequencia()+ 1/(float)(termos.size()));
                frequencias.put(termos.get(i), atual);
            }
        }
        
        return frequencias;
    }

    public static String removerAcentoStringEDeixarMinusculo(String frase){
        frase = Normalizer.normalize(frase, Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        frase = frase.toLowerCase();
        return pattern.matcher(frase).replaceAll("");
    }


    //quebra a string passando as palavras para um arraylist
    public ArrayList<String> quebrarString(String palavras) {
        ArrayList<String> termos = new ArrayList<>();
        //Separa as palavras por espaços
        Collections.addAll(termos, palavras.split(" "));
        return termos;
    }


    //remove as stop words do arraylist
    public ArrayList<String> removerStopWords(ArrayList<String> termos) {
        for(int i = 0; termos.size() > i; i++) {
            if(stopWords.contains(termos.get(i))) {
                termos.remove(i);
                i--;
            }
        }
        return termos;
    }
}
