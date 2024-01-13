document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);

    if (params.has('fail')) {
        alert('로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.');
    }

    var findId = document.getElementById('findId');
    var modal = new bootstrap.Modal(document.getElementById('verticalycentered'));

    findId.addEventListener('click', function(event) {
        event.preventDefault(); // 기본 링크 동작 방지
        modal.show(); // 모달 열기
    });
});