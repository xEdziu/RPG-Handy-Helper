<?php
session_start();
try {
    $csrf_token = bin2hex(random_bytes(32));
} catch (Exception $e) {
    die(json_encode([
        "icon" => "error",
        "title" => "We couldn't register you",
        "message" => "Contact the administrator",
        "footer" => "Kod błędu: 1101",
        "data" => [
            "error" => "Error during generating CSRF token: " . $e->getMessage(),
            "code" => 1101,
        ]
    ]));
}
$_SESSION['csrf_token'] = $csrf_token;
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../styles/login.css">
    <link rel="stylesheet" href="../styles/main.css">
    <title>Login | RPG Handy Helper</title>
    <link rel="icon" type="image/x-icon" href="../img/dark-bg-ico.ico">
    <script defer src="../js/togglePassword.js"></script>
    <script defer src="../js/changeColorTheme.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="darkMode">
    <nav>
        <img src="\frontend\img\light-theme.svg" alt="Icon" class="toggleicon" id="toggle-icon" onclick="changeColorTheme('toggle-icon', 'closedeye')">
    </nav>
    <h1 class="andika-bold link"><a href="/index.php" class="link">RPG Handy Helper</a></h1>
    <main>
        <h2 class="andika-bold">Log in to your account</h2>
        <form action="" id="loginForm">
            <input type="hidden" name="csrf_token" value="<?php echo $csrf_token;?>">
            <label for="username" class="andika-bold req">Username:</label>
            <div class="inputbox">
                <input type="text" placeholder="Enter Username" name="username" id="username" required>
            </div> 
            <label for="password" class="andika-bold req">Password: </label>
            <div class="inputbox">
                <input type="password" placeholder="Enter Password" name="password" required id="password">
                <img src="\frontend\img\cyan-hidden.svg" class="closedeye" alt="Image" id="eyeIcon-password" onclick="switchVisibility('password','eyeIcon-password')" >
            </div> 
            <button type="submit" class="loginbtn">Log in</button> <br>
            <div class="checkandforgot">
                <div class = checkboxandmark>
                    <input type="checkbox" checked="checked" id="checkbox">
                    <span class="andika-bold checkmarktext">Remember me? </span>
                </div>
                <div class="andika-bold forgor"><a href="forgetPassword.php" class="forgorpswlink">Forgot password?</a></div>
            </div>
        </form>
        <h1 class="andika-bold registerlink">Don't have an account? <a href="register.php" class="register">Register here</a> </h1>
    </main>
</body>

</html>