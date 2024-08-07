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
    <title>Register | RPG Handy Helper</title>
    <link rel="icon" type="image/x-icon" href="../img/dark-bg-ico.ico">
    <script defer src="../js/togglePassword.js"></script>
    
    
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://www.google.com/recaptcha/api.js?render=6Lfz_BsqAAAAAPepoWRQn1x7rQAxALA-wyfsjrzM"></script>
</head>
<body>
    <h1 class="andika-bold link"><a href="/index.php" class="link">RPG Handy Helper</a></h1>
    <main>
        <h2 class="andika-bold">Register your account</h2>
        <form action="" id="registerForm">
            <input type="hidden" name="csrf_token" value="<?php echo $csrf_token;?>">
            <label for="nickname" class="andika-bold req">Nickname:</label>
            <div class="inputbox">
                <input type="text" placeholder="Enter Nickname" maxlength="42" name="nickname" id="nickname" required autocomplete="off">
            </div>
            <label for="email" class="andika-bold req">Email address:</label>
            <div class="inputbox">
                <input type="text" placeholder="Enter email address" name="email" id="email" required autocomplete="off">
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
                <input type="text" placeholder="Enter Name" maxlength="42" name="name" id="name" required autocomplete="off">
            </div>
            <label for="surname" class="andika-bold req">Surname:</label>
            <div class="inputbox">
                <input type="text" placeholder="Enter Surname" maxlength="42" name="surname" id="surname" required autocomplete="off">
            </div>
            <label for="discord" class="andika-bold">Discord Tag:</label>
            <div class="inputbox">
                <input type="text" placeholder="Enter Discord Tag" maxlength="42" name="discord" id="discord" autocomplete="off"> 
            </div>
            
            <button type="submit">Register</button>
        </form>
        <h1 class="andika-bold registerlink">Already have an account? <a href="login.php" class="register">Login here</a> </h1>
    </main>
</body>
<script>
    const register = document.getElementById("registerForm");
    register.addEventListener("submit", (e) => {
        e.preventDefault();
        let recaptcha = false;
        grecaptcha.ready(function () {
            grecaptcha.execute('6Lfz_BsqAAAAAPepoWRQn1x7rQAxALA-wyfsjrzM', {action: 'submit'}).then(function (token) {
                const dataR = new FormData();
                dataR.append("token", token);
                fetch("/backend/api/v1/auth/recaptcha.php", {
                    method: "POST",
                    body: dataR
                })
                    .then(response => response.json())
                    .then(data => {
                        console.log(data);
                        if (data.success)
                            recaptcha = true;
                    })
                     .then(() => {
                         if (recaptcha) {
                             registerUser();
                        } else {
                            Swal.fire({
                                icon: 'warning',
                                title: 'Błąd',
                                text: 'Recaptcha się nie powiodła',
                                footer: 'Botom wstęp wzbroniony!'
                            })
                        }
                    })
            });
        });

        function registerUser() {
            const formData = new FormData();
            const inputs = document.getElementById("registerForm").elements;
            const inputNickname = inputs["nickname"].value;
            const inputCsrftoken = inputs["csrf_token"].value;
            const inputPassword = inputs["password"].value;
            const inputEmail = inputs["email"];
            const inputPasswordRepeat = inputs["passwordrepeat"].value;
            const inputName = inputs["name"];
            const inputSurname = inputs["surname"];
            const inputDiscord = inputs["discord"];
            if(inputPassword != inputPasswordRepeat){
                Swal.fire({
                    title: "The Internet?",
                    text: "That thing is still around?",
                    icon: "warning",
                    footer: "test"
                });
            }
            // fetch("../../api/auth/register.php", {
            //     method: "POST",
            //     body: formData
            // })
            //     .then(
            //         Swal.fire({
            //             icon: 'info',
            //             title: 'Proszę czekać',
            //             text: 'Trwa rejestracja...',
            //             allowOutsideClick: false,
            //             allowEscapeKey: false,
            //             allowEnterKey: false,
            //             showConfirmButton: false,
            //             timer: 5000
            //         })
            //     )
            //     .then(response => response.json())
            //     .then(data => {
            //         Swal.fire({
            //             icon: data.icon,
            //             title: data.title,
            //             text: data.message,
            //             footer: data.footer
            //         })
            //             .then(() => {
            //                 if (data.icon == "success") {
            //                     window.location.href = "signin.php";
            //                 }
            //             });
            //     })
            //     .catch(error => {
            //         console.error(error);
            //     });
        }
    });
</script>

</html>