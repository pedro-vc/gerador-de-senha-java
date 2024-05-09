import java.net.*;
import java.io.*;
import java.security.SecureRandom;
import java.util.Scanner;
/*
 *  Servidor TCP - versão simplificada.
 *
 *  Este é um programa servidor simples, sem thread, para atender a um
 *  protocolo genérico implementado na classe Protocolo.
 *
 */

public class Servidor {
    private static int porta = 4444;
    private static final String versao = "1.0";
    private static final String discoPrincipal = "/";
    private static final String CARACTERES_VALIDOS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=";
    
    public static String gerarSenha(int comprimento) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(comprimento);

        for (int i = 0; i < comprimento; i++) {
            int indice = random.nextInt(CARACTERES_VALIDOS.length());
            char caractere = CARACTERES_VALIDOS.charAt(indice);
            sb.append(caractere);
        }

        return sb.toString();
    }


    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        boolean run = true;

        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Nao foi possivel escutar na porta " + porta + "." + e);
            System.out.println("Erro: " + e);
            System.exit(1);
        }

        while (run) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Falha ao aceitar conexao! Erro: " + e);
                System.exit(1);
            }

            System.out.println("Conexão estabelecida com " + clientSocket.getRemoteSocketAddress().toString());

            try {
                BufferedReader inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter outStream = new PrintWriter(clientSocket.getOutputStream(), true);
                String inputLine, outputLine = null;

                while ((inputLine = inStream.readLine()) != null) {

                    // aqui vamos tratar a requisição realizada pelo cliente

                    switch (inputLine) {
                        case"geradorSenha":
                            outputLine ="insira o comprimento desejado da senha...";

                            String senha = gerarSenha(10);
                            System.out.println("Senha gerada: " + senha);

                            outputLine = "senha: " + senha;
                            break;
                        case "getVersion":
                            outputLine = versao;
                            break;
                        case "getServerTime":
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
                            java.util.Date hora = java.util.Calendar.getInstance().getTime();
                            outputLine = sdf.format(hora);
                            break;
                        case "getFreeSpace":
                            File principal = new File(discoPrincipal);
                            long freeespace = principal.getFreeSpace();
                            outputLine = "Espaço disponível = " + freeespace;
                            break;
                        case "end":
                            outputLine = "goodbye";
                            break;
                        default:
                            outputLine = "mensagem desconhecida!";
                    }
                    //aqui vamos enviar a resposta produzida para o cliente
                    outStream.println(outputLine);
                    outStream.flush();
                    if (outputLine.equals("goodbye")) break;
                }
                outStream.close();
                inStream.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Erro ao tratar as requisições! Erro:" + e);
                System.exit(1);
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Erro ao encerrar o servidor" + e);
            System.exit(1);
        }
    }
}

