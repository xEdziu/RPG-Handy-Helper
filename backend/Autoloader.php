<?php

class Autoloader {
    public static function load($className) {

        // Usuń tylko pierwszy wystąpienie 'backend' i zamień namespace na odpowiadającą strukturę katalogów
        $path = str_replace('\\', DIRECTORY_SEPARATOR, $className);
        $path = preg_replace('/^backend\\' . DIRECTORY_SEPARATOR . '/', '', $path) . '.php';

        $fullPath = __DIR__ . DIRECTORY_SEPARATOR . $path;
        
        if (file_exists($fullPath)) {
            require_once $fullPath;
            echo "Loaded: " . $fullPath . "<br>\n"; // Debugging line
        } else {
            echo "File not found: " . $fullPath . "<br>\n";
        }
    }
}

spl_autoload_register(['Autoloader', 'load']);