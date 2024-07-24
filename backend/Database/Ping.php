<?php

namespace Eddy\RpgHandyHelper\Database;

require_once __DIR__ . "/../../vendor/autoload.php";

use Eddy\RpgHandyHelper\Database\DatabaseConnector;

$database = new DatabaseConnector();

if ($database === null) {
    echo "Database connection failed";
} else {
    echo "Database connection successful";
}