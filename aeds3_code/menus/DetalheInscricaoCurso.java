package menus;

import entidades.Curso.Curso;
import entidades.CursoUsuario.ArquivoCursoUsuario;
import entidades.CursoUsuario.CursoUsuario;
import entidades.CursoUsuario.InscritoInfo;
import entidades.Usuario.Usuario;
import java.util.ArrayList;
import java.util.Scanner;
import utils.CSV;
import utils.Manipulate;

public class DetalheInscricaoCurso {
        public static void menu(Curso c, Usuario user, Scanner sc) throws Exception {

        if(c.getIdUsuario() != user.getID()) {
            throw new Exception("Nah ah! Você não é o criador do curso");
        }

        ArquivoCursoUsuario acu = new ArquivoCursoUsuario();
        ArrayList<InscritoInfo> inscList = acu.listarInscritos(c.getID());


        ArquivoCursoUsuario arquivoCursoUsuario = new ArquivoCursoUsuario();

        int s = 0;
        String aux = "";
        char opcao;
        int opcaoNumerica = Manipulate.toInt(aux);

        while (s == 0) {

            System.out.println("EntrePares 1.0");
            System.out.println("--------------");
            System.out.println("> Início > Meus Cursos > " + c.getNome() + " > Inscrições");
            for(int i = 0; i < inscList.size(); i++) {
                System.out.print("(" + i + ") " + inscList.get(i).getUsuario().getNome() + " (" + inscList.get(i).getCursoUsuario().getDataInscricao() + ")");
                if (inscList.get(i).getCursoUsuario().getCancelado()) {
                    System.out.print(" [CANCELADO]");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("(A) Exportar lista");
            System.out.println();
            System.out.println("(R) Retornar ao menu anterior");
            System.out.println();

            System.out.print("Opção: ");
            aux = sc.nextLine();

            if (aux.length() == 0) continue;

            opcao = Character.toUpperCase(aux.charAt(0));

            // voltar
            if (opcao == 'R') {
                s = 1;
                break;
            }

            // fazer inscrição
            else if (opcao == 'A') {
                CSV.exportarCSV(c);
                sc.nextLine();
                System.out.println();
            }

            else if(opcaoNumerica >= 0 && opcaoNumerica < inscList.size()) {
                DetalheInscricaoCurso.PrintUser(inscList.get(opcaoNumerica), c.getNome(), sc);
            }

            else {
                System.out.println("Opção inválida.");
                System.out.println("Pressione Enter para continuar...");
                sc.nextLine();
            }

        }
    }

    private static void PrintUser(InscritoInfo inscricao, String nameCourse, Scanner sc) throws Exception{
        boolean shouldStop = false;

        ArquivoCursoUsuario acu = new ArquivoCursoUsuario();
        CursoUsuario newCursoUsuario = inscricao.getCursoUsuario();

        while(!shouldStop) {

        boolean isCanselado = newCursoUsuario.getCancelado();

        System.out.println("TP02 Aeds3");
        System.out.println("----------");
        System.out.println("> Início > Meus Cursos > " + nameCourse + " > Inscrições > Detalhes");
        System.out.println();

        System.out.println("Meus dados:");
        System.out.println(inscricao.getUsuario().toString());
        System.out.println();

        if(isCanselado) {
            System.out.println("(A) Realocar inscrição");
        } else {
            System.out.println("(A) Cancelar inscrição");
        }
        System.out.println();
        System.out.println("(R) Retornar");
        System.out.println();
        String aux = sc.nextLine();
        char op = aux.toUpperCase().trim().charAt(0);
        
        switch(op) {
            case 'A': 
                newCursoUsuario.setCancelado(!isCanselado);
                acu.update(newCursoUsuario);
            break;
            
            case 'R': 
                shouldStop = true;
            break;

            default:
                System.out.println("Opção inválida\n Por favor, tente novamente.");
                sc.wait();
            break;
        }
    }
}
}
