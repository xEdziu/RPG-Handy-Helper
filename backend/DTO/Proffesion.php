<?php

namespace rpg\DTO\Proffesion;

class Proffesion {
    private string $name;

    public function __construct(string $name) {
        $this->name = $name;
    }
}