
function toggleChat() {

    var chatInterface = document.getElementById("chatInterface");
    if (chatInterface.style.display === "none") {
        chatInterface.style.display = "block";
    } else {
        chatInterface.style.display = "none";
    }
}

$('.toggleChatButton').on('click', toggleChat);