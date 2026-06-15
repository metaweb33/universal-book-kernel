package org.ubk.omega.io;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire d'ouverture chirurgicale du conteneur UBK
 * Extrait les données directement en RAM sous forme de Flux (Streams)
 */
public class Unzipper {

    private File ubkFile;

    public Unzipper(File file) {
        this.ubkFile = file;
    }

    /**
     * Scanne l'archive pour lister dynamiquement les langues disponibles (Dossier locales/)
     */
    public List<String> getAvailableLocales() throws Exception {
        List<String> locales = new ArrayList<>();
        
        // On ouvre l'archive comme un flux ZIP linéaire
        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(ubkFile))) {
            ZipEntry entry;
            
            while ((entry = zip.getNextEntry()) != null) {
                String name = entry.getName();
                // Si l'entrée est un dossier situé dans "locales/" (ex: locales/fr/)
                if (name.startsWith("locales/") && name.endsWith("/")) {
                    // On extrait le code langue (ex: "fr", "en", "it")
                    String[] pathParts = name.split("/");
                    if (pathParts.length == 2) {
                        locales.add(pathParts[1]);
                    }
                }
            }
        }
        return locales;
    }

    /**
     * Ouvre et renvoie un flux direct sur le fichier de configuration racine
     */
    public InputStream getMetadataStream() throws Exception {
        return getFileStreamFromZip("metadata.json");
    }

    /**
     * Ouvre et renvoie un flux direct sur un chapitre spécifique selon la langue active
     */
    public InputStream getChapterStream(String locale, int chapterNumber) throws Exception {
        // Construction du chemin dynamique (ex: locales/fr/chapters/ch1.umd)
        String chapterPath = "locales/" + locale + "/chapters/ch" + chapterNumber + ".umd";
        return getFileStreamFromZip(chapterPath);
    }

    /**
     * Méthode chirurgicale : cherche un fichier précis dans le ZIP et l'isole en RAM
     */
    private InputStream getFileStreamFromZip(String targetFilePath) throws Exception {
        FileInputStream fis = new FileInputStream(ubkFile);
        ZipInputStream zip = new ZipInputStream(fis);
        ZipEntry entry;

        // On parcourt l'archive jusqu'à trouver le fichier cible
        while ((entry = zip.getNextEntry()) != null) {
            if (entry.getName().equals(targetFilePath)) {
                // TROUVÉ ! On renvoie le flux d'octets direct sans écrire sur le disque
                return zip; 
            }
        }

        // Si on arrive ici, c'est que le fichier est manquant dans le conteneur
        zip.close();
        throw new Exception("Fichier critique manquant dans l'UBK : " + targetFilePath);
    }
}