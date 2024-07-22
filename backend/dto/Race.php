<?php

namespace rpg\DTO;

use rpg\DTO\Perk;

enum RaceName {
    case Czlowiek;
    case Elf;
    case Krasnolud;
    case Niziolek;
}

abstract class Race {
    private RaceName $name;
    private array $defaultPerks;

    public function __construct(RaceName $name) {
        $this->name = $name;
        $this->setupDefaultPerks();
    }

    /**
     * A function to set up the default perks of a race
     *
     * @return void
     */
    abstract public function setupDefaultPerks() : void;

    /**
     * Function to get the name of the race
     *
     * @return RaceName
     */
    public function getName(): RaceName {
        return $this->name;
    }
    
    /**
     * Function to get the default perks of the race
     *
     * @return array
     */
    public function getDefaultPerks(): array {
        return $this->defaultPerks;
    }
}