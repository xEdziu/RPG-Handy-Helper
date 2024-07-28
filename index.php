<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="frontend/styles/style.css">
    <title>RPG Handy Helper</title>
    <link rel="icon" type="image/x-icon" href="/frontend/img/dark-bg-ico.ico">
</head>
<body>
    <header class="andika-bold">
        <div class="logo">
        <h1><a href="/index.php" class="link">RPG Handy<br>Helper</a></h1> 
        <h3>A tool for your campaigns</h3>
        </div>
        <img src="/frontend/img/dark-bg-ico.svg" alt="swords">
    </header>
   
    
    <main class="andika-regural">
        <button class="linkBtn" onclick="location.href='frontend/html/login.html'">Login</button>
        <button class="linkBtn" onclick="location.href='frontend/html/register.html'">Register</button>
        <br>
        <button class="linkBtn" onclick="location.href='frontend/html/about.html'">Learn more</button>
        <button class="linkBtn" onclick="location.href='demo.html'">Demo campaign</button>
    </main>
    <footer class="andika-regular">
        <h2>Authors:</h2>
        <p>Wiktor Siepka, Mateusz Andrzejewski, Szymon Nowicki</p>
        <p><a class="personalLink" href="https://github.com/xEdziu">Adrian Goral</a>, Mateusz Zubrzycki</p>
    </footer>
    <div class="popup" id="cookies">
    <span class="pinfo andika-regular" >RPG Handy Helper uses cookies in order to work properly [&nbsp 
        <a href="https://allaboutcookies.org/what-is-a-cookie" class="pinfo andika-regular">More Informations</a>&nbsp]
    </span>
    <span class="pbtn andika-regular" id="cdis">I Agree</span>
</div>
</body>
<script>
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
    
</script>
</html>