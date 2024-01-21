
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
    ws.onopen = function(data){
        //소켓이 열리면 동작
    }

    ws.onmessage = function(data) {
        var msg = data.data;
        if(msg != null && msg.trim() != ''){
            var d = JSON.parse(msg);
            if(d.type == "getId"){
                var si = d.sessionId != null ? d.sessionId : "";
                if(si != ''){
                    $("#sessionId").val(si);
                }
            }else if(d.type == "message"){
                if(d.sessionId == $("#sessionId").val()){
                    $("#chating").append("<div class='outgoing_msg'>"+"<div class='sent_msg'>"+"<p>" + d.msg + "</p>"
                        +"<span class='time_date'>"+"11:01 AM"+"</span></div></div>");
                    scrollToBottom();
                }else{
                    $("#chating").append("<div class='incoming_msg'>"+"<div class='received_msg'>"
                        +"<div class='received_withd_msg'>"+"<p>" + d.msg + "</p>"
                        +"<span class='time_date'>"+"11:01 AM"+"</span></div></div></div>");
                    scrollToBottom();
                }

            }else{
                console.warn("unknown type!")
            }

        }
    }

    document.addEventListener("keypress", function(e){
        if(e.keyCode == 13){
            send();
        }
    });
}

function send() {

    var option ={
        type: "message",
        sessionId : $("#sessionId").val(),
        msg : $("#chatting").val()
    }
    ws.send(JSON.stringify(option))
    $('#chatting').val("");
    scrollToBottom();
}

function scrollToBottom() {
    var chatHistory = document.getElementById("chating");
    chatHistory.scrollTop = chatHistory.scrollHeight;
}