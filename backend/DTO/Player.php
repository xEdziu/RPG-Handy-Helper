<?php

namespace backend\DTO;

use backend\DTO\Perk;
use backend\DTO\enums\PerkName;
use backend\DTO\Race;
use backend\DTO\Proffesion;
use backend\DTO\enums\Sex;

class Player {
    private string $name;
    private string $surname;
    private Sex $sex;
    private Race $race;
    private int $age;
    private int $height;
    private int $weight;
    private int $siblings;
    private string $birthplace;
    private string $eyeColor;
    private string $hairColor;
    private string $starSign;
    private string $specialMarks;
    //tutaj w dwóch kolejnych będzie trzeba zmienić na Proffesion
    private string $currentProffesion;
    private string $previousProffesion;
    private array $perks;

    public function __construct(
        string $name,
        string $surname,
        Sex $sex,
        Race $race,
        int $age,
        int $height,
        int $weight,
        int $siblings,
        string $birthplace,
        string $eyeColor,
        string $hairColor,
        string $starSign,
        string $specialMarks,
        string $currentProffesion,
        string $previousProffesion
    ) {
        $this->name = $name;
        $this->surname = $surname;
        $this->sex = $sex;
        $this->race = $race;
        $this->age = $age;
        $this->height = $height;
        $this->weight = $weight;
        $this->siblings = $siblings;
        $this->birthplace = $birthplace;
        $this->eyeColor = $eyeColor;
        $this->hairColor = $hairColor;
        $this->starSign = $starSign;
        $this->specialMarks = $specialMarks;
        $this->currentProffesion = $currentProffesion;
        $this->previousProffesion = $previousProffesion;
        $this->perks = $race->getDefaultPerks();
    }

    /**
     * Function to get the name of the player
     * @return string
     */
    public function getName(): string {
        return $this->name;
    }

    /**
     * Function to get the surname of the player
     * @return string
     */
    public function getSurname(): string {
        return $this->surname;
    }

    /**
     * Function to get the sex of the player
     * @return string
     */
    public function getSex(): Sex {
        return $this->sex;
    }

    /**
     * Function to get the race of the player
     * @return string
     */
    public function getRace(): Race {
        return $this->race;
    }
    
    /**
     * Function to get the age of the player
     *
     * @return int
     */
    public function getAge(): int {
        return $this->age;
    }
    
    /**
     * Function to get the height of the player
     *
     * @return int
     */
    public function getHeight(): int {
        return $this->height;
    }
    
    /**
     * Function to get the weight of the player
     *
     * @return int
     */
    public function getWeight(): int {
        return $this->weight;
    }
    
    /**
     * Function to get the number of siblings of the player
     *
     * @return int
     */
    public function getSiblings(): int {
        return $this->siblings;
    }
    
    /**
     * Function to get the birthplace of the player
     *
     * @return string
     */
    public function getBirthplace(): string {
        return $this->birthplace;
    }
    
    /**
     * Function to get the eye color of the player
     *
     * @return string
     */
    public function getEyeColor(): string {
        return $this->eyeColor;
    }
    
    /**
     * Function to get the hair color of the player
     *
     * @return string
     */
    public function getHairColor(): string {
        return $this->hairColor;
    }
    
    /**
     * Function to get the star sign of the player
     *
     * @return string
     */
    public function getStarSign(): string {
        return $this->starSign;
    }
    
    /**
     * Function to get the special marks of the player
     *
     * @return string
     */
    public function getSpecialMarks(): string {
        return $this->specialMarks;
    }
    
    /**
     * Function to get the current proffesion of the player
     *
     * @return Proffesion
     */
    public function getCurrentProffesion(): string {
        return $this->currentProffesion;
    }
    
    /**
     * Function to get the previous proffesion of the player
     *
     * @return Proffesion
     */
    public function getPreviousProffesion(): string {
        return $this->previousProffesion;
    }
    
    /**
     * Function to get the perks of the player
     *
     * @return array
     */
    public function getPerks(): array {
        return $this->perks;
    }
    
    /**
     * Function to set the perks of the player
     *
     * @param  array $perks
     * @return void
     */
    public function setPerks(array $perks): void {
        $this->perks = $perks;
    }
    
    /**
     * Function to set a perk of the player
     *
     * @param  Perk $perk
     * @return void
     */
    public function setPerk(Perk $perk): void {
        $this->perks[$perk->getName()] = $perk;
    }
    
    /**
     * Function to get a perk of the player
     *
     * @param  PerkName $perkName
     * @return Perk
     */
    public function getPerk(PerkName $perkName): Perk {
        return $this->perks[$perkName];
    }
    
    /**
     * Function to get the value of a perk of the player
     *
     * @param  PerkName $perkName
     * @return int
     */
    public function getPerkValue(PerkName $perkName): int {
        return $this->perks[$perkName]->getActualValue();
    }
    
    /**
     * Function to set the value of a perk of the player
     *
     * @param  PerkName $perkName
     * @param  int $value
     * @return void
     */
    public function setPerkValue(PerkName $perkName, int $value): void {
        $this->perks[$perkName]->setActualValue($value);
    }
    
    /**
     * Function to get the development value of a perk of the player
     *
     * @param  PerkName $perkName
     * @return int
     */
    public function getPerkDevelopmentValue(PerkName $perkName): int {
        return $this->perks[$perkName]->getDevelopmentValue();
    }
    
    /**
     * Function to set the development value of a perk of the player
     *
     * @param  PerkName $perkName
     * @param  int $value
     * @return void
     */
    public function setPerkDevelopmentValue(PerkName $perkName, int $value): void {
        $this->perks[$perkName]->setDevelopmentValue($value);
    }
    
    /**
     * __toString
     *
     * @return string
     */
    public function __toString(): string {
        return $this->name . ' ' . $this->surname . ' (' . $this->race->getName()->name . ')';
    }
    
    /**
     * __toArray
     *
     * @return array
     */
    public function __toArray(): array {
        return [
            'name' => $this->name,
            'surname' => $this->surname,
            'sex' => $this->sex,
            'race' => $this->race,
            'age' => $this->age,
            'height' => $this->height,
            'weight' => $this->weight,
            'siblings' => $this->siblings,
            'birthplace' => $this->birthplace,
            'eyeColor' => $this->eyeColor,
            'hairColor' => $this->hairColor,
            'starSign' => $this->starSign,
            'specialMarks' => $this->specialMarks,
            'currentProffesion' => $this->currentProffesion,
            'previousProffesion' => $this->previousProffesion,
            'perks' => $this->perks
        ];
    }
}