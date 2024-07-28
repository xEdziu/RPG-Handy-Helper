<?php

namespace Eddy\RpgHandyHelper\Mail\Templates;


/**
 * Function to generate the activation account email template
 *
 * @param  string $hash
 * @return string HTML template as a string
 */
function activationAccount(string $url, string $hash): string
{
    return <<<HTML
    <html>
    <head>
        <title>Activate your account</title>
    </head>
    <body>
        <h1>Activate your account</h1>
        <p>Click the link below to activate your account:</p>
        <a href="$url/activate/$hash">Activate account</a>
    </body>
    </html>
    HTML;
}