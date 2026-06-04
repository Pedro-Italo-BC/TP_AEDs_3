package menus;

import entidades.Curso.ArquivoCurso;
import entidades.Curso.Curso;
import entidades.Usuario.Usuario;
import java.util.ArrayList;
import java.util.Scanner;
import utils.Manipulate;

public class Inscricao {

    //valida NanoID (10 caracteres alfanuméricos)
    public static boolean codigoValido(String codigo) {
        return codigo != null && codigo.matches("[a-zA-Z0-9]{10}");
    }

    public static void menu(Usuario user, Scanner sc) throws Exception {
        ArquivoCurso arqCurso = new ArquivoCurso();

        int s = 0;
        String aux = "";
        char opcao;

        while (s == 0) {
            System.out.println("TP01 Aeds3");
            System.out.println("----------");
            System.out.println("> Inicio > Inscrições");
            System.out.println();
            

            //listar inscrições do usuário
            Curso[] minhasInscricoes = arqCurso.readCursosUsuarioAtivo(user.getID());

            // se não tiver inscrições
            if (minhasInscricoes.length == 0) {  
                System.out.println("Você ainda não está inscrito em nenhum curso.");
                System.out.println("");

            //se tiver inscrições, lista elas    
            } else { 
                System.out.println("Suas inscrições:");
                for (int i = 0; i < minhasInscricoes.length; i++) {
                    Curso c = minhasInscricoes[i];
                    if(c != null)
                        System.out.println("(" + (i + 1) + ") " + c.getNome() + " - " + c.getInicio());
                }
                System.out.println("");
            }

            System.out.println("(A) Buscar curso por código");
            System.out.println("(B) Buscar curso por palavra-chave"); // tp03
            System.out.println("(C) Listar todos os cursos");
            System.out.println();
            System.out.println("(R) Retornar ao menu anterior");
            System.out.println();
            System.out.print("Opção: ");
            System.out.println();

            aux = sc.nextLine();
            if (aux.length() == 0) continue;

            int numericOption = Manipulate.toInt(aux);
            opcao = Character.toUpperCase(aux.charAt(0));

            // sair
            if (opcao == 'R') {
                s = 1;
                break;
            }

            // buscar por código
            else if (opcao == 'A') {
                System.out.println("TP01 Aeds3");
                System.out.println("----------");
                System.out.println("> Inicio > Inscrições > Busca por código");
                System.out.println();

                System.out.print("Digite o código: ");
                String codigo = sc.nextLine();

                // valida NanoID
                if (!codigoValido(codigo)) {
                    System.out.println("Código inválido. Deve ter 10 caracteres alfanuméricos.");
                    System.out.println();
                    continue;
                }

                //Chamada para buscar curso
                Curso c = arqCurso.buscarPorCodigo(codigo);

                if (c != null) {
                    //chama menu de detalhes do curso
                    
                    DetalheCurso.menu(c, user, sc);
                } else {
                    System.out.println("Curso não encontrado.");
                }

                System.out.println();
                System.out.println("Pressione qualquer tecla para continuar...");
                sc.nextLine();
            }

            // buscar por palavra chave
            else if (opcao == 'B') {
                System.out.println("Funcionalidade não implementada.");
                System.out.println();

                System.out.println("Pressione qualquer tecla para voltar...");
                sc.nextLine();
            }

            // listar todos os cursos
            else if (opcao == 'C') {

                //busca todos os cursos
                ArrayList<Curso> lista = arqCurso.readAllOrdenadoPorData();

                //chama o mini menu paginado
                ListaCursos.menu(lista, user, sc);

            // detalhes de um curso específico da lista de inscrições
            }else if (numericOption > 0 && numericOption <= minhasInscricoes.length) {
                Curso c = minhasInscricoes[numericOption - 1];
                if(c != null) {
                    //apenas detalhes do curso
                    DetalheCurso.menu2(c, user, sc);
                } else {
                    System.out.println("Opção inválida.");
                }
            }

            // opção inválida
            else {
                System.out.println("Opção Inválida. Deseja tentar novamente? S/N");
                char desejo = Character.toUpperCase(sc.nextLine().charAt(0));

                if (desejo == 'S') {
                    s = 0;
                } else if (desejo == 'N') {
                    s = 1;
                    break;
                } else {
                    System.out.println("Opção Inválida");
                    break;
                }
            }
        }
    }
}