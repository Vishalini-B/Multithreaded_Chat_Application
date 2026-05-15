package ChatApp;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Multithreaded Chat Server
 * Handles multiple client connections using threads.
 * Each client gets its own ClientHandler thread.
 */
public class ChatServer {

    private static final int PORT = 12345;

    // Thread-safe list of all connected clients
    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║      CHAT SERVER STARTED         ║");
        System.out.println("║      Listening on port " + PORT + "    ║");
        System.out.println("╚══════════════════════════════════╝");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Wait for a new client to connect
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] New connection from: " + clientSocket.getInetAddress());

                // Create a handler thread for this client
                ClientHandler handler = new ClientHandler(clientSocket);
                clients.add(handler);

                // Start the thread
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("[SERVER ERROR] " + e.getMessage());
        }
    }

    /**
     * Broadcast a message to ALL connected clients.
     */
    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Broadcast a message to ALL clients including sender.
     */
    public static void broadcastAll(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    /**
     * Send a private message to a specific user by username.
     * Returns true if user was found, false otherwise.
     */
    public static boolean sendPrivateMessage(String targetUsername, String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client.getUsername() != null &&
                client.getUsername().equalsIgnoreCase(targetUsername)) {
                client.sendMessage(message);
                return true;
            }
        }
        return false;
    }

    /**
     * Get list of all currently online usernames.
     */
    public static String getOnlineUsers() {
        StringBuilder sb = new StringBuilder("Online users: ");
        List<String> usernames = new ArrayList<>();
        for (ClientHandler client : clients) {
            if (client.getUsername() != null) {
                usernames.add(client.getUsername());
            }
        }
        if (usernames.isEmpty()) {
            return "No users online.";
        }
        sb.append(String.join(", ", usernames));
        return sb.toString();
    }

    /**
     * Remove a disconnected client from the list.
     */
    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}