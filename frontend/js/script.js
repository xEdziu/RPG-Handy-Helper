document.addEventListener('DOMContentLoaded', (event)=> {
    let cookies=localStorage.getItem("cookies");
    let popup= document.getElementById("cookies");
    if(cookies != 1){
        popup.style.display="flex";
        document.getElementById("cdis").onclick= function(){
            popup.style.display="none";
            localStorage.setItem("cookies",1)
        };
    }   
});

const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
        console.log(entry)
        if(entry.isIntersecting){
            entry.target.classList.add('show');
        } else {
            entry.target.classList.remove('show');
        }
    });

});

const hiddenElemnets = document.querySelectorAll('.hidden');
hiddenElemnets.forEach((el)=>observer.observe(el));



