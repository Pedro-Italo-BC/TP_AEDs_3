package entidades.Usuario;
import aed3.InterfaceHashExtensivel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParEmailId implements InterfaceHashExtensivel {
    
    private String email;  // chave
    private int id;      // valor
    private static final int TAM_EMAIL = 46;
    private final short TAMANHO = (short)(TAM_EMAIL + 4);  // tamanho em bytes

    public ParEmailId() throws Exception {
        this.email = "";
        this.id = -1;
    }

    public ParEmailId(String email, int id) throws Exception {
        if(!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) 
            throw new Exception("EMAIL inválido!");
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Math.abs(this.email.hashCode());
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return "("+this.email + ";" + this.id+")";
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        byte[] emailBytes = new byte[TAM_EMAIL];
        byte[] original = email.getBytes();
        int len = Math.min(original.length, TAM_EMAIL);
        System.arraycopy(original, 0, emailBytes, 0, len);

        dos.write(emailBytes);
        dos.writeInt(this.id);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        byte[] vb = new byte[TAM_EMAIL];
        dis.read(vb);
        this.email = new String(vb).trim();
        this.id = dis.readInt();
    }

}