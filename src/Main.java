import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Queue<Joueur> joueurs = new LinkedList<>();
        int port = 1234;
        try {
            ServerSocket server = new ServerSocket(port);
            Lobby lobby = new Lobby(joueurs, server);
            lobby.launchLobby();
        } catch (IOException e){
            System.err.println("erreur lors du lancement du Lobby : "+e.getMessage());
        }
    }
}