import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Joueur {
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;

    private char symbole;

    public Joueur(Socket client) throws IOException {
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


    public char getSymbole() {
        return symbole;
    }

    public void setSymbole(char symbole) {
        this.symbole = symbole;
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
