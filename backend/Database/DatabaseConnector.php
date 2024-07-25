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
     * @return string sanitized input
     */
    public static function sanitizeString(string $input): string {
        return htmlspecialchars(strip_tags($input));
    }
    
    
    /**
     * A function to check if migration table exists and create it if it doesn't
     * then checks every if every table has been created and creates them if they haven't
     *
     * @return array containing the status of the database setup: status, message, error_code
     */
    private function databaseSetup() : array {
        $response = [
            "status" => "success",
            "message" => "Database setup successful",
            "error_code" => 0
        ];

        try {
            // Check if the migrations table exists
            $result = $this->db->query("SHOW TABLES LIKE 'migrations'");
            if ($result->num_rows == 0) {
                // Create migrations table
                $this->db->query("CREATE TABLE `migrations` (
                    `id` int AUTO_INCREMENT PRIMARY KEY,
                    `migration` varchar(255) NOT NULL,
                    `batch` int NOT NULL
                )");
            }

            // Check if the Users table migration has been run
            $result = $this->db->query("SELECT * FROM `migrations` WHERE `migration` = 'create_users_table'");
            if ($result->num_rows == 0) {
                // Create Users table
                $this->db->query("CREATE TABLE IF NOT EXISTS `Users` (
                    `id` int AUTO_INCREMENT NOT NULL UNIQUE,
                    `email` varchar(255) NOT NULL UNIQUE,
                    `username` varchar(255) NOT NULL UNIQUE,
                    `password` varchar(500) NOT NULL,
                    `hash` varchar(2000) NOT NULL,
                    `active` int NOT NULL DEFAULT '0',
                    `name` varchar(255) NOT NULL,
                    `surname` varchar(255) NOT NULL,
                    `discord_tag` varchar(255) UNIQUE
                )");

                // Insert migration record
                $this->db->query("INSERT INTO `migrations` (`migration`, `batch`) VALUES ('create_users_table', 1)");
            }
        } catch (\Exception $e) {
            $response = [
                "status" => "error",
                "message" => "Database setup failed: " . $e->getMessage(),
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