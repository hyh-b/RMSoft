
function toggleChat() {

    var chatInterface = document.getElementById("chatInterface");
    if (chatInterface.style.display === "none") {
        chatInterface.style.display = "block";
        webSocketOpen();
    } else {
        chatInterface.style.display = "none";
        webSocketClose();
    }
}

$('.toggleChatButton').on('click', toggleChat);
$('#sendMessageButton').on('click', send);

var webSocket;

function webSocketOpen(){
    alert("열림");
    webSocket = new WebSocket("ws://" + location.host + "/chating/"+$("#roomNumber").val());
    webSocketEvent();
}

function webSocketClose() {
    alert("닫힘");
    if(webSocket) {
        webSocket.close(); // 웹소켓 연결 종료
    }
}

function webSocketEvent() {
    webSocket.onopen = function(data){
        //소켓이 열리면 동작
    }

    /*webSocket.onmessage = function(data) {
        alert("메세지옴");
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
                    $("#messageHistory").append("<div class='outgoing_msg'>"+"<div class='sent_msg'>"+"<p>" + d.msg + "</p>"
                        +"<span class='time_date'>"+"11:01 AM"+"</span></div></div>");
                    scrollToBottom();
                }else{
                    $("#messageHistory").append("<div class='incoming_msg'>"+"<div class='received_msg'>"
                        +"<div class='received_withd_msg'>"+"<p>" + d.msg + "</p>"
                        +"<span class='time_date'>"+"11:01 AM"+"</span></div></div></div>");
                    scrollToBottom();
                }

            }else{
                console.warn("unknown type!")
            }

        }
    }*/

    webSocket.onmessage = function(data) {
        var msg = data.data;
        if(msg != null && msg.trim() != ''){
            var d = JSON.parse(msg);
            if(d.type == "getId"){
                $("#sessionId").val(d.sessionId);
            } else if(d.type == "message"){
                // 세션 ID가 현재 세션과 다르면 상대방이 보낸 메시지로 간주
                if(d.sessionId !== $("#sessionId").val()){
                    $("#messageHistory").append("<div class='incoming_msg'>" +
                        "<div class='received_msg'>" +
                        "<div class='received_withd_msg'>" +
                        "<p>" + d.msg + "</p>" +
                        "<span class='time_date'>11:01 AM</span>" +
                        "</div></div></div>");
                    scrollToBottom();
                }
            } else {
                console.warn("unknown type!");
            }
        }
    };

    document.addEventListener("keypress", function(e){
        if(e.keyCode == 13){
            send();
        }
    });
}

function send() {
    var messageContent = $("#writeMessage").val();
    var memberId = $("#memberId").val();
    var option ={
        type: "message",
        chatCoder: $("#roomNumber").val(),
        memberId: memberId,
        sessionId : $("#sessionId").val(),
        message : messageContent
    }
    webSocket.send(JSON.stringify(option))

    var newMessage = "<div class='outgoing_msg'><div class='sent_msg'><p>" + messageContent + "</p><span class='time_date'> 11:01 AM </span></div></div>";
    $("#messageHistory").append(newMessage);

    $('#writeMessage').val("");
    scrollToBottom();
}

function scrollToBottom() {
    var chatHistory = document.getElementById("messageHistory");
    chatHistory.scrollTop = chatHistory.scrollHeight;
}