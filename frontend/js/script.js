document.addEventListener('DOMContentLoaded', (event)=> {
    let cookies=localStorage.getItem("cookies");
    let popup= document.getElementById("cookies");
    let container=document.querySelector(".container");
    if(cookies != 2){
        container.style.marginTop="9.75vh";
        popup.style.display="flex";
        document.getElementById("cdis").onclick= function(){
            popup.style.display="none";
            container.style.marginTop="0vh";
            localStorage.setItem("cookies",1)
        };
    }   
});
