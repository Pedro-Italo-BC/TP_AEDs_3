package menus;
import entidades.Usuario.ArquivoUsuario;
import entidades.Usuario.Usuario;
import java.util.Scanner;

public class Logado {
    public static void menu(Usuario user, Scanner sc) throws Exception {
        ArquivoUsuario arqUser = new ArquivoUsuario();

        int s = 0;
        String aux = "";
        char opcao;

        while (s == 0) {
            System.out.println("TP01 Aeds3");
            System.out.println("----------");
            System.out.println("> Inicio");
            System.out.println();
            System.out.println("(A) Meus dados");
            System.out.println("(B) Meus cursos");
            System.out.println("(C) Minhas inscrições");
            System.out.println("(D) Deletar conta");
            System.out.println();
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Opção: ");
            System.out.println();

            aux = "";
            aux = sc.nextLine();
            if(aux.length() == 0){
                System.out.println("Digite algo");
            }
            while (aux.length() == 0){
                aux = sc.nextLine();
                System.out.println("Digite algo");
            } 
            opcao = Character.toUpperCase(aux.charAt(0));

            // sair
            if (opcao == 'S') {
                s = 1;
                break;
            }

            // mostrar os dados
            else if (opcao == 'A') {
                System.out.println("TP01 Aeds3");
                System.out.println("----------");
                System.out.println("> Inicio > Meus Dados");
                System.out.println();
                System.out.println("(A) Editar informações");
                System.out.println(user.getInativo() ? "(B) Ativar conta" : "(B) Inativar conta");
                System.out.println("Meus dados:");
                System.out.println(user.toString());
                System.out.println();

                System.out.println("Pressione qualquer tecla para voltar...");
                char op2 = sc.nextLine().charAt(0);

                if(op2 == 'A') {
                System.out.print("Digite seu nome: "); //NOME
                user.setNome(sc.nextLine());

                System.out.print("Digite seu email: "); //EMAIL
                user.setEmail(sc.nextLine());


                System.out.print("Digite sua senha: "); //SENHA 1
                String senha1 = sc.nextLine();

                System.out.print("Confirme sua senha: "); //SENHA 2
                String senha2 = sc.nextLine();

                while (!senha1.equals(senha2)) {  //VERIFIÇÃO DE IGUALDADE DA SENHA
                    System.out.println("As senhas devem ser iguais. Digite novamente");
                    System.out.print("Digite sua senha: ");
                    senha1 = sc.nextLine();
                    System.out.print("Digite sua senha novamente: ");
                    senha2 = sc.nextLine();
                }

                user.setHashSenha(Usuario.gerarHash(senha1));

                System.out.print("Digite uma pergunta secreta: "); //PERGUNTA SECRETA
                user.setPerguntaSecreta(sc.nextLine());
                

                System.out.print("Digite a resposta da sua pergunta secreta\n"); //RESPOSTA DA PERGUNTA SECRETA
                System.out.print("Observação: Você precisará dela caso esqueça sua senha!:");
                user.setHashRespostaSecreta(Usuario.gerarHash(sc.nextLine()));


                System.out.print("Deseja confirmar sua edição? S/N: ");
                char desejo = Character.toUpperCase(sc.nextLine().charAt(0));
                while(desejo != 'S' && desejo != 'N'){
                    System.out.println("Opção inválida");
                    System.out.print("Deseja confirmar seu cadastro? S/N: ");
                    desejo = Character.toUpperCase(sc.nextLine().charAt(0));
                }
                

                if (desejo == 'S') {
                        arqUser.update(user);
                        System.out.println("Edição realizado com sucesso!");
                }
            } else if(op2 == 'B') {
                user.setInativo(user.getInativo());
                arqUser.update(user);
            }  
            
            }

            // meus cursos
            else if (opcao == 'B') {
                Menu_curso.menu(user, sc); // já preparando integração
            }

            // minhas inscrições
            else if (opcao == 'C') {
                Inscricao.menu(user, sc); //vai para o menu de inscrições
            }

            else if(opcao == 'D') {
                System.out.println("Tem certeza que deseja deletar sua conta? [S/n]");
                String resp = sc.nextLine();

                if(resp.toLowerCase().charAt(0) == 's') {
                    arqUser.delete(user.getID());
                }
                s=1;
            } else {
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