package   utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import aed3.ElementoLista;
import aed3.ListaInvertida;

public class IDF {
    static ListaInvertida listaInvertida = new ListaInvertida(500, "./dados/curso/nomeArquivoDicionario.db", "./dados/curso/nomeArquivoBlocos.db");
    static ArrayList<String> stopWords = new ArrayList<>(List.of("de", "a", "o", "que", "e", "do", "da", "em", "um", "para", "é", "com", "não", "uma", "os", "no", "se", "na", "por", "mais", "as", "dos", "como", "mas", "foi", "ao", "ele", "das", "tem", "à", "seu", "sua", "ou", "ser", "quando", "muito", "há", "nos", "já", "está", "eu", "também", "só", "pelo", "pela", "até", "isso", "ela", "entre", "era", "depois", "sem", "mesmo", "aos", "ter", "seus", "quem", "nas", "me", "esse", "eles", "estão", "você", "tinha", "foram", "essa", "num", "nem", "suas", "meu", "às", "minha", "têm", "numa", "pelos", "elas", "havia", "seja", "qual", "será", "nós", "tenho", "lhe"));
    
    public static ElementoLista[] pesquisa(String entrada, int qtdDados){
        ArrayList<String> entradaSemEspaco = quebrarString(entrada);
        entradaSemEspaco = removerStopWords(entradaSemEspaco);

        HashMap<Integer, Float> idf = new HashMap<>();

        //percorre cada palavra da entrada
        for(int i = 0; i < entradaSemEspaco.size(); i++) {
            try {
                ElementoLista[] elementos = listaInvertida.read(entradaSemEspaco.get(i));
                //passa por cada entidade que possui a palavra
                for(int j = 0; j < elementos.length; j++) {
                    if(!idf.containsKey(elementos[j].getId())) { //caso nao exista o id
                        idf.put(elementos[j].getId(), elementos[j].getFrequencia()*Math.log10(qtdDados/(float)(elementos.length)));
                    } else { //caso exista o id
                        idf.put(elementos[j].getId(), idf.get(elementos[j].getId()) + elementos[j].getFrequencia()*Math.log10(qtdDados/(float)(elementos.length)));
                    }
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //organiza o vetor com os resultados
        ArrayList<Integer> ids = new ArrayList<>(idf.keySet());
        ElementoLista[] resultados = new ElementoLista[ids.size()];
        for(int i = 0; i < resultados.length; i++) {
            resultados[i] = new ElementoLista(ids.get(i), idf.get(ids.get(i)));
        }

        //ordena o vetor com os resultados
        quickSort(resultados);
        return resultados;
    }

    public static void quickSort(ElementoLista[] vetor) {
        quickSort(vetor, 0, vetor.length - 1);
    }

    public static void quickSort(ElementoLista[] vetor, int esq, int dir) {
        int i = esq,j = dir;
        float pivo = vetor[(esq+dir)/2].getFrequencia();
        while (i <= j) {
            while (vetor[i].getFrequencia() > pivo)
                i++;
            while (vetor[j].getFrequencia() < pivo)
                j--;
            if (i <= j){   
                ElementoLista temp = vetor[i];
                vetor[i] = vetor[j];
                vetor[j] = temp;   
                i++;   
                j--; 
            }
        }
        if (esq < j)
            quickSort(vetor, esq, j); 
        if (i < dir)
            quickSort(vetor, i, dir);
        return;
    }

    public static ArrayList<String> quebrarString(String palavras) {
        ArrayList<String> termos = new ArrayList<>();
        //Separa as palavras por espaços
        Collections.addAll(termos, palavras.split(" "));
        return termos;
    }
    public static ArrayList<String> removerStopWords(ArrayList<String> termos) {
        for(int i = 0; termos.size() > i; i++) {
            if(stopWords.contains(termos.get(i))) {
                termos.remove(i);
                i--;
            }
        }
        return termos;
    }
}