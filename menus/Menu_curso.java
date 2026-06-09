package menus;

import entidades.Curso.*;
import entidades.Usuario.Usuario;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class Menu_curso {
    public static void mensagem(int i){
        if(i == 0){
            System.out.println("O curso ja foi finalizado, não é possível fazer mais mudanças");
        }else if(i == 1){
            System.out.println("Matricula já fechada");
        }else if(i == 2){
            System.out.println("O curso ainda não foi fechado");
        }else if(i == 3){
            System.out.println("Não foi possível remover, ja estixtem alunos matribulados na matéria");
        }
    }
    public static void menu(Usuario user, Scanner sc) {
        try {
            ArquivoCurso arqCurso = new ArquivoCurso();

            int s = 0;
            String aux = "";
            char opcao;

            while (s == 0) {
                System.out.println("EntrePares 1.0");
                System.out.println("--------------");
                System.out.println("> Início > Meus Cursos");
                System.out.println();
                

                Curso[] cursos = arqCurso.readCursosPorUsuario(user.getIdUsuario());
                if(cursos.length != 0)
                    System.out.println("CURSOS");
                else
                    System.out.println("Nenhum curso no momento");
                Arrays.sort(cursos, (a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));

                for (int i = 0; i < cursos.length; i++) {
                    System.out.println("(" + (i + 1) + ") " + cursos[i].getNome() + " - " + cursos[i].getInicio());
                }

                System.out.println();
                System.out.println("(A) Novo curso");
                System.out.println("(R) Retornar ao menu anterior");

                System.out.println();
                System.out.print("Opção: ");

                aux = sc.nextLine();
                opcao = Character.toUpperCase(aux.charAt(0));

                if (opcao == 'R') {
                    s = 1;
                    break;
                }

                else if (opcao == 'A') {
                    System.out.print("Digite o nome do curso: ");
                    String nome = sc.nextLine();

                    System.out.print("Digite a data de início (AAAA-MM-DD): ");
                    String dataStr = sc.nextLine();
                    LocalDate inicio = LocalDate.parse(dataStr);

                    System.out.print("Digite a descrição do curso: ");
                    String descricao = sc.nextLine();

                    int estado = 0;

                    Curso novo = new Curso(user.getIdUsuario(), nome, descricao, inicio, estado);

                    arqCurso.create(novo);

                    System.out.println("Curso criado com sucesso.");
                }

                else if (Character.isDigit(opcao)) {

                    int index = Character.getNumericValue(opcao) - 1;

                    if (index >= 0 && index < cursos.length) {
                        Curso c = cursos[index];

                        int voltar = 0;
                        System.out.println("EntrePares 1.0");
                            System.out.println("--------------");
                            System.out.println("> Início > Meus Cursos > " + c.getNome());
                            System.out.println();

                            System.out.println("CÓDIGO........: " + c.getCodigo());
                            System.out.println("NOME..........: " + c.getNome());
                            System.out.println("DESCRIÇÃO.....: " + c.getDescricao());
                            System.out.println("DATA DE INÍCIO: " + c.getInicio());
                            System.out.println();
                        if (c.getEstado() == 0)
                            System.out.println("Este curso está aberto para inscrições!");
                        else if (c.getEstado() == 1)
                            System.out.println("Este curso está ativo, mas sem novas inscrições.");
                        else if (c.getEstado() == 2)
                            System.out.println("Este curso já foi concluído.");
                        else
                            System.out.println("Este curso foi cancelado.");
                        int m = 4;
                        while (voltar == 0) {



                            System.out.println();
                            System.out.println("(A) Gerenciar inscritos no curso");
                            System.out.println("(B) Corrigir dados do curso");
                            System.out.println("(C) Encerrar inscrições");
                            System.out.println("(D) Concluir curso");
                            System.out.println("(E) Cancelar curso");
                            System.out.println("(F) Deletar curso");
                            System.out.println("(R) Retornar");

                            System.out.print("Opção: ");
                            String aux2 = sc.nextLine();
                            char op2 = Character.toUpperCase(aux2.charAt(0));

                            if (op2 == 'R') {
                                voltar = 1;
                            } else if (op2 == 'A') {
                                DetalheInscricaoCurso.menu(c, user, sc);
                            }
                            else if (op2 == 'B') {
                                if(c.getEstado() != 2){
                                    sc.nextLine();

                                    System.out.print("Novo nome: ");
                                    c.setNome(sc.nextLine());

                                    System.out.print("Nova descrição: ");
                                    c.setDescricao(sc.nextLine());

                                    arqCurso.update(c);
                                }else{
                                    m = 0;
                                }
                            }

                            else if (op2 == 'C') {
                                if(c.getEstado() < 1){
                                    c.setEstado(1);
                                    arqCurso.update(c);
                                }else    
                                    m = 1;
                            }

                            else if (op2 == 'D') {
                                if(c.getEstado() == 1){
                                    c.setEstado(2);
                                    arqCurso.update(c);
                                }else{
                                    m = 2;
                                }
                            }

                            else if (op2 == 'E') {
                                c.setEstado(3);
                                if(!c.hasAluno())
                                    arqCurso.update(c);
                                else
                                    m = 3;
                            } else if (op2 == 'F') {
                                if(!c.hasAluno())
                                    arqCurso.delete(c.getID());
                                else
                                    m = 3;
                                voltar = 1;
                            }
                            System.out.println("EntrePares 1.0");
                            System.out.println("--------------");
                            System.out.println("> Início > Meus Cursos > " + c.getNome());
                            System.out.println();

                            System.out.println("CÓDIGO........: " + c.getCodigo());
                            System.out.println("NOME..........: " + c.getNome());
                            System.out.println("DESCRIÇÃO.....: " + c.getDescricao());
                            System.out.println("DATA DE INÍCIO: " + c.getInicio());
                            System.out.println();
                            if (c.getEstado() == 0)
                                System.out.println("Este curso está aberto para inscrições!");
                            else if (c.getEstado() == 1)
                                System.out.println("Este curso está ativo, mas sem novas inscrições.");
                            else if (c.getEstado() == 2)
                                System.out.println("Este curso foi concluído.");
                            else
                                System.out.println("o curso foi cancelado.");
                            mensagem(m);
                            m = 4;
                        }
                    }
                }

                else {
                    System.out.println("Opção inválida.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}