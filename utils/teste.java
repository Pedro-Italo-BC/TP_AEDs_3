package utils;

import aed3.ElementoLista;
import java.util.*;

public class teste {
    public static void main(String[] args) {
        String palavras = "a casa é bonita e a casa é grande";
        TF tfClasse = new TF();
        HashMap<String, ElementoLista> resultadoTf = tfClasse.calcularTf(palavras, 1);
    }
}