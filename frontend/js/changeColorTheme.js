function changeColorTheme(themeIconId, eyeClass){
    let x = document.getElementById(themeIconId);
    let eyes = document.getElementsByClassName(eyeClass);
    document.body.classList.toggle("darkMode");
    // You shouldn't have let me cook (it was my first time with js okay plz dont judge)
    for (let i = 0; i < eyes.length; i++) {
        let eye = eyes[i];
        if(!document.body.classList.contains("darkMode")){
            x.src = "/frontend/img/dark-theme.svg";
            if(eye.src.indexOf("cyan-hidden.svg") !== -1)
                eye.src = "/frontend/img/red-hidden.svg";
            else if(eye.src.indexOf("cyan-shown.svg") !== -1)
                eye.src = "/frontend/img/red-shown.svg";
        }
        else{
            x.src = "/frontend/img/light-theme.svg";
            if(eye.src.indexOf("red-shown.svg") !== -1)
                eye.src = "/frontend/img/cyan-shown.svg";
            else if(eye.src.indexOf("red-hidden.svg") !== -1)
                eye.src = "/frontend/img/cyan-hidden.svg";
        }
    }
}