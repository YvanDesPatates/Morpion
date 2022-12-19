package serveur;

import serveur.clients.Client;
import serveur.clients.Joueur;

import java.io.IOException;

public class LobbyOptions extends Thread{
    private static Lobby lobby;
    private final Client client;

    public LobbyOptions(Client client) {
        this.client = client;
        setDaemon(true);
    }

    @Override
    public void run() {
        super.run();
        try {
            client.writeMessage("choix");
            int choix = Integer.parseInt(client.readMessage());
            if (choix == 1)
                lobby.newPlayer(new Joueur(client));
            if (choix == 2){
                client.writeMessage("clef");
                String clef = client.readMessage();
                if ( ! lobby.newViewer(client, clef)) {
                    client.writeMessage("clef incorect, relancez le programme pour réessayer");
                    client.writeMessage("stop");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setLobby(Lobby lobby) {
        LobbyOptions.lobby = lobby;
    }
}
