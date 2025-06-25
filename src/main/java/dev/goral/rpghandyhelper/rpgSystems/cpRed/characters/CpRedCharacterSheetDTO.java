package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor.CpRedCharacterArmorSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterClasses.CpRedCharacterClassesSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries.CpRedCharacterCriticalInjuriesSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCyberware.CpRedCharacterCyberwareSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEnemies.CpRedCharacterEnemiesSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEquipment.CpRedCharacterEquipmentSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterFriends.CpRedCharacterFriendsSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterSkills.CpRedCharacterSkillsSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterStats.CpRedCharacterStatsSheetDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterTragicLoveStory.CpRedCharacterTragicLoveStorySheetDTO;
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
    private String characterType; // Czy postać to NPC/Player
    // ===== Podstawowe informacje =====
    private String photoPath; // Ścieszka do zdjęcia postaci z CP_RED_CHARACTERS
    private String name; // Ksywa z CP_RED_CHARACTERS
    private List<CpRedCharacterClassesSheetDTO> characterClasses; // lista klas postaci z umiejętnościami specjalnymi i jej poziomami z CP_RED_CHARACTER_CLASSES
    private String note; // notatki z CP_RED_CHARACTER_OTHER_INFO
    private Boolean alive; // Czy postać żyje z CP_RED_CHARACTERS
    // ===== Podstawowe statystyki =====
    private Integer currentHumanity; // Aktualne człowieczeństwo z CP_RED_CHARACTER
    private Integer maxHumanity; // Maksymalne człowieczeństwo z CP_RED_CHARACTER
    private Integer currentHp; // Obecne punkty wytrzymałości z CP_RED_CHARACTER
    private Integer maxHp; // Maksymalne punkty wytrzymałości z CP_RED_CHARACTER
    private Integer seriouslyWounded; // Poważnie ranny z CP_RED_CHARACTER
    private Integer survivability; // Przeżywalność z CP_RED_CHARACTER
    private List<CpRedCharacterCriticalInjuriesSheetDTO> criticalInjuries; // Krytyczne urazy, lista nazw z CP_RED_CHARACTER_CRITICAL_INJURIES i z CP_RED_CHARACTER_CUSTOM_CRITICAL_INJURIES
    private String addictions; // Uzależnienia, to jest jedna notatka z CP_RED_CHARACTER_OTHER_INFO
    private List<CpRedCharacterStatsSheetDTO> stats; // Cechy, lista cech z CP_RED_CHARACTERS_STATS
    // ===== Umiejętności =====
    private List<List<CpRedCharacterSkillsSheetDTO>> skills; // Umiejętności, podzielone na podkategorie (każda podkategoria to osobna lista) z CP_RED_CHARACTER_SKILLS
    // ===== Broń i pancerz =====
    private List<CpRedCharacterWeaponSheetDTO> weapons; // Lista broni i customowych broni z CP_RED_CHARACTER_WEAPON i z CP_RED_CHARACTER_CUSTOM_WEAPON
    private List<CpRedCharacterArmorSheetDTO> armor; // Lista założonych pancerzy z CP_RED_CHARACTER_ARMOR i z CP_RED_CHARACTER_CUSTOM_ARMOR
    // ===== Wyposażenie =====
    private List<CpRedCharacterEquipmentSheetDTO> equipment; // Wyposażenie, lista przedmiotów z poza innych kategorii z CP_RED_CHARACTER_CUSTOM_EQUIPMENT i z CP_RED_CHARACTER_EQUIPMENT
    private Integer cash; // Gotówka posiadana przez postać z CP_RED_CHARACTER
    // ===== Wszczepy =====
    private List<List<CpRedCharacterCyberwareSheetDTO>> cyberware; // Cyborgizacje, lista list wszczepów postaci z CP_RED_CHARACTER_CYBERWARE i z CP_RED_CHARACTER_CUSTOM_CYBERWARE
    // ===== Ścieżka życia =====
    private String cultureOfOrigin; // Kultura pochodzenia z CP_RED_CHARACTER_LIFE_PATHS
    private String yourCharacter; // Twój charakter z CP_RED_CHARACTER_LIFE_PATHS
    private String clothingAndStyle; // Ubiór i styl z CP_RED_CHARACTER_LIFE_PATHS
    private String hair; // Włosy z CP_RED_CHARACTER_LIFE_PATHS
    private String mostValue; // Co cenisz najbardziej? z CP_RED_CHARACTER_LIFE_PATHS
    private String relationship; // Relacje z innymi? z CP_RED_CHARACTER_LIFE_PATHS
    private String mostImportantPerson; // Najważniejsza osoba w twoim życiu? z CP_RED_CHARACTER_LIFE_PATHS
    private String mostImportantItem; // Najważniejszy posiadany przedmiot z CP_RED_CHARACTER_LIFE_PATHS
    private String familyBackground; // Tło rodzinne z CP_RED_CHARACTER_LIFE_PATHS
    private String familyEnvironment; // Środowisko rodzinne z CP_RED_CHARACTER_LIFE_PATHS
    private String familyCrisis; // Kryzys rodzinny z CP_RED_CHARACTER_LIFE_PATHS
    private String lifeGoals; // Cele życiowe z CP_RED_CHARACTER_LIFE_PATHS
    // Przyjaciele
    private List<CpRedCharacterFriendsSheetDTO> friends; // Lista przyjaciół postaci z CP_RED_CHARACTER_FRIENDS
    // Tragiczna historia miłosna
    private List<CpRedCharacterTragicLoveStorySheetDTO> loveStory; // Lista historii miłosnych z CP_RED_CHARACTER_TRAGIC_LOVE_STORY
    // Wrogowie
    private List<CpRedCharacterEnemiesSheetDTO> enemies; // Lista wrogów postaci z CP_RED_CHARACTER_ENEMIES
    // Ścieżka życia postaci
    private String classLifePath; // Ścieżka życia postaci z CP_RED_CHARACTER_OTHER_INFO
    // ===== Pozostałe informacje =====
    private String style; // Notatka o stylu z CP_RED_CHARACTER_OTHER_INFO
    private String accommodation; // Zakwaterowanie z CP_RED_CHARACTER_OTHER_INFO
    private Integer rental; // Wynajem z CP_RED_CHARACTER_OTHER_INFO
    private String livingStandard; // Poziom życia z CP_RED_CHARACTER_OTHER_INFO
    private Integer availableExp; // Wolne punkty doświadczenia z CP_RED_CHARACTERS
    private Integer allExp; // Wszystkie punkty doświadczenia z CP_RED_CHARACTERS
    private String reputation; // Reputacja i Wydarzenia związane z Reputacją z CP_RED_CHARACTER_OTHER_INFO


}
