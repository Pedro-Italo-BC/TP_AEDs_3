package aed3;

import java.io.*;

public class ParIdNome implements InterfaceArvoreBMais<ParIdNome> {

  private int id;
  private String nome;
  private short TAMANHO = 30;

  public ParIdNome() throws Exception {
    this(-1, "");
  }

  public ParIdNome(int i) throws Exception {
    this(i, "");
  }

  public ParIdNome(int i, String n) throws Exception {
    if(n.getBytes().length > 26)
      throw new Exception("Nome extenso demais.");
    this.id = i;
    this.nome = n;
  }

  public int getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  @Override
  public ParIdNome clone() {
    try {
      return new ParIdNome(this.id, this.nome);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public short size() {
    return this.TAMANHO;
  }

  @Override
  public int compareTo(ParIdNome a) {
    return this.id - a.id; 
  }

  public String toString() {
    return String.format("%-3d", this.id) + ";" + this.nome;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    dos.writeInt(this.id);

    byte[] vb = new byte[26];
    byte[] vbNome = this.nome.getBytes();

    int i = 0;
    while(i < vbNome.length) {
      vb[i] = vbNome[i];
      i++;
    }
    while(i < 26) {
      vb[i] = ' ';
      i++;
    }

    dos.write(vb);

    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);

    this.id = dis.readInt();

    byte[] vb = new byte[26];
    dis.read(vb);
    this.nome = (new String(vb)).trim();
  }
}