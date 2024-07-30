function changeColorTheme(themeIconId, EyeId){
    let x = document.getElementById(themeIconId)
    let eye = document.getElementById(EyeId)
    document.body.classList.toggle("darkMode");
    if(!document.body.classList.contains("darkMode")){
        x.src = "/frontend/img/dark-theme.svg";
        if(eye.src.indexOf("cyan-hidden.svg"))
            eye.src == "/frontend/img/red-hidden.svg";
        else
            eye.src == "/frontend/img/cyan-hidden.svg";
    }
    else{
        x.src = "/frontend/img/light-theme.svg";
        if(eye.src.indexOf("cyan-shown.svg"))
            eye.src == "/frontend/img/red-shown.svg";
        else
            eye.src == "/frontend/img/cyan-shown.svg";
    }
}

function toggleEye(eyeId){

}