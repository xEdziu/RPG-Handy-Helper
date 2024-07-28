<?php

namespace Eddy\RpgHandyHelper\DTO;

require __DIR__ . "/../../vendor/autoload.php";

use Eddy\RpgHandyHelper\DTO\User;

$user = new User(
    "mail@02.pl",
    "tesciowa",
    "haslo",
    "ADFwdawdafcs",
    1,
    "Ala",
    "Kot",
    NULL
);
//var_dump($user);

$responseCreate = $user->createUser();
if($responseCreate["status"] == "success"){
    echo "Tworzenie usera sie udalo \n";
} else {
    echo "Tworzenie usera sie nie udalo \n".$responseCreate["message"];
}

$responseGetUserByEmail = $user->getUserByEmail("mail@02.pl");
if(gettype($responseGetUserByEmail) == "array"){
    echo "Wystąpił błąd: ".$responseGetUserByEmail["message"];
} else {
    echo "Znaleziono usera ".$responseGetUserByEmail->getUsername()."\n";
}

$responseGetUserByUsername = $user->getUserByUsername("tesciowa");
if(gettype($responseGetUserByUsername) == "array"){
    echo "Wystąpił błąd: ".$responseGetUserByUsername["message"];
} else {
    echo "Znaleziono usera ".$responseGetUserByUsername->getUsername()."\n";
}

$responseGetUserById = $user->getUserById(1);
if(gettype($responseGetUserById) == "array"){
    echo "Wystąpił błąd: ".$responseGetUserById["message"];
} else {
    echo "Znaleziono usera ".$responseGetUserById->getUsername()."\n";
}

$responseUserExists = $user->userExists("tesciowa");
if(gettype($responseUserExists) == "array"){
    echo "Wystąpił błąd: ".$responseUserExists["message"]."\n";
} else {
    if($responseUserExists == true){
        echo "User jest w bazie \n";
    } else {
        echo "Usera nie ma w bazie \n";
    }
}

$responseUpdateUser = $user->updateUser();
if($responseCreate["status"] == "success"){
    echo "Update usera sie udał \n";
} else {
    echo "Update usera sie nie udał \n".$responseCreate["message"];
}

$responseDeleteUser = $user->deleteUser("mail@02.pl");
if($responseCreate["status"] == "success"){
    echo "User usuniety \n";
} else {
    echo "Błąd:".$responseCreate["message"]."\n";
}

$responseUserExists2 = $user->userExists("tesciowa");
if(gettype($responseUserExists2) == "array"){
    echo "Brak usera: ".$responseUserExists2["message"]."\n";
} else {
    if($responseUserExists2 == true){
        echo "User jest w bazie \n";
    } else {
        echo "Usera nie ma w bazie \n";
    }
}