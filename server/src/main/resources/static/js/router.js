// 게임 사이트의 입장부터 발생하는 모든 이벤트에 따라 변경되는 화면에 대한 코드이다.

// 게임 처음 접속 시 게임 시작 화면 표시
function routeJoinView(isActive) {
    if(isActive) {
        $('#greeting').addClass('active');
    }
    else {
        $('#greeting').removeClass('active');
    }
}

// Start 버튼 클릭 시 로딩 화면 표시
function routeLoading(isActive) {
    if(isActive) {
        $('#loading').addClass('active');
    }
    else {
        $('#loading').removeClass('active');
    }
}

// 게임 세션 접속 후 사용자 대기 화면 표시
function routeWaiting(isActive) {
    if(isActive) {
        $('#waiting').addClass('active');
    }
    else {
        $('#waiting').removeClass('active');
    }
}

// Start 버튼 클릭 = 게임 화면, 게임 UI 표시
function routeGameView(isActive) {
    if(isActive) {
        $('#game-ui').addClass('active');
    }
    else {
        $('#game-ui').removeClass('active');
    }
}

function blockGameObject(isActive) {
    if(isActive && !$('#object-blocker').hasClass('active')) {
        $('#object-blocker').addClass('active');
    }
    else if(!isActive) {
        $('#object-blocker').removeClass('active');
    }
}

// 게임 시작 전 카운트
function routeReadyCount(count) {
    if(count > 0 && !$('#count-container').hasClass('active')){
        $('#count-container').addClass('active');
    }
    else if(count <= 0 && $('#count-container').hasClass('active')) {
        $('#count-container').removeClass('active');
        return;
    }
    $('#count-label').html(count);
}

// 각 라운드 별 카운트
function updateInGameCount(count) {
    var label = '' + count;
    if(count < 10) {
        label = '0' + label;
    }
    $('.time-indicator > .inner-label').html(label);
}

// 패배 시 = 게임 오버 화면
function routeGameOverView(isActive) {
    if(isActive) {
        $('.round-result-container.lose').addClass('active');
    }
    else {
        $('.round-result-container.lose').removeClass('active');
    }
}

// 생존 시 = 결과 표시 화면
function routeSurvivedView(isActive) {
    if(isActive) {
        $('.round-result-container.survived').addClass('active');
    }
    else {
        $('.round-result-container.survived').removeClass('active');
    }
}

function resetView() {
    routeJoinView(false);
    routeLoading(false);
    routeGameView(false);
    routeGameOverView(false);
    routeSurvivedView(false);
}
