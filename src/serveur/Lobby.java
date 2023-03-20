package serveur;

import serveur.clients.Client;
import serveur.clients.Joueur;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.lang3.RandomStringUtils;


public class Lobby {
    private final Queue<Joueur> joueurs;
    private final Map<String, GameSession> games;
    private KeyPair keyPair;

    public Lobby() throws NoSuchAlgorithmException {
        this.joueurs = new ConcurrentLinkedQueue<>();
        this.games = new ConcurrentHashMap<>();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        keyPair = keyPairGenerator.generateKeyPair();
    }


    public void newPlayer(Joueur newJoueur) {
        joueurs.add(newJoueur);
        if(joueurs.size() >= 2){
            String key = generateNewKey();
            GameSession gameSession = new GameSession(joueurs.poll(), joueurs.poll(), key, this);
            games.put(key, gameSession);
            gameSession.start();
            System.out.println("une partie est lancée");
        } else {
            System.out.println("un client attends");
        }
    }

    private String generateNewKey() {
        return RandomStringUtils.randomAlphanumeric(4);
    }

    public boolean newViewer(Client client, String codeGame) {
        try {
            games.get(codeGame).viewers.add(client);
            client.writeMessage("la partie est en cours et vous pourez la suivre dès que le prochain coup sera joué");
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public void finishedGame(String code, GameSession finishedGame){
        games.remove(code, finishedGame);
    }

    public void startSecurityExchange(Client client) throws Exception{
        client.startSecurityKeyExchange(keyPair);
    }
}
