<?php

namespace Eddy\RpgHandyHelper\Api\V1\Auth;

require __DIR__ . '/../../../vendor/autoload.php';

use Eddy\RpgHandyHelper\Mail\Mailer;
use Eddy\RpgHandyHelper\DTO\User;
use Eddy\RpgHandyHelper\Database\DatabaseConnector;

session_start();

if (!$_SERVER['REQUEST_METHOD'] == 'POST' || !isset($_POST['csrf_token'])) {
    http_response_code(404);
    header('Location: /errors/404.html');
}

try {
    if (!isset($_SESSION['csrf_token'])){
        $response = [
            "icon" => "warning",
            "title" => "Spokojnie!",
            "message" => "W celach bezpieczeństwa, odśwież stronę i spróbuj ponownie",
            "data" => [
                "error" => "CSRF token not set",
                "code" => 801,
            ]
        ];
        die(json_encode($response));
    }

    if(!hash_equals($_POST['csrf_token'], $_SESSION['csrf_token'])){
        http_response_code(404);
        header('Location: /errors/404.html');
    }

} catch (\Exception $e){
    $response = [
        "icon" => "warning",
        "title" => "Spokojnie!",
        "message" => "W celach bezpieczeństwa, odśwież stronę i spróbuj ponownie",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 801,
        ]
    ];
    die(json_encode($response));
}

$email = DatabaseConnector::sanitizeString($_POST['email']);

if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się wysłać linku",
        "message" => "Podaj poprawny adres email",
        "footer" => "Error: 602",
        "data" => [
            "error" => "Niepoprawny adres email",
            "code" => 602,
        ]
    ];
    die(json_encode($response));
}

try {
    if (!User::userExists($email)) {
        $response = [
            "icon" => "warning",
            "title" => "Nie udało się wysłać linku",
            "message" => "Użytkownik o podanym adresie email nie istnieje",
            "footer" => "Error: 603",
            "data" => [
                "error" => "Użytkownik nie istnieje",
                "code" => 603,
            ]
        ];
        die(json_encode($response));
    }
} catch (\Exception $e) {
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się wysłać linku",
        "message" => "Wystąpił błąd podczas wysyłania linku",
        "footer" => "Error: 604",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 604,
        ]
    ];
    die(json_encode($response));
}

try {
    $user = User::getUserByEmail($email);
} catch (\Exception $e) {
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się wysłać linku",
        "message" => "Wystąpił błąd podczas wysyłania linku",
        "footer" => "Error: 605",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 605,
        ]
    ];
    die(json_encode($response));
}

if (is_array($user)) {
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się wysłać linku",
        "message" => "Wystąpił błąd podczas pobierania informacji o użytkowniku",
        "footer" => "Error: 606",
        "data" => [
            "error" => $user,
            "code" => 606,
        ]
    ];
}

$hash = bin2hex(random_bytes(32));

$user->setHash($hash);

try {
    $user->updateUser();
} catch (\Exception $e) {
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się wysłać linku",
        "message" => "Wystąpił błąd podczas aktualizacji danych użytkownika",
        "footer" => "Error: 607",
        "data" => [
            "error" => $e->getMessage(),
            "code" => 607,
        ]
    ];
    die(json_encode($response));
}

if ($user['status'] == "error"){
    $response = [
        "icon" => "warning",
        "title" => "Nie udało się wysłać linku",
        "message" => "Wystąpił błąd podczas aktualizacji danych użytkownika",
        "footer" => "Error: 608",
        "data" => [
            "error" => $user['message'],
            "code" => 608,
        ]
    ];
    die(json_encode($response));
}

$mailer = new Mailer();
$response = $mailer->setPasswordRenewalMail($email, $hash);
for ($i = 0; $i < 3; $i++) {
    if ($response['data']['code'] == 0) {
        break;
    }
    $response = $mailer->setPasswordRenewalMail($email, $hash);
}
unset($_SESSION['csrf_token']);
die(json_encode($response));