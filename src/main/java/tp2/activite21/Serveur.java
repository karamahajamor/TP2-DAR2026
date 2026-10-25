package tp2.activite21;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
public class Serveur {
    private static final int PORT = 1234;
    public static void main(String[] args) {
        System.out.println("Serveur en attente d'un client sur le port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            try (Socket socket = serverSocket.accept();
                 BufferedReader br = new BufferedReader(
                         new InputStreamReader(
                                 socket.getInputStream(),
                                 StandardCharsets.UTF_8));
                 PrintWriter pw = new PrintWriter(
                         socket.getOutputStream(),
                         true,
                         StandardCharsets.UTF_8)) {
                System.out.println("Un client est connecté.");
                String operande1 = br.readLine();
                while (operande1 != null && !operande1.equalsIgnoreCase("quit")) {
                    String operateur = br.readLine();
                    String operande2 = br.readLine();
                    String reponse;
                    // Vérifier que les trois lignes de l'opération ont été reçues.
                    if (operateur == null || operande2 == null) {
                        reponse = "Erreur : opération incomplète";
                        pw.println(reponse);
                        System.out.println(reponse);
                        break;}
                    System.out.println(
                            "Opération reçue : "
                                    + operande1 + " "
                                    + operateur + " "
                                    + operande2);
                    try {
                        double a = Double.parseDouble(operande1.trim());
                        double b = Double.parseDouble(operande2.trim());
                        reponse = calculer(a, operateur.trim(), b);
                    } catch (NumberFormatException e) {
                        reponse = "Erreur : opérande invalide";
                    }
                    pw.println(reponse);
                    System.out.println("Réponse envoyée : " + reponse);
                    // Lire le premier élément de la prochaine opération.
                    operande1 = br.readLine();}
                System.out.println("Fin de la communication.");}
        } catch (IOException e) {
            System.err.println("Erreur côté serveur : " + e.getMessage());}}
    /**
     * Calcule l'opération et renvoie une réponse textuelle au client.
     */
    static String calculer(double a, String operateur, double b) {
        double resultat;
        switch (operateur) {
            case "+":
                resultat = a + b;
                break;
            case "-":
                resultat = a - b;
                break;
            case "*":
                resultat = a * b;
                break;
            case "/":
                if (b == 0.0) {
                    return "Erreur : division par zéro impossible";
                }
                resultat = a / b;
                break;
            default:
                return "Erreur : opérateur inconnu";
        }
        if (Double.isNaN(resultat) || Double.isInfinite(resultat)) {
            return "Erreur : résultat hors limites";
        }
        return formater(a) + " " + operateur + " " + formater(b)
                + " = " + formater(resultat);}
    /**
     * Affiche les nombres entiers sans décimale inutile.
     * Par exemple : 1375 au lieu de 1375.0.
     */
    static String formater(double valeur) {
        if (valeur == Math.rint(valeur)
                && !Double.isInfinite(valeur)
                && valeur <= Long.MAX_VALUE
                && valeur >= Long.MIN_VALUE) {
            return String.valueOf((long) valeur);
        }
        return String.valueOf(valeur);}}