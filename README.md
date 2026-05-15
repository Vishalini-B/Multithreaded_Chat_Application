# Multithreaded_Chat_Application

*COMPANY *: CODTECH IT SOLUTIONS

*NAME *: VISHALINI B

*INTERN ID *: CTIS8696

*DOMAIN *: Java Programming

*DURATION *: 4 WEEEKS

*MENTOR *: NEELA SANTOSH

# 💬 Multithreaded Chat Application — Java

A real-time console-based chat application built using Java Sockets and
Multithreading. Multiple clients can connect to a central server and
communicate simultaneously in real time. This project demonstrates
client-server architecture, socket programming, and thread management
using core Java libraries.

---

## 📌 Overview

This project is built as part of a **Multithreaded Chat Application** task
using the Java programming language. The application consists of a
`ChatServer` that listens for incoming connections and a `ChatClient` that
connects to the server. Each connected client is handled by a dedicated
`ClientHandler` thread, allowing multiple users to chat simultaneously
without blocking each other.

Multithreading and socket programming are fundamental skills in modern
backend development. This project demonstrates both concepts in a clear,
beginner-friendly way using only built-in Java libraries — no external
dependencies required.

---

## 👩‍💻 Role

**Role: Java Developer**

Responsibilities undertaken in this project:

- Designed and implemented a multithreaded server using `ServerSocket` and `Thread`
- Built a `ClientHandler` class implementing `Runnable` to manage each client independently
- Implemented real-time broadcast messaging across all connected clients
- Added private messaging (`/msg`) and online user listing (`/users`) features
- Used `CopyOnWriteArrayList` for thread-safe management of connected clients
- Built a two-threaded client — one thread for sending, one for receiving
- Applied timestamps to all messages for a clean chat experience
- Handled all edge cases including abrupt disconnections and empty inputs
- Wrote clean, modular, and well-commented Java code

---

## 🛠️ Platform & Tools Used

| Tool            | Details                          |
|-----------------|----------------------------------|
| IDE             | Eclipse IDE                      |
| Language        | Java                             |
| JDK Version     | JDK 11+                          |
| Package         | java.net, java.io, java.util, java.time |
| Libraries       | No external libraries used       |
| Protocol        | TCP via Java Sockets             |
| Architecture    | Client-Server (Multithreaded)    |

---

## ⚙️ Features

- ✅ Real-time messaging between multiple clients
- ✅ Each client handled by its own dedicated server thread
- ✅ Broadcast messages to all connected users
- ✅ `/msg <username> <text>` — Private messaging between users
- ✅ `/users` — View list of all currently online users
- ✅ `/quit` — Gracefully disconnect from the server
- ✅ Timestamps on every message (HH:mm format)
- ✅ Join and leave announcements for all users
- ✅ Thread-safe client list using `CopyOnWriteArrayList`
- ✅ No external libraries — uses only built-in Java

---

## 📂 Project Structure

```
codtech/
│
├── src/
│   └── chat app/
│       ├── ChatServer.java
│       ├── ClientHandler.java
│       └── ChatClient.java
│
├── JRE System Library/
└── README.md
```

---

## 🚀 How to Run

Follow these steps to run the project in **Eclipse IDE**:

1. Open **Eclipse IDE**
2. Go to **File → New → Java Project**
3. Name the project `ChatApp`
4. Right-click **src** → **New → Package** → name it `ChatApp`
5. Right-click **ChatApp** package → **New → Class** → create `ChatServer`, `ClientHandler`, and `ChatClient`
6. Add `package ChatApp;` as the first line of each file
7. Paste the respective source code into each class
8. Press **Ctrl + S** to save all files
9. Go to **Project → Clean → Clean All Projects**
10. Go to **Window → Preferences → Run/Debug → Launching** → uncheck **"Terminate and relaunch while launching"**
11. Open `ChatServer.java` → Right-click → **Run As → Java Application**
12. Open `ChatClient.java` → Right-click → **Run As → Java Application** (repeat for multiple clients)
13. Switch between client consoles using the monitor icon 🖥 in the Console toolbar

---

## 📋 Available Commands

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Commands:
  /users               - List online users
  /msg <user> <text>   - Private message
  /quit                - Leave the chat
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

## 🔍 How It Works

### 🖥️ ChatServer.java
Opens a `ServerSocket` on port 12345 and continuously listens for new
client connections using `serverSocket.accept()`. For every new client,
it creates a `ClientHandler` instance, adds it to a thread-safe list,
and starts a new `Thread` for it. Also provides static methods for
broadcasting messages and managing the client list.

### 🔄 ClientHandler.java
Implements `Runnable` and runs in its own thread for each connected
client. On startup, it prompts the user for a username and announces
their arrival. It then enters a read loop — parsing incoming messages
and routing them as broadcasts, private messages, or command responses.
Handles clean disconnection in a `finally` block.

### 💻 ChatClient.java
Connects to the server using a `Socket`. Spawns a **receiver thread**
(daemon) that continuously listens for and prints server messages in the
background. The main thread reads keyboard input via `Scanner` and sends
it to the server — allowing simultaneous send and receive without blocking.

### 🔒 Thread Safety
Uses `CopyOnWriteArrayList` for the shared client list so that multiple
threads can read and remove clients concurrently without race conditions
or `ConcurrentModificationException`.

---

## 🛡️ Error Handling

| Error                        | Cause                              | Handling                                  |
|------------------------------|------------------------------------|-------------------------------------------|
| `ConnectException`           | Server not running                 | "Could not connect — is server running?"  |
| `IOException` on disconnect  | Client closed abruptly             | Graceful cleanup in `finally` block       |
| Null / empty username        | User pressed Enter with no text    | Auto-assigned random username             |
| `/msg` wrong format          | Missing target or message text     | Usage hint displayed to sender            |
| Self private message         | User messaged themselves           | "You cannot message yourself"             |
| Unknown `/msg` target        | Target user offline or misspelled  | "User not found or offline"               |
| `Address already in use`     | Server already running on port     | Stop existing server and relaunch         |

---

## 🏗️ Architecture

```
           ChatServer (main thread)
                    |
         ServerSocket.accept()
                    |
       ┌────────────┼────────────┐
       ▼            ▼            ▼
 ClientHandler  ClientHandler  ClientHandler
  (Thread-1)    (Thread-2)    (Thread-3)
       │              │              │
     Alice           Bob           Carol
```

Each `ClientHandler` thread reads from its own client socket independently.
Broadcasts iterate over the `CopyOnWriteArrayList` and write to every
other client's `PrintWriter` stream.

---

## 📚 Concepts Used

| Concept                  | Class / Method Used                        |
|--------------------------|--------------------------------------------|
| Server Socket            | `ServerSocket`, `Socket`                   |
| Multithreading           | `Thread`, `Runnable`, `ClientHandler`      |
| Thread Safety            | `CopyOnWriteArrayList`                     |
| Stream I/O               | `BufferedReader`, `PrintWriter`            |
| Timestamps               | `LocalTime`, `DateTimeFormatter`           |
| User Input               | `Scanner`                                  |
| Daemon Thread            | `receiverThread.setDaemon(true)`           |
| Graceful Disconnect      | `finally` block, `socket.close()`         |
| Private Messaging        | Username lookup in client list             |
| Broadcast                | Iterate all clients, skip sender           |

---

<img width="1920" height="1020" alt="Image" src="https://github.com/user-attachments/assets/bf230c72-6a01-4c8f-be4d-48ea1aeed0a2" />
<img width="1920" height="1020" alt="Image" src="https://github.com/user-attachments/assets/88801c48-9c6d-48db-aa6d-62008ac52fb2" />

---

## 👩‍🎓 Author

**Vishalini**
B.Tech Information Technology — Third Year
Panimalar Engineering College, Chennai
