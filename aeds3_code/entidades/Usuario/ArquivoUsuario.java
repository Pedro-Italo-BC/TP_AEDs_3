package entidades.Usuario;

import aed3.*;
import entidades.Curso.ArquivoCurso;

public class ArquivoUsuario extends Arquivo<Usuario> {
    
    HashExtensivel<ParEmailId> indiceEmail;
    final int TAM_CABECALHO = 4;

    public ArquivoUsuario() throws Exception {
        super("usuario", Usuario.class.getConstructor());
        
    
        indiceEmail = new HashExtensivel<>(
            ParEmailId.class.getConstructor(), 
            TAM_CABECALHO,
            "./dados/usuario/indiceEmail.d.db",
            "./dados/usuario/indiceEmail.c.db"
        );

     
    }

    // CREATE
    public int create(Usuario u) throws Exception {
        int id = super.create(u);
        indiceEmail.create(new ParEmailId(u.getEmail(), id));
        return id;
    }

    // LOGIN (busca sequencial por enquanto)
    public Usuario encontrarPorEmailSafe(String email) throws Exception {
        ParEmailId pei = indiceEmail.read(Math.abs(email.hashCode()));
        Usuario usuarioEncontrado = null;

        if(pei != null && pei.getEmail().equals(email)) {
            usuarioEncontrado = read(pei.getId());
        }

        usuarioEncontrado.setHashSenha(null);
        usuarioEncontrado.setHashRespostaSecreta(null);
    
        return usuarioEncontrado; 
    }

    public Usuario encontrarPorEmailUnsafe(String email) throws Exception {
        ParEmailId pei = indiceEmail.read(Math.abs(email.hashCode()));
        Usuario usuarioEncontrado = null;

        if(pei != null && pei.getEmail().equals(email)) {
            usuarioEncontrado = read(pei.getId());
        }

        return usuarioEncontrado; 
    } 

      public boolean responderPergunta(Usuario user, String resposta) {
        boolean resp = false;
        try {
            if(user.getHashRespostaSecreta().equals(Usuario.gerarHash(resposta))) {
                resp = true;
            }
        } catch (Exception e) {
            System.out.println("Olha, usuário não encontrado.");
        }

        return resp;
    }

    public Usuario login(String email, String senha) throws Exception {
        ParEmailId pei = indiceEmail.read(Math.abs(email.hashCode()));
        
        if(pei == null || !pei.getEmail().equals(email))
            return null;

        Usuario u = read(pei.getId());

        String hash = Usuario.gerarHash(senha);

        if(u != null && u.getHashSenha().equals(hash)) {
            return u;
        }

        return null;
    }

    public boolean delete(int id) throws Exception {
        Usuario u = read(id);
        if(u != null) {
            if(super.delete(id)) {
                ArquivoCurso arqCurso = new ArquivoCurso();
                arqCurso.deleteTudoPorUsuario(id);
                indiceEmail.delete(Math.abs(u.getEmail().hashCode()));
                super.delete(id);
                return true;
            }
        }

        return false;
    }
}