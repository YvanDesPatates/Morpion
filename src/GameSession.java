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
            for (int i = 0; i < 5; i++) {
                player1.writeMessage( prefix + plateau);
                player2.writeMessage(prefix + plateau);
                String rep = currentPlayer.readMessage();
                int x = Integer.parseInt(String.valueOf(rep.charAt(0)));
                int y = Integer.parseInt(String.valueOf(rep.charAt(1)));
                plateau.takeCase(x, y, currentPlayer.getSymbole());

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
