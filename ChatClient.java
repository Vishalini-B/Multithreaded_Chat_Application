package ChatApp;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * ChatClient - Connects to the ChatServer.
 * Uses TWO threads:
 *   1. Main thread   → reads user keyboard input and sends to server
 *   2. Receiver thread → listens for incoming messages from server and prints them
 */
public class ChatClient {

    private static final String SERVER_HOST = "localhost";
    private static final int    SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Connecting to chat server at " + SERVER_HOST + ":" + SERVER_PORT + "...");

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            System.out.println("Connected! Waiting for server...\n");

            PrintWriter  out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // --- Thread 1: Receiver ---
            // Runs in background, continuously prints messages from server
            Thread receiverThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            receiverThread.setDaemon(true); // Dies when main thread exits
            receiverThread.start();

            // --- Thread 2: Sender (Main Thread) ---
            // Reads keyboard input and sends to server
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) continue;

                out.println(input); // Send to server

                if (input.equalsIgnoreCase("/quit")) {
                    System.out.println("You left the chat. Goodbye!");
                    break;
                }
            }

        } catch (ConnectException e) {
            System.err.println("Could not connect to server. Is ChatServer running?");
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}