package aed3;
public interface InterfaceEntidade {
    public void setID(int id);
    public int getID();
    public byte[] toByteArray() throws Exception;
    public void fromByteArray(byte[] vb) throws Exception;
}
