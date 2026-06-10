import json
import os
import re
import zipfile

# Configuration des constantes
CONFIG_FILE = "book.json"
OUTPUT_FILENAME = "book_output.ubk"
MIMETYPE_VALUE = "application/ubk+zip"  # L'identifiant officiel de ton format


def load_config():
    """Étape A1 : Charger et valider le fichier book.json"""
    if not os.path.exists(CONFIG_FILE):
        print(f"❌ Erreur : Le fichier de configuration '{CONFIG_FILE}' est introuvable à la racine.")
        return None
    
    try:
        with open(CONFIG_FILE, "r", encoding="utf-8") as f:
            return json.load(f)
    except json.JSONDecodeError as e:
        print(f"❌ Erreur : Le fichier '{CONFIG_FILE}' contient une erreur de syntaxe JSON : {e}")
        return None


def extract_tags(file_path):
    """Étape B1 : Extraire tous les identifiants {#p_...} et ^[note_...] d'un fichier UMD"""
    if not os.path.exists(file_path):
        print(f"❌ Erreur : Le fichier de texte '{file_path}' est introuvable.")
        return None, None

    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()

    # Expressions régulières pour attraper les paragraphes et les notes
    paragraphs = set(re.findall(r"\{#(p_\w+)\}", content))
    notes = set(re.findall(r"\^\[(note_\w+)\]", content))
    
    return paragraphs, notes


def validate_and_compile():
    print("🚀 Démarrage de la compilation UBK (Standard Mode)...")
    print("--------------------------------------------------")
    
    # 1. Chargement du JSON
    config = load_config()
    if not config:
        return

    # Récupération des fichiers déclarés dans le manifeste (Spine)
    spine = config.get("manifest", {}).get("spine", [])
    if not spine:
        print("❌ Erreur : Le manifeste ne contient aucun chapitre dans la section 'spine'.")
        return

    # On commence la liste des fichiers physiques à inclure APRES le mimetype
    all_files_to_pack = [CONFIG_FILE]
    error_detected = False

    # 2. Validation croisée des fichiers UMD (Vérification du parallélisme)
    print("🔍 Analyse et alignement des fichiers de texte...")
    for item in spine:
        files = item.get("files", {})
        if "FR" not in files or "EN" not in files:
            print(f"❌ Erreur : Le chapitre '{item.get('id')}' doit contenir à la fois une version 'FR' et 'EN'.")
            return
        
        fr_path = files["FR"]
        en_path = files["EN"]

        all_files_to_pack.extend([fr_path, en_path])

        # Extraction des identifiants uniques
        fr_p, fr_n = extract_tags(fr_path)
        en_p, en_n = extract_tags(en_path)

        if fr_p is None or en_p is None:
            return

        # Vérification des paragraphes
        if fr_p != en_p:
            error_detected = True
            missing_in_en = fr_p - en_p
            missing_in_fr = en_p - fr_p
            if missing_in_en:
                print(f"🛑 Désalignement : Paragraphe(s) {missing_in_en} présent(s) en FR mais manquant(s) en EN dans {en_path}")
            if missing_in_fr:
                print(f"🛑 Désalignement : Paragraphe(s) {missing_in_fr} présent(s) en EN mais manquant(s) en FR dans {fr_path}")

        # Vérification des notes
        if fr_n != en_n:
            error_detected = True
            missing_notes_in_en = fr_n - en_n
            missing_notes_in_fr = en_n - fr_n
            if missing_notes_in_en:
                print(f"🛑 Désalignement : Note(s) {missing_notes_in_en} présente(s) en FR mais manquante(s) en EN.")
            if missing_notes_in_fr:
                print(f"🛑 Désalignement : Note(s) {missing_notes_in_fr} présente(s) en EN mais manquante(s) en FR.")

    if error_detected:
        print("\n🛑 Compilation stoppée : Des erreurs d'alignement bilingue ont été détectées.")
        return

    print("✅ Alignement bilingue parfait ! Aucun paragraphe ni note orpheline.")

    # 3. Création de l'archive finale (ZIP_STORED)
    print(f"\n📦 Packaging de l'archive sans compression dans '{OUTPUT_FILENAME}'...")
    try:
        with zipfile.ZipFile(OUTPUT_FILENAME, "w", zipfile.ZIP_STORED) as ubk_zip:
            
            # CRUCIAL : Injection du fichier 'mimetype' en TOUT PREMIER position
            ubk_zip.writestr("mimetype", MIMETYPE_VALUE)
            print(f" ➕ Injecté en premier : mimetype ({MIMETYPE_VALUE})")
            
            # Ajout des autres fichiers du projet
            for file in all_files_to_pack:
                if os.path.exists(file):
                    ubk_zip.write(file)
                    print(f" ➕ Ajouté : {file}")
                else:
                    print(f"⚠️ Attention : Le fichier optionnel '{file}' n'existe pas, ignoré.")
        
        print("--------------------------------------------------")
        print(f"🎉 Succès ! Ton conteneur standardisé a été généré : {OUTPUT_FILENAME}")
        
    except Exception as e:
        print(f"❌ Erreur lors de la création de l'archive : {e}")


if __name__ == "__main__":
    validate_and_compile()
