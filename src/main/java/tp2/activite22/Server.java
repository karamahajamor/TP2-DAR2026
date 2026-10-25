package tp2.activite22;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 1234;

    public static void main(String[] args) {
        System.out.println("Serveur en attente sur le port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Un client est connecté.");

                    ObjectInputStream entree =
                            new ObjectInputStream(socket.getInputStream());

                    ObjectOutputStream sortie =
                            new ObjectOutputStream(socket.getOutputStream());
                    sortie.flush();

                    while (true) {
                        Object objetRecu = entree.readObject();

                        // Le client envoie null pour terminer.
                        if (objetRecu == null) {
                            System.out.println("Fin de la communication avec le client.");
                            break;
                        }

                        if (!(objetRecu instanceof Operation)) {
                            sortie.writeObject("ERREUR : objet reçu invalide");
                            sortie.flush();
                            continue;
                        }

                        Operation operation = (Operation) objetRecu;
                        Object reponse;

                        try {
                            double resultat = calculer(operation);
                            reponse = Double.valueOf(resultat);
                            System.out.println("Résultat calculé : " + resultat);
                        } catch (IllegalArgumentException e) {
                            reponse = "ERREUR : " + e.getMessage();
                        }

                        sortie.writeObject(reponse);
                        sortie.flush();
                        sortie.reset();

                        System.out.println("Réponse envoyée : " + reponse);
                    }

                } catch (ClassNotFoundException e) {
                    System.err.println("Classe reçue inconnue : " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("Erreur de communication : " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Impossible de démarrer le serveur : " + e.getMessage());
        }
    }

    private static double calculer(Operation operation) {
        double a = operation.getOperande1();
        double b = operation.getOperande2();
        String operateur = operation.getOperateur();

        switch (operateur) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0.0) {
                    throw new IllegalArgumentException("division par zéro impossible");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("opérateur invalide");
        }
    }
}