package entidades.CursoUsuario;

import java.io.*;
import java.time.LocalDate;

public class CursoUsuario {

    private int idCursoUsuario;
    private int idCurso;
    private int idUsuario;
    private LocalDate dataInscricao;
    private boolean cancelado; 

    public CursoUsuario() {

    }

    public CursoUsuario(int idCursoUsuario, int idCurso, int idUsuario, LocalDate dataInscricao) {
        this.idCursoUsuario = idCursoUsuario;
        this.idCurso = idCurso;
        this.idUsuario = idUsuario;
        this.dataInscricao = dataInscricao;
        this.cancelado = false;
    }

    // GETTERS E SETTERS

    public int getIdCursoUsuario() { 
        return idCursoUsuario; 
    }
    
    public void setIdCursoUsuario(int id) { 
        this.idCursoUsuario = id; 
    }

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

    public LocalDate getDataInscricao() { 
        return dataInscricao; 
    }
    

    public void setDataInscricao(LocalDate data) { 
        this.dataInscricao = data; 
    }

    public boolean getCancelado() { 
        return cancelado; 
    }
    

    public void setCancelado(boolean value) { 
        this.cancelado = value; 
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(idCursoUsuario);
        dos.writeInt(idCurso);
        dos.writeInt(idUsuario);
        dos.writeLong(dataInscricao.toEpochDay());
        dos.writeBoolean(cancelado);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        idCursoUsuario = dis.readInt();
        idCurso = dis.readInt();
        idUsuario = dis.readInt();
        dataInscricao = LocalDate.ofEpochDay(dis.readLong());
        cancelado = dis.readBoolean();
    }
}