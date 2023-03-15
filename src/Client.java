/**
 * Client.class класс выполняющий обработку действий с клиентами
 * Класс имеет один конструктор и два метода:
 * Client() - конструктор в котором инициализируется клиентский поток
 * run() - переопределенный метод выполнения потока, содержит основную механику работы склиентской части.
 * receiveMessageFromAll() - метод получения и вывода сообщений пользователю.
 */

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

    /**
     * Конструктор Client получает данные для клиентской части, и инициализирует поток
     */
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

    /* Переопределенный метод выполнения потока */
    public void run() {
        try {
            InputStream is = socket.getInputStream(); // получаем поток ввода
            OutputStream os = socket.getOutputStream();  // получаем поток вывода

            in = new Scanner(is); // объявляем объект сканера получающий значения из входного стрим потока
            out = new PrintStream(os);  // объявляем объект вывода в стрим-поток получающий значения от исходящего стрим потока

            out.println("Welcome to " + chatServerName);   // Выводим приветствие
            out.println("enter \"@who in chat\" to look who's in the chat");  // Информируем о возможности посмотреть кто сейчас в чате
            out.print("Please enter your name:");
            userName = in.next();   // просим пользователя назвать себя
            server.sendMessageToAll("User " + userName + " is connected!");  // уведомляем о входе пользователя в чат
            server.usersList.add(userName);  // добавляем пользователя в список активных пользователей
            String input = in.nextLine();    // Вводим первое значение
            while (!input.equals("bye")) {      // Цикл пока в чате не написано bye
                if (!input.equals("") && !input.equals("@who in chat")) {   // Если полученная строка неравна пустоте либо неравна who in chat
                    server.sendMessageToAll("User " + userName + " say: " + input); // Сообщаем всем переданные сообщения пользователей
                }else if (input.equals("@who in chat")) {out.println(server.usersList);} // Иначе если строка равна who in chat
                input = in.nextLine();  // Ожидаем следующего ввода
            }
            server.sendMessageToAll("User " + userName + " is out!"); // уведомляем о выходе пользователя из чата
            server.usersList.remove(userName); // удаляем пользователя из списка активных пользователей
            socket.close(); // Закрываем сокет
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Принимает сообщение для пользователя от сервера */
    public void receiveMessageFromAll(String outChatMessage){
        out.println(outChatMessage); // выводит в чат пользователя сообщение других пользователей
    }
}
