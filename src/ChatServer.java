import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChatServer extends Thread{
    private int ChatServerPort = 1234;
    private String chatServerName = null;
    private ServerSocket serverSocket = null;
    ArrayList<Client> clients = new ArrayList<>();
    LinkedList<String> usersList = new LinkedList<>();  // список подключенных пользователей чата

    public ChatServer(int chatServerPort, String chatServerName) throws IOException {
        this.ChatServerPort = chatServerPort;
        this.chatServerName = chatServerName;
        // создаем серверный сокет на порту ChatServerPort, по умолчанию 1234
        serverSocket = new ServerSocket(ChatServerPort);
    }

    public ChatServer() throws IOException {
        // создаем серверный сокет на порту по умолчанию 1234
        serverSocket = new ServerSocket(ChatServerPort);
    }

    @Override
    public void run () {

        while(true) {
            try {
                System.out.println("Chat server is start!");
                System.out.println("Waiting for clients...");

                // ждем клиента и сообщаем о его подключении.
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                // создаем клиента на своей стороне, сообщаем ему имя сервера
                clients.add(new Client(socket, this, chatServerName));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessageToAll(String inputChatMessage){
        //Отправляем сообщение, всем в чате
        for (Client client : clients){
            client.receiveMessageFromAll(inputChatMessage);
        }
    }
}
