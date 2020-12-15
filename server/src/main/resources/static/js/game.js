var gApp = null;
var gStage = null;
var gRenderer = null;
var gTiles = [];

var gAnimationCount = 0; // 선택 타일 스케일 애니메이션 용 카운트 변수.
var gSelectedTile = null; // 최근 라운드에서 선택한 타일.

var currentUserInfo = null;
var currentRoundClickedTile = -1; // 현재 라운드에 클릭한 상자의 식별값.
var survivedRound = 0;

var absoluteWidth = 1920;
var absoluteHeight = 1080;
var relativeWidth = 1920;
var relativeHeight = 1080;

const xCount = 18;
const yCount = 12;

function updateRoundInfo(mapInfo) {
    pickTileColor();
    currentUserInfo = mapInfo.rankingList.filter(user => user.name === thisUserId)[0];
    updateTileInfo(mapInfo.spotList);
    updateUserRank(mapInfo.rankingList);
}

function updateUserRank(rankInfo) {
    var template = '';
    for(idx in rankInfo) {
        var rankNum = parseInt(idx) + 1;
        template += '<tr>' +
                    '<td>' + rankNum + '</td>' +
                    '<td>' + rankInfo[idx].name + '</td>' +
                    '<td>' + rankInfo[idx].score + '</td>' +
                    '</tr>';
    }
    $('#rank-table').html(template);
}

// 각 라운드 종료 후 카운트
function updateAfterGameCount(count) {
    $('.round-result-container.survived #close-count').html(count);
    if(count <= 0) {
        routeSurvivedView(false);
    }
}

function updateSurviveInfo(data) {
    $('#survived-score').html(data.score - currentUserInfo.score);
    $('#survived-total').html(data.score);
    survivedRound++;
    if(currentRoundClickedTile >= 0) {
        var coord = convertTileIDtoPoint(currentRoundClickedTile);
        var tile = gTiles[coord.y][coord.x];
        gAnimationCount = 0;
        gSelectedTile = tile;
    }
}

function updateGameOverInfo() {
    $('#lose-score').html(currentUserInfo.score);
    $('#lose-rounds').html(survivedRound);

    if(currentRoundClickedTile >= 0) {
        var coord = convertTileIDtoPoint(currentRoundClickedTile);
        var tile = gTiles[coord.y][coord.x];
        tile.highlightGameOver();
        gAnimationCount = 0;
        gSelectedTile = tile;
    }

    survivedRound = 0;
}

function updateTileInfo(tileIndexInfo) {
    var tileIndexList = [];
    for(var id of tileIndexInfo) {
        tileIndexList.push(convertTileIDtoPoint(id));
    }

    for(var tileRow of gTiles) {
        for(var tile of tileRow) {
            tile.setEnabled(false);
        }
    }

    for(var tileIndex of tileIndexList) {
        gTiles[tileIndex.y][tileIndex.x].setEnabled(true);
        gTiles[tileIndex.y][tileIndex.x].setClicked(false);
    }
}

function resetAllTiles() {
    if(gSelectedTile) {
        gSelectedTile.position = gSelectedTile.getOriginPos();
    }
    gSelectedTile = null;
    for(var tileRow of gTiles) {
        for(var tile of tileRow) {
            tile.setEnabled(false);
            tile.setClicked(false);
        }
    }
}

function convertPointToTileID(point) {
    return point.x + point.y * xCount;
}

function convertTileIDtoPoint(spotID) {
    var xIndex = spotID % xCount;
    var yIndex = Math.floor(spotID / xCount);
    return {x: xIndex, y: yIndex};
}

function createObject() {
    const offsetX = 200;
    const offsetY = 0;
    
    let rectangle = new PIXI.Graphics();
    rectangle.beginFill(0x232323);
    rectangle.drawRect(offsetX, offsetY, relativeWidth - 400, relativeHeight);
    rectangle.endFill();
    gStage.addChild(rectangle);

    for(let i = 0; i < yCount; ++i){
        let tempList = [];
        for(let j = 0; j < xCount; ++j){
            let tile = new Tile(i, j, offsetX, offsetY, convertPointToTileID({x: j, y: i}));
            tempList.push(tile);
            gStage.addChild(tile);
        }
        gTiles.push(tempList);
    }
}

function resetGame() {
    if(gSelectedTile) {
        gSelectedTile.position = gSelectedTile.getOriginPos();
    }
    gAnimationCount = 0;
    gSelectedTile = null;
    currentUserInfo = null;
    currentRoundClickedTile = -1;
    survivedRound = 0;
}

function animate() {
    try {
        requestAnimationFrame(animate);
        gRenderer.render(gStage);
    }
    catch (e) {
        console.error(e);
    }
}

function init() {
    gApp = new PIXI.Application({width: window.innerWidth, height: window.innerHeight});

    gStage = gApp.stage;

    gStage.interactive = true;

    gRenderer = gApp.renderer;

    var w = window.innerWidth;
    var h = window.innerHeight;

    gStage.scale.x = w / relativeWidth;
    gStage.scale.y = h / relativeHeight;

    gRenderer.resize(w, h);

    absoluteWidth = w;
    absoluteHeight = h;

    createObject();

    document.body.appendChild(gApp.view);

    requestAnimationFrame(animate);

    gApp.ticker.add(function() {
        if(gSelectedTile) {
            gSelectedTile.scale.x = 4 + Math.sin(gAnimationCount) * 0.2;
            gSelectedTile.scale.y = 4 + Math.sin(gAnimationCount) * 0.2;
            var originPos = gSelectedTile.getOriginPos();
            gSelectedTile.position = {
                x: originPos.x - Math.sin(gAnimationCount) * 1.5,
                y: originPos.y - Math.sin(gAnimationCount) * 1.5
            };
            gAnimationCount += 0.1;
        }
    });
}

window.onload = init;