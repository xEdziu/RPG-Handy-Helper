<?php

namespace Eddy\RpgHandyHelper\DTO;

use Eddy\RpgHandyHelper\Database\DatabaseConnector;

class UserToken {
    private int $id;
    private string $selector;
    private string $hashedValidator;
    private int $userId;
    private string $expiry;

    public function __construct() {
        $this->id = 0;
        $this->selector = '';
        $this->hashedValidator = '';
        $this->userId = 0;
        $this->expiry = date('now');
    }
    
    /**
     * Function to generate tokens
     * selector: 16 bytes
     * validator: 32 bytes
     *
     * @return array
     */
    public static function generateTokens() : array {
        $selector = bin2hex(random_bytes(16));
        $validator = bin2hex(random_bytes(32));
        return [$selector, $validator, $selector . ':' . $validator];
    }
    
    /**
     * Function to parse token
     *
     * @param  string $token token to parse
     * @return array|null returns null if token is invalid
     */
    public static function parseToken(string $token) : ?array {
        $parts = explode(':', $token);

        if ($parts && count($parts) == 2) {
            return [$parts[0], $parts[1]];
        }
        return null;
    }
    
    /**
     * Function to insert user token
     * to the database
     *
     * @return bool returns true if token is inserted successfully, false otherwise
     */
    public function insertUserToken() : bool {
        $db = new DatabaseConnector();
        try {
            $query = "INSERT INTO user_tokens (selector, hashed_validator, user_id, expiry)
            VALUES (?, ?, ?, ?)";
           $stmt = $db->db->prepare($query);
           $stmt->bind_param('ssis', $this->selector, $this->hashedValidator, $this->userId, $this->expiry);
        } catch (\Exception) {
            return false;
        }
        return $stmt->execute();
    }
    
    /**
     * Function to find user token by selector from the database
     *
     * @param  string $selector selector to search for
     * @return ?UserToken returns null if token is not found, UserToken object otherwise
     */
    public function findUserTokenBySelector(string $selector) : ?UserToken {
        $db = new DatabaseConnector();
        $query = "SELECT * FROM user_tokens WHERE selector = ?
        AND expiry >= now() LIMIT 1";
        $stmt = $db->db->prepare($query);
        $stmt->bind_param('s', $selector);
        $stmt->execute();
        $result = $stmt->get_result();
        $row = $result->fetch_assoc();
        if ($row) {
            $this->setId($row['id']);
            $this->setSelector($row['selector']);
            $this->setHashedValidator($row['hashed_validator']);
            $this->setUserId($row['user_id']);
            $this->setexpiry($row['expiry']);
            return $this;
        }
        return null;
    }
    
    /**
     * Delete token from the database
     *
     * @return bool returns true if token is deleted successfully, false otherwise
     */
    public function deleteTokens() : bool {
        $db = new DatabaseConnector();
        $query = "DELETE FROM user_tokens WHERE id = ?";
        $stmt = $db->db->prepare($query);
        $stmt->bind_param('i', $this->id);
        return $stmt->execute();
    }
    
    /**
     * Find user associated with the token
     *
     * @param  string $token token to search for
     * @return array returns null if token is not found, array with user id and username otherwise
     */
    public function findUserByToken(string $token) : ?array {
        $tokens = self::parseToken($token);

        if (!$token)
            return null;

        $db = new DatabaseConnector();

        $sql = "SELECT users.id, users.username FROM users
            INNER JOIN user_tokens ON users.id = user_tokens.user_id
            WHERE selector = ? AND expiry >= now() LIMIT 1";

        $stmt = $db->db->prepare($sql);
        $stmt->bind_param('s', $tokens[0]);
        $stmt->execute();
        $result = $stmt->get_result();
        $row = $result->fetch_assoc();

        if ($row)
            return $row;
        return null;
    }
    
    /**
     * Check if token is valid
     *
     * @param  string $token token to check
     * @return bool returns true if token is valid, false otherwise
     */
    public function isTokenValid(string $token) : bool {
        [$selector, $validator] = self::parseToken($token);

        $tokens = self::findUserTokenBySelector($selector);
        if ($tokens == null) {
            return false;
        }

        return password_verify($validator, $tokens->getHashedValidator());
    }

    public function setId(int $id) {
        $this->id = $id;
    }

    public function getId() : int {
        return $this->id;
    }

    public function setSelector(string $selector) {
        $this->selector = $selector;
    }

    public function getSelector() : string {
        return $this->selector;
    }

    public function setHashedValidator(string $hashedValidator) {
        $this->hashedValidator = $hashedValidator;
    }

    public function getHashedValidator() : string {
        return $this->hashedValidator;
    }

    public function setUserId(int $userId) {
        $this->userId = $userId;
    }

    public function getUserId() : int {
        return $this->userId;
    }

    public function setexpiry(string $expiry) {
        $this->expiry = $expiry;
    }

    public function getexpiry() : string {
        return $this->expiry;
    }
}