import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class JoueurClient {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        DataInputStream in;
        DataOutputStream out;
        try (Socket socket = new Socket("127.0.0.1", 1234)) {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            for (int i = 0; i < 5; i++){
                System.out.println(in.readUTF());
                System.out.println(in.readUTF());
                System.out.println("tapes la case à cocher sous la forme xy (sans espace");
                out.writeUTF(scanner.nextLine());
            }
        }
    }
}
