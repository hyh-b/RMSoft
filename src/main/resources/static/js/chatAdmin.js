
var webSocket;
var sessionId;
var chatCode;
var chatInterface = document.getElementById('chatInterface');

$('#closeChatButton').on('click', function (){
    webSocketClose();
    chatInterface.style.display = 'none';
    $("#messageHistory").text('');
    const chatItems = document.querySelectorAll('.chat_list');
    chatItems.forEach(chatItem => {
        chatItem.classList.remove('active_chat');
    });
});

$('#sendMessageButton').on('click', send);
document.addEventListener('DOMContentLoaded', function() {
    const chatItems = document.querySelectorAll('.chat_list');

    chatItems.forEach(item => {
        item.addEventListener('dblclick', function() {
            chatItems.forEach(chatItem => {
                chatItem.classList.remove('active_chat');
            });

            this.classList.add('active_chat');

            if (webSocket) {
                webSocketClose();
            }
            $("#messageHistory").text('');
            chatCode = this.getAttribute('data-chat-code');
            webSocketOpen(chatCode);
            updateMessageReadStatus();
            if (chatInterface) {
                chatInterface.style.display = 'block';
            }
        });
    });
});

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

function formatTime(writeTime) {
    var date = new Date(writeTime);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

function webSocketOpen(chatCode){
    webSocket = new WebSocket("ws://" + location.host + "/ws/chat/"+chatCode);
    webSocketEvent();
}

function webSocketClose() {
    if(webSocket) {
        webSocket.close(); // 웹소켓 연결 종료
        webSocket = null;
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
                sessionId = newMessage.sessionId; // 세션 ID 저장
                /*console.log("받는메세지"+newMessage.memberId);
                console.log("내아이디"+memberId);
                if(newMessage.memberId != memberId) {
                    $('.unread_count').text('');
                }*/
            }else if (newMessage.type == "joined") {
                console.log("누구들어옴");
                $('.unread_count').text('');

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
        if(e.keyCode == 13){
            send();
        }
    });
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