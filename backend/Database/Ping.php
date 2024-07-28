<?php

namespace Eddy\RpgHandyHelper\Database;

require_once __DIR__ . "/../../vendor/autoload.php";

use Eddy\RpgHandyHelper\Database\DatabaseConnector;

class Ping {
    private $databaseConnector;

    public function __construct() {
        $this->databaseConnector = new DatabaseConnector();

        if ($this->databaseConnector === null || $this->databaseConnector->db === null) {
            echo "\nDatabase connection failed: DatabaseConnector or its db property is null.\n";
        } elseif ($this->databaseConnector->db->connect_error) {
            // If not null, then check for a connection error
            echo "Database connection failed: " . $this->databaseConnector->db->connect_error;
        } else {
            echo "Database connection successful";
        }
    }
}

// Usage
new Ping();