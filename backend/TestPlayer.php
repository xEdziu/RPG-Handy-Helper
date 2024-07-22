<?php
namespace rpg;

require_once __DIR__ . '/Autoloader.php';

use rpg\DTO\Player;
use rpg\DTO\enums\RaceName;
use rpg\DTO\enums\Sex;
use rpg\races\Human;

$janek = new Player(
    "Jan",
    "Kowalski",
    Sex::Male,
    new Human(RaceName::Czlowiek),
    25,
    180,
    80,
    2,
    "Warszawa",
    "Niebieski",
    "Blond",
    "Rak",
    "Blizna na twarzy",
    "Żołnierz",
    "Rolnik"
);

echo $janek->__toString();