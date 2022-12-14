package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Game {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private  final Scanner scanner;
    private int morpionSize;
    private String pseudo;

    public Game(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.scanner = new Scanner(System.in);
    }

    public void endGame() throws IOException {
        in.close();
        out.close();
        socket.close();
        scanner.close();
    }
    public void play() throws IOException {
        while (true){
                if (recevoirMessage())
                    break;
                if (recevoirMessage())
                    break;
                envoyerValeur();
            }
    }

    public void choisirPseudo() throws IOException {
        System.out.println("tapez votre pseudo : ");
        this.pseudo = scanner.nextLine();
        out.writeUTF(pseudo);
        System.out.println("Bienvenu "+pseudo+". Un deuxième joueur va bientôt se connecter !");
    }

    public void choisirTailleMorpion() throws IOException {
        String pseudoAdverse = in.readUTF();
        System.out.println("adversaire trouvé, vous jouez contre "+pseudoAdverse);
        System.out.println("\n\nchoisissez une taille de matrice entre 3 et 10");
        out.writeUTF(entrerValeur(3, 10));
        String x = in.readUTF();
        System.out.println(in.readUTF());
        morpionSize = Integer.parseInt(x);
    }

    /**
     * @return true if the game has to stop, false if the game continue
     */
    private boolean recevoirMessage() throws IOException {
        String receivedMessage = in.readUTF();
        String prefix = "\n\n\n\n\n\n\n\n";
        switch (receivedMessage) {
            case "win" -> System.out.println(prefix+"bravo vous avez gagné !!!");
            case "loose" -> System.out.println(prefix+"dommage vous ferez mieux la prochaine fois !\n (j'espère)");
            case "equality" -> System.out.println(prefix+"personne n'as gagné, tout le monde est content (?)");
            default -> {
                System.out.println(receivedMessage);
                return false;
            }
        }
        System.out.println(in.readUTF());
        return true;
    }

    private void envoyerValeur() throws IOException {
        boolean valeurOk = false;
        while (!valeurOk) {
            System.out.println("entrez le numéro de COLONNE choisi (de 1 à "+morpionSize+")");
            String x = entrerValeur(1, morpionSize);
            System.out.println("entrez le numéro de LIGNE choisi (de 1 à "+morpionSize+")");
            String y = entrerValeur(1, morpionSize);
            out.writeUTF(x);
            out.writeUTF(y);
            String validation = in.readUTF();
            if (validation.equals("ok"))
                valeurOk = true;
            else
                System.out.println(validation);
        }
    }

        private String entrerValeur(int min, int max) {
        String rep = scanner.nextLine();
        boolean valeurOk = false;
        while (!valeurOk) {
            if (isIntable(rep)) {
                int x = Integer.parseInt(rep);
                if (x >= min && x <= max) {
                    valeurOk = true;
                } else {
                    System.out.println("Erreur, merci de choisir un valeur entre "+min+" et "+max+" :");
                    rep = scanner.nextLine();
                }
            } else {
                System.out.println("erreur ! entrez une valeur numérique ex: 2");
                rep = scanner.nextLine();
            }
        }

        return rep;
    }

    private static boolean isIntable(String string){
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
