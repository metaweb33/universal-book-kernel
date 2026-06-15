import json
import re

class UBKParser:
    """
    ============================================================================
    UBK PARSER - UMD & OMEGA ENGINE (Python Implementation)
    ============================================================================
    EN: Parses Universal Markdown (UMD) and OMEGA matrices into semantic HTML5.
    FR: Analyse le Markdown Universel (UMD) et les matrices OMEGA en HTML5 sémantique.
    ============================================================================
    """
    
    def __init__(self, omega_path, ui_rules_path, umd_rules_path):
        # EN: Load all three flat matrices / FR: Chargement des trois matrices plates
        self.omega_header, self.omega_matrix = self._load_matrix(omega_path, "omega_master_matrix")
        self.ui_header, self.ui_matrix = self._load_matrix(ui_rules_path, "rules_matrix")
        self.umd_header, self.umd_matrix = self._load_matrix(umd_rules_path, "umd_matrix")
        
        # EN: Compile regex patterns directly from the UMD matrix
        # FR: Compilation des motifs regex directement depuis la matrice UMD
        self.umd_regex_patterns = []
        regex_col_idx = self._get_index(self.umd_header, "regex")
        
        for row in self.umd_matrix:
            if row[regex_col_idx]:  # Si la cellule regex n'est pas nulle
                self.umd_regex_patterns.append({
                    "id": row[0],
                    "pattern": re.compile(row[regex_col_idx]),
                    "row_data": row
                })

    def _load_matrix(self, filepath, matrix_key):
        """EN: Helper to load matrix / FR: Utilitaire pour charger une matrice"""
        with open(filepath, 'r', encoding='utf-8') as f:
            data = json.load(f)
            return data.get("header", []), data.get(matrix_key, [])

    def _get_index(self, header_list, key):
        """EN: Fast index resolution / FR: Résolution d'index rapide"""
        try:
            return header_list.index(key)
        except ValueError:
            return 1 

    def parse_umd_line(self, raw_line, output_target="roman"):
        """
        EN: Parses a raw line against the UMD regex patterns. Target: "roman" or "sms".
        FR: Analyse une ligne brute selon les motifs regex UMD. Cible: "roman" ou "sms".
        """
        for umd_rule in self.umd_regex_patterns:
            match = umd_rule["pattern"].match(raw_line)
            if match:
                row = umd_rule["row_data"]
                
                # EN: Extract text content (always the last Regex group)
                # FR: Extraction du contenu textuel (toujours le dernier groupe Regex)
                content = match.group(len(match.groups())) if match.groups() else ""
                
                # --- RENDU MODE ROMAN (Lecture traditionnelle) ---
                if output_target == "roman":
                    prefix_idx = self._get_index(self.umd_header, "roman_prefix")
                    align_idx = self._get_index(self.umd_header, "roman_alignment")
                    style_idx = self._get_index(self.umd_header, "roman_style")
                    
                    prefix = row[prefix_idx] if row[prefix_idx] else ""
                    alignment = row[align_idx] if row[align_idx] else "left"
                    style_class = row[style_idx] if row[style_idx] else "normal"
                    
                    return f"<p class='ubk-roman {style_class}' style='text-align: {alignment};'>{prefix}{content}</p>"
                    
                # --- RENDU MODE SMS (Visual Novel / Chat) ---
                elif output_target == "sms":
                    comp_idx = self._get_index(self.umd_header, "sms_component")
                    fx_idx = self._get_index(self.umd_header, "sms_visual_fx")
                    
                    component = row[comp_idx] if row[comp_idx] else "standard_bubble"
                    visual_fx = row[fx_idx] if row[fx_idx] else "none"
                    
                    return f"<div class='ubk-sms {component}' data-fx='{visual_fx}'>{content}</div>"

        # EN: Fallback if no UMD tag matched / FR: Repli par défaut si aucune balise ne correspond
        return f"<p>{raw_line}</p>"