<?php

namespace rpg\DTO;

use rpg\DTO\Perk;
use rpg\DTO\enums\RaceName;
use rpg\DTO\enums\PerkName;
use rpg\DTO\enums\PerkType;

abstract class Race {
    public RaceName $name;
    public array $defaultPerks;

    public function __construct(RaceName $name) {
        $this->name = $name;
        $this->defaultPerks = array(
            new Perk(PerkType::Main, PerkName::WalkaWrecz, 0),
            new Perk(PerkType::Main, PerkName::UmiejetnosciStrzeleckie, 0),
            new Perk(PerkType::Main, PerkName::Krzepa, 0),
            new Perk(PerkType::Main, PerkName::Odpornosc, 0),
            new Perk(PerkType::Main, PerkName::Zrecznosc, 0),
            new Perk(PerkType::Main, PerkName::Inteligencja, 0),
            new Perk(PerkType::Main, PerkName::SilaWoli, 0),
            new Perk(PerkType::Main, PerkName::Oglada, 0),
            new Perk(PerkType::Secondary, PerkName::Akcja, 0),
            new Perk(PerkType::Secondary, PerkName::Zywotnosc, 0),
            new Perk(PerkType::Secondary, PerkName::Sila, 0),
            new Perk(PerkType::Secondary, PerkName::Wytrzymalosc, 0),
            new Perk(PerkType::Secondary, PerkName::Szybkosc, 0),
            new Perk(PerkType::Secondary, PerkName::PunktyObledu, 0),
            new Perk(PerkType::Secondary, PerkName::PunktyPrzeznaczenia, 0),
            new Perk(PerkType::Secondary, PerkName::PunktySzczescia, 0)
        );
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