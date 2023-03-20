package serveur.clients;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Client {
    private final static String finMessage = "finDuMessage";
    private final Queue<String> bufferPerso = new LinkedList<>();
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;

    private Cipher cipherDecode;
    private Cipher cipherEncode;

    public Client(Socket client) throws IOException {
        socket = client;
        input = new DataInputStream(client.getInputStream());
        output = new DataOutputStream(client.getOutputStream());
    }

    public Client(Client client) throws IOException {
        this(client.getSocket());
        cipherDecode = client.getCipherDecode();
        cipherEncode = client.cipherEncode;
    }

    public String readMessage() throws Exception {
        if (bufferPerso.isEmpty()) {
            byte[] messageEncode = new byte[1000];
            int length = input.read(messageEncode);
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

    public void writeMessage(String message) throws Exception {
        String finalMessage = message+finMessage;
        byte[] messageClair = finalMessage.getBytes();
        byte[] messageCrypte = cipherEncode.doFinal(messageClair);
        output.write(messageCrypte);
    }

    public boolean writeMessageCheckSuccess(String message) {
        try {
            writeMessage(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Socket getSocket() {
        return socket;
    }

    public void startSecurityKeyExchange(KeyPair keyPair) throws Exception {
        output.write(keyPair.getPublic().getEncoded());

        byte[] clefDESEncode = input.readNBytes(64);
        cipherDecode = Cipher.getInstance("RSA");
        cipherDecode.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] clefDES = cipherDecode.doFinal(clefDESEncode);

        SecretKey secretKey = new SecretKeySpec(clefDES, "DES");

        cipherDecode = Cipher.getInstance("DES");
        cipherDecode.init(Cipher.DECRYPT_MODE, secretKey);

        cipherEncode = Cipher.getInstance("DES");
        cipherEncode.init(Cipher.ENCRYPT_MODE, secretKey);
    }

    public Cipher getCipherDecode() {
        return cipherDecode;
    }

    public Cipher getCipherEncode() {
        return cipherEncode;
    }
}
