import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class JoueurClient {
    public static void main(String[] args){
        DataInputStream in;
        DataOutputStream out;
        try (Socket socket = new Socket("127.0.0.1", 1234)) {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            for (int i = 0; i < 5; i++){
                System.out.println(in.readUTF());
                System.out.println(in.readUTF());
                envoyerValeur(out, in);
            }
        } catch (IOException e){
            System.out.println("votre adversaire s'est déconnecté ! relancez une partie");
        }
    }

    private static void envoyerValeur(DataOutputStream out, DataInputStream in) throws IOException {
        boolean valeurOk = false;
        while (!valeurOk) {
            System.out.println("entrez le numéro de COLONNE -> choisi (de 1 à 3)");
            String x = entrerValeur();
            System.out.println("entrez le numéro de LIGNE î choisi (de 1 à 3)");
            String y = entrerValeur();
            out.writeUTF(x);
            out.writeUTF(y);
            String validation = in.readUTF();
            if (validation.equals("ok"))
                valeurOk = true;
            else
                System.out.println(validation);
        }
    }

    private static String entrerValeur() {
        Scanner scanner = new Scanner(System.in);
        String rep = scanner.nextLine();
        boolean valeurOk = false;
        while (!valeurOk) {
            if (isIntable(rep)) {
                int x = Integer.parseInt(rep);
                if (x >= 1 && x <= 3) {
                    valeurOk = true;
                } else {
                    System.out.println("Erreur, merci de choisir un valeur entre 1 et 3 :");
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
