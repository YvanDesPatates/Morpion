import java.io.IOException;

public class GameSession extends Thread {
    private final Joueur player1;
    private final Joueur player2;
    private final Plateau plateau;

    public GameSession(Joueur player1, Joueur player2) {
        this.player1 = player1;
        this.player2 = player2;
        plateau = new Plateau();
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

            player1.writeMessage("");
            String prefix = "le jeux commence\n";
            boolean jeuxEnCours = true;
            while (jeuxEnCours){

                player1.writeMessage( prefix + plateau);
                player2.writeMessage(prefix + plateau);
                prendreCase(currentPlayer);

                if(plateau.isWinner(currentPlayer)){
                    Joueur looser = currentPlayer == player1 ? player2 : player1;
                    jeuxEnCours = false;
                    currentPlayer.writeMessage("win");
                    looser.writeMessage("loose");
                } else if (plateau.isFull()){
                    jeuxEnCours = false;
                    player1.writeMessage("equality");
                    player2.writeMessage("equality");
                }

                prefix = "\n";
                if( currentPlayer == player1 )
                    currentPlayer = player2;
                else
                    currentPlayer = player1;
            }

        } catch (Exception e) {
            System.err.println("erreur lors d'envois de message : " + e.getMessage());
        }
        player1.close();
        player2.close();
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

}
