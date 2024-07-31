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
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
    <h1 class="andika-bold link"><a href="/index.php" class="link">RPG Handy Helper</a></h1>
    <main>
        <h2 class="andika-bold">Log in to your account</h2>
        <form action="" id="loginForm">
            <label for="nickname" class="andika-bold req">Nickname:</label>
            <div class="inputbox">
                <input type="text" placeholder="Enter Username" name="nickname" id="nickname" required>
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