var stompClient = null;
var thisPoolId = null;
var thisUserId = null;
var sessionId;
var socket;
// Server에 WebSocket 연결을 하는 함수
function connect() {
   resetView();
   resetGame();
   resetAllTiles();
   socket = new SockJS('/tutorialspoint-websocket');
   stompClient = Stomp.over(socket);
   stompClient.connect({}, function (frame) {
       console.log('Connected: ' + frame);
       sessionId = socket._transport.url.split("/")[5];
       //이 채널은 유저에게 방 입장이 되었을 때와 유저에게 게임 결과를 제공할 때 쓰임
       stompClient.subscribe('/topic/greetings/' + sessionId, function (socketMessage) {
            routeSocketMessage(socketMessage);
       });
       routeJoinView(true);
   });
}

function disconnect() {
   if (stompClient !== null) {
      stompClient.disconnect();
   }
}

function routeSocketMessage(socketMessage) {
    var data = JSON.parse(socketMessage.body);
    switch(data.header) {
         case 'POOL_ID':
             subscribePool(data.body.poolId);
             break;
         case 'COUNT_DOWN':
             updateCountdown(data.body);
             break;
         case 'MAP_DATA':
             updateRoundInfo(data.body);
             break;
         case 'USER_GAME_RESULT':
             updateGameResult(data.body);
             break;
         case 'WAIT_MESSAGE':
             routeLoading(false);
             routeWaiting(true);
             break;
         default:
             break;
    }
}

//방에 들어갈 때 요청을 날림
function startSearchingRoom() {
    thisUserId = $('#user-name').val();
    stompClient.send("/app/enter", {}, thisUserId);
    resetAllTiles();
    routeJoinView(false);
    blockGameObject(true);
    routeLoading(true);
}

//방에 대한 Message를 Subscribe함
function subscribePool(poolId) {
    // 이 채널은 방의 이벤트가 발생하였을 때 메시지가 전달되는 채널임
    stompClient.subscribe('/topic/greetings/' + poolId, function (socketMessage) {
       routeSocketMessage(socketMessage);
    });
    routeGameView(true);
    routeLoading(false);
    thisPoolId = poolId;
}

function updateCountdown(data) {
    switch(data.countCase) {
        case 'BEFORE_GAME':
            {
                resetAllTiles();
                blockGameObject(true);
                routeWaiting(false);
                routeReadyCount(data.count);
                break;
            }
        case 'IN_GAME':
            {
                blockGameObject(false);
                updateInGameCount(data.count);
                break;
            }
        case 'AFTER_GAME':
            {
                blockGameObject(true);
                updateAfterGameCount(data.count);
                break;
            }
        default:
            break;
    }
}

function updateGameResult(data) {
    updateUserRank(data.rankingList);

    if (!data.alive) {
        disconnect();
    }
    showResult(data);
}

function showResult(data) {
    if (data.alive) {
        // 본인의 게임 결과를 보여주는 창을 띄움
        routeSurvivedView(true);
        updateSurviveInfo(data);
        currentRoundClickedTile = -1;
    }
    else {
        // 게임 오버 창 표시
        blockGameObject(true);
        updateGameOverInfo();
        var gameOverCallback = () => {
            routeGameOverView(true);
            currentRoundClickedTile = -1;
        };
        if(currentRoundClickedTile >= 0) {
            setTimeout(() => { gameOverCallback(); }, 1000);
        }
        else {
            gameOverCallback();
        }
    }
}

//Click Event를 서버로 보냄
function sendClickEvent(spot) {
    stompClient.send('/app/click', {}, JSON.stringify({'poolId': thisPoolId, 'spot': spot}));
}



connect();