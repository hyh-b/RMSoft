// 스피너 출력
function showSpinner() {
    $('#loadingSpinner').css('display', 'block');
}
function hideSpinner() {
    $('#loadingSpinner').css('display', 'none');
}

function setupEventListeners() {
    var findIdLink = $('#findIdLink');
    var findIdmodal = new bootstrap.Modal($('#findIdModal'));
    var findPasswordLink = $('#findPasswordLink');
    var findPasswordmodal = new bootstrap.Modal($('#findPasswordModal'));

    // 아이디 찾기 링크
    findIdLink.on('click', function(event) {
        event.preventDefault();
        findIdmodal.show();
    });

    // 비밀번호 찾기 링크
    findPasswordLink.on('click', function(event) {
        event.preventDefault();
        findPasswordmodal.show();
    });

    // 모달 종료 시 입력필드 초기화
    findIdmodal._element.addEventListener('hidden.bs.modal', function() {
        $('#emailForIdFind').val('');
        $('#idResult').empty();
    });
    findPasswordmodal._element.addEventListener('hidden.bs.modal', function() {
        $('#idForPasswordFind').val('');
        $('#emailForPasswordFind').val('');
        hideSpinner();
    });
}

// 아이디 찾기 메서드
function findId() {
    var email = $('#emailForIdFind').val();

    $.ajax({
        url: '/api/member/id',
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
        error: function(error) {
            alert('아이디 찾기 실패');
            console.log('에러 발생: ',error);
        }
    });
}

// 비밀번호 찾기 버튼 메서드
function findPassword() {
    var memberId = $('#idForPasswordFind').val();
    var email = $('#emailForPasswordFind').val();
    showSpinner();
    $.ajax({
        url: '/api/member/password/recovery',
        type: 'POST',
        data: {
            memberId: memberId,
            email: email
        },
        success: function(response) {
            alert(response);
            $('#findPasswordModal').modal('hide');
        },
        error: function(xhr, error) {
            alert(xhr.responseText);
            console.log('에러 발생: '+error);
            hideSpinner();
        }
    });
}

$('#findId').on('click', findId);
$('#findPassword').on('click', findPassword);

$(document).ready(function() {
    setupEventListeners();

    const params = new URLSearchParams(window.location.search);
    // 로그인 실패 시
    if (params.has('fail')) {
        alert('로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.');
    }
    // 비밀번호 재설정 토큰이 유효하지 않을 시
    if (params.has('expiredPage')) {
        alert('유효하지 않은 토큰입니다. 메일을 다시 받아주세요.');
    }
});
