
var chatInterface = document.getElementById("chatInterface");
var chatCode;
var sessionId;
var webSocket;

// 채팅아이콘 혹은 닫기 버튼 클릭 시
$('.toggleChatButton').on('click', function (){
    if(chatInterface.style.display === "none") {
        checkChatRoom();
    }else {
        webSocketClose();
        $("#messageHistory").text('');
    }
});
// 메세지 전송 버튼 클릭 시
$('#sendMessageButton').on('click', send);

// 채팅방 생성 여부 확인
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

// 채팅방 생성
function createChatRoom(memberId) {

    $.ajax({
        url: '/api/chat/room',
        type: 'POST',
        data: { memberId: memberId },
        success: function(chatCode) {
            webSocketOpen(chatCode);
            chatInterface.style.display = "block";
        },
        error: function(error) {
            console.error('채팅룸 생성 실패:', error);
        }
    });
}

// 유저 아이디로 만들어진 채팅방의 채팅 코드 찾기
function findChatCode() {
    $.ajax({
        url: '/api/chat/code',
        type: 'GET',
        data: { memberId: memberId },
        success: function(chatCode) {
            webSocketOpen(chatCode);
            updateMessageReadStatus();
            chatInterface.style.display = "block";
        },
        error: function(error) {
            console.error('챗코드 확인 실패:', error);
        }
    });
}

// 채팅방의 채팅 기록 가져오기
function getChatMessage() {
    $.ajax({
        url: '/api/chat/message',
        type: 'GET',
        data: { chatCode: chatCode },
        success: function(messageList) {
            showMessage(messageList);
        },
        error: function(error) {
            console.error('메세지 반환 오류:', error);
        }
    });
}

// 채팅창에 채팅 기록 출력
function showMessage(messageList) {
    messageList.forEach(function (message) {
        var isMyMessage = message.memberId === memberId;
        var messageContent = '';
        var unreadDisplay = message.isRead === 'N' ? '1' : ''; // 'N'이면 '1'을, 아니면 공백을 표시
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
                                <span class="unread_count">${unreadDisplay}</span>
                            </div>
                            <span class="time_date">${formatTime(message.writeTime)}</span>
                        </div>
                    </div>
                </div>`;
        }

        $("#messageHistory").append(messageContent);
        scrollToBottom();
    })
}

// 채팅 읽음 여부 업데이트
function updateMessageReadStatus() {
    $.ajax({
        url: '/api/chat/readStatus',
        type: 'PATCH',
        data: { memberId : memberId },
        success: function(response) {
            console.log(response);
            getChatMessage();
        },
        error: function(error) {
            console.error('읽음 상태 업데이트 오류:', error);
        }
    });
}

// 스크롤 최하단으로 내림
function scrollToBottom() {
    var chatHistory = document.getElementById("messageHistory");
    chatHistory.scrollTop = chatHistory.scrollHeight;
}

// 날짜데이터 포맷을 시간과 분만 표시
function formatTime(writeTime) {
    var date = new Date(writeTime);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

// 현재 시간을 String객체로 만듬
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

// 웹소켓 시작
function webSocketOpen(chatCodeParam){
    chatCode = chatCodeParam;
    webSocket = new WebSocket("ws://" + location.host + "/ws/chat/"+chatCode);
    webSocketEvent();
}

// 웹소켓 종료
function webSocketClose() {
    if(webSocket) {
        webSocket.close(); // 웹소켓 연결 종료
        webSocket = null;
        chatInterface.style.display = "none";
    }
}

// 웹소켓 이벤트
function webSocketEvent() {
    webSocket.onopen = function(data){
        //소켓이 열리면 동작
    }

    webSocket.onmessage = function(data) {
        var msg = data.data;

        if(msg != null && msg.trim() != ''){
            var newMessage = JSON.parse(msg);

            // 웹소켓에 새로운 연결이 발생한 경우
            if (newMessage.type == "getId") {
                sessionId = newMessage.sessionId; // 세션 ID 저장

            // 웹소켓에 본인이 아닌 다른 사람이 들어온 경우
            }else if (newMessage.type == "joined") {
                // 내 채팅 읽음 처리
                $('.unread_count').html('&nbsp;');

            // 새로운 채팅이 전송된 경우
            } else if (newMessage.type == "message") {
                var isOwnMessage = newMessage.sessionId === sessionId; // 자신이 보낸 메시지인지 확인
                var unreadDisplay = newMessage.isRead === 'N' ? '1' : ''; // 'N'이면 '1'을, 아니면 공백을 표시
                var messageContent;
                if (isOwnMessage) {
                    // 자신이 보낸 메시지
                    messageContent = "<div class='outgoing_msg'>" +
                        "<div class='sent_msg'>" +
                        "<div class='message_content'>"+
                        "<span class='unread_count'>"+unreadDisplay+"</span>"+
                        "<p>" + newMessage.message + "</p>" +
                        "</div>"+
                        "<span class='time_date'>"+formatTime(newMessage.writeTime)+"</span>" +
                        "</div>" +
                        "</div>";
                } else {
                    // 다른 사용자가 보낸 메시지
                    messageContent = "<div class='incoming_msg'>" +
                        "<div class='received_msg'>" +
                        "<div class='received_withd_msg'>" +
                        "<div class='message_content'>"+
                        "<p>" + newMessage.message + "</p>" +
                        "<span class='unread_count'>"+unreadDisplay+"</span>"+
                        "</div>"+
                        "<span class='time_date'>"+formatTime(newMessage.writeTime)+"</span>" +
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

// 웹소켓 메세지 전송
function send() {

    var message = $('#writeMessage').val(); // 입력 필드에서 메시지 가져오기
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

