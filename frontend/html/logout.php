<?php

session_start();

require __DIR__ . '/../../../vendor/autoload.php';

use function Eddy\RpgHandyHelper\API\isUserLoggedIn;
use Eddy\RpgHandyHelper\DTO\UserToken;

if (isUserLoggedIn()) {
    $userToken = new UserToken();
    $userToken->deleteTokens($_SESSION['user_id']);

    if (isset($_COOKIE['remember_me'])) {
        unset($_COOKIE['remember_me']);
        setcookie('remember_me', null, -1, '/');
    }

    session_unset();
    session_destroy();
    header('Location: login.php?action=logout');
    die();
}
header('Location: login.php');
die();