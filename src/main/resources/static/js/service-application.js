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

// 서비스 명과 가격 출력 메서드
function showServiceNameAndPrice() {
    var serviceType = $('#serviceType').val();
    var storage = $('#storageCapacity').val();

    $.ajax({
        url: '/api/service/price',
        type: 'GET',
        dataType: 'json',
        data: {
            serviceType: serviceType,
            storage: storage
        },
        success: function(response) {
            var serviceName = $('#serviceName');
            var servicePrice = $('#servicePrice');
            var subscriptionPeriod = $('#subscriptionPeriod').val();

            var price = response.price * subscriptionPeriod;
            var formattedPrice = price.toLocaleString('ko-KR');

            serviceName.text(response.name);
            servicePrice.text(formattedPrice + "원");
        },
        error: function(error) {
            console.log('에러 발생: '+error);
        }
    });
}

// 섹션 표시/숨김 토글 메서드
function toggleSectionDisplay(selector, display) {
    const sections = $(selector);
    sections.css('display', display ? 'block' : 'none');
}

// 서비스 타입 변경 이벤트 핸들러
function onServiceTypeChange() {
    const storageSection = $('.storage');
    const isDisplay = this.value ? true : false;

    storageSection.css('display', isDisplay ? 'block' : 'none');
    toggleSectionDisplay('.subscription', isDisplay);
}

// 스토리지 용량 변경 이벤트 핸들러
function onStorageCapacityChange() {
    const isDisplay = this.value ? true : false;
    toggleSectionDisplay('.subscription', isDisplay);
}

// 서비스 구독 신청 폼 제출
function createSubscription(){
    var form = $('.needs-validation');
    var servicePriceText = $('#servicePrice').text();
    var paymentAmount = parseInt(servicePriceText.replace(/[^0-9]/g, ''));
    var startDate = $('#subscriptionStartDate').val();
    var period = parseInt($('#subscriptionPeriod').val());
    var phone = $('#phone1').val() + $('#phone2').val() + $('#phone3').val();
    var address = $('#sample6_postcode').val() + '/' +
        $('#sample6_address').val() + '/' +
        $('#sample6_detailAddress').val() + '/' +
        $('#sample6_extraAddress').val();

    // 구독기간과 구독시작일을 이용해 구독 종료일 생성
    if(startDate !== "") {
        var endDate = new Date(startDate);
        var newMonth = endDate.getMonth() + period;
        endDate.setMonth(newMonth);

        if (endDate.getDate() < new Date(startDate).getDate()) {
            endDate.setDate(0);
        }
        var subscriptionEndDate = endDate.toISOString().split('T')[0];
    }

    var formData = {
        serviceType: $('#serviceName').text(),
        userCount: $('#userCount').val(),
        paymentAmount: paymentAmount,
        subscriptionPeriod: period,
        subscriptionStartDate: startDate,
        subscriptionEndDate: subscriptionEndDate,
        companyName: $('#companyName').val(),
        phone: phone,
        email: $('#email').val(),
        address: address
    }
    // 폼 유효성 검사
    if (!form[0].checkValidity()) {
        form.addClass('was-validated');
        return;
    }

    $.ajax({
        url: '/api/subscription',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(response) {
            alert(response);
            window.location.href = '/';
        },
        error: function(xhr, status, error) {
            console.error('에러 발생:', error);
            alert('서비스 구독 신청 중 오류가 발생했습니다.');
        }
    });
}

// 서비스 타입 변경 시
$('#serviceType').on('change', onServiceTypeChange);
$('#serviceType').on('change', function() {
    var storageValue = $('#storageCapacity').val();

    if (storageValue) {
        showServiceNameAndPrice();
    }
});

// 스토리지 값 변경 시
$('#storageCapacity').on('change', onStorageCapacityChange);
$('#storageCapacity').on('change', showServiceNameAndPrice);

// 구독 기간 변경 시
$('#subscriptionPeriod').on('change', showServiceNameAndPrice);

// 서비스 신청 버튼
$('#serviceScriptionButton').on('click', function () {
    var memberName = $('#memberName').text();

    if(!memberName) {
        alert("로그인이 필요합니다");
        return;
    }else {
        createSubscription();
    }
});

$(document).ready(function() {
    // 구독 시작일은 신청일 이전 날짜 선택 불가능
    var currentDate = new Date();
    var year = currentDate.getFullYear();
    var month = String(currentDate.getMonth() + 1).padStart(2, '0');
    var day = String(currentDate.getDate()).padStart(2, '0');
    var localDateString = year + '-' + month + '-' + day;

    $("#subscriptionStartDate").attr("min", localDateString);


});