package entidades.CursoUsuario;

import aed3.ArvoreBMais;
import aed3.HashExtensivel;
import aed3.ParIDEndereco;
import aed3.ParIdId;
import entidades.Curso.Curso;
import entidades.Usuario.ArquivoUsuario;
import entidades.Usuario.Usuario;
import java.io.*;
import java.util.ArrayList;

public class ArquivoCursoUsuario {

    private final ArvoreBMais<ParIdId> idxUsuarioCurso;
    private final HashExtensivel<ParIDEndereco> idxDireto;
    private final ArvoreBMais<ParIdId> idxCursoUsuario;

    private RandomAccessFile arq;
    private final int TAM_CABECALHO = 4;

    public ArquivoCursoUsuario() throws Exception {
        arq = new RandomAccessFile("./dados/cursoUsuario/cursoUsuario.db", "rw"); 

        if (arq.length() == 0) {
            arq.writeInt(0); // último ID
        }

        idxUsuarioCurso = new ArvoreBMais<>(
            ParIdId.class.getConstructor(), 4, "./dados/cursoUsuario/idxUsuarioCurso.db"
        );

        idxDireto = new HashExtensivel<>(
            ParIDEndereco.class.getConstructor(), 4, "./dados/cursoUsuario/indiceCursoUsuario.d.db", "./dados/cursoUsuario/indiceCursoUsuario.c.db"
        );

        idxCursoUsuario = new ArvoreBMais<>(
            ParIdId.class.getConstructor(), 4, "./dados/cursoUsuario/idxCursoUsuario.db"
        );

        if (idxUsuarioCurso.empty()) {
            reconstruirIndice();
        }
    }

    // Reconstrói todos os índices a partir do arquivo principal
    private void reconstruirIndice() throws Exception {

        arq.seek(TAM_CABECALHO);

        while (arq.getFilePointer() < arq.length()) {

            long pos = arq.getFilePointer(); 
            byte lapide = arq.readByte();
            short tam = arq.readShort();

            byte[] ba = new byte[tam];
            arq.readFully(ba);

            if(lapide == '*') {
                return;
            }

            CursoUsuario cu = new CursoUsuario();
            cu.fromByteArray(ba);

            // índice B+
            idxUsuarioCurso.create(
                new ParIdId(cu.getIdUsuario(), cu.getIdCursoUsuario())
            );

            // índice hash (direto)
            idxDireto.create(
                new ParIDEndereco(cu.getIdCursoUsuario(), pos)
            );

            idxCursoUsuario.create(
                new ParIdId(cu.getIdCurso(), cu.getIdCursoUsuario())
            );
            
        }
    }

    //SELECIONAR CURSOS DE UM USUÁRIO
    public CursoUsuario readById(int id) throws Exception {

        ParIDEndereco pie = idxDireto.read(id);

        if (pie == null)
            return null;

        arq.seek(pie.getEndereco());

        byte lapide = arq.readByte();
        short tam = arq.readShort();

        if (lapide == '*')
            return null;

        byte[] ba = new byte[tam];
        arq.readFully(ba);

        CursoUsuario cu = new CursoUsuario();
        cu.fromByteArray(ba);

        return cu;
    }

     // Retorna todas as inscrições de um determinado curso (usando árvore B+)
    public ArrayList<CursoUsuario> readAllByIdCurso(int idCursoBuscado) throws Exception {

        ArrayList<CursoUsuario> listaFinal = new ArrayList<>();

        ArrayList<ParIdId> lista =
            idxCursoUsuario.read(new ParIdId(idCursoBuscado, -1));

        for (ParIdId p : lista) {
            CursoUsuario cu = readById(p.getId2());
            if (cu != null)
                listaFinal.add(cu);
        }

        return listaFinal;
    }

    // Retorna todas as inscrições de um determinado usuário (usando árvore B+)
    public ArrayList<CursoUsuario> readAllByIdUsuario(int idUsuarioBuscado) throws Exception {

        ArrayList<CursoUsuario> listaFinal = new ArrayList<>();

        ArrayList<ParIdId> lista =
            idxUsuarioCurso.read(new ParIdId(idUsuarioBuscado, -1));

        for (ParIdId p : lista) {
            CursoUsuario cu = readById(p.getId2());
            if (cu != null)
                listaFinal.add(cu);
        }

        return listaFinal;
    }


    //verificar se o usuário já está inscrito no curso
public boolean isInscrito(int idUsuarioBuscado, int idCursoBuscado) throws Exception {
        ArrayList<CursoUsuario> listaCursosDoUsuario = readAllByIdUsuario(idUsuarioBuscado);
        int tam = listaCursosDoUsuario.size();
        for (int i = 0; i < tam; i++) {
            CursoUsuario cu = listaCursosDoUsuario.get(i);
            if (cu.getIdCurso() == idCursoBuscado) {
                return true;
            }
        }
        return false;
}
    
    //CREATE -> Fazer uma inscrição

    public int create(CursoUsuario cu) throws Exception {

        arq.seek(0);
        int ultimoID = arq.readInt();
        int novoID = ultimoID + 1;

        cu.setIdCursoUsuario(novoID);

        arq.seek(0);
        arq.writeInt(novoID);

        // posição do registro
        arq.seek(arq.length());
        long pos = arq.getFilePointer();

        byte[] ba = cu.toByteArray();

        arq.writeByte(' ');
        arq.writeShort(ba.length);
        arq.write(ba);

        // índice B+
        idxUsuarioCurso.create(
            new ParIdId(cu.getIdUsuario(), novoID)
        );
        idxCursoUsuario.create(
            new ParIdId(cu.getIdCurso(), novoID)
        );

        // índice hash
        idxDireto.create(
            new ParIDEndereco(novoID, pos)
        );

        return novoID;
    }


    //READ ALL

    public ArrayList<CursoUsuario> readAll() throws Exception {
        ArrayList<CursoUsuario> lista = new ArrayList<>();

        arq.seek(TAM_CABECALHO);

        while (arq.getFilePointer() < arq.length()) {
            byte lapide = arq.readByte();
            short tam = arq.readShort();

            if (lapide != '*') {
                byte[] ba = new byte[tam];
                arq.readFully(ba);

                CursoUsuario cu = new CursoUsuario();
                cu.fromByteArray(ba);

                lista.add(cu);
            } else {
                arq.skipBytes(tam);
            }
        }

        return lista;
    }

    public ArrayList<InscritoInfo> listarInscritos(int idCurso) throws Exception {

    ArrayList<InscritoInfo> lista = new ArrayList<>();
    ArrayList<CursoUsuario> inscricoes = readAllByIdCurso(idCurso);
    ArquivoUsuario arqUsuario = new ArquivoUsuario();
    for (CursoUsuario cu : inscricoes) {

        Usuario usuario = arqUsuario.read(cu.getIdUsuario());
        if (usuario != null) {
            lista.add(
                new InscritoInfo(usuario, cu)
            );
        }
    }

    return lista;
}   

public CursoUsuario readByUsuarioAndCurso(int idUsuario, int idCurso) throws Exception {

    ArrayList<ParIdId> lista =
        idxUsuarioCurso.read(new ParIdId(idUsuario, -1));

    for (ParIdId p : lista) {
        CursoUsuario cu = readById(p.getId2());

        if (cu != null && cu.getIdCurso() == idCurso) {
            return cu;
        }
    }

    return null;
}

public boolean update(CursoUsuario cu) throws Exception {
        ParIDEndereco pie = idxDireto.read(cu.getIdCursoUsuario());

        if(pie == null) {
            return false;
        }

        long pos = pie.getEndereco();

        arq.seek(pos);

        byte lapide = arq.readByte();
        arq.readShort();
        
        if(lapide == '*') {
            return false;
        }

        arq.write(cu.toByteArray());

        return true;
    }

//Delete -> cancelar a inscrição
public boolean delete(int id) throws Exception {

    ParIDEndereco pie = idxDireto.read(id);

    if (pie == null){
        return false;
    }

    long pos = pie.getEndereco();

    arq.seek(pos);

    byte lapide = arq.readByte();
    short tam = arq.readShort();

    if (lapide == '*')
        return false;

    byte[] ba = new byte[tam];
    arq.readFully(ba);

    CursoUsuario cu = new CursoUsuario();
    cu.fromByteArray(ba);

    // remove dos índices
    idxUsuarioCurso.delete(
        new ParIdId(cu.getIdUsuario(), cu.getIdCursoUsuario())
    );

    idxCursoUsuario.delete(
        new ParIdId(cu.getIdCurso(), cu.getIdCursoUsuario())
    );

    idxDireto.delete(cu.getIdCursoUsuario());

    // marca como deletado
    arq.seek(pos);
    arq.writeByte('*');

    return true;
}

public void deleteInscritosPorCurso(Curso curso) throws Exception {
        ArrayList<CursoUsuario> listaCursosDoUsuario = readAllByIdCurso(curso.getID());
        for (CursoUsuario cu : listaCursosDoUsuario) {
            delete(cu.getIdCursoUsuario());
        }
}

public boolean InscreverEmCurso(Usuario user, Curso curso) throws Exception {
        if(isInscrito(user.getID(), curso.getID())) {
            System.out.println("Você já está inscrito neste curso.");  
            return false;
        }

        if(user.getInativo()) {
            System.out.println("Você precisa de uma conta ativa para se inscrever em um curso!");
            return false;
        }


        CursoUsuario cursoUsuario = new CursoUsuario(0, curso.getIdCurso(), user.getID(), java.time.LocalDate.now());
        create(cursoUsuario);
        System.out.println("Inscrição realizada com sucesso!");

        return true;
    }
}