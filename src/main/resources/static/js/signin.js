/* 스피너 출력 */
function showSpinner() {
    document.getElementById('loadingSpinner').style.display = 'block';
}
function hideSpinner() {
    document.getElementById('loadingSpinner').style.display = 'none';
}

function setupEventListeners() {
    var findIdLink = document.getElementById('findIdLink');
    var findIdmodal = new bootstrap.Modal(document.getElementById('findIdModal'));
    var findPasswordLink = document.getElementById('findPasswordLink');
    var findPasswordmodal = new bootstrap.Modal(document.getElementById('findPasswordModal'));
    /* 아이디 찾기 링크 */
    findIdLink.addEventListener('click', function(event) {
        event.preventDefault();
        findIdmodal.show();
    });
    /* 비밀번호 찾기 링크 */
    findPasswordLink.addEventListener('click', function(event) {
        event.preventDefault();
        findPasswordmodal.show();
    });

    /* 모달 종료 시 입력필드 초기화*/
    findIdmodal._element.addEventListener('hidden.bs.modal', function() {
        $('#emailForIdFind').val('');
        $('#idResult').empty();
    });
    findPasswordmodal._element.addEventListener('hidden.bs.modal', function() {
        $('#idForPasswordFind').val('');
        $('#emailForPasswordFind').val('');
    });
}

/* 아이디 찾기 버튼 메서드*/
function findId() {
    var email = $('#emailForIdFind').val();

    $.ajax({
        url: '/api/signin/findId',
        type: 'GET',
        data: { email: email },
        success: function(data) {
            var resultDiv = $('#idResult');
            if (data.length > 0) {
                resultDiv.text('회원님의 아이디: ' + data.join(', '));
            } else {
                resultDiv.text('해당 이메일로 등록된 아이디가 없습니다.');
            }
        },
        error: function() {
            console.log('에러가 발생했습니다.');
        }
    });
}
/* 비밀번호 찾기 버튼 메서드*/
function findPassword() {
    var memberId = $('#idForPasswordFind').val();
    var email = $('#emailForPasswordFind').val();
    showSpinner();
    $.ajax({
        url: '/api/signin/findPassword',
        type: 'Post',
        data: {
            memberId: memberId,
            email: email
        },
        success: function(response) {
            alert(response);
            hideSpinner();
        },
        error: function(xhr, status, error) {
            alert(xhr.responseText);
            hideSpinner();
        }
    });
}

$('#findId').click(findId);

$('#findPassword').click(findPassword);

$(document).ready(function() {
    setupEventListeners();

    const params = new URLSearchParams(window.location.search);
    if (params.has('fail')) {
        alert('로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.');
    }

    if (params.has('expiredPage')) {
        alert('유효하지 않은 토큰입니다. 메일을 다시 받아주세요.');
    }
});