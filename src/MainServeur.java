import serveur.LobbyOptions;
import serveur.Serveur;
import serveur.Lobby;

import java.io.IOException;
import java.net.ServerSocket;

public class MainServeur {
    public static void main(String[] args) {
        int port = 1234;
        LobbyOptions.setLobby(new Lobby());

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            new Serveur(serverSocket).launchServeur();
        } catch (IOException e){
            System.err.println("erreur lors du lancement du serveur.Lobby : "+e.getMessage());
        }
    }
}