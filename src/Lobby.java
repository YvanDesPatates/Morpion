import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.Queue;


public class Lobby {
    private Queue<Joueur> joueurs;
    private ServerSocket server;

    public Lobby(Queue<Joueur> joueurs, ServerSocket server) {
        this.joueurs = joueurs;
        this.server = server;
    }

    public void launchLobby() {

        while(true){
            try {
                Joueur newPlayer = new Joueur(server.accept());
                joueurs.add(newPlayer);
            } catch (IOException e){
                System.err.println("erreur dans le morpion.src.Lobby lors de la connexion d'un client : "+e.getMessage());
            }

            if(joueurs.size() >= 2){
                new GameSession(joueurs.poll(), joueurs.poll()).start();
                System.out.println();
            } else {
                System.out.println("un client attends");
            }

        }
    }

}
