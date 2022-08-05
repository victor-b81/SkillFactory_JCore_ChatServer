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
    LinkedList<String> usersList = new LinkedList<>();  // ������ ������������ ������������� ����

    public ChatServer(int chatServerPort, String chatServerName) throws IOException {
        this.ChatServerPort = chatServerPort;
        this.chatServerName = chatServerName;
        // ������� ��������� ����� �� ����� ChatServerPort, �� ��������� 1234
        serverSocket = new ServerSocket(ChatServerPort);
    }

    public ChatServer() throws IOException {
        // ������� ��������� ����� �� ����� �� ��������� 1234
        serverSocket = new ServerSocket(ChatServerPort);
    }

    @Override
    public void run () {

        while(true) {
            try {
                System.out.println("Chat server is start!");
                System.out.println("Waiting for clients...");

                // ���� ������� � �������� � ��� �����������.
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                // ������� ������� �� ����� �������, �������� ��� ��� �������
                clients.add(new Client(socket, this, chatServerName));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessageToAll(String inputChatMessage){
        //���������� ���������, ���� � ����
        for (Client client : clients){
            client.receiveMessageFromAll(inputChatMessage);
        }
    }
}
