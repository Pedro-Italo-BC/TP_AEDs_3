package aed3;
import java.io.*;
import java.lang.reflect.Constructor;

public class Arquivo<T extends InterfaceEntidade> {

    String nomeEntidade;
    Constructor<T> construtor;
    RandomAccessFile arquivo;
    HashExtensivel<ParIDEndereco> indiceDireto;
    public final int TAM_CABECALHO = 12;

    public Arquivo(String nomeEntidade, Constructor<T> construtor) throws Exception {

        File d = new File("dados");
        if (!d.exists())
            d.mkdir();
        d = new File("./dados/"+nomeEntidade);
        if (!d.exists())
            d.mkdir();

        this.nomeEntidade = nomeEntidade;
        this.construtor = construtor;
        arquivo = new RandomAccessFile("./dados/"+nomeEntidade+"/dados.db", "rw");
        if(arquivo.length() < TAM_CABECALHO) {
            arquivo.writeInt(0);   // último ID usado
            arquivo.writeLong(-1);   // cabeça da lista de espaços vazios
        }
        indiceDireto = new HashExtensivel<>(
            ParIDEndereco.class.getConstructor(), 
            4, 
            "./dados/"+nomeEntidade+"/indiceDireto.d.db", 
            "./dados/"+nomeEntidade+"/indiceDireto.c.db");
    }

    public int create(T entidade) throws Exception {
        arquivo.seek(0);
        int ultimoID = arquivo.readInt();
        int id = ultimoID + 1;
        arquivo.seek(0);
        arquivo.writeInt(id);

        entidade.setID(id);
        byte[] aux = entidade.toByteArray();
        short tamanho = (short)aux.length;
        long endereco = encontraEspacoVazio(tamanho);
        if(endereco==-1) {   // não há espaços vazios para reuso
            endereco = arquivo.length();
            arquivo.seek(arquivo.length());
            arquivo.writeByte(' ');    // lápide
            arquivo.writeShort(tamanho); // indicador de tamanho do vetor de bytes
        }
        else {              // reusa espaço vazio deixado por registro excluído
            arquivo.seek(endereco);
            arquivo.writeByte(' ');    // lápide
            arquivo.skipBytes(2);      // mantém o indicador de tamanho anterior
        }
        arquivo.write(aux);
        indiceDireto.create(new ParIDEndereco(id, endereco));
        return id;
    }

    public T read(int id) throws Exception {
        ParIDEndereco pie = indiceDireto.read(id);
        if(pie == null)
            return null;

        arquivo.seek(pie.getEndereco());
        byte lapide = arquivo.readByte();
        short tam = arquivo.readShort();
        if(lapide==' ') {
            byte[] aux = new byte[tam];
            arquivo.read(aux);
            T entidade = construtor.newInstance();
            entidade.fromByteArray(aux);
            if(entidade.getID() == id)
                return entidade;
        }
        return null;
    }

    public boolean delete(int id) throws Exception {
        ParIDEndereco pie = indiceDireto.read(id);
        if(pie == null)
            return false;

        long endereco = pie.getEndereco();
        arquivo.seek(endereco);
        byte lapide = arquivo.readByte();
        short tam = arquivo.readShort();
        if(lapide==' ') {
            byte[] aux = new byte[tam];
            arquivo.read(aux);
            T entidade = construtor.newInstance();
            entidade.fromByteArray(aux);
            if(entidade.getID() == id) {
                arquivo.seek(endereco);
                arquivo.writeByte('*');
                arquivo.skipBytes(2);
                for(int i=0; i<tam; i++)
                    arquivo.writeByte(0);
                insereEspacoVazio(endereco, tam);
                indiceDireto.delete(id);
                return true;
            }
        }
        return false;
    }

    public boolean update(T novaEntidade) throws Exception {
        int id = novaEntidade.getID();
        ParIDEndereco pie = indiceDireto.read(id);
        if(pie == null)
            return false;

        long endereco = pie.getEndereco();
        arquivo.seek(endereco);
        byte lapide = arquivo.readByte();
        short tam = arquivo.readShort();
        if(lapide==' ') {
            byte[] aux = new byte[tam];
            arquivo.read(aux);
            T entidade = construtor.newInstance();
            entidade.fromByteArray(aux);
            if(entidade.getID() == novaEntidade.getID()) {

                byte[] novoAux = novaEntidade.toByteArray();
                short novoTamanho = (short)novoAux.length;

                if(novoTamanho<=tam) { // registro manteve tamanho ou diminuiu
                    arquivo.seek(endereco+3);  // pula o lápide e o indicador de tamanho
                    arquivo.write(novoAux);
                    for(int i=0; i<tam-novoTamanho; i++)
                        arquivo.writeByte(0);
                } else {   // registro aumentou de tamanho  
                    arquivo.seek(endereco);
                    arquivo.writeByte('*');
                    arquivo.skipBytes(2);
                    for(int i=0; i<tam; i++)
                        arquivo.writeByte(0);
                    insereEspacoVazio(endereco, tam);

                    long novoEndereco = encontraEspacoVazio(novoTamanho);
                    if(novoEndereco==-1) {
                        novoEndereco = arquivo.length();
                        arquivo.seek(arquivo.length());
                        arquivo.writeByte(' ');
                        arquivo.writeShort(novoTamanho);
                    } else {
                        arquivo.seek(novoEndereco);
                        arquivo.writeByte(' ');
                        arquivo.skipBytes(2);
                    }
                    arquivo.write(novoAux);
                    indiceDireto.update(new ParIDEndereco(id, novoEndereco));
                }
                return true;
            }
        }
        return false;
    }


    public void close() throws Exception {
        arquivo.close();
        indiceDireto.close();
    }

    public void insereEspacoVazio(long endereco, short tamanho) throws Exception {
        arquivo.seek(4);   // posição da cabeça da lista no arquivo
        long end = arquivo.readLong();   // endereço do primeiro elemento da lista

        // lista vazia
        if(end == -1) {
            arquivo.seek(4);
            arquivo.writeLong(endereco);
            arquivo.seek(endereco+3);
            arquivo.writeLong(-1);
        }

        // insere na lista existente, de forma ordenada
        else {
            long endAnterior = 4;
            while(end!=-1) {
                arquivo.seek(end+1);
                short tam = arquivo.readShort();
                if(tamanho<tam)
                    break;
                endAnterior = end;
                end = arquivo.readLong();
            }
            arquivo.seek(endereco+3);
            arquivo.writeLong(end);
            if(endAnterior==4)
                arquivo.seek(4);
            else
                arquivo.seek(endAnterior+3);
            arquivo.writeLong(endereco);
        }
    }

    public long encontraEspacoVazio(short tamanho) throws Exception {
        arquivo.seek(4);   // posição da cabeça da lista no arquivo
        long end = arquivo.readLong();   // endereço do primeiro elemento da lista

        // lista vazia
        if(end == -1) {
            return -1;
        }

        // busca na lista existente, de forma ordenada
        else {
            long endAnterior = 4;
            arquivo.seek(end+1);
            short tam = arquivo.readShort();
            while(tamanho>tam) {
                endAnterior = end;
                end = arquivo.readLong();
                if(end==-1)
                    return -1;
                arquivo.seek(end+1);
                tam = arquivo.readShort();
            }
            long endProximo = arquivo.readLong();
            if(endAnterior==4)   // O espaço é o primeiro elemento da lista
                arquivo.seek(4);
            else
                arquivo.seek(endAnterior+3);
            arquivo.writeLong(endProximo);
            return end;            
        }
    }
    
}