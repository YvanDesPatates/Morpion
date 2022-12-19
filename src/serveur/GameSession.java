package serveur;

import serveur.clients.Client;
import serveur.clients.Joueur;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameSession extends Thread {
    private final Joueur player1;
    private final Joueur player2;
    private Plateau plateau;
    protected Collection<Client> viewers;
    private final String key;
    Lobby lobby;

    public GameSession(Joueur player1, Joueur player2, String key, Lobby lobby) {
        this.player1 = player1;
        this.player2 = player2;
        this.viewers = new ConcurrentLinkedQueue<>();
        this.key = key;
        this.lobby = lobby;
        setDaemon(true);
    }

    @Override
    public void run() {
        super.run();
        System.out.println("deux joueurs jouent ensemble");
        player1.setSymbole('X');
        player2.setSymbole('O');


        Joueur currentPlayer = player1;
        try {
            choisirPseudo();

            choisirTailleMatrice();

            player1.writeMessage("");
            String prefix = "le jeux commence\n";
            boolean jeuxEnCours = true;
            while (jeuxEnCours){

                player1.writeMessage( prefix + plateau);
                player2.writeMessage(prefix + plateau);
                writeToViewers(prefix + plateau + "\n" + "c'est à " + currentPlayer.getPseudo() + " de jouer");

                prendreCase(currentPlayer);

                jeuxEnCours = noWinnerNorEquality(currentPlayer);

                prefix = "\n";
                if( currentPlayer == player1 )
                    currentPlayer = player2;
                else
                    currentPlayer = player1;
            }
            //quand le jeux est fini on envois une dernière fois la matrice
            player1.writeMessage(plateau.toString());
            player2.writeMessage(plateau.toString());

        } catch (Exception e) {
            System.err.println("erreur lors d'envois de message : " + e.getMessage());
            writeToViewers("un joueur s'est déconnecté, la partie s'arrête ici :(");
            writeToViewers("stop");
        }
        try {
            sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player1.close();
        player2.close();
        lobby.finishedGame(key, this);
    }

    private void choisirTailleMatrice() throws IOException {
        player1.writeMessage(player2.getPseudo());
        player2.writeMessage(player1.getPseudo());
        writeToBothPlayers(key);
        int x1 = Integer.parseInt(player1.readMessage());
        int x2 = Integer.parseInt(player2.readMessage());
        int x;
        if (x1 == x2){
            x = x1;
            writeToBothPlayers(String.valueOf(x));
            writeToBothPlayers("votre adversaire et vous êtes d'accords pour une matrice de "+x+"x"+x);
        } else {
            x = (x1+x2)/2;
            writeToBothPlayers(String.valueOf(x));
            writeToBothPlayers("vous n'êtes pas d'accords avec votre adversaire !\nla moyenne (approximative) vous donne donc une grille de "+x+"x"+x);
        }
        plateau = new Plateau(x);
    }

    public void choisirPseudo() throws IOException {
        player1.setPseudo(player1.readMessage());
        player2.setPseudo(player2.readMessage());
    }

    /**
     * @param currentPlayer the last plays which has play
     * @return true if there is no winner and the serveur.Plateau is not full yet
     */
    private boolean noWinnerNorEquality(Joueur currentPlayer) throws IOException {
        boolean res = true;
        if(plateau.isWinner(currentPlayer)){
            Joueur looser = currentPlayer == player1 ? player2 : player1;
            res = false;
            currentPlayer.writeMessage("win");
            looser.writeMessage("loose");
            writeToViewers("win");
            writeToViewers("fin ! La partie à été remportée par " + currentPlayer.getPseudo());
            writeToViewers("dommage pour " + looser.getPseudo());
            writeToViewers("stop");
        } else if (plateau.isFull()){
            res = false;
            writeToBothPlayers("equality");
            writeToViewers("fin de la partie, personne n'as gagné !");
            writeToViewers("stop");
        }
        return res;
    }

    private void prendreCase(Joueur currentPlayer) throws IOException {
        boolean valeurOk = false;
        while (!valeurOk) {
            int x = Integer.parseInt(currentPlayer.readMessage());
            int y = Integer.parseInt(currentPlayer.readMessage());
            if(plateau.cellIsFree(x, y)) {
                valeurOk = true;
                currentPlayer.writeMessage("ok");
                plateau.takeCase(x, y, currentPlayer.getSymbole());
            } else
                currentPlayer.writeMessage("erreur ! choisissez une case vide");
        }
    }

    private void writeToBothPlayers(String message) throws IOException {
        player1.writeMessage(message);
        player2.writeMessage(message);
    }

    private void writeToViewers(String message) {
        viewers.removeIf( viewer -> ! viewer.writeMessageCheckSuccess(message));
    }


}
