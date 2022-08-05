import java.io.IOException;
import java.util.Scanner;

public class Main {
    static int ChatServerPort;
    static String chatServerName;
    public static void main(String[] args) throws IOException {
        Scanner keyScanner = new Scanner(System.in);
        System.out.print("Please set the port number:");
        ChatServerPort = keyScanner.nextInt(); //Просим указать Порт сервера перед его созданием
        System.out.print("Please set the Chat name:");
        chatServerName = keyScanner.next(); //Просим указать Имя сервера перед его созданием

        Thread threadChatServer = new Thread(new ChatServer(ChatServerPort, chatServerName));
        threadChatServer.start();
    }
}