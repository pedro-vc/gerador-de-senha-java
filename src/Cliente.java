import java.io.*;
import java.net.*;
/*
 *  Cliente TCP - versão simplificada para o laboratório de redes
 *
 *
 *  Este é um programa cliente simples que ler uma string via teclado e
 *  envia o texto como requisição para um servidor.
 *
 */

public class Cliente {
    public static void main(String[] args) {
        java.util.Scanner teclado = new java.util.Scanner(System.in);
        String enderecoServidor;
        int portaServidor;

        System.out.print("Endereço do servidor: ");
        enderecoServidor = teclado.next();

        System.out.print("Porta do servidor: ");
        portaServidor = teclado.nextInt();

        try {
            Socket commsock = new Socket(enderecoServidor, portaServidor);
            PrintStream output = new PrintStream(commsock.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(commsock.getInputStream()));

            String fromServer, fromUser;
            do {
                System.out.print("Mensagem a enviar para o servidor: ");
                fromUser = teclado.next();
                if (fromUser != null) {
                    output.println(fromUser);
                    fromServer = input.readLine();
                    System.out.println("Resposta do servidor: " + fromServer);
                    if (fromServer.equals("goodbye")) break;
                }
            } while (fromUser != null);

            output.close();
            input.close();
            commsock.close();
        } catch (UnknownHostException e) {
            System.err.println("Erro ao conectar ao servidor: " + e);
        } catch (Exception e) {
            System.err.println("Erro:  " + e);
        }
    }
}
