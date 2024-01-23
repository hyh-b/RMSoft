
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
            getChatMessage();
            chatInterface.style.display = "block";
        },
        error: function(error) {
            console.error('챗코드 확인 실패:', error);
        }
    });
}

function getChatMessage() {
    console.log("겟메시지 호ㅊㄹ");
    $.ajax({
        url: '/api/chat/message',
        type: 'GET',
        data: { chatCode: chatCode },
        success: function(messageList) {
            console.log("겟메시지 "+messageList);
            showMessage(messageList);
        },
        error: function(error) {
            console.error('메세지 반환 오류:', error);
        }
    });
}

function showMessage(messageList) {
    messageList.forEach(function (message) {
        var isMyMessage = message.memberId === memberId;
        var messageContent = '';
        var unreadDisplay = message.unreadCount === 'N' ? '1' : ''; // 'N'이면 '1'을, 아니면 공백을 표시
        if (isMyMessage) {
            // 내 메시지
            messageContent = `
                <div class="outgoing_msg">
                    <div class="sent_msg">
                        <div class="message_content">
                            <span class="unread_count">${unreadDisplay}</span>
                            <p>${message.message}</p>
                        </div>
                        <span class="time_date">${formatTime(message.writeTime)}</span>
                    </div>
                </div>`;
        } else {
            // 상대방 메시지
            messageContent = `
                <div class="incoming_msg">
                    <div class="received_msg">
                        <div class="received_withd_msg">
                            <div class="message_content">
                                <p>${message.message}</p>
                                <span class="time_date">${formatTime(message.writeTime)}</span>
                            </div>
                            <span class="unread_count">${unreadDisplay}</span>
                        </div>
                    </div>
                </div>`;
        }

        $("#messageHistory").append(messageContent);
    })
}

var webSocket;

function webSocketOpen(chatCodeParam){
    chatCode = chatCodeParam;
    webSocket = new WebSocket("ws://" + location.host + "/ws/chat/"+chatCode);
    webSocketEvent();
}

function webSocketClose() {
    if(webSocket) {
        webSocket.close(); // 웹소켓 연결 종료
        chatInterface.style.display = "none";
    }
}

function webSocketEvent() {
    webSocket.onopen = function(data){
        //소켓이 열리면 동작
    }

    webSocket.onmessage = function(data) {
        var msg = data.data;

        if(msg != null && msg.trim() != ''){
            var newMessage = JSON.parse(msg);
            if (newMessage.type == "getId") {
                $("#sessionId").val(newMessage.sessionId); // 세션 ID 저장
            } else if (newMessage.type == "message") {
                var isOwnMessage = newMessage.sessionId === $("#sessionId").val(); // 자신이 보낸 메시지인지 확인
                var messageDate = new Date(newMessage.writeTime);
                var timeString = messageDate.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
                var messageContent;
                if (isOwnMessage) {
                    // 자신이 보낸 메시지
                    messageContent = "<div class='outgoing_msg'>" +
                        "<div class='sent_msg'>" +
                        "<p>" + newMessage.message + "</p>" +
                        "<span class='time_date'>"+timeString+"</span>" + // 시간은 예시입니다.
                        "</div>" +
                        "</div>";
                } else {
                    // 다른 사용자가 보낸 메시지
                    messageContent = "<div class='incoming_msg'>" +
                        "<div class='received_msg'>" +
                        "<div class='received_withd_msg'>" +
                        "<p>" + newMessage.message + "</p>" +
                        "<span class='time_date'>"+timeString+"</span>" + // 시간은 예시입니다.
                        "</div>" +
                        "</div>" +
                        "</div>";
                }

                $("#messageHistory").append(messageContent);
                scrollToBottom();
            } else {
                console.warn("unknown type!", newMessage);
            }
        }
    };

    document.addEventListener("keypress", function(e){
        if(e.keyCode === 13){
            send();
        }
    });
}

function formatTime(writeTime) {
    var date = new Date(writeTime);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}


function getCurrentTimeString() {
    var now = new Date();

    var year = now.getFullYear();
    var month = String(now.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더해줍니다.
    var day = String(now.getDate()).padStart(2, '0');
    var hours = String(now.getHours()).padStart(2, '0');
    var minutes = String(now.getMinutes()).padStart(2, '0');
    var seconds = String(now.getSeconds()).padStart(2, '0');

    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
}

function send() {

    var message = $('#writeMessage').val(); // 입력 필드에서 메시지 가져오기
    var sessionId = $("#sessionId").val();
    var currentTime = getCurrentTimeString();
    if(message.trim() != '') {
        var chatMessage = {
            type: 'message',
            memberId: memberId,
            chatCode: chatCode,
            message: message,
            sessionId: sessionId,
            writeTime: currentTime
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