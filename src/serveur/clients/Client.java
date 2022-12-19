package serveur.clients;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;

    public Client(Socket client) throws IOException {
        socket = client;
        input = new DataInputStream(client.getInputStream());
        output = new DataOutputStream(client.getOutputStream());
    }

    public String readMessage() throws IOException {
        return input.readUTF();
    }

    public void writeMessage(String message) throws IOException {
        output.writeUTF(message);
    }

    public boolean writeMessageCheckSuccess(String message){
        try {
            output.writeUTF(message);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Socket getSocket(){
        return socket;
    }
}
