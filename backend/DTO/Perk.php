<?php

namespace rpg\DTO;

use rpg\DTO\enums\PerkName;
use rpg\DTO\enums\PerkType;

class Perk {
    private PerkType $type;
    private PerkName $name;
    private int $startingValue;
    private int $developmentValue = 0;
    private int $actualValue = 0;

    public function __construct(PerkType $type, PerkName $name,
            int $startingValue) {
        $this->type = $type;
        $this->name = $name;
        $this->startingValue = $startingValue;
        $this->actualValue = $startingValue;
    }


    /**
     * Function to get the type of the perk
     *
     * @return PerkType
     */
    public function getType(): PerkType {
        return $this->type;
    }
    
    /**
     * Function to get the name of the perk
     *
     * @return PerkName
     */
    public function getName(): PerkName {
        return $this->name;
    }
    
    /**
     * Function to get the starting value of the perk
     *
     * @return int
     */
    public function getStartingValue(): int {
        return $this->startingValue;
    }
    
    /**
     * Function to get the development value of the perk
     *
     * @return int
     */
    public function getDevelopmentValue(): int {
        return $this->developmentValue;
    }
    
    /**
     * Function to get the actual value of the perk
     *
     * @return int
     */
    public function getActualValue(): int {
        return $this->actualValue;
    }
    
    /**
     * Function to set the development value of the perk
     *
     * @param  mixed $developmentValue
     * @return void
     */
    public function setDevelopmentValue(int $developmentValue): void {
        $this->developmentValue = $developmentValue;
    }
    
    /**
     * Function to set the actual value of the perk
     *
     * @param  mixed $actualValue
     * @return void
     */
    public function setActualValue(int $actualValue): void {
        $this->actualValue = $actualValue;
    }
    
    /**
     * __toString
     *
     * @return string
     */
    public function __toString(): string {
        return $this->name . ' - ' . $this->actualValue;
    }
    
    /**
     * __toArray
     *
     * @return array
     */
    public function __toArray(): array {
        return [
            'type' => $this->type,
            'name' => $this->name,
            'startingValue' => $this->startingValue,
            'developmentValue' => $this->developmentValue,
            'actualValue' => $this->actualValue
        ];
    }
}
