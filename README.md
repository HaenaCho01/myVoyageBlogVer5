# 스프링 부트로 회원가입, 로그인, 댓글 작성/조회/수정/삭제 기능이 추가된 나만의 항해 블로그 백엔드 서버 만들기

## *** 해결하지 못한 문제 ***
1. 예외 처리 중 토큰이 필요한 요청에서 토큰이 아예 없을 때 Client Response에 오류내용 반환 안됨
2. 예외 처리 중 게시글/댓글을 선택하는 과정에서 해당 게시글/댓글이 없을 때 Client Response에 오류내용 반환 안됨
3. 게시글/댓글 수정 시 수정시간이 이전 수정시간으로 Client에 Response 됨
4. 예외처리 관련 코드가 지저분한 느낌이 들고, 잘못된 방법으로 하고 있는 것 같다는 생각이 듬 -> 예외처리에 대한 추가 공부 필요!

<br/>
<br/>

## 1. Use Case
![나만의 항해 블로그 백엔드 서버 만들기 유스케이스 다이어그램 ver 3 230629 drawio](https://github.com/HaenaCho01/myVoyageBlogVer3/assets/131599243/20d2f287-d3ce-4800-9746-e1384cf5cb41)

<br/>
<br/>

## 2. ERD
![나만의 항해 블로그 백엔드 서버 만들기 erd ver 3 230629 drawio](https://github.com/HaenaCho01/myVoyageBlogVer3/assets/131599243/2929e330-b25c-431e-9869-ae884730e754)

<br/>
<br/>

## 3. API 명세서
https://documenter.getpostman.com/view/27924273/2s93z9ci2b

<br/>
<br/>
