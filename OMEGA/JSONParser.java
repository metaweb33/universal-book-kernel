package org.ubk.omega.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject; // Utilisation d'une bibliothèque JSON légère
import org.json.JSONArray;

/**
 * Cartographe de l'environnement OMEGA
 * Charge les métadonnées et profils de personnages directement en RAM
 */
public class JSONParser {

    // La mémoire vive ultra-rapide des personnages
    private Map<String, JSONObject> characterRegistry = new HashMap<>();
    private String defaultLocale = "en";

    /**
     * Constructeur : Aspire le flux JSON et le cartographie en RAM
     */
    public JSONParser(InputStream metadataStream) {
        try {
            // Lecture du flux d'octets
            StringBuilder jsonText = new StringBuilder();
            try (InputStreamReader reader = new InputStreamReader(metadataStream, "UTF-8")) {
                int ch;
                while ((ch = reader.read()) != -1) {
                    jsonText.append((char) ch);
                }
            }

            // Analyse de la structure racine
            JSONObject root = new JSONObject(jsonText.toString());
            this.defaultLocale = root.optString("default_locale", "en");

            // Si le fichier contient un tableau ou un objet de personnages, on l'indexe
            if (root.has("characters")) {
                JSONObject chars = root.getJSONObject("characters");
                for (String key : chars.keySet()) {
                    characterRegistry.put(key, chars.getJSONObject(key));
                }
            }
        } catch (Exception e) {
            System.err.println("[OMEGA-JSON] Erreur de cartographie : " + e.getMessage());
        }
    }

    /**
     * Le secret du Fallback : Détermine la langue à charger
     */
    public String getValidLocale(java.util.List<String> availableLocales) {
        // On cherche d'abord à matcher la langue du système de l'utilisateur (ex: récupérée par l'OS)
        String systemLanguage = java.util.Locale.getDefault().getLanguage(); // ex: "fr"
        
        if (availableLocales.contains(systemLanguage)) {
            return systemLanguage;
        }
        // Si la langue de l'OS n'est pas dispo ou a été supprimée, on force la langue d'usine
        return this.defaultLocale;
    }

    /**
     * Fournit instantanément la fiche d'un personnage à l'UMDParser
     */
    public JSONObject getCharacterData(String characterId) {
        return characterRegistry.get(characterId);
    }
}