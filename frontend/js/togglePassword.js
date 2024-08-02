function switchVisibility(inputName,eyeIconId)
    {
        let passwordInput = document.getElementById(inputName);
        let eyeIcon = document.getElementById(eyeIconId);
        if (passwordInput.type === 'password'){
            if(document.body.classList.contains("darkMode"))
                eyeIcon.src = "../img/cyan-shown.svg";
            else
                eyeIcon.src = "../img/red-shown.svg";
            passwordInput.type = 'text';
        } else {
            if(document.body.classList.contains("darkMode"))
                eyeIcon.src = "../img/cyan-hidden.svg";
            else
                eyeIcon.src = "../img/red-hidden.svg";
            passwordInput.type = 'password';
        }
    }