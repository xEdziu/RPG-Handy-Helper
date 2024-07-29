<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../styles/login.css">
    <title>Login | RPG Handy Helper</title>
    <link rel="icon" type="image/x-icon" href="../img/dark-bg-ico.ico">
</head>
<body>
    <h1 class="andika-bold link"><a href="/index.php" class="link">RPG Handy Helper</a></h1>
    <main>
        <h2 class="andika-bold">Log in to your account</h2> <br>
        <form action="">
            <label for="nickname" class="andika-bold">Nickname:</label> 
            <input type="text" placeholder="Enter Username" name="nickname" required maxlength="42" class="container" required> 
            <label for="password" class="andika-bold">Password: </label>
            <div class="inputbox">
                <input class="container" type="password" placeholder="Enter Password" name="password" required maxlength="42" required>
                <img src="..\img\closedeye.png" class="closedeye" alt="chuj">
            </div> 
            <button type="submit" class="loginbtn">Log in</button> <br>
            <div class="checkandforgot">
                <div class = checkboxandmark>
                    <input type="checkbox" checked="checked">
                    <span class="andika-bold checkmarktext">Remember me? </span>
                </div>
                <div class="andika-bold forgor"><a href="forgetPassword.php" class="forgorpswlink">Forgot password?</a></div>
            </div>
        </form>
        <h1 class="andika-bold registerlink">Don't have an account? <a href="register.php" class="register">Register here</a> </h1>
    </main>

</body>


</html>