package utils;

import aed3.ElementoLista;

public class nomeFrequencia {
    private String palavra;
    private ElementoLista elemento;

    public nomeFrequencia(String p, ElementoLista e) {
        this.palavra = p;
        this.elemento = e;
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public ElementoLista getElemento() {
        return elemento;
    }

    public void setFrequencia(float frequencia) {
        this.elemento.setFrequencia(frequencia);
    }

    public float getFrequencia() {
        return this.elemento.getFrequencia();
    }

    public String toString() {
        return palavra+":" + getFrequencia();
    }
}
