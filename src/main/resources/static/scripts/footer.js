const hearts = ['❤️', '💙', '💚', '💛', '💜', '🧡', '🫀'];
const footer = document.querySelector("footer");
const footerH3 = footer.querySelector("h3");
function changeHeart() {
    const randomIndex = Math.floor(Math.random() * hearts.length);
    footerH3.textContent = `Z miłości do TTRPG ${hearts[randomIndex]} © 2025 RPG Handy Helper`
}
changeHeart();