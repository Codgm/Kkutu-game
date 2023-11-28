# OpenKKuTu-Game
OOP - Project4로 개발중인 글자로 놀자! 끄투의 게임서버 Java Rebase Version입니다.

## TODO
- 타이머가 능동적으로 들어오게끔 + 라운드의 제한시간이 아닌 개개인의 제한시간도 정해야함. 개개인의 제한시간은 라운드의 시간에 따라서 달라지게끔.
- 옵션들 추가
- 웹이랑 어떻게 이을지? -> Spring User와 Client와 이어야하나...
- DB단에 이상한 언어들 대충 수정해야함.
- 한글 입력 및 출력 지원(writeUTF 쓰면 해결될듯함.)
- 더러운 코드 좀 정리 ex) Game이랑 Words랑 통합하던가 기능을 확실하게 분리하던가..
- 점수 계산식 추가 ![Method](https://github.com/jason0904/OpenKKuTu-Game/assets/37035547/23354fb0-e7f3-4330-bbf1-d2190e7fb030)

## 구조
- Main  -> GameServer <-> Client
- Main에서 방을 팜. GameServer를 새로 구축하듯이.
- GameServer는 Client를 받아서 Game을 만들고, Game은 Client를 받아서 Words를 만든다.


## How to run
- Install PostgreSQL
- PostgreSQL설치한 경로에서 cmd를 키고 createdb -U "postgres" "kkutudb"
- psql -U "postgres" -d "kkutudb" -f "(프로젝트 경로)/OpenKKuTu-Game/src/main/resources/db.sql"
- Passwd.java를 만들고 String passwd와 그에 getter를 넣어서 DB의 비밀번호를 넣는다.
- MySocketServer.java와 MySocketClient.java를 실행한다.