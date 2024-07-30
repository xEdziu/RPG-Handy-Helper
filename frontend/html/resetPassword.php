<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../styles/login.css">
    <title>Reset password | RPG Handy Helper</title>
    <link rel="icon" type="image/x-icon" href="../img/dark-bg-ico.ico">
    <script defer src="../js/togglePassword.js"></script>
</head>
<body>
    <h1 class="andika-bold link"><a href="/index.php" class="link">RPG Handy Helper</a></h1>
    <main>
        <form action="" id="resetForm">
        <label for="newpassword" class="andika-bold req">Your new password:</label>
        <div class="inputbox">
            <input type="password" name="newpassword"  placeholder="Enter your new passowrd" autocomplete="off" required id="password">
            <img src="..\img\cyan-hidden.svg" class="closedeye" alt="Image" id="eyeIcon-password" onclick="switchVisibility('password','eyeIcon-password')">
        </div>
        <label for="resetrepeatpassword" class="andika-bold req">Repeat your password:</label>
        <div class=inputbox>
            <input type="password" name="resetrepeatpassword" placeholder="Repeat your new passowrd" autocomplete="off" required id="passwordrepeat">
            <img src="..\img\cyan-hidden.svg" class="closedeye" alt="Image" id="eyeIcon-passwordrepeat" onclick="switchVisibility('passwordrepeat','eyeIcon-passwordrepeat')">
        </div>
        <button type="submit">Reset</button>
        </form>
        <h1 class="andika-bold registerlink"><a href="/index.php" class="register">Temporary link for going back.</a></h1>
    </main>
</body>

</html>