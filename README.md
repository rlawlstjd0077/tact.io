# tact.io
simple timing game on the web

## 1. 프로젝트 소개
- 유명한 Web Game (http://slither.io/ 등)을 모티브 삼아 개발한 웹 상에서 로그인 없이 간편하게 즐길 수 있는 미니게임 
- 눈치라는 뜻의 "tact" 에서 착안한 이름으로 여러 플레이어들이 격자 Board 에서 랜덤하게 등장하는 상자들을 제한 시간안에 겹치지 않게 선택하는 방식의 게임
- 서바이벌 형식의 진행으로 같은 상자를 여러 플레이어가 선택한 경우 제일 처음으로 선택한 플레이어 외의 나머지 플레이어들은 사망 (자세한 내용은 아래 Demo 참고..)

# 2. 세부 개발 내용
- Back-end
  - WebSocket을 활용한 게임 진행/결과 데이터 Broad Cast 기능, 개별 유저 Connect/DisConnect 기능 구현
  - 게임 데이터 생성, 동시성 체크, 점수/랭킹 계산 등 게임 로직을 수행하는 서비스 구현

## 3. 사용 기술
- Back-end: SpringBoot,WebSocket (STOMP) 
- Front-end: HTML, CSS, JavaScript(Socket.io, PixiJS)

## 4. 결과물
![video](https://streamable.com/e/yh5w2v)

![1](https://github.com/rlawlstjd0077/tact.io/blob/main/images/1.png)
![2](https://github.com/rlawlstjd0077/tact.io/blob/main/images/2.png)
