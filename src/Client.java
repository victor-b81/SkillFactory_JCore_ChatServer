import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{
    Socket socket;
    ChatServer server;
    private String chatServerName = null;
    Scanner in;
    PrintStream out;
    String userName;

    public Client(Socket socket, ChatServer server, String chatServerName){
        if (chatServerName == null){
            this.chatServerName = "Unnamed Chat";
        }else{
            this.chatServerName = chatServerName;
        }
        this.server = server;
        this.socket = socket;
        new Thread(this).start();
    }

    public void run() {
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // создаем удобные средства ввода и вывода
            in = new Scanner(is);
            out = new PrintStream(os);

            out.println("Welcome to " + chatServerName);   // Выводим приветствие
            out.println("enter \"@who in chat\" to look who's in the chat");  // Информируем о возможности посмотреть кто сейчас в чате
            out.print("Please enter your name:");
            userName = in.next();   // просим пользователя назвать себя
            server.sendMessageToAll("User " + userName + " is connected!");  // уведомляем о входе пользователя в чат
            server.usersList.add(userName);  // добавляем пользователя в список активных пользователей
            String input = in.nextLine();
            while (!input.equals("bye")) {
                if (!input.equals("") && !input.equals("@who in chat")) {
                    server.sendMessageToAll("User " + userName + " say: " + input);
                }else if (input.equals("@who in chat")) {out.println(server.usersList);}
                input = in.nextLine();
            }
            server.sendMessageToAll("User " + userName + " is out!"); // уведомляем о выходе пользователя из чата
            server.usersList.remove(userName); // удаляем пользователя из списка активных пользователей
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessageFromAll(String outChatMessage){
        out.println(outChatMessage);
    }
}
