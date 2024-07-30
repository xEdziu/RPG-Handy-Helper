<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../styles/login.css">
    <title>Register | RPG Handy Helper</title>
    <link rel="icon" type="image/x-icon" href="../img/dark-bg-ico.ico">
    <script defer src="../js/togglePassword.js"></script>
</head>
<body>
    <h1 class="andika-bold link"><a href="/index.php" class="link">RPG Handy Helper</a></h1>
    <main>
        <h2 class="andika-bold">Register your account</h2>
        <form action="" id="registerForm">
            <label for="nickname" class="andika-bold req">Nickname:</label>
            <div class="inputbox">
                <input type="text" placeholder="Enter Nickname" maxlength="42" name="nickname" required autocomplete="off">
            </div>
            <label for="email" class="andika-bold req">Email address:</label>
            <div class="inputbox">
                <input type="text" placeholder="Enter email address" name="email" required autocomplete="off">
            </div>
            <label for="password" class="andika-bold req">Password:</label>
            <div class="inputbox">
                <input type="password" placeholder="Enter password"  name="password" required autocomplete="off" id="password">
                <img src="..\img\cyan-hidden.svg" class="closedeye" alt="Image" id="eyeIcon-password" onclick="switchVisibility('password','eyeIcon-password')">
            </div>
            <label for="passowrdrepeat" class="andika-bold req">Repeat your password:</label>
            <div class="inputbox">
                <input type="password" placeholder="Repeat password" name="passowrdrepeat" required autocomplete="off" id="passwordrepeat">
                <img src="..\img\cyan-hidden.svg" class="closedeye" alt="Image" id="eyeIcon-passwordrepeat" onclick="switchVisibility('passwordrepeat','eyeIcon-passwordrepeat')">
            </div>
            <label for="name" class="andika-bold req">Name:</label>
            <div class="inputbox">
                <input type="text" placeholder="Enter Name" maxlength="42" name="name" required autocomplete="off">
            </div>
            <label for="surname" class="andika-bold req">Surname:</label>
            <div class="inputbox">
                <input type="text" placeholder="Enter Surname" maxlength="42" name="surname" required autocomplete="off">
            </div>
            <label for="discord" class="andika-bold">Discord Tag:</label>
            <div class="inputbox">
                <input type="text" placeholder="Enter Discord Tag" maxlength="42" name="discord" autocomplete="off"> 
            </div>
            
            <button type="submit">Register</button>
        </form>
        <h1 class="andika-bold registerlink">Already have an account? <a href="login.php" class="register">Login here</a> </h1>
    </main>
</body>

</html>