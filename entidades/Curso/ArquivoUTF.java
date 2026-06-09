package entidades.Curso;

import java.util.HashMap;
import utils.TF;
import aed3.ListaInvertida;
import aed3.ElementoLista;

public class ArquivoUTF {
    ListaInvertida indiceNome;

    public ArquivoUTF() throws Exception {
        indiceNome = new ListaInvertida(500, "./dados/curso/nomeArquivoDicionario.db", "./dados/curso/nomeArquivoBlocos.db");
    }
    public boolean inserir(Curso c, int id) {
        TF tf = new TF();
        HashMap<String, ElementoLista> freqs = tf.calcularTf(c.getNome(), id);
        String chaves[] = freqs.keySet().toArray(new String[0]);
        
        return true;
    }
}

