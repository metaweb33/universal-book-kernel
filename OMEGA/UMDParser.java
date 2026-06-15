package org.ubk.omega.parser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// Importation d'une bibliothèque JSON standard (ex: org.json)
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Le Scanner Aveugle d'OMEGA
 * Ne contient plus aucune balise textuelle en dur.
 * Compile sa matrice logique au démarrage en lisant le fichier rules.json externe.
 */
public class UMDParser {

    /**
     * Structure interne représentant une règle de grammaire compilée à chaud
     */
    public static class DynamicRule {
        public String id;
        public String symbol;
        public String type;
        public Pattern compiledPattern;

        public DynamicRule(String id, String symbol, String regex, String type) {
            this.id = id;
            this.symbol = symbol;
            this.type = type.toUpperCase();
            // Compilation à la volée de la Regex lue dans le JSON
            this.compiledPattern = Pattern.compile(regex);
        }
    }

    // La mémoire vive des balises actives du système
    private final List<DynamicRule> grammarRules = new ArrayList<>();

    public UMDParser() {
        // Le constructeur démarre vide, prêt à recevoir sa configuration
    }

    /**
     * MÉTHODE CLÉ : Aspire le fichier de configuration externe
     * @param jsonFilePath Le chemin d'accès vers le rules.json localisé sur la machine
     */
    public void initializeGrammar(String jsonFilePath) {
        try {
            File configFile = new File(jsonFilePath);
            if (!configFile.exists()) {
                System.err.println("[ERREUR OMEGA] Fichier de configuration introuvable : " + jsonFilePath);
                return;
            }

            // Lecture brute du fichier texte en UTF-8
            String rawJson = new String(Files.readAllBytes(Paths.get(jsonFilePath)), "UTF-8");
            JSONObject configRoot = new JSONObject(rawJson);
            JSONArray tagsArray = configRoot.getJSONArray("tags");

            // Vidage de sécurité en cas de rechargement à chaud
            grammarRules.clear();

            // Traduction du JSON en objets Java compilés
            for (int i = 0; i < tagsArray.length(); i++) {
                JSONObject tagData = tagsArray.getJSONObject(i);
                
                DynamicRule rule = new DynamicRule(
                    tagData.getString("id"),
                    tagData.getString("symbol"),
                    tagData.getString("regex"),
                    tagData.getString("type")
                );
                
                grammarRules.add(rule);
            }

            System.out.println("[CORE] Matrice de grammaire initialisée : " + grammarRules.size() + " balises opérationnelles.");

        } catch (Exception e) {
            System.err.println("[ERREUR CRITIQUE] Échec du parsing du fichier rules.json : " + e.getMessage());
        }
    }

    /**
     * Analyse une ligne de texte UMD à travers le prisme des règles chargées
     */
    public ParsedLine parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new ParsedLine("EMPTY", null, "");
        }

        // Test de chaque règle séquentiellement (Vitesse optimisée par la pré-compilation des Patterns)
        for (DynamicRule rule : grammarRules) {
            Matcher matcher = rule.compiledPattern.matcher(line);
            
            if (matcher.matches()) {
                // Extraction dynamique des captures selon la forme de la Regex
                String firstCapture = matcher.groupCount() >= 1 ? matcher.group(1) : "";
                String secondCapture = matcher.groupCount() >= 2 ? matcher.group(2) : "";

                // Si la règle a deux groupes (ex: ID personnage + texte), on renvoie les deux.
                // Sinon, la première capture est le contenu principal.
                String finalContent = secondCapture.isEmpty() ? firstCapture : secondCapture;
                String finalIdOrIcon = secondCapture.isEmpty() ? rule.symbol : firstCapture;

                return new ParsedLine(rule.type, finalIdOrIcon, finalContent.trim());
            }
        }

        // Fallback absolu : Si aucun motif ne correspond, c'est de la narration littéraire pure
        return new ParsedLine("TEXT", null, line);
    }

    /**
     * Conteneur de données standardisé pour le RenderEngine
     */
    public static class ParsedLine {
        public String type;      // INTERACTION, CONTEXT, LAYOUT, EMPTY, TEXT
        public String idOrIcon;  // Identifiant (ex: CHAR_001) ou Icône (ex: 🌩️)
        public String content;   // Texte extrait nettoyé

        public ParsedLine(String type, String idOrIcon, String content) {
            this.type = type;
            this.idOrIcon = idOrIcon;
            this.content = content;
        }
    }
}