document.getElementById('resetPasswordButton').addEventListener('click', submitResetPasswordForm);

function submitResetPasswordForm() {
    var password = document.getElementById('password').value;
    var confirmPassword = document.getElementById('confirmPassword').value;
    var passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/;
    var urlParams = new URLSearchParams(window.location.search);
    var token = urlParams.get('token');

    if (!passwordPattern.test(password)) {
        alert('비밀번호는 최소 6자 이상이며, 문자와 숫자를 모두 포함해야 합니다.');
        return;
    }

    if (password !== confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return;
    }

    $.ajax({
        url: '/api/signin/resetPassword',
        type: 'POST',
        data: {
            token: token,
            password: password
        },
        success: function() {
            alert('비밀번호가 재설정되었습니다.');
            window.location.href = '/signin';
        },
        error: function() {
            alert('오류가 발생했습니다.');
        }
    });
}