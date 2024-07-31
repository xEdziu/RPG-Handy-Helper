<?php

namespace Eddy\RpgHandyHelper\Mail;

require __DIR__ . '/../../vendor/autoload.php';

use Eddy\RpgHandyHelper\Mail;

$mailer = new Mail\Mailer();
$email = '';
$token = 'testToken';

$response = $mailer->sendActivationMail($email, $token);

if ($response['data']['code'] === 0) {
    echo 'Email sent successfully';
} else {
    echo "Email not sent \n<hr/>\n";
    var_dump($response);
}