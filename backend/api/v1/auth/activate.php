<?php

require __DIR__ . '/../../../vendor/autoload.php';
use Eddy\RpgHandyHelper\DTO\User;
use Eddy\RpgHandyHelper\Database\DatabaseConnector;

if (empty($_GET['hash'])) {
    header('Location: ../../../frontend/errors/badActivationLink.html');
    die();
}

$hash = DatabaseConnector::sanitizeString($_GET['hash']);

try {
    $user = User::getUserByHash($hash);
} catch (Exception $e) {
    header('Location: ../../../frontend/errors/badActivationLink.html?error=1');
    die();
}

if (is_array($user)) {
    header('Location: ../../../frontend/errors/badActivationLink.html?error=2');
    die();
}

if ($user->getActive() === 1) {
    header('Location: ../../../frontend/errors/badActivationLink.html?error=3');
    die();
}

$user->setActive(1);

try {
    $user->updateUser();
} catch (Exception $e) {
    header('Location: ../../../frontend/errors/badActivationLink.html?error=4');
    die();
}

if ($user["status"] != "success") {
    header('Location: ../../../frontend/errors/badActivationLink.html?error=5');
    die();
}

header('Location: ../../../frontend/login.php?action=1');