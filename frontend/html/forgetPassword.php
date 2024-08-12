<?php
session_start();
try {
    $csrf = bin2hex(random_bytes(32));
} catch (Exception $e) {
    echo "An error occurred. Please try again.";
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
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="darkMode">
    <nav>
        <img src="\frontend\img\light-theme.svg" alt="Icon" class="toggleicon" id="toggle-icon" onclick="changeColorTheme('toggle-icon', 'closedeye')">
    </nav>
    <h1 class="andika-bold link"><a href="/index.php" class="link">RPG Handy Helper</a></h1>
    <main>
        <form action="" id="forgetForm">
            <input type="hidden" name="csrf" value="<?php echo $csrf; ?>">
            <label for="resetemail" class="andika-bold"><p class="forgetlabeltext">Forgot your password? Don't worry, we will send you a link to reset it!</p></label>
            <div class=inputbox>
                <input type="text" placeholder="Enter email" name="resetemail" id="resetemail" autocomplete="off" required>
            </div>
            <button type="submit">Submit</button>
        </form>
        <h1 class="andika-bold registerlink">Temporary Link for password reset site <a href="resetPassword.php" class="register">here</a></h1>
    </main>
</body>
</html>