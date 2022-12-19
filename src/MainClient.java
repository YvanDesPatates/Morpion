import client.Game;

import java.io.IOException;
import java.net.Socket;

public class MainClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 1234)) {
            Game game = new Game(socket);
            try {
                game.playOrWatch();
            } catch (IOException e){
                System.out.println("un joueur s'est déconnecté !\n   lancez une autre partie :)");
                game.endGame();
            }
            game.endGame();
        } catch (IOException e){
            System.out.println("erreur lors de la connexion au serveur");
        }
    }
}
