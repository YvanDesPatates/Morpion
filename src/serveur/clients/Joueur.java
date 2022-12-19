package serveur.clients;

import java.io.IOException;
import java.net.Socket;

public class Joueur extends Client {
    private String pseudo;
    private char symbole;

    public Joueur(Socket client) throws IOException {
        super(client);
    }

    public Joueur(Client client) throws IOException {
        super(client.getSocket());
    }

    public char getSymbole() {
        return symbole;
    }

    public void setSymbole(char symbole) {
        this.symbole = symbole;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
