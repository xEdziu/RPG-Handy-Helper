<?php

require_once __DIR__ . '/Autoloader.php';

use backend\DTO\Player;
use backend\DTO\enums\RaceName;
use backend\DTO\enums\Sex;
use backend\races\Human;

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