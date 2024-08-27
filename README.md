# Chat-Project
A chat project for CS465 at NAU

# Description
```
What to do
You are supposed to design and implement a chat server and client - in Java (and only in Java, as all assignments that follow this one). The server serves as a "central hub", which allows chat clients to:

Register for participating in the chat (aka JOIN)
Leave the chat (aka LEAVE)
Send messages (called "notes") to all registered chat participants.
Similarly, the chat client will be able to:

JOIN a chat, i.e., register with a known chat server to participate in the chat. The information of the server, i.e., its IP/port, needs to be read from a properties file upon starting up the chat client. Note that a client who has joined before, should not be able to join again.
LEAVE a chat - while not terminating itself, i.e. it may register again at some later time. Note that leaving a chat anticipates that the client has joined the chat before.
Send a note to all other chat clients, using the chat server. Note that a client can only send a note, if it has joined the chat before. For simplicity's sake we want to assume that a message is being forwarded by the server to ALL chat participants, including the originator of the note.
SHUTDOWN, which terminates the chat client. This may involve to first leave the chat, if the client is registered at the server.
SHUYTDOWN_ALL, which shuts down the whole chat, including the server.
In order to trigger all above activities/states, the client needs to provide a primitive command-line user interface for the user to use. The interface will recognise the following "commands":
JOIN IP port
LEAVE
SHUTDOWN
SHUTDOWN_ALL
any other text, which is interpreted to be a note to be sent to all other registered chat clients. Note, however, that the client has to be a chat participant, before being able to send notes.
Clients have logical, human-readable names, like Paul, Mickey Mouse, Sandy etc. When sending a message, the logical name will be prepended to the message that is supposed to be sent and thus will automatically be displayed on the receiving side, when displaying the message string.
```
