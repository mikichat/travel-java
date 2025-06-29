# Travel CRM (Java Spring Boot)

이 프로젝트는 Spring Boot 프레임워크를 기반으로 구축된 여행사 고객 관계 관리(CRM) 시스템입니다. 고객, 여행 일정 및 예약 정보를 효율적으로 관리할 수 있도록 설계되었습니다.

## 🚀 주요 기능

- **대시보드**: 실시간 통계, 최근 예약, 시스템 알림을 한눈에 확인
- **사용자 인증**: JWT(JSON Web Token) 기반의 안전한 사용자 등록 및 로그인 시스템
- **고객 관리**: 고객 정보 생성, 조회, 수정 및 삭제 기능, **여권 파일 첨부 및 정보 추출 기능 (OCR 기반)**
- **여행 관리**: 여행 정보 생성, 조회, 수정 및 삭제 기능
- **일정 관리**: 여행 일정 생성, 조회, 수정 및 삭제 기능
- **예약 관리**: 여행 예약 생성, 조회, 수정 및 삭제 기능 (QR코드 지원 및 여행/사용자 ID 기반 조회 기능 포함)
- **공개 예약 조회**: 예약 코드를 통해 로그인 없이 개별 예약 정보 조회 기능
- **발권 관리**: 항공 발권 정보 생성, 조회, 수정 및 삭제 기능 (항공사, 비행 유형, 진행 상태, 코드, 여권 첨부, 메모 포함)
- **데이터 내보내기**: 고객, 일정, 예약 데이터를 CSV 파일로 내보내는 기능
- **반응형 UI**: Thymeleaf 및 Tailwind CSS를 활용한 현대적이고 반응형 웹 인터페이스
- **모듈화된 구조**: Spring Boot 표준 구조를 따르는 깔끔한 코드 구조
- **실시간 통계**: 데이터베이스 기반 실시간 통계 및 성장률 분석
- **업체(렌드사) 관리**: 여행사와 협력하는 항공, 호텔, 교통, 식사, 가이드, 옵션투어, 보험, 비자 등 공급업체 정보 관리 (등록/수정/삭제/목록, 체크박스 취급항목)
- **예약코드 QR**: 예약수정 페이지에서 예약코드에 마우스를 올리면 해당 예약의 QR코드(예약조회 URL)가 팝업으로 표시됨 (모바일 터치 지원)

## 🛠 기술 스택

- **백엔드**: Java 17, Spring Boot 3.x, Spring Web, Spring Data JPA, Lombok
- **데이터베이스**: MariaDB
- **인증**: Spring Security, JWT (JJWT)
- **프론트엔드**: HTML, Thymeleaf, Tailwind CSS, Font Awesome
- **OCR**: Tesseract OCR (Tesseract OCR 엔진 시스템 설치 필요)
- **유틸리티**: QR코드 생성 (ZXing)

## 📁 프로젝트 구조

```
travel-java/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── travelcrm/
│   │   │           ├── TravelCrmApplication.java  # 메인 애플리케이션 클래스
│   │   │           ├── config/                   # Spring 설정 클래스
│   │   │           │   ├── JwtAuthenticationFilter.java
│   │   │           │   └── SecurityConfig.java
│   │   │           ├── controller/               # REST 컨트롤러
│   │   │           │   ├── AuthController.java
│   │   │           │   ├── ItineraryController.java
│   │   │           │   ├── ReservationController.java
│   │   │           │   ├── TravelController.java
│   │   │           │   ├── VendorController.java
│   │   │           │   └── WebController.java
│   │   │           ├── dto/                      # 데이터 전송 객체 (DTO)
│   │   │           │   ├── AuthResponseDto.java
│   │   │           │   ├── ItineraryRequestDto.java
│   │   │           │   ├── ItineraryResponseDto.java
│   │   │           │   ├── ReservationRequestDto.java
│   │   │           │   ├── ReservationResponseDto.java
│   │   │           │   ├── TravelRequestDto.java
│   │   │           │   ├── TravelResponseDto.java
│   │   │           │   ├── UserLoginRequestDto.java
│   │   │           │   ├── UserRegisterRequestDto.java
│   │   │           │   ├── UserRequestDto.java
│   │   │           │   └── UserResponseDto.java
│   │   │           ├── entity/                   # JPA 엔티티
│   │   │           │   ├── Itinerary.java
│   │   │           │   ├── Reservation.java
│   │   │           │   ├── Travel.java
│   │   │           │   ├── User.java
│   │   │           │   └── Vendor.java
│   │   │           ├── repository/               # JPA 리포지토리
│   │   │           │   ├── ItineraryRepository.java
│   │   │           │   ├── ReservationRepository.java
│   │   │           │   ├── TravelRepository.java
│   │   │           │   ├── UserRepository.java
│   │   │           │   └── VendorRepository.java
│   │   │           ├── service/                  # 서비스 인터페이스 및 구현체
│   │   │           │   ├── AuthService.java
│   │   │           │   ├── CustomUserDetailsService.java
│   │   │           │   ├── ItineraryService.java
│   │   │           │   ├── ReservationService.java
│   │   │           │   ├── TravelService.java
│   │   │           │   ├── UserService.java
│   │   │           │   ├── VendorService.java
│   │   │           │   └── impl/
│   │   │           │       ├── AuthServiceImpl.java
│   │   │           │       ├── CustomerServiceImpl.java
│   │   │           │       ├── ItineraryServiceImpl.java
│   │   │           │       ├── ReservationServiceImpl.java
│   │   │           │       ├── TravelServiceImpl.java
│   │   │           │       ├── UserServiceImpl.java
│   │   │           │       └── VendorServiceImpl.java
│   │   │           └── util/                     # 유틸리티 클래스
│   │   │               ├── ApiResponse.java
│   │   │               ├── GlobalExceptionHandler.java
│   │   │               └── JwtUtil.java
│   │   └── resources/
│   │       ├── application.properties    # Spring 설정 파일
│   │       ├── templates/                # Thymeleaf 템플릿
│   │       │   ├── customers.html
│   │       │   ├── dashboard.html
│   │       │   ├── fragments/
│   │       │   │   └── header.html
│   │       │   ├── itineraries.html
│   │       │   ├── login.html
│   │       │   ├── reservations.html
│   │       │   ├── travels.html
│   │       │   └── vendors.html
│   │       └── static/                   # 정적 파일 (CSS, JS, 이미지)
│   │           ├── css/
│   │           ├── js/
│   │           └── images/
├── mvnw
├── mvnw.cmd
├── pom.xml                     # Maven 프로젝트 설정 파일
├── README.md                   # 프로젝트 문서
├── README2.md
└── LICENSE
```

## 🚀 시작하기

### 1. 저장소 클론

```bash
git clone <repository-url>
cd travel-java
```

### 2. Tesseract OCR 설치 (Windows)

여권 사진에서 정보를 추출하려면 Tesseract OCR 엔진이 시스템에 설치되어 있어야 합니다.

1.  **Tesseract OCR 다운로드**: 다음 링크에서 Windows용 설치 프로그램을 다운로드합니다.
    *   [Tesseract OCR for Windows](https://tesseract-ocr.github.io/tessdoc/Downloads.html) (현재 권장 버전: `tesseract-ocr-w64-setup-5.5.0.20241111.exe`와 유사한 파일명)
    https://github.com/tesseract-ocr/tesseract/releases/download/5.5.0/tesseract-ocr-w64-setup-5.5.0.20241111.exe
2.  **설치 진행**:
    *   다운로드한 `.exe` 파일을 실행하여 설치를 시작합니다.
    *   설치 과정 중 **"Add Tesseract to Path"** 옵션을 반드시 체크하여 시스템 환경 변수에 Tesseract 실행 파일 경로가 자동으로 추가되도록 합니다. (이 단계를 건너뛰었다면 수동으로 PATH를 설정해야 합니다.)
    *   한국어 등 추가 언어 팩이 필요하면 설치 시 선택합니다.
3.  **설치 확인**: 명령 프롬프트(CMD) 또는 PowerShell을 열고 다음 명령어를 입력하여 Tesseract가 올바르게 설치되었는지 확인합니다.
    ```bash
    tesseract --version
    ```
    버전 정보가 출력되면 성공적으로 설치된 것입니다.

### 3. MariaDB 데이터베이스 설정

`src/main/resources/application.properties` 파일에 MariaDB 연결 정보를 설정합니다.

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/travelcrm
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
```

### 4. 의존성 설치 및 애플리케이션 실행

Maven을 사용하여 의존성을 설치하고 애플리케이션을 빌드 및 실행합니다.

```bash
mvn clean install
mvn spring-boot:run
```

애플리케이션은 `http://localhost:8080`에서 실행됩니다.

## 📖 사용법

### 기본 워크플로우

1.  **회원가입/로그인**: `/register` 또는 `/login`에서 계정 생성 또는 로그인
2.  **대시보드**: `/dashboard`에서 전체 시스템 개요 확인
    -   실시간 통계 (고객 수, 일정 수, 예약 수, 수익)
    -   최근 예약 현황
    -   시스템 알림
    -   빠른 액션 버튼
3.  **고객 관리**: `/customers`에서 고객 정보 관리
4.  **여행 관리**: `/travels`에서 여행 정보 관리
5.  **일정 관리**: `/itineraries`에서 여행 일정 관리
6.  **예약 관리**: `/reservations`에서 예약 정보 관리
    -   **진행 단계**:
        1.  예약 요청 - 고객상담
        2.  진행 확인 - 담당자와 협의
        3.  대기 예약 - 입금 전
        4.  계약 확정 - 계약금만, 잔금 (미수)
        5.  완납 서비스 - 체크리스트
        6.  완료 도착
        7.  VIP 고객 - 재구매는 (V.V.IP 고객)
        8.  불만
        9.  처리완료
    -   **QR코드 및 검색**: 예약 페이지에서 예약 ID 또는 사용자 ID로 예약 목록을 조회할 수 있으며, 각 예약에는 QR 코드가 포함됩니다.
7.  **발권 관리**: `/ticketing`에서 항공 발권 정보 관리
    -   항공사 종류, 비행 유형 (편도, 왕복, 경유), 발권 진행 상태, 항공 발권 코드, 여권 첨부 및 메모 관리
-   **예약코드와 QR코드**: 예약수정 페이지에서 예약코드에 마우스를 올리면 해당 예약의 QR코드(예약조회 URL)가 팝업으로 표시됩니다. (모바일 터치 지원)
8.  **업체 관리**: `/vendors`에서 업체 정보 관리

### 대시보드 기능

-   **통계 카드**: 실시간 데이터베이스 기반 통계
-   **성장률 표시**: 지난 달 대비 성장률 계산
-   **최근 예약**: 최근 5개 예약 현황
-   **빠른 액션**: 새 고객/일정/예약 생성 바로가기
-   **시스템 알림**: 동적 알림 시스템

## 🔧 개발 가이드

### 코드 구조

이 프로젝트는 Spring Boot의 표준 계층형 아키텍처를 따릅니다:

-   **controller/**: 클라이언트 요청 처리 및 응답 반환
-   **service/**: 비즈니스 로직 구현
-   **repository/**: 데이터베이스 상호작용 (JPA)
-   **entity/**: 데이터베이스 테이블 매핑
-   **dto/**: 데이터 전송 객체
-   **util/**: 공통 유틸리티 클래스
-   **config/**: Spring 설정

### 오류 처리

`GlobalExceptionHandler`를 통해 중앙 집중식으로 오류를 처리합니다.

```java
// 예시: IllegalArgumentException 발생 시
throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
```

### 인증 시스템

Spring Security와 JWT를 사용하여 인증 시스템을 구현합니다.

```java
// 예시: JWT 인증이 필요한 컨트롤러 메서드
@GetMapping("/protected")
public ResponseEntity<String> protectedRoute() {
    // 인증된 사용자만 접근 가능
    return ResponseEntity.ok("Protected content");
}
```

## 📊 API 엔드포인트

### 인증 API

-   `POST /api/auth/register` - 사용자 등록
-   `POST /api/auth/login` - 사용자 로그인 (JWT 토큰 반환)

### 고객 관리 API (`/api/customers`)

-   `GET /api/customers` - 모든 고객 조회
-   `GET /api/customers/{id}` - 특정 고객 조회
-   `POST /api/customers` - 새 고객 생성
-   `PUT /api/customers/{id}` - 고객 정보 업데이트
-   `DELETE /api/customers/{id}` - 고객 삭제

### 여행 관리 API (`/api/travels`)

-   `GET /api/travels` - 모든 여행 조회
-   `GET /api/travels/{id}` - 특정 여행 조회
-   `POST /api/travels` - 새 여행 생성
-   `PUT /api/travels/{id}` - 여행 업데이트

### 일정 관리 API (`/api/itineraries`)

-   `GET /api/itineraries` - 모든 여행 일정 조회
-   `GET /api/itineraries/{id}` - 특정 여행 일정 조회
-   `POST /api/itineraries` - 새 여행 일정 생성
-   `PUT /api/itineraries/{id}` - 여행 일정 업데이트
-   `DELETE /api/itineraries/{id}` - 여행 일정 삭제
-   `GET /api/itineraries/travel/{travelId}` - 특정 여행 ID로 여행 일정 조회

### 예약 관리 API (`/api/reservations`)

-   `GET /api/reservations` - 모든 예약 조회
-   `GET /api/reservations/{id}` - 특정 예약 조회
-   `POST /api/reservations` - 새 예약 생성
-   `PUT /api/reservations/{id}` - 예약 업데이트
-   `DELETE /api/reservations/{id}` - 예약 삭제
-   `GET /api/reservations/travel/{travelId}` - 특정 여행 ID로 예약 조회
-   `GET /api/public/reservations/{reservationCode}` - 예약 코드를 통한 공개 예약 조회

### 업체 관리 API (`/api/vendors`)

-   `POST /api/vendors` - 새 업체 생성
-   `GET /api/vendors` - 모든 업체 조회
-   `GET /api/vendors/{id}` - 특정 업체 조회
-   `PUT /api/vendors/{id}` - 업체 정보 업데이트
-   `DELETE /api/vendors/{id}` - 업체 삭제