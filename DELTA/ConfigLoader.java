package com.ubk.core.config;

import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * CONFIG LOADER - UNIVERSAL BOOK KERNEL (UBK) / OMEGA
 * ============================================================================
 * EN: Dynamic configuration loader for OMEGA rules matrix (omega_master_matrix).
 * FR: Chargeur de configuration dynamique pour la matrice de règles OMEGA.
 *
 * Licensed under the Apache License, Version 2.0 (GitHub compliant)
 * ============================================================================
 */
public class ConfigLoader {

    // EN: Nested Map: Language Context -> (Concept/Emoji -> Localized Code)
    // FR: Map imbriquée : Contexte de Langage -> (Concept/Émoji -> Code localisé)
    private final Map<String, Map<String, String>> masterMatrix;

    // EN: Tracks the currently active language layer for the reader
    // FR: Garde en mémoire la couche de langage actuellement active pour le lecteur
    private String currentLanguageContext;

    /**
     * EN: Initializes the ConfigLoader with default safety fallbacks.
     * FR: Initialise le ConfigLoader avec des valeurs de repli de sécurité par défaut.
     */
    public ConfigLoader() {
        this.masterMatrix = new HashMap<>();
        this.currentLanguageContext = "Python"; // Fallback par défaut (langage pivot)
    }

    /**
     * EN: Sets the active language context based on inline OMEGA tags.
     * FR: Définit le contexte de langage actif basé sur les balises OMEGA en ligne.
     *
     * @param tag EN: The language tag extracted from text (e.g., "!!java", "!!py").
     *            FR: La balise de langage extraite du texte (ex: "!!java", "!!py").
     */
    public void setLanguageContextFromTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) return;

        // EN: Clean the tag for switch evaluation
        // FR: Nettoie la balise pour l'évaluation du switch
        String cleanTag = tag.trim().toLowerCase().replace("!!", "");

        switch (cleanTag) {
            case "java":
                this.currentLanguageContext = "Java";
                break;
            case "py":
            case "python":
                this.currentLanguageContext = "Python";
                break;
            case "js":
            case "javascript":
                this.currentLanguageContext = "JavaScript/TS";
                break;
            case "cpp":
            case "c++":
                this.currentLanguageContext = "C++";
                break;
            case "rs":
            case "rust":
                this.currentLanguageContext = "Rust";
                break;
            case "go":
                this.currentLanguageContext = "Go";
                break;
            default:
                // EN: Unrecognized tag: keep current context to avoid disruption
                // FR: Balise non reconnue : on conserve le contexte actuel pour éviter les coupures
                System.out.println("[UBK WARN] Unrecognized language tag: " + tag);
                break;
        }
    }

    /**
     * EN: Lightweight JSON parser loop to load the matrix without heavy dependencies.
     * FR: Boucle d'analyse JSON allégée pour charger la matrice sans dépendances lourdes.
     *
     * @param rawJsonSnippet EN: Raw JSON string from omega_master_matrix.
     *                       FR: Chaîne JSON brute de la matrice omega_master_matrix.
     */
    public void loadMatrix(String rawJsonSnippet) {
        // EN: Note for implementation: Apply the same high-speed string tokenization
        // as used in AlignmentManager.java to map concepts to localized code.
        // FR: Note d'implémentation : Appliquer la même tokenisation de chaîne à haute vitesse
        // que celle utilisée dans AlignmentManager.java pour mapper les concepts au code localisé.
    }

    /**
     * EN: Retrieves the language-specific mapping for a given OMEGA concept.
     * FR: Récupère la correspondance spécifique au langage pour un concept OMEGA.
     *
     * @param conceptKey EN: The concept name or Emoji (e.g., "🖨️" or "Print").
     *                   FR: Le nom du concept ou l'Émoji (ex: "🖨️" ou "Print").
     * @return EN: Mapped code snippet, or a fallback string if missing.
     *         FR: Bouton de code correspondant, ou une chaîne de repli si manquant.
     */
    public String getMapping(String conceptKey) {
        Map<String, String> langMap = masterMatrix.get(this.currentLanguageContext);

        if (langMap != null && langMap.containsKey(conceptKey)) {
            return langMap.get(conceptKey);
        }

        return "[Missing Concept: " + conceptKey + " in " + this.currentLanguageContext + "]";
    }

    /**
     * EN: Returns the current language context for debugging UI.
     * FR: Retourne le contexte de langage actuel pour le débogage de l'UI.
     */
    public String getCurrentLanguageContext() {
        return this.currentLanguageContext;
    }
}
