<?php

namespace Eddy\RpgHandyHelper\Database;

require_once __DIR__ . "/../../vendor/autoload.php";

use mysqli;
use mysqli_sql_exception;
use Dotenv;

$dotenv = Dotenv\Dotenv::createImmutable(__DIR__ . "/../../");

try {
    $dotenv->load();
} catch (Dotenv\Exception\InvalidPathException $e) {
    $error = "Error loading .env file: " . $e->getMessage();
    die($error);
}

//TODO: Remove this in production
error_reporting(E_ALL);
ini_set('display_errors', 1);

class DatabaseConnector {
    private string $host;
    private string $database;
    private string $username;
    private string $password;

    public ?mysqli $db = null;

    public function __construct() {
        try {
            $this->host = $_ENV['DB_HOST'];
            $this->database = $_ENV['DB_NAME'];
            $this->username = $_ENV['DB_USER'];
            $this->password = $_ENV['DB_PASS'];
            $this->db = new mysqli($this->host, $this->username, $this->password, $this->database);
            $response = $this->databaseSetup();
            if ($response["status"] === "error") {
                $response["message"];
            }
        } catch (mysqli_sql_exception $e) {
            echo "Connection failed: " . $e->getMessage();
        }
    }
    
    /**
     * Function to sanitize user input, to prevent SQL injection
     *
     * @param  string $input
     * @return string
     */
    public static function sanitizeString(string $input): string {
        return htmlspecialchars(strip_tags($input));
    }

    private function databaseSetup() {
        $response = [
            "status" => "success",
            "message" => "Database setup successful",
            "error_code" => 0
        ];
        try {
            $this->db->query("CREATE TABLE IF NOT EXISTS `Users` (
                `id` int AUTO_INCREMENT NOT NULL UNIQUE,
                `email` varchar(255) NOT NULL UNIQUE,
                `username` varchar(255) NOT NULL UNIQUE,
                `password` varchar(500) NOT NULL,
                `hash` varchar(2000) NOT NULL,
                `active` int NOT NULL DEFAULT '0',
                `name` varchar(255) NOT NULL,
                `surname` varchar(255) NOT NULL,
                `discord_tag` varchar(255) UNIQUE,
                PRIMARY KEY (`id`)
            )");
        } catch (mysqli_sql_exception $e) {
            $error = "Error creating table: " . $e->getMessage();
            $response = [
                "status" => "error",
                "message" => $error,
                "error_code" => 1
            ];
        }
        return $response;
    }

    public function __destruct() {
        if ($this->db) {
            $this->db->close();
        }
    }
}