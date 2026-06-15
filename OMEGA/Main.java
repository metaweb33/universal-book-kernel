package org.ubk.omega;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

// Utilisation d'une bibliothèque JSON standard
import org.json.JSONArray;
import org.json.JSONObject;

import org.ubk.omega.parser.UMDParser;
import org.ubk.omega.parser.UMDParser.ParsedLine;
import org.ubk.omega.render.RenderEngine;

/**
 * Le Point d'Entrée Principal d'OMEGA
 * C'est ce fichier qui est exécuté par le double-clic ou la ligne de commande.
 */
public class Main {

    public static void main(String[] args) {
        
        // 1. VÉRIFICATION DES ARGUMENTS SYSTÈME
        // OMEGA attend au moins 2 paramètres : le fichier du livre et le mode d'affichage
        if (args.length < 2) {
            System.out.println("[ERREUR] Paramètres manquants.");
            System.out.println("Usage : java -jar omega-core.jar <chemin_du_livre.umd> <MODE>");
            return;
        }

        String bookPath = args[0];
        String renderMode = args[1]; // Exemples : "roman", "sms", "office"

        try {
            // 2. LOCALISATION DE L'ADN DU MOTEUR (rules.json)
            // On cherche le fichier de règles dans le même dossier que le logiciel
            String appFolder = System.getProperty("user.dir");
            String rulesPath = appFolder + File.separator + "rules.json";

            File rulesFile = new File(rulesPath);
            if (!rulesFile.exists()) {
                System.err.println("[CRITIQUE] Le fichier de configuration rules.json est introuvable !");
                return;
            }

            System.out.println("--- DÉMARRAGE DU MOTEUR OMEGA ---");

            // 3. ALLUMAGE DES CERVEAUX ET INJECTION DES RÈGLES
            
            // A. Le Scanner Aveugle (UMDParser) s'initialise en lisant le chemin du fichier
            UMDParser parser = new UMDParser();
            parser.initializeGrammar(rulesPath);

            // B. Le Projecteur (RenderEngine) a besoin du tableau JSON
            // Le Main lit donc le fichier texte JSON pour l'envoyer au RenderEngine
            String rawJson = new String(Files.readAllBytes(Paths.get(rulesPath)), "UTF-8");
            JSONObject configRoot = new JSONObject(rawJson);
            JSONArray tagsArray = configRoot.getJSONArray("tags");

            RenderEngine renderer = new RenderEngine(renderMode);
            renderer.loadRules(tagsArray);

            System.out.println("--- LECTURE DU LIVRE : " + bookPath + " | MODE : " + renderMode.toUpperCase() + " ---");

            // 4. LA BOUCLE DE LECTURE (Le Tapis Roulant)
            // On ouvre le fichier .umd (En conditions réelles avec un .ubk compressé, 
            // c'est ici qu'interviendrait la classe Unzipper.class pour extraire le flux)
            
            try (BufferedReader reader = new BufferedReader(new FileReader(bookPath))) {
                String line;
                
                // Tant qu'il y a des lignes dans le livre, on les fait avancer
                while ((line = reader.readLine()) != null) {
                    
                    // ÉTAPE 1 : Le parseur découpe et qualifie la ligne grâce à ses Regex en RAM
                    ParsedLine parsedLine = parser.parseLine(line);
                    
                    // ÉTAPE 2 : Le renderer projette la ligne selon les règles du mode choisi
                    renderer.render(parsedLine);
                }
            }

            System.out.println("--- FIN DE LECTURE ---");

        } catch (Exception e) {
            // Si le fichier JSON est mal écrit ou si le livre est corrompu, le moteur intercepte le crash
            System.err.println("[CRASH SYSTÈME OMEGA] Une erreur fatale est survenue.");
            e.printStackTrace();
        }
    }
}