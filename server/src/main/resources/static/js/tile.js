const gTextureOfTile = PIXI.Texture.from('./images/treasure_chests_32x32.png');

const gFilters = [
    new PIXI.filters.GlowFilter(10, 3, 0, 0xffffff, 0.1)
];

const gGameOverFilter = [
    new PIXI.filters.GlowFilter(10, 3, 0, 0xBD253E, 0.1)
];

const gColorTemplates = [
    0x7C87F0,
    0x69D1F6,
    0xEE4C88,
    0xF1B749,
    0xF9944E,
    0xDD4955,
    0x95EA59
];

const gHoverColor = 0xFCFCFC;
const gSelectedColor = 0xAAAAAA;

let gCurrentTileColorIndex = 0;

function Tile(i, j, offsetX, offsetY, spotId) {
    PIXI.Sprite.call(this, null);
    this.buttonMode = true;

    this.originWidth = 64,
    this.originHeight = 64;
    this.margin = 10;
    this.offsetPos = {x: offsetX, y: offsetY};
    this.index = {x: j, y: i};

    this.spotId = spotId;
    this.width = this.originWidth;
    this.height = this.originHeight;
    this.position = this.getOriginPos();
    this.enabled = false; // 활성화 여부. true로 바뀌지 않는 한 텍스처가 빈 상태로 존재한다.
    this.clicked = true; // 초기 상태는 모두 클릭할 수 없는 상태
}

Tile.prototype = new PIXI.Sprite();
Tile.prototype.constructor = PIXI.Sprite;

Tile.prototype.mouseover = function() {
    if(!this.clicked && currentRoundClickedTile === -1) {
        // this.filters = [...gFilters];
    }
}

Tile.prototype.mouseout = function() {
    if(currentRoundClickedTile !== this.spotId) {
    }
}

Tile.prototype.click = function() {
    if(!this.clicked && currentRoundClickedTile === -1) {
        this.setClicked(true);
        sendClickEvent(this.spotId);
        currentRoundClickedTile = this.spotId;
    }
}

Tile.prototype.highlightGameOver = function() {
    this.filters = [...gGameOverFilter];
}

Tile.prototype.getOriginPos = function() {
    return {
        x: (this.offsetPos.x + 10) + this.index.x * (this.originWidth + this.margin * 2) + this.margin,
        y: (this.offsetPos.y + 5) + this.index.y * (this.originHeight + this.margin * 2) + this.margin
    };
}

Tile.prototype.setEnabled = function(enabled) {
    this.enabled = enabled;
    if(enabled) {
        this.texture = PIXI.Texture.WHITE;
        this.setClicked(this.clicked);
    }
    else {
        this.texture = null;
    }
    this.interactive = enabled;
}

Tile.prototype.setClicked = function(clicked) {
    if(!this.enabled) {
        return;
    }
    if(clicked) {
        this.tint = gSelectedColor;
    }
    else {
        this.tint = gColorTemplates[gCurrentTileColorIndex];
    }
    this.clicked = clicked;
}

function pickTileColor() {
    var oldColorIndex = gCurrentTileColorIndex;
    while(oldColorIndex === gCurrentTileColorIndex) {
        gCurrentTileColorIndex = Math.floor(Math.random() * gColorTemplates.length);
    }
}