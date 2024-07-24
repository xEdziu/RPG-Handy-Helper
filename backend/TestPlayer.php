<?php

require __DIR__.'/../vendor/autoload.php';

use Eddy\RpgHandyHelper\DTO\Player;
use Eddy\RpgHandyHelper\DTO\enums\RaceName;
use Eddy\RpgHandyHelper\DTO\enums\Sex;
use Eddy\RpgHandyHelper\races\Human;

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