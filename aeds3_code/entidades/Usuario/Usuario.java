package entidades.Usuario;

import aed3.InterfaceEntidade;
import java.io.*;

public class Usuario implements InterfaceEntidade {
    private int idUsuario;
    private String nome;
    private String email;
    private String hashSenha;
    private String perguntaSecreta;
    private String hashRespostaSecreta;
    private boolean inativo;
    
    public Usuario() {
    }

    public Usuario(int id, String nome, String email, String hashSenha, String perguntaSecreta, String hashRespostaSecreta) {
        this.idUsuario = id;
        this.nome = nome;
        this.email = email;
        this.hashSenha = hashSenha;
        this.perguntaSecreta = perguntaSecreta;
        this.hashRespostaSecreta = hashRespostaSecreta;
        this.inativo = false;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashSenha() {
        return hashSenha;
    }

    public void setHashSenha(String hashSenha) {
        this.hashSenha = hashSenha;
    }

    public String getPerguntaSecreta() {
        return perguntaSecreta;
    }

    public void setPerguntaSecreta(String perguntaSecreta) {
        this.perguntaSecreta = perguntaSecreta;
    }

    public void setInativo(boolean value) {
        this.inativo = value;
    }

    public boolean getInativo() {
        return this.inativo;
    }

    public String getHashRespostaSecreta() {
        return hashRespostaSecreta;
    }

    public void setHashRespostaSecreta(String hashRespostaSecreta) {
        this.hashRespostaSecreta = hashRespostaSecreta;
    }

    public int getID() {
        return idUsuario;
    }

    public void setID(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Nome: " + nome +
               "\nEmail: " + email +
               (this.inativo ? "\nInativo" : "\nAtivo");
    }

    // hash simples (placeholder)
    public static String gerarHash(String texto) {
        return Integer.toString(texto.hashCode());
    }

    // serialização
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(ba);

        dos.writeInt(idUsuario);
        dos.writeUTF(nome);
        dos.writeUTF(email);
        dos.writeUTF(hashSenha);
        dos.writeUTF(perguntaSecreta);
        dos.writeUTF(hashRespostaSecreta);
        dos.writeBoolean(inativo);

        return ba.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bi = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bi);

        idUsuario = dis.readInt();
        nome = dis.readUTF();
        email = dis.readUTF();
        hashSenha = dis.readUTF();
        perguntaSecreta = dis.readUTF();
        hashRespostaSecreta = dis.readUTF();
        inativo = dis.readBoolean();
    }
}