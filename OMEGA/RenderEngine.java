package org.ubk.omega.render;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ubk.omega.parser.UMDParser.ParsedLine;

/**
 * Le Projecteur Universel d'OMEGA
 * Ne contient plus aucun style en dur. Il exécute les directives du rules.json.
 */
public class RenderEngine {

    private String mode; // "roman", "sms", etc.
    
    // Le dictionnaire de mise en page en mémoire vive : Symbole -> Instructions de rendu
    // Ex: "🌩️" -> { "roman": { "style": "italic" }, "sms": { "audio": "thunder.wav" } }
    private Map<String, JSONObject> renderBehaviors = new HashMap<>();

    public RenderEngine(String mode) {
        this.mode = mode.toLowerCase(); // On normalise le mode en minuscules
    }

    /**
     * MÉTHODE CLÉ : Charge les directives graphiques et sonores au démarrage
     */
    public void loadRules(JSONArray tagsArray) {
        renderBehaviors.clear();
        
        for (int i = 0; i < tagsArray.length(); i++) {
            JSONObject tagData = tagsArray.getJSONObject(i);
            String symbol = tagData.getString("symbol");
            
            // Si la balise possède un bloc "render", on le stocke en RAM
            if (tagData.has("render")) {
                renderBehaviors.put(symbol, tagData.getJSONObject("render"));
            }
        }
        System.out.println("[RENDER] Directives graphiques chargées pour " + renderBehaviors.size() + " balises.");
    }

    /**
     * Le chef d'orchestre visuel : applique le rendu selon la règle chargée
     */
    public void render(ParsedLine line) {
        // 1. Narration pure (aucun symbole associé)
        if ("TEXT".equals(line.type)) {
            System.out.println(line.content);
            return;
        }

        // 2. Recherche des consignes pour la balise (ex: on cherche "💬" ou "🌩️")
        JSONObject tagRules = renderBehaviors.get(line.idOrIcon);

        // Si la balise est inconnue ou n'a pas de règle de rendu, on affiche le texte brut
        if (tagRules == null || !tagRules.has(mode)) {
            System.out.println(line.content);
            return;
        }

        // 3. Extraction des instructions spécifiques au mode en cours (roman ou sms)
        JSONObject modeInstructions = tagRules.getJSONObject(mode);

        // -- APPLICATION DYNAMIQUE DES EFFETS --
        
        // A. Traitement Audio (Exclusif au mode SMS/Interactif généralement)
        if (modeInstructions.has("audio_trigger")) {
            String audioFile = modeInstructions.getString("audio_trigger");
            System.out.println("[AUDIO-PLAYER] Lecture du fichier : " + audioFile);
        }
        
        if (modeInstructions.has("visual_fx")) {
            String fx = modeInstructions.getString("visual_fx");
            System.out.println("[VISUAL-FX] Déclenchement de l'effet : " + fx);
        }

        // B. Traitement Typographique (Préfixes et Styles)
        String prefix = modeInstructions.optString("prefix", "");
        String style = modeInstructions.optString("style", "normal");
        
        // Construction de la ligne finale
        String outputLine = prefix + line.content;
        
        // Simulation d'application de style dans la console (en UI réelle, on modifie la police)
        if ("italic".equals(style)) {
            outputLine = "*" + outputLine + "*";
        } else if ("center".equals(style)) {
            outputLine = "    " + outputLine + "    ";
        }

        // C. Affichage final
        if (modeInstructions.has("component") && "bubble".equals(modeInstructions.getString("component"))) {
            // Rendu spécifique UI Complexe (Bulle SMS)
            System.out.println("[BULLE UI dynamique] " + outputLine);
        } else {
            // Rendu Littéraire Standard
            System.out.println(outputLine);
        }
    }
}