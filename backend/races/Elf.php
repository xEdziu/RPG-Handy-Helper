<?php
namespace rpg\races;

use rpg\DTO\Race;
use rpg\DTO\enums\PerkName;

class Elf extends Race
{
    public function setupDefaultPerks(): void
    {
        $this->defaultPerks[PerkName::WalkaWrecz->name] = 30;
        $this->defaultPerks[PerkName::UmiejetnosciStrzeleckie->name] = 20;
        $this->defaultPerks[PerkName::Krzepa->name] = 20;
        $this->defaultPerks[PerkName::Odpornosc->name] = 30;
        $this->defaultPerks[PerkName::Zrecznosc->name] = 10;
        $this->defaultPerks[PerkName::Inteligencja->name] = 20;
        $this->defaultPerks[PerkName::SilaWoli->name] = 20;
        $this->defaultPerks[PerkName::Oglada->name] = 20;
        $this->defaultPerks[PerkName::Akcja->name] = 1;
        $this->defaultPerks[PerkName::Zywotnosc->name] = 10;
        $this->defaultPerks[PerkName::Sila->name] = $this->defaultPerks[PerkName::Krzepa->name] % 10;
        $this->defaultPerks[PerkName::Wytrzymalosc] = $this->defaultPerks[PerkName::Odpornosc->name] % 10;
        $this->defaultPerks[PerkName::Szybkosc->name] = 5;
        $this->defaultPerks[PerkName::PunktyObledu->name] = 0;
        $this->defaultPerks[PerkName::PunktyPrzeznaczenia->name] = 2;
        $this->defaultPerks[PerkName::PunktySzczescia->name] = 2;
    }
}