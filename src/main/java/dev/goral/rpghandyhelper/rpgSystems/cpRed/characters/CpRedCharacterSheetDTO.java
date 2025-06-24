package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterClasses.CpRedCharacterClassesSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterSkills.CpRedCharacterSkillsSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterStats.CpRedCharacterStatsSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeaponSheetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterSheetDTO {
    private Long characterId;
    // ===== Podstawowe informacje =====
    private String photoPath; // Ścieszka do zdjęcia postaci z CP_RED_CHARACTERS
    private String name; // Ksywa z CP_RED_CHARACTERS
    private List<CpRedCharacterClassesSheetDTO> characterClasses; // lista klas postaci z umiejętnościami specjalnymi i jej poziomami z CP_RED_CHARACTER_CLASSES
    private String note; // notatki z CP_RED_CHARACTER_OTHER_INFO
    // ===== Podstawowe statystyki =====
    private Map<Integer, Integer> humanity; // Człowieczeństwo (aktualna, maksymalna) TODO: zrobić to wogóle bo nie ma w CP_RED_CHARACTER
    private Map<Integer, Integer> hitPoints; // Punkty wytrzymałości (aktualna, maksymalna) TODO: zrobić to wogóle bo nie ma CP_RED_CHARACTER
    private Integer seriouslyWounded; // Poważnie ranny TODO: zrobić to wogóle bo nie ma CP_RED_CHARACTER
    private Integer survivability; // Przeżywalność TODO: zrobić to wogóle bo nie ma CP_RED_CHARACTER
    private List<String> criticalInjuries; // Krytyczne urazy, lista nazw z CP_RED_CHARACTER_CRITICAL_INJURIES i z CP_RED_CHARACTER_CUSTOM_CRITICAL_INJURIES
    private String addictions; // Uzależnienia, to jest jedna notatka
    private List<CpRedCharacterStatsSheetDTO> stats; // Cechy, lista cech z CP_RED_CHARACTERS_STATS
    // ===== Umiejętności =====
    private List<List<CpRedCharacterSkillsSheetDTO>> skills; // Umiejętności, podzielone na podkategorie (każda podkategoria to osobna lista) z CP_RED_CHARACTER_SKILLS
    private List<CpRedCharacterWeaponSheetDTO> weapons; // Lista broni i customowych broni z CP_RED_CHARACTER_WEAPON i z CP_RED_CHARACTER_CUSTOM_WEAPON
    private List<> armor; // Lista założonych pancerzy z CP_RED_CHARACTER_ARMOR i z CP_RED_CHARACTER_CUSTOM_ARMOR
    // ===== Wyposażenie =====
}
