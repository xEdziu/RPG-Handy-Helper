<?php

namespace Eddy\RpgHandyHelper\Api\V1\Auth;

require_once __DIR__ . '/../../../vendor/autoload.php';

use Eddy\RpgHandyHelper\Database\DatabaseConnector;
use Eddy\RpgHandyHelper\DTO\User;
use Exception;

session_start();

if ($_SERVER['REQUEST_METHOD'] !== 'POST' 
    || !isset($_POST['csrf_token'])
    || !hash_equals($_POST['csrf_token'], $_SESSION['csrf_token'])) {
    http_response_code(404);
    header('Location: /errors/404.html');
}

if (empty($_POST['password'])){
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się zresetować hasła",
        "message" => "Podaj nowe hasło",
        "footer" => "Error: 603",
        "data" => [
            "error" => "Pole hasło nie może być puste",
            "code" => 603,
        ]
    ];
    die(json_encode($response));
}

$hash = DatabaseConnector::sanitizeString($_POST['hash']);
$password = DatabaseConnector::sanitizeString($_POST['password']);

try {
    $user = User::getUserByHash($hash);
} catch (Exception $e) {
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się zresetować hasła",
        "message" => "Podany link jest niepoprawny",
        "footer" => "Error: 604",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 604,
        ]
    ];
    die(json_encode($response));
}

if (is_array($user)){
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się zresetować hasła",
        "message" => "Podany link jest niepoprawny",
        "footer" => "Error: 604",
        "data" => [
            "error" => "Podany link jest niepoprawny",
            "code" => 604,
        ]
    ];
    die(json_encode($response));
}

$hashedPassword = password_hash($password, PASSWORD_ARGON2ID);
$hash = bin2hex(random_bytes(32));

if (!password_verify($password, $hashedPassword)){
    $response = [
        "icon" => "error",
        "title" => "Błąd serwera",
        "message" => "Skontaktuj się z administratorem",
        "footer" => "Kod błędu: 1007",
        "data" => [
            "error" => "Hasło nie zostało zahashowane poprawnie",
            "code" => 1007,
        ]
    ];
    die(json_encode($response));
}

try {
    $user->setPassword($hashedPassword);
    $user->setHash($hash);
} catch (Exception $e) {
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się zresetować hasła",
        "message" => "Skontaktuj się z administratorem",
        "footer" => "Error: 605",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 605,
        ]
    ];
    die(json_encode($response));
}

try {
    $return = $user->updateUser();
} catch (Exception $e) {
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się zresetować hasła",
        "message" => "Skontaktuj się z administratorem",
        "footer" => "Error: 606",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 606,
        ]
    ];
    die(json_encode($response));
}

if ($return['status'] == 'error'){
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się zresetować hasła",
        "message" => "Skontaktuj się z administratorem",
        "footer" => "Error: 607",
        "data" => [
            "error" => $return['message'],
            "code" => 607,
        ]
    ];
    die(json_encode($response));
}

$response = [
    "icon" => "success",
    "title" => "Hasło zostało zresetowane",
    "message" => "Możesz się zalogować",
    "footer" => "Success: 200",
    "data" => [
        "redirect" => "login.php?action=2",
        "error" => null,
        "code" => 200,
    ]
];
unset($_SESSION['csrf_token']);
die(json_encode($response));