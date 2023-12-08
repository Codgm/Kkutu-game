# OpenKKuTu-Game
OOP - Project4로 개발중인 글자로 놀자! 끄투의 게임서버 Java Rebase Version입니다.

## Build Environment
JDK : Amazon Corretto 17.01
OS : Windows 11
Build System : Gradle 17

## How to run
- Install PostgreSQL
- PostgreSQL설치한 경로에서 cmd를 키고 createdb -U "postgres" "kkutudb"
- psql -U "postgres" -d "kkutudb" -f "(프로젝트 경로)/OpenKKuTu-Game/src/main/resources/db.sql"
- Passwd.java에 "postgres" account의 password 삽입.
- MySocketServer.java와 MySocketClient.java를 실행한다.

## 혹시라도 끄투 서버에 참고하실 분들 위해 미구현된 부분
- manner 미구현
- Only 게임 규칙만 구현되어있음. 상점, 로그인, 이런 부분 다 따로 구현해야합니다
- Client도 일단은 AWT로 구현되어있음. 이걸 실제로 사용하려면 WebSocket을 사용하는 웹 클라이언트를 따로 구현해야합니다.
