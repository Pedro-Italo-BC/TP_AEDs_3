package entidades.Curso;
import aed3.*;
import entidades.CursoUsuario.ArquivoCursoUsuario;
import entidades.CursoUsuario.CursoUsuario;
import entidades.Usuario.ArquivoUsuario;
import entidades.Usuario.Usuario;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ArquivoCurso extends Arquivo<Curso> {
    private final int TAM_CABECALHO = 4;

    private final ArquivoUsuario arqUsuario = new ArquivoUsuario();
    RandomAccessFile arq;
    
    private final ArquivoCursoUsuario arquivoCursoUsuario = new ArquivoCursoUsuario();
    
    ArvoreBMais<ParIdId> relacionamentoUsuarioCurso;
    ArvoreBMais<ParNomeId> indiceNome;

    public ArquivoCurso() throws Exception {
        super("curso", Curso.class.getConstructor());
        arq = new RandomAccessFile("./dados/curso/dado.db", "rw");
        relacionamentoUsuarioCurso = new ArvoreBMais<>(ParIdId.class.getConstructor(), TAM_CABECALHO, "./dados/curso/relacionamentoUsuarioCurso.db");
        indiceNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), TAM_CABECALHO, "./dados/curso/indiceNome.db");
    }

    @Override
    public int create(Curso c) throws Exception {
        Usuario user = arqUsuario.read(c.getIdUsuario());
        
        if(user.getInativo()) {
            System.out.println("Você precisa estar com uma conta ativada para cadastrar um novo curso");
            return -1;
        }
        
        int id = super.create(c);
        relacionamentoUsuarioCurso.create(new ParIdId(c.getIdUsuario(), id));
        indiceNome.create(new ParNomeId(c.getNome(), id));
        return id;
    }

    //procura um curso sequencialmente utilizando o id (nao esta funcionando)
    public Curso findCursoPorId(int id) throws Exception {
        arq.seek(TAM_CABECALHO);
        while (arq.getFilePointer() < arq.length()) {
            byte lapide = arq.readByte();
            short tam = arq.readShort();

            if (lapide != '*') {
                byte[] ba = new byte[tam];
                arq.readFully(ba);
                Curso c = new Curso();
                c.fromByteArray(ba);

                if (c.getIdCurso() == id) {
                    return c;
                }
            } else {
                arq.skipBytes(tam);
            }
        }
        return null;        
    }

    //encontra todos os cursos que um usuario se inscreveu baseado no id
    public Curso[] readCursosUsuarioAtivo(int idUsuario) throws Exception {
       
        ArrayList<CursoUsuario> lista = new ArrayList<>(
                arquivoCursoUsuario.readAllByIdUsuario(idUsuario)
                .stream()
                .filter(cu -> cu.getCancelado() == false)
                .toList()
);

        Curso[] cursos = new Curso[lista.size()];

        for (int i = 0; i < lista.size(); i++) {
            Curso c = super.read(lista.get(i).getIdCurso());
            if (c != null) {
                cursos[i] = c;
            }
        }
        
        return cursos;
    }

    //encontra todos os cursos que um usuario disponibilizou
    public Curso[] readCursosPorUsuario(int idUsuario) throws Exception {
        ArrayList<ParIdId> piis = relacionamentoUsuarioCurso.read(new ParIdId(idUsuario, -1));
        
        if(piis.isEmpty()) {
            return new Curso[0];
        }

        Curso[] cursos = new Curso[piis.size()];
        int i = 0;
        for(ParIdId pii : piis) {
            cursos[i++] = super.read(pii.getId2());
        }

        return cursos;
    }

    public String[] readNomeCursosPorUsuario(int idUsuario) throws Exception {
        ArrayList<ParIdId> lista = relacionamentoUsuarioCurso.read(new ParIdId(idUsuario, -1));

        String[] nomes = new String[lista.size()];
        int i = 0;

        for(ParIdId p : lista) {
            Curso c = super.read(p.getId2());
            nomes[i++] = c.getEstado() == 3 ? c.getNome() + " [Cancelado]": c.getNome();
        }

        return nomes;
    }

    @Override
    public boolean update(Curso novo) throws Exception {
        Curso c = read(novo.getID());
        if(c==null) {
            return false;
        } 

        if(super.update(novo)) {
            if(c.getNome().compareTo(novo.getNome()) != 0) {
                indiceNome.delete(new ParNomeId(c.getNome(), c.getID()));
                indiceNome.create(new ParNomeId(novo.getNome(), novo.getID()));
            } 

            if(c.getIdUsuario() != novo.getIdUsuario()) {
                relacionamentoUsuarioCurso.delete(new ParIdId(c.getIdUsuario(), c.getID()));
                relacionamentoUsuarioCurso.create(new ParIdId(novo.getIdUsuario(), novo.getID()));
            } 

            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Curso c = read(id);
        if(c != null) {
            if(super.delete(id)) {
                indiceNome.delete(new ParNomeId(c.getNome(), c.getID()));
                relacionamentoUsuarioCurso.delete(new ParIdId(c.getIdUsuario(), c.getID()));
                return true;
            }
        }

        return false;
    }

    public void deleteTudoPorUsuario(int userId) throws Exception {
        Curso[] cursos = readCursosPorUsuario(userId);
        for(Curso curso : cursos) {
            delete(curso.getID());
            relacionamentoUsuarioCurso.delete(new ParIdId(userId, curso.getID()));
            
            arquivoCursoUsuario.deleteInscritosPorCurso(curso);
        }
    }

    public Curso buscarPorCodigo(String codigo) throws Exception {

        for (int id = 0; id < 1000; id++) {
            Curso c = super.read(id);

            if (c != null && c.getCodigo() != null && c.getCodigo().equals(codigo)) {
                return c;
            }
        }

        return null;
    }
    
    //Ler todos para ordenar
    public ArrayList<Curso> readAll() throws Exception {
        ArrayList<Curso> lista = new ArrayList<>();

        for (int id = 0; id < 1000; id++) {
            Curso c = super.read(id);
            if (c != null) {
                lista.add(c);
            }
        }

        return lista;
    }

    //Ordena por data
    public ArrayList<Curso> readAllOrdenadoPorData() throws Exception {
        ArrayList<Curso> lista = readAll();

        lista.sort((c1, c2) -> {
            if (c1.getInicio() == null) return 1;
            if (c2.getInicio() == null) return -1;
            return c1.getInicio().compareTo(c2.getInicio());
        });

        return lista;
    }

}