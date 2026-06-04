package entidades.Curso;

import aed3.InterfaceEntidade;
import java.io.*;
import java.time.LocalDate;
import java.util.Random;

public class Curso implements InterfaceEntidade {
    private int idCurso;
    private int idUsuario; // relacionamento 1:N
    private String nome;
    private String descricao;
    private LocalDate inicio;
    private String codigo;

    // 0 - Aberto para inscrições
    // 1 - Inscrições encerradas
    // 2 - Curso concluido
    // 3 - Curso cancelado
    private int estado;

    public Curso() {
    }

    public Curso(int idUsuario, String nome, String descricao, LocalDate inicio, int estado) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.descricao = descricao;
        this.inicio = inicio;
        this.estado = estado;
        this.codigo = gerarCodigo(); // gera automaticamente
    }

    // Geração do código estilo NanoID (10 caracteres)
    public static String gerarCodigo() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            int index = rand.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    // GETTERS E SETTERS

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public int getID() {
        return this.idCurso;
    }

    public void setID(int id) {
        this.idCurso = id;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(ba);

        dos.writeInt(idCurso);
        dos.writeInt(idUsuario);
        dos.writeUTF(nome);
        dos.writeUTF(descricao);
        dos.writeUTF(inicio.toString());
        dos.writeUTF(codigo);
        dos.writeInt(estado);

        return ba.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bi = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bi);

        idCurso = dis.readInt();
        idUsuario = dis.readInt();
        nome = dis.readUTF();
        descricao = dis.readUTF();
        inicio = LocalDate.parse(dis.readUTF());
        codigo = dis.readUTF();
        estado = dis.readInt();
    }

    //transformar os cases do estado para a mensagem a ser exibida
    public String getEstadoString() {
    switch (estado) {
        case 0: return "";
        case 1: return "INSCRIÇÕES ENCERRADAS";
        case 2: return "CURSO CONCLUÍDO";
        case 3: return "CURSO CANCELADO";
        default: return "";
    }
}

    @Override
    public String toString() {
        return "\nID: " + idCurso +
               "\nUsuário: " + idUsuario +
               "\nNome: " + nome +
               "\nDescrição: " + descricao +
               "\nData de início: " + inicio +
               "\nCódigo: " + codigo +
               "\nEstado: " + estado + "\n";
    }
    public boolean hasAluno(){
        return false;
    }
}