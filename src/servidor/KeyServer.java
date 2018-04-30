package servidor;

/**
 *
 * @author lucas
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A TCP server that runs on port 9090.  When a client connects, it
 * sends the client the current date and time, then closes the
 * connection with that client.  Arguably just about the simplest
 * server you can write.
 */
public class KeyServer {
    
    /**
     * Executa o server.
     * FIca escutando a porta 9090
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        int clientes = 0;
        ServerSocket listener = new ServerSocket(9090);
        
        System.out.print("Executando o servidor de chaves \nClientes:" +clientes);
        
        try {
            while (true) {
                new Gerenciador(listener.accept(), clientes++).start();
            
            }
            
        }
        finally {
            listener.close();

        }
        
    }

    private static class Gerenciador extends Thread{
        private final Socket socket;
        private final int clientNumber;

        public Gerenciador(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            
            System.out.print
                ("Nova conexão \n"
                +"Cliente# " + clientNumber + " em " + socket);
        }
        
        //Para esse cliente/thread faça:
        @Override
        public void run(){
            IDChave chave = new IDChave();
            
            try{
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                //Mensagem de boas vindas.
                out.println("Olá, você é o cliente #" + clientNumber + ".");
                out.println("Informe seu ID e sua chave pública no formato"
                           +"ID-CHAVE\n");
            
                //Receber a mensagem do cliente
                while (true) {
                    String[] dados;
                    String input = in.readLine();
                    
                    dados = input.split("-");
                    
                    setID(dados[0]);
                    this.ID = dados[0];
                    
                    
                    if (input == null || input.equals(".")) {
                        break;
                    }
                    out.println(input.toUpperCase());
                }
                
            }
            catch(IOException e){
                System.out.print("Erro ao lidar com o cliente "+clientNumber);
            
            }
            finally {
                try {
                    socket.close();
                
                } 
                catch (IOException e) {
                    System.out.print("Erro ao fechar socket, que que houve?");
                
                }    
                System.out.print("Conexão com o cliente# " + clientNumber + " fechada");
            
            }
            
        }
    
    }

}