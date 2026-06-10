package entidades.Curso;

import java.util.HashMap;
import utils.TF;
import aed3.ListaInvertida;
import aed3.ElementoLista;

public class ArquivoTF {
    ListaInvertida indiceNome;

    public ArquivoTF() throws Exception {
        indiceNome = new ListaInvertida(500, "./dados/curso/nomeArquivoDicionario.db", "./dados/curso/nomeArquivoBlocos.db");
    }

    public boolean update(Curso c, int id, String nomeAntigo) throws Exception {
        System.out.println(nomeAntigo);
        TF tf = new TF();
        
        HashMap<String, ElementoLista> freqs = tf.calcularTf(nomeAntigo, id);
        String chaves[] = freqs.keySet().toArray(new String[0]);
        for(int i = 0; i < chaves.length; i++) {
            indiceNome.delete(chaves[i], id);
        }

        freqs = tf.calcularTf(c.getNome(), id);
        String chaves2[] = freqs.keySet().toArray(new String[0]);
        for(int i = 0; i < chaves2.length; i++) {
            indiceNome.create(chaves2[i], freqs.get(chaves2[i]));
        }
        return true;
    }
    public boolean create(Curso c, int id) throws Exception {
        TF tf = new TF();
        HashMap<String, ElementoLista> freqs = tf.calcularTf(c.getNome(), id);

        String chaves[] = freqs.keySet().toArray(new String[0]);
        for(int i = 0; i < chaves.length; i++) {
            indiceNome.create(chaves[i], freqs.get(chaves[i]));
        }
        return true;
    }
    public void print() throws Exception {
        indiceNome.print();
    }
    public void read(){
        try {
            ElementoLista[] ocorrencias= indiceNome.read("matematica");
            for(int i = 0; i < ocorrencias.length; i++) {
                System.out.println(ocorrencias[i].toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}