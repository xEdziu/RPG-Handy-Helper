<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="frontend/styles/style.css">
    <link rel="stylesheet" href="frontend/styles/main.css">
    <title>RPG Handy Helper</title>
    <link rel="icon" type="image/x-icon" href="/frontend/img/dark-bg-ico.ico">
    <script src="/frontend/js/script.js" defer></script>
</head>

<body>
    <div class="content-wrapper">
        <div class=container>
            <div class="column1">
                <header class="andika-bold">
                    <div class="title">
                    <h1><a href="/index.php" class="link">RPG Handy<br>Helper</a></h1> 
                    <h3>A tool for your campaigns</h3>
                    </div>
                </header>

                <main class="andika-regural">
                    <div class="buttons1">
                        <button class="linkBtn" onclick="location.href='frontend/html/login.html'">Login</button>
                        <button class="linkBtn" onclick="location.href='frontend/html/register.html'">Register</button>
                    </div>
                    <div class="buttons2">
                        <button class="linkBtn" onclick="location.href='demo.html'">Join</button>   
                        <button class="linkBtn" onclick="location.href='frontend/html/about.html'">Learn more</button>        
                    </div>
                    <div class="buttons3">
                        <button class="linkBtn" onclick="location.href='demo.html'">Demo campaign</button>
                        <button class="linkBtn" onclick="location.href='demo.html'">Authors</button>
                    </div>
                    
                </main>
            </div>

            <div class="column2">
                <img src="/frontend/img/dark-bg.svg" alt="swords">
            </div>
        </div>

       
       

        
    </div>
    <div class="popup" id="cookies">
        <span class="pinfo andika-regular" >RPG Handy Helper uses cookies in order to work properly [&nbsp 
            <a href="https://allaboutcookies.org/what-is-a-cookie" class="pinfo andika-regular">More Informations</a>&nbsp]
        </span>
        <span class="pbtn andika-regular" id="cdis">I Agree</span>
    </div>
   
   
    <footer class="andika-regular" id="footer">
            <!-- <h2>Authors:</h2> -->
            <!-- <p>Wiktor Siepka, Mateusz Andrzejewski, Szymon Nowicki, <a class="personalLink" href="https://github.com/xEdziu">Adrian Goral</a>, Mateusz Zubrzycki</p> -->
            <p>© <?php echo date("Y"); ?> RPG Handy Helper. All rights reserved.</p>
        </footer>

</body>
</html>