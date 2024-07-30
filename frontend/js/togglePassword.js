function switchVisibility(inputName,eyeIconId)
    {
        let passwordInput = document.getElementById(inputName);
        let eyeIcon = document.getElementById(eyeIconId);
        if (passwordInput.type === 'password'){
            passwordInput.type = 'text';
            eyeIcon.src = "../img/cyan-shown.svg";
        } else {
            passwordInput.type = 'password';
            eyeIcon.src = "../img/cyan-hidden.svg";
        }
    }