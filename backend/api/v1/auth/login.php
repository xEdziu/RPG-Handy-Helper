<?php

require __DIR__ . '/../../../vendor/autoload.php';
use Eddy\RpgHandyHelper\DTO\User;
use Eddy\RpgHandyHelper\DTO\UserToken;
use Eddy\RpgHandyHelper\Database\DatabaseConnector;

session_start();

/**
 * Function to log user in
 *
 * @param  string $username
 * @param  string $password
 * @param  bool $remember
 * @return array returns an array with the status of the login
 */
function login(string $username, string $password, bool $remember = false) : array {

    $user = User::getUserByUsername($username);

    if (is_array($user))
        return $user;
    
    if ($user->getActive() === 0) {
        return [
            "icon" => "error",
            "title" => "We couldn't log you in",
            "message" => "Your account is not activated",
            "footer" => "Error: 604",
            "data" => [
                "error" => null,
                "code" => 604,
            ]
        ];
    }

    if (!password_verify($password, $user->getPassword())) {
        return [
            "icon" => "error",
            "title" => "We couldn't log you in",
            "message" => "Please check your credentials",
            "footer" => "Error: 603",
            "data" => [
                "error" => null,
                "code" => 603,
            ]
        ];
    }

    if(!logUserIn($user->getUsername(), $user->getId())) {
        return [
            "icon" => "error",
            "title" => "We couldn't log you in",
            "message" => "Please try again",
            "footer" => "Error: 605",
            "data" => [
                "error" => null,
                "code" => 605,
            ]
        ];
    }

    if (!$remember){
        return [
            "icon" => "success",
            "title" => "Welcome back",
            "message" => "You have been logged in",
            "footer" => "Success: 200",
            "data" => [
                "error" => null,
                "code" => 200,
            ]
        ];
    }
    
    if (!rememberMe($user->getId())){
        return [
            "icon" => "warning",
            "title" => "We couldn't remember you",
            "message" => "Please contact support for help",
            "footer" => "Error: 605",
            "data" => [
                "error" => null,
                "code" => 605,
            ]
        ];
    }

    return [
        "icon" => "success",
        "title" => "Welcome back",
        "message" => "You have been logged in",
        "footer" => "Success: 200",
        "data" => [
            "error" => null,
            "code" => 200,
        ]
    ];

}

/**
 * Function that is responsible for logging the user in
 * by setting the session variables
 *
 * @param  string $username username of the user
 * @param  int $id id of the user
 * @return bool returns true if the user was logged in successfully, false otherwise
 */
function logUserIn(string $username, int $id) : bool {
    if (session_regenerate_id()) {
        $_SESSION['username'] = $username;
        $_SESSION['id'] = $id;
        return true;
    }
    return false;
}

/**
 * Function that is responsible for remembering the user
 * by setting the remember me cookie and inserting the token
 * into the database
 *
 * @param  int $userId id of the user
 * @param  int $days [optional] number of days to remember the user, default is 30 days
 * @return bool returns true if the user was remembered successfully, false otherwise
 */
function rememberMe(int $userId, int $days = 30) : bool {
    $userToken = new UserToken();
    [$selector, $validator, $token] = $userToken->generateTokens();

    $userToken->deleteTokens($userId);

    $expired_seconds = time() + 60 * 60 * 24 * $days;

    $expiry = date('Y-m-d H:i:s', $expired_seconds);
    $hash_validator = password_hash($validator, PASSWORD_ARGON2ID);

    $userToken->setSelector($selector);
    $userToken->setHashedValidator($hash_validator);
    $userToken->setUserId($userId);
    $userToken->setExpiry($expiry);

    if (!$userToken->insertUserToken())
        return false;

    setcookie('remember_me', $token, $expired_seconds);
    return true;
}

/**
 * Function to check if the user is logged in
 * by checking the session variables, the remember me cookie
 * and validating the token in the database
 *
 * @return bool returns true if the user is logged in, false otherwise
 */
function isUserLoggedIn() : bool {
    $userToken = new UserToken();

    if(isset($_SESSION['username']))
        return true;

    $token = filter_input(INPUT_COOKIE, 'remember_me');

    if (!$token || !$userToken->isTokenValid($token))
        return false;
    
    $user = $userToken->findUserByToken($token);

    if (!$user)
        return false;

    return logUserIn($user['username'], $user['id']);
}


if (isUserLoggedIn()) {
    header('Location: ../../../frontend/dashboard.php');
    exit();
}

if ($_SERVER['REQUEST_METHOD'] !== 'POST' 
    || !isset($_POST['csrf_token'])
    || !hash_equals($_POST['csrf_token'], $_SESSION['csrf_token'])) {
    http_response_code(404);
    header('Location: ../../../frontend/errors/404.html');
}

$username = DatabaseConnector::sanitizeString($_POST['username']);
$password = DatabaseConnector::sanitizeString($_POST['password']);
$remember = isset($_POST['remember']) ? true : false;

die(json_encode(login($username, $password, $remember)));