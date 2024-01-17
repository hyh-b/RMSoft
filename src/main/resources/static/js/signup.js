//다음 주소 api
function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("sample6_extraAddress").value = extraAddr;

            } else {
                document.getElementById("sample6_extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}

let isIdChecked = false; // 아이디 중복 검사 확인 변수

// 아이디 중복 검사 메서드
function checkDuplicateId() {
    let memberId = $('#id').val();

    if (memberId === '') {
        alert('아이디를 입력해주세요');
        return;
    }

    $.ajax({
        url: `/api/member/id/exists`,
        type: 'GET',
        data: { memberId: memberId },
        dataType: 'json',
        success: function(data) {
            if (data) {
                alert('이미 존재하는 아이디입니다.');
            } else {
                alert('사용 가능한 아이디입니다.');
                isIdChecked = true; // 중복 검사 통과
            }
        },
        error: function(error) {
            alert('아이디 중복 검사에 실패');
            console.error('에러 발생: ', error);
        }
    });
}

// 회원가입 폼 제출 메서드
function submitSignupForm() {
    if (!isIdChecked) {
        alert('아이디 중복 검사를 진행해주세요.');
        return;
    }

    var form = $('.needs-validation');

    var id = $('#id').val();
    var idPattern = /^[A-Za-z0-9]{6,20}$/;
    var password = $('#password').val();
    var confirmPassword = $('#confirmPassword').val();
    var passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/;

    if (!idPattern.test(id)) {
        alert('아이디는 6자 이상 20자 이하의 영문자와 숫자만 사용할 수 있습니다.');
        return;
    }

    if (!passwordPattern.test(password)) {
        alert('비밀번호는 최소 6자 이상이며, 문자와 숫자를 모두 포함해야 합니다.');
        return;
    }

    if (password !== confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return;
    }

    // 폼 유효성 검사
    if (!form[0].checkValidity()) {
        form.addClass('was-validated');
        return;
    }

    var formData = {
        memberId: $('#id').val(),
        password: $('#password').val(),
        name: $('#name').val(),
        email: $('#email').val(),
        phone: $('#phone1').val() +
            $('#phone2').val() +
            $('#phone3').val(),
        address: $('#sample6_postcode').val() + '/' +
            $('#sample6_address').val() + '/' +
            $('#sample6_detailAddress').val() + '/' +
            $('#sample6_extraAddress').val()
    };

    $.ajax({
        url: '/api/member/signup',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        dataType: 'text',
        success: function(data) {
            alert(data);
            window.location.href = '/signin';
        },
        error: function(error) {
            alert('회원가입에 실패');
            console.error('에러 발생: ', error);
        }
    });
}

// 아이디 입력칸에 문자 입력할 때마다 중복검사 요구
$('#id').on('input', function() {
    isIdChecked = false;
});

// 아이디 중복 확인 버튼
$('#idCheck').on('click', checkDuplicateId);

// 회원가입 버튼
$('#signupButton').on('click', submitSignupForm);
