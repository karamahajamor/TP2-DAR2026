package tp2.activite21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    // Nombre, opérateur (+, -, * ou /), nombre ; espaces facultatifs.
    private static final Pattern SYNTAXE = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+*/-])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$"
    );

    public static void main(String[] args) {
        System.out.println("Je suis un client pas encore connecté...");

        try (
                Socket socket = new Socket("localhost", 1234);
                PrintWriter pw = new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),
                                StandardCharsets.UTF_8
                        ),
                        true
                );
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                );
                Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8.name())
        ) {
            System.out.println("Je suis un client connecté.");

            while (true) {
                System.out.print(
                        "Donnez une opération (ex. 55 * 25) ou quit : "
                );

                if (!sc.hasNextLine()) {
                    break;
                }

                String saisie = sc.nextLine().trim();

                if (saisie.equalsIgnoreCase("quit")) {
                    // Le serveur attend le mot quit comme prochaine ligne.
                    pw.println("quit");
                    break;
                }

                // Vérifier l'opération avant de l'envoyer au serveur.
                Matcher matcher = SYNTAXE.matcher(saisie);

                if (!matcher.matches()) {
                    System.out.println(
                            "Syntaxe invalide, rien n'est envoyé. Exemple : 55 * 25"
                    );
                    continue;
                }

                // Envoyer les trois éléments : opérande, opérateur, opérande.
                pw.println(matcher.group(1));
                pw.println(matcher.group(2));
                pw.println(matcher.group(3));

                // Lire et afficher la réponse du serveur.
                String reponse = br.readLine();

                if (reponse == null) {
                    System.out.println("Le serveur a fermé la connexion.");
                    break;
                }

                System.out.println("Résultat : " + reponse);
            }

            System.out.println("Fin de la communication.");

        } catch (IOException e) {
            System.err.println("Erreur côté client : " + e.getMessage());
            System.err.println(
                    "Vérifiez que le serveur est démarré sur le port 1234."
            );
        }
    }
}