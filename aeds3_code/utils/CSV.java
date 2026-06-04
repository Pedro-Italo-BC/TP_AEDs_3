package utils;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import entidades.Curso.Curso;
import entidades.CursoUsuario.ArquivoCursoUsuario;
import entidades.CursoUsuario.InscritoInfo;

public class CSV {
    public static void exportarCSV(Curso curso) throws Exception {

    ArquivoCursoUsuario arqCU = new ArquivoCursoUsuario();

    ArrayList<InscritoInfo> inscritos = arqCU.listarInscritos(curso.getID());

    String nomeArquivo = "curso_" + curso.getID() + "_inscritos.csv";

    PrintWriter pw = new PrintWriter(new FileWriter("./csv/" + nomeArquivo));

    // cabeçalho
    pw.println("nome,email,cancelado");

    for (InscritoInfo info : inscritos) {

        String nome = info.getUsuario().getNome();
        String email = info.getUsuario().getEmail();
        boolean cancelado = info.getCursoUsuario().getCancelado();

        pw.println(nome + "," + email + "," + cancelado);
    }

    pw.close();

    System.out.println("CSV exportado com sucesso: " + nomeArquivo);
}
}
