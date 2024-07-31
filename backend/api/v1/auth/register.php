<?php

namespace Eddy\RpgHandyHelper\Api\V1\Auth;

require __DIR__ . '/../../../../vendor/autoload.php';

use Eddy\RpgHandyHelper\Database\DatabaseConnector;
use Eddy\RpgHandyHelper\DTO\User;
use Eddy\RpgHandyHelper\Mail\Mailer;

session_start();

if ($_SERVER['REQUEST_METHOD'] !== 'POST' 
    || !isset($_POST['csrf_token'])
    || !hash_equals($_POST['csrf_token'], $_SESSION['csrf_token'])) {
    http_response_code(404);
    header('Location: ../../../frontend/errors/404.html');
}

$username = DatabaseConnector::sanitizeString($_POST['username']);
$email = DatabaseConnector::sanitizeString($_POST['email']);
$password = DatabaseConnector::sanitizeString($_POST['password']);
$name = DatabaseConnector::sanitizeString($_POST['name']);
$surname = DatabaseConnector::sanitizeString($_POST['surname']);
$discordTag = null;

if (empty($username) 
    || empty($email) 
    || empty($password)
    || empty($name)
    || empty($surname)) {
    $response = [
        "icon" => "warning",
        "title" => "We couldn't register you",
        "message" => "Please fill in all the required fields",
        "footer" => "Error: 601",
        "data" => [
            "error" => null,
            "code" => 601,
        ]
    ];
    die(json_encode($response));
}

if (isset($_POST['discordTag']) && !empty($_POST['discordTag'])) {
    $discordTag = DatabaseConnector::sanitizeString($_POST['discordTag']);
}

if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    $response = [
        "icon" => "warning",
        "title" => "We couldn't register you",
        "message" => "Please enter a valid email address",
        "footer" => "Error: 602",
        "data" => [
            "error" => null,
            "code" => 602,
        ]
    ];
    die(json_encode($response));
}

if (strlen($password) < 8) {
    $response = [
        "icon" => "warning",
        "title" => "We couldn't register you",
        "message" => "Password must be at least 8 characters long",
        "footer" => "Error: 603",
        "data" => [
            "error" => null,
            "code" => 603,
        ]
    ];
    die(json_encode($response));
}

try {
    if (User::userExists($email)) {
        $response = [
            "icon" => "warning",
            "title" => "We couldn't register you",
            "message" => "User connected to this email already exists",
            "footer" => "Error: 604",
            "data" => [
                "error" => null,
                "code" => 604,
            ]
        ];
        die(json_encode($response));
    }
} catch (\Exception $e) {
    $response = [
        "icon" => "error",
        "title" => "We couldn't register you",
        "message" => "Something went wrong",
        "footer" => "Error: 605",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 605,
        ]
    ];
    die(json_encode($response));
}

try {
    if (User::userExists($username)) {
        $response = [
            "icon" => "warning",
            "title" => "We couldn't register you",
            "message" => "User connected to this username already exists",
            "footer" => "Error: 606",
            "data" => [
                "error" => null,
                "code" => 606,
            ]
        ];
        die(json_encode($response));
    }
} catch (\Exception $e) {
    $response = [
        "icon" => "error",
        "title" => "We couldn't register you",
        "message" => "Something went wrong",
        "footer" => "Error: 607",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 607,
        ]
    ];
    die(json_encode($response));
}

$hashedPassword = password_hash($password, PASSWORD_ARGON2ID);

try {
    $hashActivation = bin2hex(random_bytes(32));
} catch (\Exception $e) {
    $response = [
        "icon" => "error",
        "title" => "We couldn't register you",
        "message" => "Something went wrong",
        "footer" => "Error: 608",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 608,
        ]
    ];
    die(json_encode($response));
}

if (!password_verify($password, $hashedPassword)) {
    $response = [
        "icon" => "error",
        "title" => "We couldn't register you",
        "message" => "Something went wrong",
        "footer" => "Error: 609",
        "data" => [
            "error" => null,
            "code" => 609,
        ]
    ];
    die(json_encode($response));
}

$user = User::newUserRegister($username, $email, $hashedPassword, $hashActivation, $name, $surname, $discordTag);

$responseCreation = [];

try {
    $responseCreation = $user->createUser();
} catch (\Exception $e) {
    $response = [
        "icon" => "error",
        "title" => "We couldn't register you",
        "message" => "Something went wrong",
        "footer" => "Error: 610",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 610,
        ]
    ];
    die(json_encode($response));
}

if ($responseCreation['status'] !== 'success') {
    $response = [
        "icon" => "error",
        "title" => "We couldn't register you",
        "message" => "Something went wrong",
        "footer" => "Error: 611",
        "data" => [
            "error" => $responseCreation['message'],
            "code" => 611,
        ]
    ];
    die(json_encode($response));
}

try {
    $user = User::getUserByEmail($email);
} catch (\Exception $e) {
    $response = [
        "icon" => "error",
        "title" => "We couldn't register you",
        "message" => "Something went wrong",
        "footer" => "Error: 612",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 612,
        ]
    ];
    die(json_encode($response));
}

if (is_array($user)){
    $response = [
        "icon" => "error",
        "title" => "We couldn't register you",
        "message" => "Something went wrong",
        "footer" => "Error: 613",
        "data" => [
            "error" => $user['message'],
            "code" => 613,
        ]
    ];
    die(json_encode($response));
}

$mail = new Mailer();
for ($i = 0; $i < 3; $i++) {
    $response = [];
    $response = $mail->sendActivationMail($user->getEmail(), $user->getHash());
    if ($response['data']['code'] === 0) {
        break;
    }
}

unset($_SESSION['csrf_token']);
die(json_encode($response));