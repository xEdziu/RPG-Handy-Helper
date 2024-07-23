<?php

class Autoloader {
    public static function load($className) {
        $path = str_replace(['\\', 'rpg\\'], [DIRECTORY_SEPARATOR, ''], $className) . '.php';
        $fullPath = __DIR__ . DIRECTORY_SEPARATOR . $path;
        
        if (file_exists($fullPath)) {
            require_once $fullPath;
            echo "Loaded: " . $fullPath . "\n"; // Debugging line
        } else {
            echo "File not found: " . $fullPath . "\n";
        }
    }
}

spl_autoload_register(['Autoloader', 'load']);