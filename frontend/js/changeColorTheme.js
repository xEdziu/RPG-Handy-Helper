function changeColorTheme(themeIconId, eyeClass){
    const body = document.body;
    body.classList.toggle('darkMode');
    const icons = document.querySelectorAll('.toggleicon, .closedeye');

    icons.forEach(icon => {
        const iconName = icon.src.split('/').pop();

        if (icon.classList.contains('toggleicon')) {
            if (iconName === 'light-theme.svg') {
                icon.src = "../img/dark-theme.svg";
            } else {
                icon.src = "../img/light-theme.svg";
            }
        } else if (icon.classList.contains('closedeye')) {
            if (iconName === 'cyan-hidden.svg') {
                icon.src = "../img/red-hidden.svg";
            } else if (iconName === 'cyan-shown.svg') {
                icon.src = "../img/red-shown.svg";
            } else if (iconName === 'red-hidden.svg') {
                icon.src = "../img/cyan-hidden.svg";
            } else if (iconName === 'red-shown.svg') {
                icon.src = "../img/cyan-shown.svg";
            }
        }
    });
}