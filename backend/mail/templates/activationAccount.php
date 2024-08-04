<?php

/**
 * Function to generate the activation account email template
 *
 * @param  string $hash
 * @return string HTML template as a string
 */
function activationAccount(string $url, string $hash): string
{
    $html = '
        <!DOCTYPE html>

    <html lang="en" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:v="urn:schemas-microsoft-com:vml">
    <head>
    <title></title>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <style>
            * {
                box-sizing: border-box;
            }

            body {
                margin: 0;
                padding: 0;
            }

            a[x-apple-data-detectors] {
                color: inherit !important;
                text-decoration: inherit !important;
            }

            #MessageViewBody a {
                color: inherit;
                text-decoration: none;
            }

            p {
                line-height: inherit
            }

            .desktop_hide,
            .desktop_hide table {
                mso-hide: all;
                display: none;
                max-height: 0px;
                overflow: hidden;
            }

            .image_block img+div {
                display: none;
            }

            @media (max-width:620px) {
                .desktop_hide table.icons-inner {
                    display: inline-block !important;
                }

                h1 {
                    font-size: 30px !important;
                }

                h3 {
                    font-size: 14px !important;
                }

                p {
                    font-size: 18px !important;
                }

                .icons-inner {
                    text-align: center;
                }

                .icons-inner td {
                    margin: 0 auto;
                }

                .mobile_hide {
                    display: none;
                }

                .row-content {
                    width: 90% !important;
                }

                .stack .column {
                    width: 100%;
                    display: block;
                }

                .mobile_hide {
                    min-height: 0;
                    max-height: 0;
                    max-width: 0;
                    overflow: hidden;
                    font-size: 0px;
                }

                .desktop_hide,
                .desktop_hide table {
                    display: table !important;
                    max-height: none !important;
                }
            }
        </style>
    </head>
    <body class="body" style="background-color: #070d29; margin: 0; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;">
    <table border="0" cellpadding="0" cellspacing="0" class="nl-container" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #070d29;" width="100%">
    <tbody>
    <tr>
    <td>
    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-1" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
    <tbody>
    <tr>
    <td>
    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 600px; margin: 0 auto;" width="600">
    <tbody>
    <tr>
    <td class="column column-1" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;" width="100%">
    <table border="0" cellpadding="0" cellspacing="0" class="image_block block-1" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
    <tr>
    <td class="pad" style="width:100%;">
    <div align="center" class="alignment" style="line-height:10px">
    <div style="max-width: 500px;"><img src="https://github.com/xEdziu/RPG-Handy-Helper/raw/main/banner-rpg.png" alt="Logo" height="auto"  style="display: block; height: auto; border: 0; width: 100%;" title="Logo" width="500"/></div>
    </div>
    </td>
    </tr>
    </table>
    <table border="0" cellpadding="10" cellspacing="0" class="divider_block block-2" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
    <tr>
    <td class="pad">
    <div align="center" class="alignment">
    <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
    <tr>
    <td class="divider_inner" style="font-size: 1px; line-height: 1px; border-top: 1px solid #dddddd;"><span style="word-break: break-word;"> </span></td>
    </tr>
    </table>
    </div>
    </td>
    </tr>
    </table>
    <table border="0" cellpadding="10" cellspacing="0" class="heading_block block-3" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
    <tr>
    <td class="pad">
    <h1 style="margin: 0; color: #12e1b9; direction: ltr; font-family: Arial, \'Helvetica Neue\', Helvetica, sans-serif; font-size: 38px; font-weight: 700; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 45.6px;"><span class="tinyMce-placeholder" style="word-break: break-word;">ACCOUNT ACTIVATION</span></h1>
    </td>
    </tr>
    </table>
    <table border="0" cellpadding="10" cellspacing="0" class="paragraph_block block-4" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;" width="100%">
    <tr>
    <td class="pad">
    <div style="color:#f3f4f4;direction:ltr;font-family:Arial, \'Helvetica Neue\', Helvetica, sans-serif;font-size:21px;font-weight:400;letter-spacing:0px;line-height:150%;text-align:justify;mso-line-height-alt:31.5px;">
    <p style="margin: 0;">Hi! Welcome to the game. It\'s good to have you on board. Please, click the button below to activate your account and start your own RPG campaign!</p>
    </div>
    </td>
    </tr>
    </table>
    <table border="0" cellpadding="10" cellspacing="0" class="button_block block-5" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
    <tr>
    <td class="pad">
    <div align="center" class="alignment">
    <a href="{url}" style="background-color:#12e1b9;border-bottom:0px solid transparent;border-left:0px solid transparent;border-radius:4px;border-right:0px solid transparent;border-top:0px solid transparent;color:#070d29;display:inline-block;font-family:Arial, \'Helvetica Neue\', Helvetica, sans-serif;font-size:18px;font-weight:400;mso-border-alt:none;padding-bottom:5px;padding-top:5px;text-align:center;text-decoration:none;width:auto;word-break:keep-all;"><span style="word-break: break-word; padding-left: 20px; padding-right: 20px; font-size: 18px; display: inline-block; letter-spacing: normal;"><span style="word-break: break-word; line-height: 36px;">ACTIVATE</span></span></a></div>
    </td>
    </tr>
    </table>
    <table border="0" cellpadding="10" cellspacing="0" class="divider_block block-6" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
    <tr>
    <td class="pad">
    <div align="center" class="alignment">
    <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
    <tr>
    <td class="divider_inner" style="font-size: 1px; line-height: 1px; border-top: 1px solid #dddddd;"><span style="word-break: break-word;"></span></td>
    </tr>
    </table>
    </div>
    </td>
    </tr>
    </table>
    <table border="0" cellpadding="10" cellspacing="0" class="heading_block block-7" role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
    <tr>
    <td class="pad">
    <h3 style="margin: 0; color: #f4f3f3; direction: ltr; font-family: Arial, \'Helvetica Neue\', Helvetica, sans-serif; font-size: 19px; font-weight: 700; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 22.8px;"><span class="tinyMce-placeholder" style="word-break: break-word;">RPG Handy Helper Team</span></h3>
    </td>
    </tr>
    </table>
    </td>
    </tr>
    </tbody>
    </table>
    </td>
    </tr>
    </tbody>
    </table>
    </td>
    </tr>
    </tbody>
    </table><!-- End -->
    </body>
    </html>
    ';

    $replacement = $url . "backend/api/v1/auth/activate.php?hash=" . $hash;
    return str_replace("{url}", $replacement, $html);

}