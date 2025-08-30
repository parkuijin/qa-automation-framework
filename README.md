# <div align=center>QA 자동화 프로젝트</div>

<div align=center>QA 엔지니어 취업 준비 및 역량 강화를 위해 진행한 포트폴리오 프로젝트입니다. </div>

<br>

## 목차

1. [프로젝트 소개](#project-intro)<br>
2. [테스트 대상](#test-target)<br>
3. [주요 기능](#features)<br>
4. [테스트 케이스](#test-cases)<br>

## 🖥️ 프로젝트 소개 <a id="project-intro"></a>

<img style="max-width:100%; height:auto;" alt="Project Info" src="https://github.com/user-attachments/assets/8c2b0133-51ae-4ef9-a4b9-0f6a30562572" />


<br>

## 📄 테스트 대상 <a id="test-target"></a>
### Web
- [Selenium 공식 홈페이지](https://www.selenium.dev)
### Android
- [Sauce Labs Sample App - Android](https://github.com/saucelabs/my-demo-app-android.git)
### iOS
- [Sauce Labs Sample App - iOS](https://github.com/saucelabs/my-demo-app-ios.git)

<br>

## 📌 주요 기능 <a id="features"></a>

### 테스트 범위
- Web : 홈페이지 핵심 기능, 다운로드 링크, 다국어 지원, 검색 기능
- Mobile : 로그인/로그아웃, 장바구니, 결제

### Web Test
- Selenium 공식 홈페이지 대상 자동화 테스트
- 브라우저 지원 : Chrome, Firefox, Edge (WebDriverManager 및 드라이버 직접 설정)
- 테스트 항목 :
  - 홈페이지 타이틀 확인
  - 메뉴 페이지 이동 및 URL 확인 (Downloads, Documentation, Projects, Support, Blog)
  - 검색 기능 테스트
    - 존재하는 키워드 검색 시 결과 확인
    - 존재하지 않는 키워드 검색 시 결과 없음 확인
  - 다운로드 링크 검증
    - Selenium Client 언어별 바인딩 다운로드 링크 유효성 확인
    - Selenium Server 다운로드 링크 유효성 확인
  - 언어 변경 기능 테스트
    - 드롭다운을 통한 다양한 언어 선택 및 URL 확인
    - 초기 언어로 되돌리기

### Mobile Test
- Android 및 iOS 앱 테스트 지원
- Appium 및 TestNG 기반 모바일 자동화 테스트
- 테스트 항목 :
  - 로그인/로그아웃 기능 자동화
    - 로그인 상태 확인 및 조건부 로그인/로그아웃 처리
  - 상품 선택, 장바구니 담기/삭제 테스트
    - 상품 상세 정보 확인 (이름, 가격, 설명)
    - 장바구니 추가 및 삭제 확인
  - 상품 결제 테스트 (iOS 시뮬레이터 키보드 제한으로 결제 테스트 제외)
    - 결제 화면 이동
    - 주소 및 결제 수단 입력
    - 결제 정보 확인 및 주문 완료 검증

## 테스트 케이스 <a id="test-cases"></a>
### 플랫폼별 대표 테스트 케이스 요약
전체 테스트 케이스는 [QA_Test_Cases.pdf](QA_Test_Cases.pdf) 파일에서 확인 가능합니다. 
| 플랫폼   | 기능       | TC ID        | 제목                         | 시나리오                                                                 | 상태 |
|----------|------------|-------------|-----------------------------|-------------------------------------------------------------------------|------|
| WEB      | 타이틀 확인 | WEB_TC_001  | Selenium 홈페이지 타이틀 확인 | Selenium 홈페이지 접속 후 타이틀에 "Selenium" 포함 여부 확인           | PASS |
| Android  | 로그인      | AND_TC_001  | 로그인 기능 검증             | 앱 실행 후 로그아웃 상태라면 메뉴에서 로그인 화면 이동, ID/PW 입력 후 로그인 성공 확인 | PASS |
| iOS      | 로그인      | IOS_TC_001  | 로그인 기능 검증             | 앱 실행 후 메뉴에서 로그인 화면 이동, ID/PW 입력 후 로그인 성공 확인    | PASS |

### Allure Report
- 테스트 실행 결과 시각화 도구
- 테스트 단계별 스텝, 스크린샷, PASS/FAIL 상태 확인 가능
- Allure 실행 방법 :
  ```bash
  mvn clean test
  mvn allure:serve
  ```

#### Allure Report 예시
<div style="display:flex; flex-wrap:wrap; gap:10px;">
<img style="max-width:100%; height:auto; margin:10px;" alt="Allure Overview" src="https://github.com/user-attachments/assets/186af830-89a1-426d-9cf6-482c0cb3d4a2" />
<img style="max-width:100%; height:auto; margin:10px;" alt="Allure Test Suite" src="https://github.com/user-attachments/assets/def6c237-8713-48b4-85bc-756b2de91bbf" />
<img style="max-width:100%; height:auto; margin:10px;" alt="Allure Test Graph" src="https://github.com/user-attachments/assets/0985dfe4-d2a1-45d8-afa3-a027ca1d4cb1" />
</div>

