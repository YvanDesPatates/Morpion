package serveur;
import serveur.clients.Client;
import serveur.clients.Joueur;

import java.io.IOException;
import java.net.ServerSocket;


public class Serveur {
    ServerSocket server;

    public Serveur(ServerSocket serverSocket) {
        this.server = serverSocket;
    }

    public void launchServeur() {
        while (true){
            try {
                Client newClient = new Joueur(server.accept());
                new LobbyOptions(newClient).start();
            } catch (IOException e){
                System.err.println("erreur dans le server lors de la connexion d'un client : "+e.getMessage());
            }
        }
    }
}
