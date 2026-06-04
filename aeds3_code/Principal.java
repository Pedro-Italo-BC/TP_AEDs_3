import entidades.Usuario.*;
import java.util.*;
import menus.Logado;

public class Principal {
    public static void main(String[] args)  throws Exception{
        
        ArquivoUsuario arqUsuario = new ArquivoUsuario();

        Scanner sc = new Scanner(System.in);
        String aux = "";
        char opcao = 'O';

        while (opcao != 'S') {
            System.out.println("TP01 Aeds3");
            System.out.println("----------");
            System.out.println();
            System.out.println("(A) Login");
            System.out.println("(B) Novo Usuário");
            System.out.println("(C) Recuperar Senha");
            System.out.println();
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Opção: ");

            aux = sc.nextLine();
            opcao = Character.toUpperCase(aux.charAt(0));

            // sair
            if (opcao == 'S') {
                System.out.println("Tem certeza que deseja sair? S/N");
                char desejo = Character.toUpperCase(sc.nextLine().charAt(0));
                while(desejo != 'S' && desejo != 'N'){
                    System.out.println("Opção Inválida");
                    System.out.println("Tem certeza que deseja sair? S/N");
                    desejo = Character.toUpperCase(sc.nextLine().charAt(0));
                }
                if(desejo == 'N'){
                    System.out.println("Cancelado");
                    
                    opcao = 'P';
                }
            }

            // logar
            else if (opcao == 'A') {
                System.out.print("Digite seu email: ");
                String email = sc.nextLine();
                
                System.out.print("Digite sua senha: ");
                String senha = sc.nextLine();
                System.out.println();

                Usuario u = arqUsuario.login(email, senha);

                if (u != null) {
                    // próximo menu
                    Logado.menu(u, sc);
                } else {
                    System.out.println("Email ou senha inválidos.");
                }

            }

            // cadastrar
            else if (opcao == 'B') {
                System.out.print("Digite seu nome: "); //NOME
                String nome = sc.nextLine();

                System.out.print("Digite seu email: "); //EMAIL
                String email = sc.nextLine();

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

                System.out.print("Digite uma pergunta secreta: "); //PERGUNTA SECRETA
                String perguntaSecreta = sc.nextLine();
                

                System.out.print("Digite a resposta da sua pergunta secreta\n"); //RESPOSTA DA PERGUNTA SECRETA
                System.out.print("Observação: Você precisará dela caso esqueça sua senha!:");
                String respostaSecreta = sc.nextLine();
                

                System.out.print("Deseja confirmar seu cadastro? S/N: ");
                char desejo = Character.toUpperCase(sc.nextLine().charAt(0));
                while(desejo != 'S' && desejo != 'N'){
                    
                    System.out.println("Opção inválida");
                    System.out.print("Deseja confirmar seu cadastro? S/N: ");
                    desejo = Character.toUpperCase(sc.nextLine().charAt(0));
                }
                

                if (desejo == 'S') {
                    Usuario novo = new Usuario(
                        0,
                        nome,
                        email,
                        Usuario.gerarHash(senha1),
                        perguntaSecreta,
                        Usuario.gerarHash(respostaSecreta)
                    );
                    arqUsuario.create(novo);
                    System.out.println("Cadastro realizado com sucesso!");
                } else if (desejo == 'N') {
                    opcao = 'P';
                }

            }  else if (opcao == 'C') {
                System.out.println("Digite seu email: ");
                String email = sc.nextLine();

                Usuario usuario = arqUsuario.encontrarPorEmailUnsafe(email);
            
                if(usuario == null) {
                    System.out.println("Usuário não encontrado.");
                } else {
                    System.out.println("Pergunta secreta: ");
                    System.out.print(usuario.getPerguntaSecreta() + ": ");
                    String resposta = sc.nextLine();
                    System.out.println("");

                    if (arqUsuario.responderPergunta(usuario, resposta)) {
                        System.out.print("Digite sua nova senha: ");
                        String novaSenha = sc.nextLine();
                        System.out.println("");
                        usuario.setHashSenha(Usuario.gerarHash(novaSenha));
                        arqUsuario.update(usuario);
                        System.out.println("Senha redefinida");
                    } else {
                        System.out.println("Resposta errada");
                    }
                }
            }
            else {
                char desejo = 'P';
                while(desejo != 'N' && desejo != 'S'){
                    
                    System.out.println("Opção Inválida");
                    System.out.println("Deseja tentar novamente? S/N");
                    desejo = Character.toUpperCase(sc.nextLine().charAt(0));
                }
                if (desejo == 'N') {
                    opcao = 'S';
                }else{
                    
                }
            }
        }
    }
}
