<?php

namespace Eddy\RpgHandyHelper\Api\V1\Auth;
require __DIR__ . '/../../../../vendor/autoload.php';

use Dotenv;

$dotenv = Dotenv\Dotenv::createImmutable(__DIR__ . "/../../../../");

try {
    $dotenv->load();
} catch (Dotenv\Exception\InvalidPathException $e) {
    $error = "Error loading .env file: " . $e->getMessage();
    die($error);
}

if ($_ENV['PRODUCTION'] === "true") {
    error_reporting(0);
    ini_set('display_errors', 0);
} else {
    error_reporting(E_ALL);
    ini_set('display_errors', 1);
}

function verifyReCaptcha($recaptchaCode): bool
{
    $postdata = http_build_query(["secret"=>$_ENV['RECAPTCHA_SECRET'],"response"=>$recaptchaCode]);
    $opts = ['http' =>
        [
            'method'  => 'POST',
            'header'  => 'Content-type: application/x-www-form-urlencoded',
            'content' => $postdata
        ]
    ];
    $context  = stream_context_create($opts);
    $result = file_get_contents('https://www.google.com/recaptcha/api/siteverify', false, $context);
    $content = json_decode($result);
    if (!$content->success)
        return false;
    if ($content->score < 0.7)
        return false;
    return true;
}

if (!isset($_POST['token']))
    header("Location: ../../../frontend/errors/404.html");

$client_token = $_POST['token'];

$success = verifyReCaptcha($client_token);

$response = ["success" => $success];

die(json_encode($response));