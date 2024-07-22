<?php

namespace rpg\DTO;

use rpg\DTO\Perk;

enum RaceName {
    case Czlowiek;
    case Elf;
    case Krasnolud;
    case Niziolek;
}

class Race {
    private RaceName $name;
    private array $defaultPerks;

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

    private function setupDefaultPerks(){
        switch($this->name){
            case RaceName::Czlowiek:
                $perks[PerkName::WalkaWrecz] = 20;
                $perks[PerkName::UmiejetnosciStrzeleckie] = 20;
                $perks[PerkName::Krzepa] = 20;
                $perks[PerkName::Odpornosc] = 20;
                $perks[PerkName::Zrecznosc] = 20;
                $perks[PerkName::Inteligencja] = 20;
                $perks[PerkName::SilaWoli] = 20;
                $perks[PerkName::Oglada] = 20;
                $perks[PerkName::Akcja] = 1;
                $perks[PerkName::Zywotnosc] = 11;
                $perks[PerkName::Sila] = $perks[PerkName::Krzepa] % 10;
                $perks[PerkName::Wytrzymalosc] = $perks[PerkName::Odpornosc] % 10;
                $perks[PerkName::Szybkosc] = 4;
                $perks[PerkName::PunktyObledu] = 0;
                $perks[PerkName::PunktyPrzeznaczenia] = 3;
                $perks[PerkName::PunktySzczescia] = 3;
                break;
            case RaceName::Elf:
                $perks[PerkName::WalkaWrecz] = 30;
                $perks[PerkName::UmiejetnosciStrzeleckie] = 20;
                $perks[PerkName::Krzepa] = 20;
                $perks[PerkName::Odpornosc] = 30;
                $perks[PerkName::Zrecznosc] = 10;
                $perks[PerkName::Inteligencja] = 20;
                $perks[PerkName::SilaWoli] = 20;
                $perks[PerkName::Oglada] = 20;
                $perks[PerkName::Akcja] = 1;
                $perks[PerkName::Zywotnosc] = 10;
                $perks[PerkName::Sila] = $perks[PerkName::Krzepa] % 10;
                $perks[PerkName::Wytrzymalosc] = $perks[PerkName::Odpornosc] % 10;
                $perks[PerkName::Szybkosc] = 5;
                $perks[PerkName::PunktyObledu] = 0;
                $perks[PerkName::PunktyPrzeznaczenia] = 2;
                $perks[PerkName::PunktySzczescia] = 2;
                break;
            case RaceName::Krasnolud:
                $perks[PerkName::WalkaWrecz] = 30;
                $perks[PerkName::UmiejetnosciStrzeleckie] = 20;
                $perks[PerkName::Krzepa] = 20;
                $perks[PerkName::Odpornosc] = 30;
                $perks[PerkName::Zrecznosc] = 10;
                $perks[PerkName::Inteligencja] = 20;
                $perks[PerkName::SilaWoli] = 20;
                $perks[PerkName::Oglada] = 10;
                $perks[PerkName::Akcja] = 1;
                $perks[PerkName::Zywotnosc] = 12;
                $perks[PerkName::Sila] = $perks[PerkName::Krzepa] % 10;
                $perks[PerkName::Wytrzymalosc] = $perks[PerkName::Odpornosc] % 10;
                $perks[PerkName::Szybkosc] = 3;
                $perks[PerkName::PunktyObledu] = 0;
                $perks[PerkName::PunktyPrzeznaczenia] = 2;
                $perks[PerkName::PunktySzczescia] = 2;
                break;
            case RaceName::Niziolek:
                $perks[PerkName::WalkaWrecz] = 10;
                $perks[PerkName::UmiejetnosciStrzeleckie] = 30;
                $perks[PerkName::Krzepa] = 10;
                $perks[PerkName::Odpornosc] = 10;
                $perks[PerkName::Zrecznosc] = 30;
                $perks[PerkName::Inteligencja] = 20;
                $perks[PerkName::SilaWoli] = 20;
                $perks[PerkName::Oglada] = 30;
                $perks[PerkName::Akcja] = 1;
                $perks[PerkName::Zywotnosc] = 9;
                $perks[PerkName::Sila] = $perks[PerkName::Krzepa] % 10;
                $perks[PerkName::Wytrzymalosc] = $perks[PerkName::Odpornosc] % 10;
                $perks[PerkName::Szybkosc] = 4;
                $perks[PerkName::PunktyObledu] = 0;
                $perks[PerkName::PunktyPrzeznaczenia] = 2;
                $perks[PerkName::PunktySzczescia] = 2;
                break;
            default:
                break;
        }
    }
    
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