import client.Game;

import java.io.IOException;
import java.net.Socket;

public class MainClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 1234)) {
            Game game = new Game(socket);
            game.choisirPseudo();
            try {
                game.choisirTailleMorpion();
                game.play();
            } catch (IOException e){
                System.out.println("votre adversaire s'est déconnecté !\n   lancez une autre partie :)");
                game.endGame();
            }
            game.endGame();
        } catch (IOException e){
            System.out.println("erreur lors de la connexion au serveur");
        }
    }
}
