<?php

namespace Eddy\RpgHandyHelper\DTO;

require __DIR__ . "/../../vendor/autoload.php";

use Eddy\RpgHandyHelper\Database\DatabaseConnector;
use mysqli_sql_exception;

class User{

    private int $id;
    private string $email; 
    private string $username;
    private string $password;
    private string $hash;
    private int $active;
    private string $name;
    private string $surname;
    private ?string $discordTag;
    private DatabaseConnector $connector;
    
    public function __construct(int $id, string $email, string $username,
     string $password, string $hash, int $active, string $name, string $surname,
     ?string $discordTag){

        $this->id = $id;
        $this->email = $email;
        $this->username = $username;
        $this->password = $password;
        $this->hash = $hash;
        $this->active = $active;
        $this->name = $name;
        $this->surname = $surname;
        $this->discordTag = $discordTag;
        $this->connector = new DatabaseConnector();
    }


    //TODO: Add a function similar to get a user by their email and username @Zuber
    /**
     * Function to get a user by their id
     *
     * @param  int $id The id of the user
     * @return User|array The user object or an array with an error message
     */
    public static function getUserById(int $id): User|array {
        $response = [
            "status" => "error",
            "message" => "User not found",
            "error_code" => 404
        ];
        $connector = new DatabaseConnector();
        try {
            $query = $connector->db->prepare("SELECT * FROM Users WHERE id = ?");
            $query->bind_param("i", $id);
        } catch (mysqli_sql_exception $e) {
            $response = [
                "status" => "error",
                "message" => "Error: " . $e->getMessage(),
                "error_code" => 500
            ];
            return $response;
        }

        $query->execute();

        $result = $query->get_result();
        if ($result->num_rows == 0) {
            return $response;
        }

        $user = $result->fetch_assoc();
        return new User($user["id"], $user["email"], $user["username"], $user["password"],
         $user["hash"], $user["active"], $user["name"], $user["surname"], $user["discord_tag"]);
    }
    
    /**
     * Get the Id of the user
     *
     * @return int
     */
    public function getId(): int {
        return $this->id;
    }

    /**
     * Get the email of the user
     *
     * @return string
     */
    public function getEmail(): string {
        return $this->email;
    }

    /**
     * Get the username of the user
     *
     * @return string
     */
    public function getUsername(): string {
        return $this->username;
    }

    /**
     * Get the password of the user
     *
     * @return string
     */
    public function getPassword(): string {
        return $this->password;
    }

    /**
     * Get the hash of the user
     *
     * @return string
     */
    public function getHash(): string {
        return $this->hash;
    }

    /**
     * Get the active status of the user
     *
     * @return int
     */
    public function getActive(): int {
        return $this->active;
    }

    /**
     * Get the name of the user
     *
     * @return string
     */
    public function getName(): string {
        return $this->name;
    }

    /**
     * Get the surname of the user
     *
     * @return string
     */
    public function getSurname(): string {
        return $this->surname;
    }

    /**
     * Get the discord tag of the user
     *
     * @return string
     */
    public function getDiscordTag(): ?string {
        return $this->discordTag;
    }

        
    /**
     * Set the Id of the user
     *
     * @param  mixed $id
     * @return void
     */
    public function setId(int $id): void {
        $this->id = $id;
    }

    /**
     * Set the email of the user
     *
     * @param  mixed $email
     * @return void
     */
    public function setEmail(string $email): void {
        $this->email = $email;
    }

    /**
     * Set the username of the user
     *
     * @param  mixed $username
     * @return void
     */
    public function setUsername(string $username): void {
        $this->username = $username;
    }

    /**
     * Set the password of the user
     *
     * @param  mixed $password
     * @return void
     */
    public function setPassword(string $password): void {
        $this->password = $password;
    }

    /**
     * Set the hash of the user
     *
     * @param  mixed $hash
     * @return void
     */
    public function setHash(string $hash): void {
        $this->hash = $hash;
    }

    /**
     * Set the active status of the user
     *
     * @param  mixed $active
     * @return void
     */
    public function setActive(int $active): void {
        $this->active = $active;
    }

    /**
     * Set the name of the user
     *
     * @param  mixed $name
     * @return void
     */
    public function setName(string $name): void {
        $this->name = $name;
    }

    /**
     * Set the surname of the user
     *
     * @param  mixed $surname
     * @return void
     */
    public function setSurname(string $surname): void {
        $this->surname = $surname;
    }

    /**
     * Set the discord tag of the user
     *
     * @param  mixed $discordTag
     * @return void
     */
    public function setDiscordTag(?string $discordTag): void {
        $this->discordTag = $discordTag;
    }


}