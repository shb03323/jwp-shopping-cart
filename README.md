# 기능목록
## 1단계

- [x] database 세팅
    - [x] data.sql(product table) 생성

- [x] 상품 관리 CRUD API 작성
    - [x] Create
        - [x] repository 구현
        - [x] service 구현
        - [x] controller 구현
    - [x] Read
        - [x] repository 구현
        - [x] service 구현
        - [x] controller 구현
    - [x] Update
        - [x] repository 구현
        - [x] service 구현
        - [x] controller 구현
    - [x] Delete
        - [x] repository 구현
        - [x] service 구현
        - [x] controller 구현


- [x] 상품 목록 페이지 연동
    - [x] "/" : index.html 를 보낸다

- [x] 관리자 도구 페이지 연동
    - [x] "/admin" : admin.html 를 보낸다

---
## 2단계

- [x] database 세팅
  - [x] user table 생성
  - [x] cart table 생성 및 연관 관계 설정
  - [x] data.sql 수정

- [x] 사용자 설정
  - [x] basic authorization 구현
  - [x] email, password로 사용자 조회 구현
    - [x] repository 구현
    - [x] service 구현
  - [x] 전체 사용자 조회 api 구현
    - [x] repository 구현
    - [x] service 구현
    - [x] controller 구현
  - [x] 로그인 된 사용자 정보 조회 기능 구현
  - [x] 사용자 설정 페이지 연동

- [x] 장바구니 설정
  - [x] 장바구니에 상품 추가 api 구현
    - [x] repository 구현
    - [x] service 구현
    - [x] controller 구현
  - [x] 장바구니 조회 api 구현
    - [x] repository 구현
    - [x] service 구현
    - [x] controller 구현
  - [x] 장바구니에서 상품 삭제 api 구현
    - [x] repository 구현
    - [x] service 구현
    - [x] controller 구현
  - [x] 장바구니 페이지 연동

- [x] 상품 설정
  - [x] 상품 삭제할 때 모든 장바구니에서 해당 상품이 삭제되도록 수정
