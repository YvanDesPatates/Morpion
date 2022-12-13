package serveur;

import java.io.IOException;

public class GameSession extends Thread {
    private final Joueur player1;
    private final Joueur player2;
    private Plateau plateau;

    public GameSession(Joueur player1, Joueur player2) {
        this.player1 = player1;
        this.player2 = player2;
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

            choisirTailleMatrice();

            player1.writeMessage("");
            String prefix = "le jeux commence\n";
            boolean jeuxEnCours = true;
            while (jeuxEnCours){

                player1.writeMessage( prefix + plateau);
                player2.writeMessage(prefix + plateau);
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
        }
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player1.close();
        player2.close();
    }

    private void choisirTailleMatrice() throws IOException {
        writeToBothPlayers("choice");
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
            writeToBothPlayers("vous n'êtes pas d'accords avec votre adversaire !\nla moyenne 'approximative vous donne donc une grille de "+x+"x"+x);
        }
        plateau = new Plateau(x);
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
        } else if (plateau.isFull()){
            res = false;
            writeToBothPlayers("equality");
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

}
