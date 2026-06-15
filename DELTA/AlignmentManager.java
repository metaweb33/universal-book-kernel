package com.ubk.core.alignment;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * ARCHITECTURE DELTA - UNIVERSAL BOOK KERNEL (UBK)
 * ============================================================================
 * EN: Manages bilingual/trilingual alignment and text synchronization without
 * modifying raw manuscripts. Implements high-performance mapping for Hot-Swap.
 * FR: Gère l'alignement bilingue/trilingue et la synchronisation du texte sans
 * modifier les manuscrits bruts. Implémente un mapping haute performance pour le Hot-Swap.
 *
 * Licensed under the Apache License, Version 2.0 (GitHub compliant)
 * ============================================================================
 */
public class AlignmentManager {

    // EN: Nested Map structure: ChapterID -> (SourceLineNumber -> TargetLineRangeOrIndex)
    // FR: Structure de Map imbriquée : ID_Chapitre -> (NuméroLigneSource -> PlageOuIndexCible)
    private final Map<String, Map<Integer, String>> alignmentExceptions;

    /**
     * EN: Initializes the global structural alignment registry.
     * FR: Initialise le registre global d'alignement structurel.
     */
    public AlignmentManager() {
        this.alignmentExceptions = new HashMap<>();
    }

    /**
     * EN: Loads alignment exceptions from a JSON-like raw string snippet.
     * Designed for zero-dependency embedded environments (pure parsing loop).
     * FR: Charge les exceptions d'alignement depuis une chaîne brute de type JSON.
     * Conçu pour les environnements embarqués sans dépendance externe (boucle pure).
     *
     * @param chapterId EN: Unique identification key for the current book chapter.
     * FR: Clé d'identification unique du chapitre en cours.
     * @param rawJsonSnippet EN: Raw content string under the "alignment_exceptions" node.
     * FR: Contenu textuel brut sous le nœud "alignment_exceptions".
     */
    public void loadAlignmentExceptions(String chapterId, String rawJsonSnippet) {
        Map<Integer, String> chapterMap = alignmentExceptions.get(chapterId);
        if (chapterMap == null) {
            chapterMap = new HashMap<>();
            alignmentExceptions.put(chapterId, chapterMap);
        }

        if (rawJsonSnippet == null || rawJsonSnippet.trim().isEmpty()) {
            return;
        }

        try {
            // EN: Clean standard structural characters for manual high-speed tokenization
            // FR: Nettoyage des caractères de structure standard pour une tokenisation manuelle rapide
            String clean = rawJsonSnippet.replaceAll("[{}\"\\s]", "");
            String[] pairs = clean.split(",");

            for (String pair : pairs) {
                if (!pair.contains(":")) continue;
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    Integer srcLine = Integer.parseInt(parts[0].trim());
                    String targetRange = parts[1].trim();
                    chapterMap.put(srcLine, targetRange);
                }
            }
        } catch (Exception e) {
            // EN: Graceful failure recording for open source continuous integration
            // FR: Journalisation transparente des échecs d'analyse pour l'intégration continue GitHub
            System.err.println("[UBK ERROR] Failed to parse delta alignment block: " + e.getMessage());
        }
    }

    /**
     * EN: Resolves the target indices for the reader UI when a 2-finger swipe occurs.
     * FR: Résout les index cibles pour l'interface du lecteur lors d'un glissement à 2 doigts.
     *
     * @param chapterId EN: Active chapter context identifier.
     * FR: Identifiant du contexte de chapitre actif.
     * @param currentSourceLine EN: The line index currently displayed in the source language.
     * FR: L'index de ligne actuellement affiché dans la langue source.
     * @return EN: List of integers representing matching line indices in the target document.
     * FR: Liste d'entiers représentant les index de lignes correspondants dans le document cible.
     */
    public List<Integer> getTargetLines(String chapterId, int currentSourceLine) {
        List<Integer> targetLines = new ArrayList<>();
        Map<Integer, String> chapterMap = alignmentExceptions.get(chapterId);

        // EN: Check if an exception exists for this specific line index
        // FR: Vérifier si une exception structurelle existe pour cet index de ligne spécifique
        if (chapterMap != null && chapterMap.containsKey(currentSourceLine)) {
            String range = chapterMap.get(currentSourceLine);

            if (range.contains("-")) {
                // EN: Splitting/Césure handling: maps 1 line to multiple target lines (e.g., "13-14")
                // FR: Gestion des césures : associe 1 ligne à plusieurs lignes cibles (ex: "13-14")
                String[] bounds = range.split("-");
                int start = Integer.parseInt(bounds[0]);
                int end = Integer.parseInt(bounds[1]);
                for (int i = start; i <= end; i++) {
                    targetLines.add(i);
                }
            } else {
                // EN: Direct mapping anomaly override
                // FR: Remplacement direct d'anomalie de mapping
                targetLines.add(Integer.parseInt(range));
            }
        } else {
            // EN: Default Mode: Strict structural 1-to-1 linear correspondence rule
            // FR: Mode par défaut : Règle de correspondance linéaire stricte 1 pour 1
            targetLines.add(currentSourceLine);
        }
        return targetLines;
    }

    /**
     * EN: Clear cached alignment map entries to prevent memory leaks during book switching.
     * FR: Vide les entrées de mapping du cache pour éviter les fuites mémoire lors du changement de livre.
     */
    public void clearCache() {
        this.alignmentExceptions.clear();
    }
}
