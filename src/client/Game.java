package client;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Game {
    private final static String finMessage = "finDuMessage";
    private final Queue<String> bufferPerso = new LinkedList<>();
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private  final Scanner scanner;
    private int morpionSize;
    private String pseudo;
    private Cipher cipherDecode;
    private Cipher cipherEncode;

    public Game(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.scanner = new Scanner(System.in);
    }

    public void playOrWatch() throws Exception {
        //send 1 to play and 2 to watch
        read();
        System.out.println("tapez 1 pour jouer, 2 pour regarder une partie");
        String rep = entrerValeur(1, 2);
        write(rep);
        if (rep.equals("1")) {
            choisirPseudo();
            choisirTailleMorpion();
        }
        else if (rep.equals("2")) {
            read();
            System.out.println("tapez la clef de la partie que vous voulez regarder\n" +
                    "cette clef à été communiqué aux joueurs au début de la partie");
            write(scanner.nextLine());
            watch();
        }else
            System.out.println("erreur lors de la prise en compte du choix, veuillez relancer le programme");

    }

    public void endGame() throws IOException {
        in.close();
        out.close();
        socket.close();
        scanner.close();
    }
    public void play() throws Exception {
        while (true){
                if (recevoirMessage())
                    break;
                if (recevoirMessage())
                    break;
                envoyerValeur();
            }
    }
    public void watch() throws Exception {
        String receivedMessage = read();
        while (! receivedMessage.equals("stop")){
            System.out.println(receivedMessage);
            receivedMessage = read();
        }
    }

    public void choisirPseudo() throws Exception {
        // pour dire au serveur qu'on veut être un joueur
        System.out.println("tapez votre pseudo : ");
        this.pseudo = scanner.nextLine();
        write(pseudo);
        System.out.println("Bienvenu "+pseudo+". Un deuxième joueur va bientôt se connecter !");
    }

    public void choisirTailleMorpion() throws Exception {
        String pseudoAdverse = read();
        String gameCode = read();
        System.out.println("adversaire trouvé, vous jouez contre "+pseudoAdverse);
        System.out.println("Si vous voulez des spectateurs, le code de votre partie est : " + gameCode);
        System.out.println("\n\nchoisissez une taille de matrice entre 3 et 10");
        write(entrerValeur(3, 10));
        String x = read();
        System.out.println(read());
        morpionSize = Integer.parseInt(x);
        play();
    }

    /**
     * @return true if the game has to stop, false if the game continue
     */
    private boolean recevoirMessage() throws Exception {
        String receivedMessage = read();
        String prefix = "\n\n\n\n\n\n\n\n";

        if ("win".equals(receivedMessage)) {
            System.out.println(prefix + "bravo vous avez gagné !!!");
        } else if ("loose".equals(receivedMessage)) {
            System.out.println(prefix + "dommage vous ferez mieux la prochaine fois !\n (j'espère)");
        } else if ("equality".equals(receivedMessage)) {
            System.out.println(prefix + "personne n'as gagné, tout le monde est content (?)");
        } else {
            System.out.println(receivedMessage);
            return false;
        }

        System.out.println(read());
        return true;
    }

    private void envoyerValeur() throws Exception {
        boolean valeurOk = false;
        while (!valeurOk) {
            System.out.println("entrez le numéro de COLONNE choisi (de 1 à "+morpionSize+")");
            String x = entrerValeur(1, morpionSize);
            System.out.println("entrez le numéro de LIGNE choisi (de 1 à "+morpionSize+")");
            String y = entrerValeur(1, morpionSize);
            write(x);
            write(y);
            String validation = read();
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

    private String read() throws Exception {
        if (bufferPerso.isEmpty()) {
            byte[] messageEncode = new byte[1000];
            int length = in.read(messageEncode);
            byte[] messageDecode = cipherDecode.doFinal(messageEncode, 0, length);
            String[] messages = new String(messageDecode).split(finMessage);
            if (messages.length > 1){
                for (int i = 1; i < messages.length; i++) {
                    bufferPerso.add(messages[i]);
                }
            }
            return messages[0];
        } else {
            return bufferPerso.poll();
        }
    }

    private void write(String message) throws Exception {
        String finalMessage = message+finMessage;
        byte[] messageClair = finalMessage.getBytes();
        byte[] messageCrypte = cipherEncode.doFinal(messageClair);
        out.write(messageCrypte);
    }

    public void startSecurityExchange() throws Exception{
        byte[] key = in.readNBytes(94);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key));

        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(56);
        SecretKey secretKey = keyGenerator.generateKey();

        cipherEncode = Cipher.getInstance("RSA");
        cipherEncode.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] clefDESEncode = cipherEncode.doFinal(secretKey.getEncoded());
        out.write(clefDESEncode);

        cipherEncode = Cipher.getInstance("DES");
        cipherEncode.init(Cipher.ENCRYPT_MODE, secretKey);

        cipherDecode = Cipher.getInstance("DES");
        cipherDecode.init(Cipher.DECRYPT_MODE, secretKey);

        System.out.println("connexion sécurisée ok");
    }
}
