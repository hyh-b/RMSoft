
function toggleChat() {

    var chatInterface = document.getElementById("chatInterface");
    if (chatInterface.style.display === "none") {
        chatInterface.style.display = "block";
        wsOpen();
    } else {
        chatInterface.style.display = "none";
        wsClose();
    }
}

$('.toggleChatButton').on('click', toggleChat);
$('#sendMessageButton').on('click', send);

var ws;

function wsOpen(){
    alert("열림");
    ws = new WebSocket("ws://" + location.host + "/chating");
    wsEvt();
}

function wsClose() {
    alert("닫힘");
    if(ws) {
        ws.close(); // 웹소켓 연결 종료
    }
}

function wsEvt() {

    ws.onmessage = function(data) {
        var msg = data.data;
        if(msg != null && msg.trim() != ''){
            $("#chating").append("<div class='outgoing_msg'>"+"<div class='sent_msg'>"+"<p>" + msg + "</p>"
                +"<span class='time_date'>"+"11:01 AM"+"</span></div></div>");
            scrollToBottom();
        }
    }

    document.addEventListener("keypress", function(e){
        if(e.keyCode == 13){
            send();
        }
    });
}

function send() {

    var msg = $("#chatting").val();
    ws.send(msg);
    $('#chatting').val("");
    scrollToBottom();
}

function scrollToBottom() {
    var chatHistory = document.getElementById("chating");
    chatHistory.scrollTop = chatHistory.scrollHeight;
}