package tp2.activite22;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String ADRESSE_SERVEUR = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (Scanner clavier = new Scanner(System.in);
             Socket socket = new Socket(ADRESSE_SERVEUR, PORT)) {

            System.out.println("Le client est connecté.");

            ObjectOutputStream sortie =
                    new ObjectOutputStream(socket.getOutputStream());
            sortie.flush();

            ObjectInputStream entree =
                    new ObjectInputStream(socket.getInputStream());

            while (true) {
                System.out.println("\nChoisissez une opération :");
                System.out.println("1. Addition");
                System.out.println("2. Soustraction");
                System.out.println("3. Multiplication");
                System.out.println("4. Division");
                System.out.println("0. Quitter");
                System.out.print("Votre choix : ");

                String texteChoix = clavier.nextLine().trim();
                int choix;

                try {
                    choix = Integer.parseInt(texteChoix);
                } catch (NumberFormatException e) {
                    System.out.println("Saisissez un choix entre 0 et 4.");
                    continue;
                }

                if (choix == 0) {
                    sortie.writeObject(null);
                    sortie.flush();
                    break;
                }

                String operateur;
                switch (choix) {
                    case 1:
                        operateur = "+";
                        break;
                    case 2:
                        operateur = "-";
                        break;
                    case 3:
                        operateur = "*";
                        break;
                    case 4:
                        operateur = "/";
                        break;
                    default:
                        System.out.println("Choix invalide.");
                        continue;
                }

                double premierNombre;
                double deuxiemeNombre;

                try {
                    System.out.print("Saisissez le premier nombre : ");
                    premierNombre = Double.parseDouble(clavier.nextLine().trim());

                    System.out.print("Saisissez le deuxième nombre : ");
                    deuxiemeNombre = Double.parseDouble(clavier.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Saisissez des nombres valides.");
                    continue;
                }

                Operation operation =
                        new Operation(premierNombre, operateur, deuxiemeNombre);

                sortie.writeObject(operation);
                sortie.flush();
                sortie.reset();

                Object reponse = entree.readObject();

                if (reponse instanceof Double) {
                    System.out.println("Résultat reçu du serveur : " + reponse);
                } else {
                    System.out.println("Message du serveur : " + reponse);
                }
            }

            System.out.println("Fin de la communication.");

        } catch (ClassNotFoundException e) {
            System.err.println("Classe de réponse inconnue : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur côté client : " + e.getMessage());
            System.err.println("Vérifiez que le serveur est lancé sur le port 1234.");
        }
    }
}