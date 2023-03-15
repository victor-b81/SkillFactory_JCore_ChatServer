/**
 * ChatServer.java класс выполнения чат сервера.
 * Класс имеет один конструктор и два метода:
 * ChatServer()  - конструктор имеет перегрузку с приемом имени и порта чата. В ином случае используются данные по умолчанию.
 * run() - переопределенный метод выполнения потока, содержит основную механику работы сервера.
 * sendMessageToAll() - метод отправки сообщений всем.
 */


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

    /* Перегруженный конструктор с полученными значениями порта и имени */
    public ChatServer(int chatServerPort, String chatServerName) throws IOException {
        this.ChatServerPort = chatServerPort;
        this.chatServerName = chatServerName;
        serverSocket = new ServerSocket(ChatServerPort);         // создаем серверный сокет на переданном значении порта ChatServerPort
    }

    /* Конструктор со значениями по умолчанию */
    public ChatServer() throws IOException {
        serverSocket = new ServerSocket(ChatServerPort);         // создаем серверный сокет на порту по умолчанию 1234
    }

    /* Переопределенный метод выполнения потока */
    @Override
    public void run () {

        while(true) {
            try {
                // Выводим техническую информацию в консоль.
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

    /* Метод отправки сообщений всем пользователям */
    public void sendMessageToAll(String inputChatMessage){
        for (Client client : clients){
            client.receiveMessageFromAll(inputChatMessage);  // Отправляем сообщение, всем в чате
        }
    }
}