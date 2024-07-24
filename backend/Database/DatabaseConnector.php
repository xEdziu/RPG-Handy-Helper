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
    echo "Error loading .env file: " . $e->getMessage();
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
            $this->host = getenv('DB_HOST');
            $this->database = getenv('DB_NAME');
            $this->username = getenv('DB_USER');
            $this->password = getenv('DB_PASS');
            $this->db = new mysqli($this->host, $this->username, $this->password, $this->database);
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

    public function __destruct() {
        $this->db->close();
    }
}