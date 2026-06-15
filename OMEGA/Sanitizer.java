package org.ubk.omega.parser;

import java.util.regex.Pattern;

/**
 * Filtre de sécurité sanitaire d'OMEGA
 * Nettoie le texte brut de toute intrusion HTML/CSS/JS parasite
 */
public class Sanitizer {

    // 1. Les motifs de détection (Patterns Regex) définis à l'avance
    
    // Détecte n'importe quelle balise HTML (ex: <p>, <div style="...">, <script>)
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>");
    
    // Détecte les entités HTML courantes (ex: &nbsp;, &amp;, &lt;)
    private static final Pattern HTML_ENTITY_PATTERN = Pattern.compile("&[a-zA-Z0-9#]+;");
    
    // Détecte les tentatives d'injection de scripts ou d'URL suspectes (sécurité renforcée)
    private static final Pattern JAVASCRIPT_PATTERN = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);

    /**
     * Méthode principale : prend une ligne brute, la passe au karcher, et renvoie une ligne pure
     */
    public String sanitize(String rawLine) {
        if (rawLine == null || rawLine.trim().isEmpty()) {
            return "";
        }

        // Étape A : On applique la feuille de sécurité
        String cleanLine = rawLine;

        // 1. Destruction massive de toutes les balises HTML/CSS injectées
        if (HTML_TAG_PATTERN.matcher(cleanLine).find()) {
            // On remplace tout ce qui ressemble à du HTML par du vide
            cleanLine = HTML_TAG_PATTERN.matcher(cleanLine).replaceAll("");
        }

        // 2. Nettoyage des entités HTML résiduelles
        if (HTML_ENTITY_PATTERN.matcher(cleanLine).find()) {
            cleanLine = HTML_ENTITY_PATTERN.matcher(cleanLine).replaceAll(" ");
        }

        // 3. Neutralisation des scripts malveillants
        if (JAVASCRIPT_PATTERN.matcher(cleanLine).find()) {
            cleanLine = JAVASCRIPT_PATTERN.matcher(cleanLine).replaceAll("[BLOCKED_SCRIPT]");
        }

        // Étape B : Nettoyage des espaces blancs superflus en début et fin de ligne
        return cleanLine.trim();
    }
}