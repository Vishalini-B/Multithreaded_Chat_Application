package ChatApp;
import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.*;

/**
 * ClientHandler - Runs in its own thread for each connected client.
 * Handles reading messages from the client and sending responses.
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    private static final DateTimeFormatter TIME_FORMAT =
        DateTimeFormatter.ofPattern("HH:mm");

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Set up input/output streams
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Step 1: Ask client for a username
            out.println("Welcome to the Chat Room!");
            out.println("Enter your username: ");

            username = in.readLine();
            if (username == null || username.trim().isEmpty()) {
                username = "User" + (int)(Math.random() * 1000);
            }
            username = username.trim();

            // Announce the new user to everyone
            String joinMsg = "[" + getTime() + "] *** " + username + " has joined the chat! ***";
            ChatServer.broadcastAll(joinMsg);
            System.out.println("[SERVER] " + username + " connected.");

            // Send available commands to the new user
            out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            out.println("Commands:");
            out.println("  /users          - List online users");
            out.println("  /msg <user> <text> - Private message");
            out.println("  /quit           - Leave the chat");
            out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            // Step 2: Main message loop - keep reading until client disconnects
            String message;
            while ((message = in.readLine()) != null) {
                message = message.trim();

                if (message.isEmpty()) continue;

                if (message.equalsIgnoreCase("/quit")) {
                    // Client wants to leave
                    break;

                } else if (message.equalsIgnoreCase("/users")) {
                    // List all online users
                    out.println("[SERVER] " + ChatServer.getOnlineUsers());

                } else if (message.startsWith("/msg ")) {
                    // Private message: /msg <username> <text>
                    handlePrivateMessage(message);

                } else {
                    // Regular broadcast message
                    String formatted = "[" + getTime() + "] " + username + ": " + message;
                    ChatServer.broadcastMessage(formatted, this);
                    out.println("[" + getTime() + "] You: " + message); // Echo back to sender
                }
            }

        } catch (IOException e) {
            System.out.println("[SERVER] Connection lost: " + (username != null ? username : "unknown"));
        } finally {
            disconnect();
        }
    }

    /**
     * Parse and send a private message.
     * Format: /msg <targetUser> <message text>
     */
    private void handlePrivateMessage(String input) {
        // Remove the /msg prefix
        String[] parts = input.substring(5).split(" ", 2);

        if (parts.length < 2) {
            out.println("[SERVER] Usage: /msg <username> <message>");
            return;
        }

        String targetUser = parts[0].trim();
        String text       = parts[1].trim();

        if (targetUser.equalsIgnoreCase(username)) {
            out.println("[SERVER] You cannot message yourself.");
            return;
        }

        String pmMessage = "[" + getTime() + "] [PM from " + username + "]: " + text;
        boolean found = ChatServer.sendPrivateMessage(targetUser, pmMessage, this);

        if (found) {
            out.println("[" + getTime() + "] [PM to " + targetUser + "]: " + text);
        } else {
            out.println("[SERVER] User '" + targetUser + "' not found or offline.");
        }
    }

    /**
     * Send a message to this client.
     */
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    /**
     * Clean up on disconnect.
     */
    private void disconnect() {
        try {
            ChatServer.removeClient(this);
            if (username != null) {
                String leaveMsg = "[" + getTime() + "] *** " + username + " has left the chat. ***";
                ChatServer.broadcastAll(leaveMsg);
                System.out.println("[SERVER] " + username + " disconnected.");
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("[SERVER] Error closing socket: " + e.getMessage());
        }
    }

    /**
     * Get current time as HH:mm string.
     */
    private String getTime() {
        return LocalTime.now().format(TIME_FORMAT);
    }

    public String getUsername() {
        return username;
    }
}