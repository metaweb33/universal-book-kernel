<div align="center">
  <h1>🌌 Universal Book Kernel (UBK)</h1>
  <h3>Project OMEGA | Next-Generation Semantic Reading Engine</h3>
</div>

<br/>

<table>
<tr>
<th width="50%">🇬🇧 English (Technical Vision)</th>
<th width="50%">🇫🇷 Français (Vision Technique)</th>
</tr>
<tr>
<td valign="top">

UBK (Project OMEGA) is not just another e-reader; it is a **Data-Driven Semantic Engine** built on top of standard Markdown. 

While Markdown handles standard text formatting (bold, italics, headers), the OMEGA Engine interprets **behavior, audio-visual context, and pedagogical interactivity** without modifying the core text structure.

### 🏗️ Core Architecture

**1. Markdown + Semantic Layer**
UBK utilizes a two-pass pipeline. Pass 1 handles standard Markdown formatting. Pass 2 intercepts custom OMEGA tags (e.g., `::🌩️` for a lightning strike) to trigger dynamic UI components or audio overlays.

**2. Data-Driven Configuration**
The engine logic is entirely decoupled from the source code via JSON:
* `rules.json`: Governs the narrative experience (Standard dialogue, SMS bubbles, visual layout, audio triggers).
* `syntax_rules.json`: Governs technical and pedagogical text behavior.

**3. The "U-Emoji" Pedagogical Switch**
UBK introduces a dual-reading mode for technical content. A developer can write raw code blocks (`{java}`), and by using the gamified tag (`{/ujava}`), the engine automatically translates keywords into visual emojis based on the dictionary, transforming a standard ebook into an interactive learning environment.

**4. Strict Tooling Separation**
The core reader (`omega-core.jar`) is lightweight and read-only. Editing is managed by decoupled, web-based local tools (Python/Flask) that generate the `.umd` files and update the JSON rules dynamically.

</td>
<td valign="top">

UBK (Projet OMEGA) n'est pas une simple liseuse ; c'est un **Moteur Sémantique Piloté par les Données** construit au-dessus du Markdown standard.

Là où le Markdown gère la mise en forme classique (gras, italique, titres), le moteur OMEGA interprète **le comportement, le contexte audio-visuel et l'interactivité pédagogique** sans altérer la structure du texte.

### 🏗️ Architecture Centrale

**1. Markdown + Couche Sémantique**
UBK utilise un pipeline à double passe. La passe 1 gère le formatage Markdown. La passe 2 intercepte les balises OMEGA (ex: `::🌩️` pour un coup de foudre) pour déclencher des composants d'interface ou des effets sonores.

**2. Configuration Pilotée par la Donnée**
La logique du moteur est totalement séparée du code source via JSON :
* `rules.json` : Gère l'expérience narrative (Dialogues, bulles SMS, mise en page, audio).
* `syntax_rules.json` : Gère le comportement des textes techniques et pédagogiques.

**3. La Bascule Pédagogique "U-Emoji"**
UBK introduces a dual-reading mode for technical content. A developer can write raw code blocks (`{java}`), and by using the gamified tag (`{/ujava}`), the engine automatically translates keywords into visual emojis based on the dictionary, transforming a standard ebook into an interactive learning environment.

**4. Séparation Stricte des Outils**
Le moteur de lecture (`omega-core.jar`) is lightweight and read-only. Editing is managed by decoupled, web-based local tools (Python/Flask) that generate the `.umd` files and update the JSON rules dynamically.

</td>
</tr>
</table>

---

## 📦 The `.ubk` Container Structure / Structure du Conteneur `.ubk`

The output format is a single `.ubk` file, which is a compressed ZIP archive containing the manuscript, localizations, and static media assets.

Le format de sortie est un fichier unique `.ubk`, qui est une archive ZIP compressée contenant le manuscrit, les traductions et les médias statiques.

```text
mon_livre.ubk (ZIP Archive)
├── mimetype                  # Archive identifier / Identifiant de l'archive (application/ubk+zip)
├── metadata.json              # Global book metadata / Métadonnées globales du livre
├── content.umd               # Main system router / L'aiguilleur système principal
├── assets/                   # Shared media / Médias communs (images, webp, mp3, flac, webm)
└── locales/                  # Removable language modules / Dossier des langues amovibles
    ├── fr/                   # French Localization Module
    │   ├── toc.json          # Chapter index router / Aiguilleur du dictionnaire de chapitres
    │   ├── dict.json         # Language variables / Variables et traductions spécifiques FR
    │   ├── characters.json   # Protagonists metadata / Métadonnées des personnages (ID, avatars)
    │   └── chapters/         # Fragmented narrative text / Dossier contenant le récit morcelé
    │       ├── ch1_introduction.umd
    │       ├── ch2_la_rencontre.umd
    │       └── ch3_la_distorsion.umd
    └── en/                   # English Localization Module
        ├── toc.json
        ├── dict.json
        ├── characters.json
        └── chapters/
            ├── ch1_introduction.umd
            ├── ch2_the_encounter.umd
            └── ch3_the_distortion.umd

```

📂 Source Project Tree / Arborescence Source (Développement)

```Plaintext
universal-book-kernel/
├── engine/                 # Core reading engine (Java) / Moteur de lecture
│   ├── src/
│   └── omega-core.jar      
├── tools/                  # Authoring & Editor tools (Python) / Outils d'édition
│   ├── rules_editor.py     # GUI to manage rules.json
│   └── char_manager.py     # GUI to manage characters database
├── workspace/              # Working directory for books / Espace de travail des livres avant compilation
│   └── blockchain-novel/   # Source folder before being zipped to .ubk / Dossier source avant compression
├── rules.json              # Main narrative configuration
└── syntax_rules.json       # Pedagogical/Code configuration

```
UBK (.umd format inside a .ubk container) provides a living, interactive environment with hot-swappable language packs. However, to ensure maximum commercial distribution, the UBK ecosystem will include a specific Converter to export standard .umd projects into clean, static ePub files (stripping out audio and dynamic UI but preserving the core text and Markdown layout).

UBK (format .umd encapsulé dans un conteneur .ubk) offre un environnement vivant et interactif avec des packs de langues amovibles à chaud. Néanmoins, pour garantir une distribution commerciale maximale, l'écosystème UBK inclura un Convertisseur spécifique pour exporter les projets vers des fichiers ePub propres et statiques (en retirant l'audio et l'UI dynamique, mais en préservant le texte et la mise en page).
