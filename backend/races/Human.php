<?php
namespace backend\races;

use backend\DTO\Race;
use backend\DTO\enums\PerkName;

class Human extends Race
{
    public function setupDefaultPerks(): void
    {
        $this->defaultPerks[PerkName::WalkaWrecz->name] = 20;
        $this->defaultPerks[PerkName::UmiejetnosciStrzeleckie->name] = 20;
        $this->defaultPerks[PerkName::Krzepa->name] = 20;
        $this->defaultPerks[PerkName::Odpornosc->name] = 20;
        $this->defaultPerks[PerkName::Zrecznosc->name] = 20;
        $this->defaultPerks[PerkName::Inteligencja->name] = 20;
        $this->defaultPerks[PerkName::SilaWoli->name] = 20;
        $this->defaultPerks[PerkName::Oglada->name] = 20;
        $this->defaultPerks[PerkName::Akcja->name] = 1;
        $this->defaultPerks[PerkName::Zywotnosc->name] = 11;
        $this->defaultPerks[PerkName::Sila->name] = $this->defaultPerks[PerkName::Krzepa->name] % 10;
        $this->defaultPerks[PerkName::Wytrzymalosc->name] = $this->defaultPerks[PerkName::Odpornosc->name] % 10;
        $this->defaultPerks[PerkName::Szybkosc->name] = 4;
        $this->defaultPerks[PerkName::PunktyObledu->name] = 0;
        $this->defaultPerks[PerkName::PunktyPrzeznaczenia->name] = 3;
        $this->defaultPerks[PerkName::PunktySzczescia->name] = 3;
    }
}