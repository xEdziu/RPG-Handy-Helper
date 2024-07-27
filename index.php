<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="frontend/styles/style.css">
    <title>RPG Handy Helper</title>
</head>
<body>
    <header class="andika-bold">
        <h1>RPG Handy<br>Helper</h1>
        <h3>A tool for your campaigns</h3>
    </header>
    <main class="andika-regural">
        <button class="linkBtn" onclick="location.href='html/login.html'">Login</button>
        <button class="linkBtn" onclick="location.href='html/register.html'">Register</button>
        <br>
        <button class="linkBtn" onclick="location.href='about.html'">Learn more</button>
        <button class="linkBtn" onclick="location.href='demo.html'">Demo campaign</button>
    </main>
    <footer class="andika-regular">
        <h2>Authors:</h2>
        <p>Wiktor Siepka, Mateusz Andrzejewski</p>
        <p><a class="personalLink" href="https://github.com/xEdziu">Adrian Goral</a>, Mateusz Zubrzycki</p>
    </footer>
    <div class="popup" id="cookies">
    <span class="pinfo andika-regular" >RPG Handy Helper uses cookie files in order to work properly [&nbsp 
        <a href="" class="pinfo andika-regular">More Informations</a>&nbsp]
    </span>
    <span class="pbtn andika-regular" id="cdis">I Agree</span>
</div>
<script>
    document.addEventListener('DOMContentLoaded', (event)=> {
            document.getElementById("cookies").style.bottom="0";
        });
    document.getElementById("cdis").onclick= function(){
        document.getElementById("cookies").style.display="none";
        };
</script>
</body>

</html>