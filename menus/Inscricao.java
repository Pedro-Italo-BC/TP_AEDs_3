package menus;

import entidades.Curso.ArquivoCurso;
import entidades.Curso.Curso;
import entidades.Usuario.Usuario;
import java.util.ArrayList;
import java.util.Scanner;

import aed3.*;
import utils.Manipulate;
import utils.IDF;

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

            // buscar por palavra chave ////////////////
            else if (opcao == 'B') {
                char escolha = '0';
                while(escolha != 'R') {
                    System.out.println("TP01 Aeds3");
                    System.out.println("----------");
                    System.out.println("> Inicio > Inscrições > Busca por palavra-chave");
                    System.out.println();

                    System.out.print("Digite a palavra-chave: ");
                    String palavraChave = sc.nextLine();

                    IDF IDF = new IDF();
                    //Chamada para buscar curso
                    ElementoLista[] resultados = IDF.pesquisa(palavraChave, arqCurso.readAll().size());

                    if (resultados.length > 0) {
                        System.out.println("TP01 Aeds3");
                        System.out.println("----------");
                        System.out.println("> Inicio > Inscrições > Busca por palavra-chave > " + palavraChave);
                        System.out.println();
                        //chama menu de listagem paginada
                        Curso[] cursosEncontrados = new Curso[resultados.length];
                        int esq = 0, dir = 10;
                        escolha = '0';
                        
                        while(escolha != 'N' && escolha != 'R') {
                            System.out.println("\n\n\n\n\n");
                            //printa os cursos de 10 em 10
                            for (int i = esq; i < dir && i < resultados.length; i++) {
                                cursosEncontrados[i] = arqCurso.read(resultados[i].getId());
                                System.out.println("[" + i + "] " + cursosEncontrados[i].getNome());
                            }
                            System.out.println();
                            System.out.println("Pagina " + (esq / 10 + 1) + " de " + ((resultados.length - 1) / 10 + 1));
                            System.out.println("(A) Página anterior");
                            System.out.println("(B) Próxima página");
                            System.out.println("(N) Para buscar uma nova palavra-chave");
                            System.out.println("(R) Retornar ao menu anterior");
                            aux = "";
                            aux = sc.nextLine();
                            while(aux.length() == 0) {
                                System.out.println("Opção inválida. Digite novamente.");
                                aux = sc.nextLine();
                            }
                            escolha = Character.toUpperCase(aux.charAt(0));
                            if(escolha == 'A') {
                                if(esq - 10 >= 0) {
                                    esq -= 10;
                                    dir -= 10;
                                } else {
                                    System.out.println("Você já está na primeira página.");
                                }
                            } else if(escolha == 'B') {
                                if(esq + 10 < resultados.length) {
                                    esq += 10;
                                    dir += 10;
                                } else {
                                    System.out.println("Você já está na última página.");
                                }
                            } else if(escolha == 'N') {
                                //VAI SAIR SOZINHO
                            } else if(escolha == 'R') {
                                //VAI SAIR SOZINHO
                            }else if(Manipulate.toInt(aux) > 0 && Manipulate.toInt(aux) > esq && Manipulate.toInt(aux) <= dir) {
                                DetalheCurso.menu(cursosEncontrados[Manipulate.toInt(aux) - 1], user, sc);
                            }else{
                                System.out.println("Opção inválida.");
                            }
                        }
                    } else {
                        System.out.println();
                        System.out.println("Nenhum curso encontrado para a palavra-chave: " + palavraChave);
                        System.out.println();
                    }
                }
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