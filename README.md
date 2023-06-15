# 스프링 부트로 로그인 기능이 없는 나만의 항해 블로그 백엔드 서버 만들기

## 1. Use Case
![나만의 항해 블로그 백엔드 서버 만들기 유스케이스 다이어그램 230615](https://github.com/HaenaCho01/myVoyageBlog/assets/131599243/4fa5f3ad-8bbd-49e3-adb8-e064b3837ae2)
<br/>
<br/>

## 2. API 명세서
<img width="1282" alt="나만의 항해 블로그 백엔드 서버 만들기 API 명세 230615" src="https://github.com/HaenaCho01/myVoyageBlog/assets/131599243/3c43849d-9844-441b-acb8-7c87a98e1cb8">
<br/>
<br/>

## 3. 과제 제출 질문에 대한 답변
1. 수정, 삭제 API의 request를 어떤 방식으로 사용하셨나요? (param, query, body)
   <br/> => 비밀번호와 수정 내용을 @Requestbody를 사용해 JSON형으로 받았습니다.
2. 어떤 상황에 어떤 방식의 request를 써야하나요?
   <br/> => @RequestParam: 1개의 HTTP 파라미터를 얻으며, 기본값을 지정하고자 하는 경우 
   <br/> => @RequestBody: JSON형의 HTTP Body 데이터를 Java 객체로 변환시키고자 하는 경우
   <br/> => @ModelAttribute: 폼(form) 형태의 HTTP Body와 요청 파라미터들을 객체에 바인딩시키고자 하는 경우
3. RESTful한 API를 설계했나요? 어떤 부분이 그런가요? 어떤 부분이 그렇지 않나요?
   <br/> => URI 명을 명사형(복수형)으로 작성하고, CRUD에 맞는 적절한 http 메소드를 사용하였습니다.
4. 적절한 관심사 분리를 적용하였나요? (Controller, Repository, Service)
   <br/> => 3 Layer Architecture로 구분하여 분리하였습니다.
5. API 명세서 작성 가이드라인을 검색하여 직접 작성한 API 명세서와 비교해보세요!
   <br/> => 확인 후 적절하게 수정하였습니다!