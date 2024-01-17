// 비밀번호 변경 폼 제출
function submitResetPasswordForm() {
    var password = $('#password').val();
    var confirmPassword = $('#confirmPassword').val();
    var passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/;
    var urlParams = new URLSearchParams(window.location.search);
    var token = urlParams.get('token');
    var requestData = {
        token: token,
        password: password
    };

    if (!passwordPattern.test(password)) {
        alert('비밀번호는 최소 6자 이상이며, 문자와 숫자를 모두 포함해야 합니다.');
        return;
    }

    if (password !== confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return;
    }

    $.ajax({
        url: '/api/member/password/reset',
        type: 'PATCH',
        contentType: 'application/json',
        data: JSON.stringify(requestData),
        success: function() {
            alert('비밀번호가 재설정되었습니다.');
            window.location.href = '/signin';
        },
        error: function(error) {
            alert('비밀번호 재설정에 실패');
            console.log('에러 발생: '+error);
        }
    });
}

// 비밀번호 변경 버튼
$('#resetPasswordButton').on('click', submitResetPasswordForm);

