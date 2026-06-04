package menus;

import entidades.Curso.Curso;
import entidades.Usuario.Usuario;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ListaCursos {

    public static void menu(ArrayList<Curso> lista, Usuario user, Scanner sc) {

        if (lista == null || lista.isEmpty()) {
            System.out.println("Nenhum curso encontrado.");
            return;
        }

        int pagina = 0;
        int porPagina = 10;
        int totalPaginas = (int) Math.ceil((double) lista.size() / porPagina);

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (true) {

            // HEADER
            System.out.println("EntrePares 1.0");
            System.out.println("--------------");
            System.out.println("> Início > Minhas inscrições > Lista de cursos");
            System.out.println();
            System.out.println("Página " + (pagina + 1) + " de " + totalPaginas);
            System.out.println();

            int inicio = pagina * porPagina;
            int fim = Math.min(inicio + porPagina, lista.size());

            int cont = 1;

            for (int i = inicio; i < fim; i++) {
                Curso c = lista.get(i);
                String numero = String.valueOf(cont); // Numero do curso

                //Print certo com estado
                String estadoCurso = c.getEstadoString(); // Estado do curso

                if (!estadoCurso.isEmpty()) {
                    System.out.println("(" + numero + ") " + c.getNome() + " - " +
                        c.getInicio().format(formato) + " (" + estadoCurso + ")");
                } else {
                    System.out.println("(" + numero + ") " + c.getNome() + " - " +
                        c.getInicio().format(formato));
                }

                //Print certo inicial
                //System.out.println("(" + numero + ") " + c.getNome() + " - " + c.getInicio().format(formato));

                //Print com código
                /*System.out.println("(" + cont + ") " + c.getNome() + " - " + c.getInicio().format(formato)+ " | Código: " + c.getCodigo());*/

                cont++;
            }

            //OPÇÕES
            System.out.println();
            System.out.println("(A) Página anterior");
            System.out.println("(B) Próxima página");
            System.out.println("(R) Retornar ao menu anterior");
            System.out.print("Opção: ");

            String opcao = sc.next().toUpperCase();

            //Opções da Ane -> ir para próxima página ou voltar

            if (opcao.equals("A")) {
                if (pagina > 0) {
                    pagina--;
                } else {
                    System.out.println("Você já está na primeira página.");
                }

            } else if (opcao.equals("B")) {
                if (pagina < totalPaginas - 1) {
                    pagina++;
                } else {
                    System.out.println("Você já está na última página.");
                }

            } else if (opcao.equals("R")) {
                break;

            } 

            //Opção da Camila -> ver detalhes de um curso

            else if (opcao.matches("\\d+")) { //se opção for um número
                int escolha = Integer.parseInt(opcao);
                int qtdNaPagina = fim - inicio;

                if (escolha >= 1 && escolha <= qtdNaPagina) {
                    Curso cursoSelecionado = lista.get(inicio + escolha - 1);

                    // chama o menu de detalhes
                    try {
                        DetalheCurso.menu(cursoSelecionado, user, sc);
                    } catch (Exception e) {
                        System.out.println("Erro ao abrir o curso.");
                        e.printStackTrace();
                    }
                }
            }
            
            else {
                System.out.println("Opção inválida.");
            }
        }


    }
}