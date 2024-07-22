<?php
namespace rpg\races;

use rpg\DTO\Race;
use rpg\DTO\PerkName;

class Halfling extends Race
{
    public function setupDefaultPerks(): void
    {
        $this->defaultPerks[PerkName::WalkaWrecz] = 10;
        $this->defaultPerks[PerkName::UmiejetnosciStrzeleckie] = 30;
        $this->defaultPerks[PerkName::Krzepa] = 10;
        $this->defaultPerks[PerkName::Odpornosc] = 10;
        $this->defaultPerks[PerkName::Zrecznosc] = 30;
        $this->defaultPerks[PerkName::Inteligencja] = 20;
        $this->defaultPerks[PerkName::SilaWoli] = 20;
        $this->defaultPerks[PerkName::Oglada] = 30;
        $this->defaultPerks[PerkName::Akcja] = 1;
        $this->defaultPerks[PerkName::Zywotnosc] = 9;
        $this->defaultPerks[PerkName::Sila] = $this->defaultPerks[PerkName::Krzepa] % 10;
        $this->defaultPerks[PerkName::Wytrzymalosc] = $this->defaultPerks[PerkName::Odpornosc] % 10;
        $this->defaultPerks[PerkName::Szybkosc] = 4;
        $this->defaultPerks[PerkName::PunktyObledu] = 0;
        $this->defaultPerks[PerkName::PunktyPrzeznaczenia] = 2;
        $this->defaultPerks[PerkName::PunktySzczescia] = 2;
    }
}