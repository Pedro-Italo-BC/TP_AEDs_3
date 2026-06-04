package aed3;
/*
Esta classe representa um objeto para uma entidade
que será armazenado em uma árvore B+

Neste caso em particular, este objeto é representado
por uma string e um inteiro para que possa ser usado
como índice indireto de nomes para uma entidade qualquer.

Implementado pelo Prof. Marcos Kutova
v1.0 - 2024
*/
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIdId implements InterfaceArvoreBMais<ParIdId> {

  private int id1;
  private int id2;
  private short TAMANHO = 8;

  public ParIdId() throws Exception {
    this(-1,-1);
  }

  public ParIdId(int id1, int id2) throws Exception {
    this.id1 = id1; // ID da primeira entidade
    this.id2 = id2; // ID da segunda entidade
  }

    public int getId1() {
        return id1;
    }

    public int getId2() {
        return id2;
    }

  @Override
  public ParIdId clone() {
    try {
      return new ParIdId(this.id1, this.id2);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public short size() {
    return this.TAMANHO;
  }

  public int compareTo(ParIdId a) {
    if(this.id1 != a.id1)
      return this.id1 - a.id1;
    else {
      if(this.id2 == -1)
        return 0;
      else
         return this.id2 - a.id2;    
    }
  }

  public String toString() {
    return this.id1 + ";" + this.id2;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.id1);
    dos.writeInt(this.id2);
    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.id1 = dis.readInt();
    this.id2 = dis.readInt();
  }

}