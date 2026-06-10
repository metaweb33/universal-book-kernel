# 🚀 Universal Book Kernel (UBK) & Ultra Markdown (UMD)

*Read this in: [English](#-universal-book-kernel-ubk--ultra-markdown-umd) | [Français](#-universal-book-kernel-ubk--ultra-markdown-umd-fr)*

---

The universal, open, eco-designed document standard featuring native bilingual mapping, multi-voice text-to-speech casting, and adaptive markdown structures.

## 📌 Project Vision

Current digital publishing formats are obsolete or overly complex. **PDFs** remain rigid and completely unsuited for modern, responsive screens. **EPUB3** has morphed into a heavy ecosystem, cluttering file containers with duplicated HTML files, intrusive JavaScript, and bloated CSS stylesheets that cause e-readers to lag.

**UBK (Universal Book Kernel)** reinvents the document and digital book architecture around a simple triptyque: **Absolute lightness (zero data duplication), Universal structure, and Progressive enhancement.**

At the core of UBK is **UMD (Ultra Markdown)**: a syntax fully backward-compatible with standard Markdown, but augmented with invisible semantic metadata. These metadata unlock groundbreaking reading features (dynamic multi-voice audio, synchronous bilingual display, intelligent popups) while maintaining a clean text base.

---

## ✨ Key Features (Phase 1 Specifications)

### 1. Progressive Enhancement
An `.umd` file is, first and foremost, a plain text file.
* Open it in any traditional Markdown editor (Obsidian, VS Code, GitHub): **the text remains 100% readable** in its raw form (headings `#` remain headings, bold remains bold).
* Open it inside the **UBK Reader**: the engine interprets the semantic tags and deploys the full interactive experience.

### 2. Synchronous Multilinguism ("The Empty Shell")
No more alignment nightmares for bilingual books or translations. The UMD format implements a unique **Mapping by ID** system. Every paragraph or dialogue line is indexed invisibly. The translator simply fills an "empty shell" mirroring the exact same identifiers.

* **Source File (`chapter1_FR.umd`)**:
  ```markdown
  {#p_001} La Brume s'avançait lentement sur le Marbre de la cité.

```

* **Mirror File (`chapter1_EN.umd`)**:
```markdown
{#p_001} The Mist was slowly advancing over the Marble of the city.

```



The UBK Reader maps these IDs to render them pixel-perfect side-by-side on split screens, or to allow instant language switching mid-read without losing the reader's focus.

### 3. Invisible Audio Casting (Multi-Voice TTS)

UMD natively handles dialogue structures for augmented Text-to-Speech. Authors tag characters directly inside the text. In audio mode, the reading engine automatically switches voices (Narrator, Character A, Character B) to generate a full audio play experience, with zero visual clutter on the screen.

```markdown
- [Marc] Is the conversion script ready yet?
- [Lucie] Almost, compilation is underway.

```

### 4. 3-Tier Connected Footnotes

Footnotes no longer disrupt the reading flow. The UBK Reader analyzes the text length of the footnote dynamically to adapt the UI:

* **Tier 1 (< 100 characters)**: A lightweight, transient popup bubble on hover or click.
* **Tier 2 (101 to 200 characters)**: A contextual drawer modal with a scrolling behavior.
* **Tier 3 (> 200 characters)**: A distraction-free full-screen overlay with a clean `X` close button.

---

## 📂 Internal Container Architecture

The `.ubk` (or `.epub4`) container is a compressed archive packaged in **Stored** mode (`ZIP_STORED`). Files undergo zero internal compression, allowing any smartphone or e-reader CPU to read raw text straight from memory without any decompression overhead.

```text
📂 my-book.ubk
├── 📄 mimetype          # Raw MIME type: application/ubk+zip
├── 📄 book.json         # Global contract (Metadata, languages, AI voice casting)
├── 📂 Text/
│   ├── 📄 ch1_FR.umd    # Original text in Ultra Markdown
│   ├── 📄 ch1_EN.umd    # Translated version aligned on identical IDs
│   └── 📄 notes.umd     # Centralized footnotes and references registry
├── 📂 Media/
│   └── 🖼️ image1.jpg    # Eco-designed hack: ultra-light WebP hidden under .jpg extension
└── 📂 Style/
    └── 🎨 theme.css     # Universal unique stylesheet (Adaptive Grid/Flexbox)

```

---

## 🗺️ Roadmap

The UBK ecosystem is deployed progressively and modularly:

* [x] **Phase 1: UMD & UBK Specifications** (Current) — Defining the text grammar, linguistic rules, audio casting, and container file tree.
* [ ] **Phase 2: The Universal Reader** — Development of the rendering engine capable of parsing the archive, handling visual themes (Dark Mode / Minimalist Reader Mode), and triggering the multi-voice TTS audio engine.
* [ ] **Phase 3: The Virtuoso Editor** — Creation interface for authors (featuring the "Show All" hidden characters toggle, and AI-assisted importing to auto-parse standard manuscripts).
* [ ] **Phase 4: Complete Interactive UDS Module** — Integration of the **Ultra Data Structure (UDS)** to turn text documents into dynamic data interfaces (mail merges, templates, SHA256 security hashing).

---

## 🤝 Contributing

The UBK project is an open standard. Whether you are a software engineer, UI/UX designer, or digital publishing enthusiast, feel free to open an Issue to discuss the UMD grammar or submit pull requests regarding the container architecture.

Let's build the lightest, cleanest, and most powerful document engine on the Web.

---

# 🚀 Universal Book Kernel (UBK) & Ultra Markdown (UMD) [FR]

Le standard de document universel, ouvert, éco-conçu, nativement bilingue et taillé pour l'immersion interactive.

## 📌 Vision du Projet

Les formats d'édition actuels sont obsolètes ou trop lourds. Le **PDF** reste figé et inadapté aux écrans modernes. L'**EPUB3** est devenu une usine à gaz, surchargeant des conteneurs de fichiers HTML dupliqués, de JavaScript intrusif et de feuilles CSS lourdes qui font ramer les liseuses.

**UBK (Universal Book Kernel)** réinvente l'écosystème du document et du livre numérique autour d'un triptyque simple : **Légèreté absolue (zéro duplication), Structure universelle et Amélioration progressive.**

À la base de l'UBK se trouve l'**UMD (Ultra Markdown)** : une syntaxe textuelle standard, entièrement compatible avec le Markdown classique, mais augmentée de métadonnées sémantiques invisibles qui activent des fonctionnalités de lecture inédites (audio multi-voix, affichage bilingue synchrone, infobulles intelligentes).

---

## ✨ Fonctionnalités Clés (Spécifications de l'Étape 1)

### 1. Amélioration Progressive (Progressive Enhancement)

Un fichier `.umd` est avant tout un fichier texte.

* Ouvrez-le dans n'importe quel éditeur Markdown classique (Obsidian, VS Code, GitHub) : **le texte reste 100 % lisible** sous sa forme brute.
* Ouvrez-le dans le **Reader UBK** : le moteur interprète les balises sémantiques et déploie l'expérience interactive complète.

### 2. Multilinguisme Synchrone ("La Coquille Vide")

Fini les galères d'alignement pour les livres bilingues ou les traductions. Le format UMD utilise un **Mapping par ID** unique. Chaque paragraphe ou réplique est indexé de manière invisible. Le traducteur n'a qu'à remplir une "coquille vide" reprenant les mêmes identifiants.

* **Fichier Source (`chapitre1_FR.umd`)** :
```markdown
{#p_001} La Brume s'avançait lentement sur le Marbre de la cité.

```


* **Fichier Miroir (`chapitre1_EN.umd`)** :
```markdown
{#p_001} The Mist was slowly advancing over the Marble of the city.

```



Le Reader UBK utilise ces identifiants pour aligner le texte au pixel près sur un écran divisé ou pour permettre une bascule de langue instantanée en cours de lecture, sans perte de focus.

### 3. Casting Invisible (Audio TTS Multi-Voix)

L'UMD intègre nativement la gestion des dialogues pour le *Text-to-Speech* augmenté. L'auteur attribue un personnage à chaque réplique. En mode audio, le moteur de lecture bascule dynamiquement de voix (Narrateur, Personnage A, Personnage B) pour créer une véritable pièce de théâtre audio, sans aucune fioriture visible à l'écran.

```markdown
- [Marc] Est-ce que le script de conversion est prêt ?
- [Lucie] Presque, la compilation est en cours.

```

### 4. Footnotes Connectées à 3 Paliers

Les notes de bas de page ne coupent plus le rythme de lecture. Le Reader UBK analyse dynamiquement la longueur du texte de la note pour adapter l'affichage :

* **Palier 1 (< 100 caractères)** : Une simple infobulle éphémère au clic/survol.
* **Palier 2 (101 à 200 caractères)** : Une fenêtre contextuelle avec ascenseur (scroll).
* **Palier 3 (> 200 caractères)** : Un mode plein écran épuré avec une croix `X` de fermeture.

---

## 📂 Architecture interne de l'archive UBK

Le conteneur `.ubk` (ou `.epub4`) est une archive compressée en mode **Stored** (`ZIP_STORED`). Les fichiers ne subissent aucune compression interne, ce qui permet au processeur de n'importe quel smartphone ou liseuse d'accéder instantanément au texte brut en mémoire, sans latence de décompression.

```text
📂 mon-livre.ubk
├── 📄 mimetype          # Type MIME brut : application/ubk+zip
├── 📄 book.json         # Contrat global (Métadonnées, langues, casting des voix IA)
├── 📂 Text/
│   ├── 📄 cap1_FR.umd   # Texte original en Ultra Markdown
│   ├── 📄 cap1_EN.umd   # Version traduite alignée sur les mêmes ID
│   └── 📄 notes.umd     # Registre centralisé des notes et références
├── 📂 Media/
│   └── 🖼️ image1.jpg    # Hack éco-conçu : fichier WebP ultra-léger masqué sous extension .jpg
└── 📂 Style/
    └── 🎨 theme.css     # Feuille de style universelle unique (Grid/Flexbox adaptatif)

```

---

## 🗺️ Feuille de Route (Roadmap)

Le déploiement de l'écosystème UBK se fait de manière progressive et modulaire :

* [x] **Étape 1 : Spécifications UMD & UBK** (Dépôt actuel) — Fixation de la grammaire textuelle, des règles liguistiques, du casting audio et de l'arborescence du conteneur.
* [ ] **Étape 2 : Le Reader Universel** — Développement du moteur de rendu capable d'interpréter l'archive, de gérer les modes visuels (Mode Sombre / Mode Sobre épuré) et le moteur audio TTS multivoix.
* [ ] **Étape 3 : L'Éditeur Virtuose (Introduction à l'UDS)** — Interface de création pour les auteurs (avec bouton "Afficher tout" pour voir les balises, importation assistée par IA pour baliser automatiquement les dialogues des vieux manuscrits).
* [ ] **Étape 4 : Le Module UDS Interactif Complet** — Intégration de l'**Ultra Data Structure (UDS)** pour transformer le document texte en interface de données dynamique (génération de factures, publipostage par templates JSON/CSV, signature de sécurité SHA256 pour geler les documents).

---

## 🤝 Contribuer

Le projet UBK est un standard ouvert. Si vous êtes développeur, designer d'interface ou passionné d'édition numérique, n'hésitez pas à ouvrir une *Issue* pour discuter de la grammaire de l'UMD ou à proposer des améliorations sur la structure du conteneur.

Bâtissons ensemble le moteur de document le plus léger, vertueux et puissant du Web.

```

***
