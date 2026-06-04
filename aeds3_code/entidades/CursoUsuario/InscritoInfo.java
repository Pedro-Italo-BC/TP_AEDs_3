package entidades.CursoUsuario;

import entidades.Usuario.Usuario;

public class InscritoInfo {

    private Usuario usuario;
    private CursoUsuario cursoUsuario;

    public InscritoInfo(Usuario usuario, CursoUsuario cursoUsuario) {
        this.usuario = usuario;
        this.cursoUsuario = cursoUsuario;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario value) {
        this.usuario = value;
    }

    public CursoUsuario getCursoUsuario() {
        return this.cursoUsuario;
    }
    
    public void setCursoUsuario(CursoUsuario value) {
        this.cursoUsuario = value;
    }
}