<?php
session_start();

require '../../vendor/autoload.php';

use Eddy\RpgHandyHelper\DTO\User;
use Eddy\RpgHandyHelper\Database\DatabaseConnector;

try {
    $csrf = bin2hex(random_bytes(32));
} catch (Exception $e) {
    echo "An error occurred. Please try again.";
    exit;
}

if (empty($_GET['hash'])){
    http_response_code(302);
    header("Location: /errors/badActivationLink.html");
}
$hash = DatabaseConnector::sanitizeString($_GET['hash']);

try {
    $user = User::getUserByHash($hash);
} catch (Exception $e) {
    http_response_code(500);
    header("Location: /errors/500.html");
    exit;
}

if (is_array($user)){
    http_response_code(302);
    header("Location: /errors/badActivationLink.html");
    exit;
}

if ($user->getHash() != $hash){
    http_response_code(302);
    header("Location: /errors/badActivationLink.html");
    exit;
}
$_SESSION['csrf'] = $csrf;
?>
<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../styles/login.css">
    <link rel="stylesheet" href="../styles/main.css">
    <title>Reset password | RPG Handy Helper</title>
    <link rel="icon" type="image/x-icon" href="../img/dark-bg-ico.ico">
    <script defer src="../js/changeColorTheme.js"></script>
    <script defer src="../js/togglePassword.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="darkMode">
    <nav>
        <img src="\frontend\img\light-theme.svg" alt="Icon" class="toggleicon" id="toggle-icon" onclick="changeColorTheme('toggle-icon', 'closedeye')">
    </nav>
    <h1 class="andika-bold link"><a href="/index.php" class="link">RPG Handy Helper</a></h1>
    <main>
        <form action="" id="resetForm">
        <input type="hidden" name="csrf" value="<?php echo $csrf; ?>">
        <input type="hidden" name="hash" value="<?php echo $hash; ?>">
        <label for="newpassword" class="andika-bold req">Your new password:</label>
        <div class="inputbox">
            <input type="password" name="newpassword"  placeholder="Enter your new passoword" autocomplete="off" required id="password">
            <img src="..\img\cyan-hidden.svg" class="closedeye" alt="Image" id="eyeIcon-password" onclick="switchVisibility('password','eyeIcon-password')">
        </div>
        <label for="resetrepeatpassword" class="andika-bold req">Repeat your password:</label>
        <div class=inputbox>
            <input type="password" name="resetrepeatpassword" placeholder="Repeat your new passoword" autocomplete="off" required id="passwordrepeat">
            <img src="..\img\cyan-hidden.svg" class="closedeye" alt="Image" id="eyeIcon-passwordrepeat" onclick="switchVisibility('passwordrepeat','eyeIcon-passwordrepeat')">
        </div>
        <button type="submit">Reset</button>
        </form>
        <h1 class="andika-bold registerlink"><a href="/index.php" class="register">Temporary link for going back.</a></h1>
    </main>
</body>

</html>