<?php

namespace Eddy\RpgHandyHelper\Mail;

require __DIR__ . '/../../vendor/autoload.php';

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require_once __DIR__ . '/templates/activationAccount.php';

use Dotenv;

$dotenv = Dotenv\Dotenv::createImmutable(__DIR__ . "/../../");

try {
    $dotenv->load();
} catch (Dotenv\Exception\InvalidPathException $e) {
    $error = "Error loading .env file: " . $e->getMessage();
    die($error);
}

class Mailer {
    private PHPMailer|null $mail = null;
    private string $url;

    function __construct()
    {
        if ($_ENV['PRODUCTION'] === "true") {
            error_reporting(0);
            ini_set('display_errors', 0);
            $this->mail = new PHPMailer(false);
            $this->mail->SMTPDebug = 0;
            $this->url = "https://rpg.webace-group.dev/";
        } else {
            error_reporting(E_ALL);
            ini_set('display_errors', 1);
            $this->mail = new PHPMailer(true);
            $this->mail->SMTPDebug = 3;
            $this->mail->Debugoutput = 'html';
            $this->url = "http://localhost:8000/";
        }
        $this->mail->CharSet = "UTF-8";
        $this->mail->SMTPAuth = TRUE;
        $this->mail->SMTPAutoTLS = true;
        $this->mail->SMTPSecure = 'ssl';
        $this->mail->SMTPKeepAlive = true;
        $this->mail->Host = $_ENV["SERWER_SMTP"];
        $this->mail->Port = $_ENV["PORT_SMTP"];
        $this->mail->WordWrap = 50;
        $this->mail->Priority = 1;
        $this->mail->isSMTP();
        $this->mail->isHTML();
        $this->mail->Username = $_ENV["USER_SMTP"];
        $this->mail->Password = $_ENV["PASS_SMTP"];
        $this->mail->setFrom($_ENV["FROM_MAIL"], "RPG Handy Helper Team");
        $this->mail->addReplyTo($_ENV["FROM_MAIL"], "RPG Handy Helper Team");
        $this->mail->From = $_ENV["FROM_MAIL"];
        $this->mail->FromName = "RPG Handy Helper Team";
    }

    public function sendActivationMail(string $email, string $token): array
    {
        try {
            $this->mail->addAddress($email);
            $this->mail->Subject = "RPG Handy Helper | Account activation";
            $this->mail->Body = activationAccount($this->url, $token);
            $this->mail->AltBody = "Hi!\n\n Thanks for signing up. \n\n To activate your account please click here: \n\n " . $this->url . "activate/" . $token;
            if (!$this->mail->send()) {
                $response = [
                    "icon" => "error",
                    "title" => "We couldn't send the activation email",
                    "message" => "Contact the administrator",
                    "footer" => "Error: 901",
                    "data" => [
                        "error" => $this->mail->ErrorInfo,
                        "code" => 901,
                    ]
                ];
            } else {
                $response = [
                    "icon" => "success",
                    "title" => "We sent you an activation email",
                    "message" => "Check your inbox",
                    "data" => [
                        "error" => null,
                        "code" => 0,
                    ]
                ];
            }
        } catch (Exception $e) {
            $response = [
                "icon" => "error",
                "title" => "We couldn't send the activation email",
                "message" => "Contact the administrator",
                "footer" => "Error: 902",
                "data" => [
                    "error" => $e->getMessage(),
                    "code" => 902,
                ]
            ];
        }
        return $response;
    }
}