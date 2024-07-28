<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../styles/login.css">
    <title>Register | RPG Handy Helper</title>
    <link rel="icon" type="image/x-icon" href="../img/dark-bg-ico.ico">
</head>
<body>
    <h1 class="andika-bold link"><a href="/index.php" class="link">RPG Handy Helper</a></h1>
    <main>
        <h2 class="andika-bold">Register your account</h2>
        <form action="">
            <label for="nickname" class="andika-bold">Nickname:</label>
            <input type="text" placeholder="Enter Nickname" class="container" maxlength="42" name="nickname" required>
            <label for="email" class="andika-bold">Email address:</label>
            <input type="text" placeholder="Enter email address" class="container" name="email" required>
            <label for="password" class="andika-bold">Password:</label>
            <input type="password" placeholder="Enter passoword" class="container" name="password" required>
            <label for="passowrdrepeat" class="andika-bold">Repeat your password:</label>
            <input type="password" placeholder="Repeat password" class="container" name="passowrdrepeat" required>
            <label for="name" class="andika-bold">Name:</label>
            <input type="text" placeholder="Enter Name" class="container" maxlength="42" name="name">
            <label for="surname" class="andika-bold">Surname:</label>
            <input type="text" placeholder="Enter Surname" class="container" maxlength="42" name="surname" required>
            <label for="discord" class="andika-bold">Discord Tag</label>
            <input type="text" placeholder="Enter Discord Tag" class="container" maxlength="42" name="discord">
            
            <button type="submit">Register</button>
        </form>
        <h1 class="andika-bold registerlink">Already have an account? <a href="login.php" class="register">Login here</a> </h1>
    </main>
</body>
</html>