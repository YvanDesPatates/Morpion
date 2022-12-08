import java.io.IOException;

public class GameSession extends Thread {
    private final Joueur player1;
    private final Joueur player2;
    private String plateau;

    public GameSession(Joueur player1, Joueur player2) {
        this.player1 = player1;
        this.player2 = player2;
        plateau = "_|_|_|\n_|_|_|\n_|_|_|\n";
        setDaemon(true);
    }

    @Override
    public void run() {
        super.run();
        System.out.println("deux joueurs jouent ensemble");
        Joueur currentPlayer = player1;
        try {

            player1.writeMessage("");
            String prefix = "le jeux commence\n";
            for (int i = 0; i < 5; i++) {
                player1.writeMessage( prefix + plateau);
                player2.writeMessage(prefix + plateau);
                plateau += currentPlayer.readMessage();

                prefix = "\n";
                if( currentPlayer == player1 )
                    currentPlayer = player2;
                else
                    currentPlayer = player1;
            }

        } catch (IOException e) {
            System.err.println("erreur lors d'envois de message : " + e.getMessage());
        }
    }

}
