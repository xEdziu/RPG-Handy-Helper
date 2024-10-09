document.addEventListener('DOMContentLoaded', (event)=> {
    let cookies=localStorage.getItem("cookies");
    let popup= document.getElementById("cookies");
    let container=document.querySelector(".container");
    if(cookies != 1){
        popup.style.display="flex";
        document.getElementById("cdis").onclick= function(){
            popup.style.display="none";
            localStorage.setItem("cookies",0)
        };
    }   
});
