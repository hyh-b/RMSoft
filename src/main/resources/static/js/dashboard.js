
// 서비스 명과 가격 출력 메서드
function showServiceNameAndPrice() {
    var extensionPeriod = $('#extensionPeriod').val();
    var serviceName = $('#name').text();
    var priceText = $('#price').text();
    var price = parseInt(priceText.replace(/[^0-9]/g, '')) * extensionPeriod;
    var formattedPrice = price.toLocaleString('ko-KR');

    $('#serviceName').text(serviceName);
    $('#servicePrice').text(formattedPrice + "원");
}

// 대시보드 컨텐츠 출력
function updateDashboardInfo(selectedService) {
    var price = selectedService.price;
    var formattedPrice = price.toLocaleString('ko-KR');

    // 페이지의 각 요소에 대한 데이터 업데이트
    $('#name').text(selectedService.name);
    $('#price').text(formattedPrice + '원');
    $('#storage').text(selectedService.storage + "TB");
    $('#subscriptionEndDate').text("~" + selectedService.subscriptionEndDate);
    $('#storageAmount').text("1TB(임시 데이터)");

    var currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0);

    var subscriptionEndDate = new Date(selectedService.subscriptionEndDate);
    subscriptionEndDate.setHours(0, 0, 0, 0);

    // 남은 일수 계산
    var diff = subscriptionEndDate.getTime() - currentDate.getTime();
    var remainingDays = Math.ceil(diff / (1000 * 60 * 60 * 24));

    // 남은 기간 표시
    $('#remainingDays').text(" "+ remainingDays + "일");
}

// 구독 연장 메서드
function subscriptionExtension() {
    var selectedName = $('#serviceName').text();
    var selectedService = services.find(service => service.name === selectedName);
    var subscriptionCode = selectedService.subscriptionCode;
    var extensionPeriod = parseInt($('#extensionPeriod').val());
    var servicePriceText = $('#servicePrice').text();
    var paymentAmount = parseInt(servicePriceText.replace(/[^0-9]/g, ''));
    var subscriptionEndDate = new Date(selectedService.subscriptionEndDate);

    // subscriptionEndDate에 하루를 더해 extensionStartDate 구함
    subscriptionEndDate.setDate(subscriptionEndDate.getDate() + 1);
    var extensionStartDate = subscriptionEndDate.toISOString().split('T')[0];

    // extensionStartDate와 extensionPeriod 이용해 extensionEndDate 구함
    var endDate = new Date(extensionStartDate);
    var newMonth = endDate.getMonth() + extensionPeriod;
    endDate.setMonth(newMonth);
    if (endDate.getDate() < new Date(extensionStartDate).getDate()) {
        endDate.setDate(0);
    }
    var extensionEndDate = endDate.toISOString().split('T')[0];

    var formData = {
        subscriptionCode: subscriptionCode,
        extensionPeriod: extensionPeriod,
        paymentAmount: paymentAmount,
        extensionStartDate: extensionStartDate,
        extensionEndDate: extensionEndDate
    }

    $.ajax({
        url: '/api/subscription/extension',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(response) {
            alert(response);
            // 연장된 구독 종료일을 대시보드 컨텐츠 업데이트
            selectedService.subscriptionEndDate = extensionEndDate;
            updateDashboardInfo(selectedService);
            $('#subscriptionExtensionModal').modal('hide');
        },
        error: function(error) {
            console.error('에러 발생:', error);
            alert('구독 연장 신청 중 오류가 발생했습니다.');
        }
    });
}

// 연장 기간 값 변경 시
$('#extensionPeriod').on('change', function() {
    showServiceNameAndPrice();
});

// 서비스 목록 값 변경 시
$('#serviceList').on('change', function() {
    var selectedName = $(this).val();
    var selectedService = services.find(service => service.name === selectedName);

    updateDashboardInfo(selectedService);
});

// 구독 연장 신청 버튼 클릭 시
$('#sumitSubscriptionExtension').on('click', subscriptionExtension);

// 구독 연장 버튼 클릭 시
$('#subscriptionExtensionButton').on('click', function() {
    var nameText = $('#name').text();
    var memberName = $('#memberName').text();

    if(!memberName) {
        alert('로그인이 필요합니다');
        return;
    }else if(!nameText.trim()) {
        alert('서비스를 선택해주세요');
        return;
    }else{
        $('#subscriptionExtensionModal').modal('show');
    }
});

$(document).ready(function() {
    // 구독 연장 모달 열릴 시 서비스명과 가격 출력
    $('#subscriptionExtensionModal').on('show.bs.modal', function (e) {
        showServiceNameAndPrice();
    });
});