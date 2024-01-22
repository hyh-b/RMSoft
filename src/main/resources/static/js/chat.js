
var chatInterface = document.getElementById("chatInterface");
var chatCode;
$('.toggleChatButton').on('click', function (){
    if(chatInterface.style.display === "none") {
        checkChatRoom();
    }else {
        webSocketClose();
    }
});

$('#sendMessageButton').on('click', send);
function checkChatRoom() {
    $.ajax({
        url: '/api/chat/check',
        type: 'GET',
        data: {memberId: memberId},
        success: handleChatCheckRoomResponse,
        error: function (error) {
            console.error('Error:', error);
        }
    });

}

// 채팅방 존재 여부에 따른 처리
function handleChatCheckRoomResponse(response) {
    if (!response) {
        if (confirm("채팅 상담을 시작하시겠습니까?")) {
            createChatRoom(memberId);
        }
    } else {
        findChatCode();
    }
}

function createChatRoom(memberId) {

    $.ajax({
        url: '/api/chat/room',
        type: 'POST',
        data: { memberId: memberId },
        success: function(chatCode) {

            console.log('채팅룸 생성 성공:'+chatCode);
            webSocketOpen(chatCode);
            chatInterface.style.display = "block";
        },
        error: function(error) {
            console.error('채팅룸 생성 실패:', error);
        }
    });
}

function findChatCode() {
    $.ajax({
        url: '/api/chat/code',
        type: 'GET',
        data: { memberId: memberId },
        success: function(chatCode) {
            console.log('챗코드 확인:'+chatCode);
            webSocketOpen(chatCode);
            chatInterface.style.display = "block";
        },
        error: function(error) {
            console.error('챗코드 확인 실패:', error);
        }
    });
}

var webSocket;

function webSocketOpen(chatCodeParam){
    chatCode = chatCodeParam;
    alert("열림");
    webSocket = new WebSocket("ws://" + location.host + "/ws/chat/"+chatCode);
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

    webSocket.onmessage = function(data) {
        var msg = data.data;

        if(msg != null && msg.trim() != ''){
            var d = JSON.parse(msg);
            if (d.type == "getId") {
                $("#sessionId").val(d.sessionId); // 세션 ID 저장
            } else if (d.type == "message") {
                var isOwnMessage = d.sessionId === $("#sessionId").val(); // 자신이 보낸 메시지인지 확인
                var messageContent;
                if (isOwnMessage) {
                    // 자신이 보낸 메시지
                    messageContent = "<div class='outgoing_msg'>" +
                        "<div class='sent_msg'>" +
                        "<p>" + d.message + "</p>" +
                        "<span class='time_date'>11:01 AM</span>" + // 시간은 예시입니다.
                        "</div>" +
                        "</div>";
                } else {
                    // 다른 사용자가 보낸 메시지
                    messageContent = "<div class='incoming_msg'>" +
                        "<div class='received_msg'>" +
                        "<div class='received_withd_msg'>" +
                        "<p>" + d.message + "</p>" +
                        "<span class='time_date'>11:01 AM</span>" + // 시간은 예시입니다.
                        "</div>" +
                        "</div>" +
                        "</div>";
                }

                $("#messageHistory").append(messageContent);
                scrollToBottom();
            } else {
                console.warn("unknown type!", d);
            }
        }
    };

    document.addEventListener("keypress", function(e){
        console.log("엔터");
        if(e.keyCode === 13){
            send();
        }
    });
}

function send() {

    var message = $('#writeMessage').val(); // 입력 필드에서 메시지 가져오기
    var sessionId = $("#sessionId").val();
    if(message.trim() != '') {
        var chatMessage = {
            type: 'message',
            memberId: memberId,
            chatCode: chatCode,
            message: message,
            sessionId: sessionId
        };

        webSocket.send(JSON.stringify(chatMessage)); // 웹소켓을 통해 메시지 전송
        $('#writeMessage').val(''); // 메시지 필드 초기화
        scrollToBottom();
    }

}

function scrollToBottom() {
    var chatHistory = document.getElementById("messageHistory");
    chatHistory.scrollTop = chatHistory.scrollHeight;
}