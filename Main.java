import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

// Observer Pattern: Interface to notify users of new messages
interface ChatObserver {
    void update(String message, String roomID);
}

// Concrete Observer: User
class User implements ChatObserver {
    private String username;

    public User(String username) {
        this.username = username;
    }

    @Override
    public void update(String message, String roomID) {
        System.out.println(username + " received a new message in " + roomID + ": " + message);
    }

    public String getUsername() {
        return username;
    }
}

// Subject Interface for Chat Room
interface ChatSubject {
    void addUser(User user);
    void removeUser(User user);
    void notifyUsers(String message);
}

// Singleton Pattern: Chat Room Manager to manage state of all rooms
class ChatRoomManager {
    private static ChatRoomManager instance;
    private Map<String, ChatRoom> rooms;

    private ChatRoomManager() {
        rooms = new HashMap<>();
    }

    public static ChatRoomManager getInstance() {
        if (instance == null) {
            instance = new ChatRoomManager();
        }
        return instance;
    }

    public ChatRoom getChatRoom(String roomID, MessageProtocolAdapter adapter) {
        if (!rooms.containsKey(roomID)) {
            rooms.put(roomID, new ChatRoom(roomID, adapter));
        }
        return rooms.get(roomID);
    }
}

// Adapter Pattern: Interface for communication protocols
interface MessageProtocolAdapter {
    void sendMessage(String message, String roomID);
    String receiveMessage(String roomID);
}

// Concrete Adapter: WebSocket Protocol
class WebSocketAdapter implements MessageProtocolAdapter {
    @Override
    public void sendMessage(String message, String roomID) {
        System.out.println("Sending via WebSocket: [" + roomID + "] " + message);
    }

    @Override
    public String receiveMessage(String roomID) {
        System.out.println("Receiving via WebSocket from room: " + roomID);
        return "WebSocket message from " + roomID;
    }
}

// Concrete Adapter: HTTP Protocol
class HttpAdapter implements MessageProtocolAdapter {
    @Override
    public void sendMessage(String message, String roomID) {
        System.out.println("Sending via HTTP: [" + roomID + "] " + message);
    }

    @Override
    public String receiveMessage(String roomID) {
        System.out.println("Receiving via HTTP from room: " + roomID);
        return "HTTP message from " + roomID;
    }
}

// Chat Room Class implementing the Observer pattern
class ChatRoom implements ChatSubject {
    private String roomID;
    private MessageProtocolAdapter protocolAdapter;
    private List<User> users;

    public ChatRoom(String roomID, MessageProtocolAdapter protocolAdapter) {
        this.roomID = roomID;
        this.protocolAdapter = protocolAdapter;
        this.users = new ArrayList<>();
    }

    @Override
    public void addUser(User user) {
        users.add(user);
        System.out.println(user.getUsername() + " has joined " + roomID);
    }

    @Override
    public void removeUser(User user) {
        users.remove(user);
        System.out.println(user.getUsername() + " has left " + roomID);
    }

    @Override
    public void notifyUsers(String message) {
        for (User user : users) {
            user.update(message, roomID);
        }
    }

    public void sendMessage(String message) {
        protocolAdapter.sendMessage(message, roomID);
        notifyUsers(message);
    }

    public void receiveMessage() {
        String message = protocolAdapter.receiveMessage(roomID);
        System.out.println("Message Received in " + roomID + ": " + message);
    }
}

// Command Pattern: Command interface for sending messages
interface Command {
    void execute();
}

// Concrete Command: SendMessageCommand
class SendMessageCommand implements Command {
    private ChatRoom chatRoom;
    private String message;

    public SendMessageCommand(ChatRoom chatRoom, String message) {
        this.chatRoom = chatRoom;
        this.message = message;
    }

    @Override
    public void execute() {
        chatRoom.sendMessage(message);
    }
}

// Strategy Pattern: Message Formatter
interface MessageFormatter {
    String format(String message);
}

// Concrete Formatter: PlainTextFormatter
class PlainTextFormatter implements MessageFormatter {
    @Override
    public String format(String message) {
        return message;
    }
}

// Concrete Formatter: HtmlFormatter
class HtmlFormatter implements MessageFormatter {
    @Override
    public String format(String message) {
        return "<html><body>" + message + "</body></html>";
    }
}

// Factory Method Pattern: MessageFactory
class MessageFactory {
    public static Command createSendMessageCommand(ChatRoom chatRoom, String message) {
        return new SendMessageCommand(chatRoom, message);
    }
}

// Prototype Pattern: Cloning Message
class MessagePrototype {
    private String content;

    public MessagePrototype(String content) {
        this.content = content;
    }

    public MessagePrototype clone() {
        return new MessagePrototype(content);
    }

    public String getContent() {
        return content;
    }
}

// Decorator Pattern: MessageDecorator
abstract class MessageDecorator implements Command {
    protected Command command;

    public MessageDecorator(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {
        command.execute();
    }
}

// Concrete Decorator: LoggingDecorator
class LoggingDecorator extends MessageDecorator {
    public LoggingDecorator(Command command) {
        super(command);
    }

    @Override
    public void execute() {
        System.out.println("Logging message...");
        super.execute();
    }
}

// Composite Pattern: GroupChatRoom
class GroupChatRoom implements ChatSubject {
    private List<ChatSubject> chatRooms = new ArrayList<>();

    public void addChatRoom(ChatSubject chatRoom) {
        chatRooms.add(chatRoom);
    }

    public void removeChatRoom(ChatSubject chatRoom) {
        chatRooms.remove(chatRoom);
    }

    @Override
    public void addUser(User user) {
        for (ChatSubject chatRoom : chatRooms) {
            chatRoom.addUser(user);
        }
    }

    @Override
    public void removeUser(User user) {
        for (ChatSubject chatRoom : chatRooms) {
            chatRoom.removeUser(user);
        }
    }

    @Override
    public void notifyUsers(String message) {
        for (ChatSubject chatRoom : chatRooms) {
            chatRoom.notifyUsers(message);
        }
    }
}

// Main Class for running the application
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ChatRoomManager manager = ChatRoomManager.getInstance();

        while (true) {
            System.out.println("Enter command (create/join/send/exit): ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                logger.info("Exiting the application.");
                break;
            }

            try {
                if (command.equalsIgnoreCase("create")) {
                    System.out.println("Enter room ID: ");
                    String roomID = scanner.nextLine();
                    if (roomID.isEmpty()) {
                        logger.warning("Room ID cannot be empty.");
                        System.out.println("Room ID cannot be empty. Please try again.");
                        continue;
                    }
                    System.out.println("Choose protocol (WebSocket/HTTP): ");
                    String protocol = scanner.nextLine();
                    MessageProtocolAdapter adapter = protocol.equalsIgnoreCase("WebSocket") ? new WebSocketAdapter() : new HttpAdapter();
                    manager.getChatRoom(roomID, adapter);
                    System.out.println("Chat room " + roomID + " created.");
                } else if (command.equalsIgnoreCase("join")) {
                    System.out.println("Enter room ID: ");
                    String roomID = scanner.nextLine();
                    if (roomID.isEmpty()) {
                        logger.warning("Room ID cannot be empty.");
                        System.out.println("Room ID cannot be empty. Please try again.");
                        continue;
                    }
                    System.out.println("Enter your username: ");
                    String username = scanner.nextLine();
                    if (username.isEmpty()) {
                        logger.warning("Username cannot be empty.");
                        System.out.println("Username cannot be empty. Please try again.");
                        continue;
                    }
                    User user = new User(username);
                    ChatRoom room = manager.getChatRoom(roomID, new WebSocketAdapter());
                    room.addUser(user);
                } else if (command.equalsIgnoreCase("send")) {
                    System.out.println("Enter room ID: ");
                    String roomID = scanner.nextLine();
                    if (roomID.isEmpty()) {
                        logger.warning("Room ID cannot be empty.");
                        System.out.println("Room ID cannot be empty. Please try again.");
                        continue;
                    }
                    System.out.println("Enter your message: ");
                    String message = scanner.nextLine();
                    if (message.isEmpty()) {
                        logger.warning("Message cannot be empty.");
                        System.out.println("Message cannot be empty. Please try again.");
                        continue;
                    }
                    ChatRoom room = manager.getChatRoom(roomID, new WebSocketAdapter());
                    
                    // Using Command Pattern
                    Command sendMessageCommand = MessageFactory.createSendMessageCommand(room, message);
                    // Using Decorator Pattern
                    Command loggingCommand = new LoggingDecorator(sendMessageCommand);
                    loggingCommand.execute();
                } else {
                    logger.warning("Invalid command: " + command);
                    System.out.println("Invalid command.");
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "An error occurred: ", e);
            }
        }

        scanner.close();
    }
}
