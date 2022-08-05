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
            // �������� ������ ����� � ������
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // ������� ������� �������� ����� � ������
            in = new Scanner(is);
            out = new PrintStream(os);

            out.println("Welcome to " + chatServerName);   // ������� �����������
            out.println("enter \"@who in chat\" to look who's in the chat");  // ����������� � ����������� ���������� ��� ������ � ����
            out.print("Please enter your name:");
            userName = in.next();   // ������ ������������ ������� ����
            server.sendMessageToAll("User " + userName + " is connected!");  // ���������� � ����� ������������ � ���
            server.usersList.add(userName);  // ��������� ������������ � ������ �������� �������������
            String input = in.nextLine();
            while (!input.equals("bye")) {
                if (!input.equals("") && !input.equals("@who in chat")) {
                    server.sendMessageToAll("User " + userName + " say: " + input);
                }else if (input.equals("@who in chat")) {out.println(server.usersList);}
                input = in.nextLine();
            }
            server.sendMessageToAll("User " + userName + " is out!"); // ���������� � ������ ������������ �� ����
            server.usersList.remove(userName); // ������� ������������ �� ������ �������� �������������
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessageFromAll(String outChatMessage){
        out.println(outChatMessage);
    }
}
