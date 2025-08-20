# 📱 전화번호부 API 명세서

## 📋 개요
- **서비스명**: api-phonebook-svc
- **버전**: v1.0.0
- **베이스 URL**: `http://localhost:8081/api/phonebook`
- **설명**: 사용자별 전화번호부 관리 CRUD API

---

## 🔧 환경 설정

### 필수 환경변수
```bash
DATABASE_URL=jdbc:postgresql://your-server:5432/database?currentSchema=api_phonebook_svc&sslmode=require
DATABASE_USERNAME=your-username
DATABASE_PASSWORD=your-password
SERVER_PORT=8081
```

---

## 📊 데이터 모델

### Phonebook Entity
```json
{
  "id": "Long (자동생성)",
  "ownerEmail": "String (전화번호부 주인 이메일)",
  "contactName": "String (연락처 이름)",
  "phoneNumber": "String (전화번호, 010-XXXX-XXXX 형식)",
  "carrier": "Enum (통신사: SKT, KT, LG, MVNO)",
  "createdAt": "LocalDateTime (생성일시)",
  "updatedAt": "LocalDateTime (수정일시)"
}
```

### Carrier Enum
- `SKT`: SK텔레콤
- `KT`: KT
- `LG`: LG유플러스
- `MVNO`: 알뜰폰

---

## 🛠️ API 엔드포인트

### 1. 연락처 생성
**POST** `/api/phonebook`

#### Request Body
```json
{
  "ownerEmail": "test@example.com",
  "contactName": "홍길동",
  "phoneNumber": "010-1234-5678",
  "carrier": "SKT"
}
```

#### Response (201 Created)
```json
{
  "id": 1,
  "ownerEmail": "test@example.com",
  "contactName": "홍길동",
  "phoneNumber": "010-1234-5678",
  "carrier": "SKT",
  "createdAt": "2024-08-19T16:30:00",
  "updatedAt": "2024-08-19T16:30:00"
}
```

#### 유효성 검증
- `ownerEmail`: 이메일 형식 검증
- `contactName`: 필수값, 공백 불가
- `phoneNumber`: 휴대폰 번호 정규식 검증 (`^01[016789]-?\\d{3,4}-?\\d{4}$`)
- `carrier`: SKT, KT, LG, MVNO 중 하나

---

### 2. 사용자별 전화번호부 조회
**GET** `/api/phonebook/owner/{ownerEmail}`

#### Path Parameters
- `ownerEmail`: 전화번호부 주인 이메일

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "ownerEmail": "test@example.com",
    "contactName": "홍길동",
    "phoneNumber": "010-1234-5678",
    "carrier": "SKT",
    "createdAt": "2024-08-19T16:30:00",
    "updatedAt": "2024-08-19T16:30:00"
  },
  {
    "id": 2,
    "ownerEmail": "test@example.com",
    "contactName": "김철수",
    "phoneNumber": "010-2345-6789",
    "carrier": "KT",
    "createdAt": "2024-08-19T16:31:00",
    "updatedAt": "2024-08-19T16:31:00"
  }
]
```

#### 정렬
- 연락처 이름순 오름차순 정렬

---

### 3. 연락처 상세 조회
**GET** `/api/phonebook/{id}`

#### Path Parameters
- `id`: 연락처 ID

#### Response (200 OK)
```json
{
  "id": 1,
  "ownerEmail": "test@example.com",
  "contactName": "홍길동",
  "phoneNumber": "010-1234-5678",
  "carrier": "SKT",
  "createdAt": "2024-08-19T16:30:00",
  "updatedAt": "2024-08-19T16:30:00"
}
```

#### Error Response (404 Not Found)
```json
{
  "error": "연락처를 찾을 수 없습니다"
}
```

---

### 4. 연락처 수정
**PUT** `/api/phonebook/{id}?ownerEmail={ownerEmail}`

#### Path Parameters
- `id`: 연락처 ID

#### Query Parameters
- `ownerEmail`: 전화번호부 주인 이메일 (권한 확인용)

#### Request Body
```json
{
  "contactName": "홍길동(수정됨)",
  "phoneNumber": "010-1111-2222",
  "carrier": "LG"
}
```

#### Response (200 OK)
```json
{
  "id": 1,
  "ownerEmail": "test@example.com",
  "contactName": "홍길동(수정됨)",
  "phoneNumber": "010-1111-2222",
  "carrier": "LG",
  "createdAt": "2024-08-19T16:30:00",
  "updatedAt": "2024-08-19T16:35:00"
}
```

#### 권한 검증
- 요청한 `ownerEmail`과 연락처의 실제 소유자가 일치해야 함
- 불일치시 400 Bad Request 반환

---

### 5. 연락처 삭제
**DELETE** `/api/phonebook/{id}?ownerEmail={ownerEmail}`

#### Path Parameters
- `id`: 연락처 ID

#### Query Parameters
- `ownerEmail`: 전화번호부 주인 이메일 (권한 확인용)

#### Response (204 No Content)
- 성공적으로 삭제된 경우 응답 본문 없음

#### 권한 검증
- 요청한 `ownerEmail`과 연락처의 실제 소유자가 일치해야 함
- 불일치시 400 Bad Request 반환

---

### 6. 연락처 이름으로 검색
**GET** `/api/phonebook/search/name?ownerEmail={ownerEmail}&contactName={contactName}`

#### Query Parameters
- `ownerEmail`: 전화번호부 주인 이메일
- `contactName`: 검색할 연락처 이름 (부분 검색 지원)

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "ownerEmail": "test@example.com",
    "contactName": "홍길동",
    "phoneNumber": "010-1234-5678",
    "carrier": "SKT",
    "createdAt": "2024-08-19T16:30:00",
    "updatedAt": "2024-08-19T16:30:00"
  }
]
```

#### 검색 특징
- 대소문자 구분 없음
- 부분 검색 지원 (예: "김" 검색시 "김철수", "김영희" 모두 검색)
- 연락처 이름순 정렬

---

### 7. 전화번호로 검색
**GET** `/api/phonebook/search/phone?ownerEmail={ownerEmail}&phoneNumber={phoneNumber}`

#### Query Parameters
- `ownerEmail`: 전화번호부 주인 이메일
- `phoneNumber`: 검색할 전화번호 (부분 검색 지원)

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "ownerEmail": "test@example.com",
    "contactName": "홍길동",
    "phoneNumber": "010-1234-5678",
    "carrier": "SKT",
    "createdAt": "2024-08-19T16:30:00",
    "updatedAt": "2024-08-19T16:30:00"
  }
]
```

#### 검색 특징
- 부분 검색 지원 (예: "1234" 검색시 "010-1234-5678" 검색됨)
- 하이픈(-) 포함/미포함 모두 검색 가능

---

### 8. 통신사별 조회
**GET** `/api/phonebook/carrier/{carrier}?ownerEmail={ownerEmail}`

#### Path Parameters
- `carrier`: 통신사 코드 (SKT, KT, LG, MVNO)

#### Query Parameters
- `ownerEmail`: 전화번호부 주인 이메일

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "ownerEmail": "test@example.com",
    "contactName": "홍길동",
    "phoneNumber": "010-1234-5678",
    "carrier": "SKT",
    "createdAt": "2024-08-19T16:30:00",
    "updatedAt": "2024-08-19T16:30:00"
  },
  {
    "id": 5,
    "ownerEmail": "test@example.com",
    "contactName": "최지연",
    "phoneNumber": "010-5678-9012",
    "carrier": "SKT",
    "createdAt": "2024-08-19T16:32:00",
    "updatedAt": "2024-08-19T16:32:00"
  }
]
```

#### 정렬
- 연락처 이름순 오름차순 정렬

---

## ❌ 에러 응답

### 400 Bad Request
```json
{
  "error": "입력값이 유효하지 않습니다",
  "details": "올바른 이메일 형식이 아닙니다"
}
```

### 404 Not Found
```json
{
  "error": "연락처를 찾을 수 없습니다"
}
```

### 500 Internal Server Error
```json
{
  "error": "서버 내부 오류가 발생했습니다"
}
```

---

## 🧪 테스트 예시

### cURL 예시

#### 1. 연락처 추가
```bash
curl -X POST http://localhost:8081/api/phonebook \
-H "Content-Type: application/json" \
-d '{
  "ownerEmail": "test@example.com",
  "contactName": "홍길동",
  "phoneNumber": "010-1234-5678",
  "carrier": "SKT"
}'
```

#### 2. 내 전화번호부 조회
```bash
curl http://localhost:8081/api/phonebook/owner/test@example.com
```

#### 3. 이름으로 검색
```bash
curl "http://localhost:8081/api/phonebook/search/name?ownerEmail=test@example.com&contactName=홍길동"
```

#### 4. 연락처 수정
```bash
curl -X PUT "http://localhost:8081/api/phonebook/1?ownerEmail=test@example.com" \
-H "Content-Type: application/json" \
-d '{
  "contactName": "홍길동(수정됨)",
  "phoneNumber": "010-1111-2222",
  "carrier": "LG"
}'
```

#### 5. 연락처 삭제
```bash
curl -X DELETE "http://localhost:8081/api/phonebook/1?ownerEmail=test@example.com"
```

---

## 🔒 보안 고려사항

1. **권한 검증**: 수정/삭제 시 소유자 이메일 확인
2. **입력값 검증**: 모든 입력값에 대한 유효성 검증
3. **SQL 인젝션 방지**: JPA/Hibernate 사용으로 자동 방지
4. **환경변수**: 민감한 정보는 환경변수로 관리

---

## 📦 Docker 배포

### 환경변수와 함께 실행
```bash
docker run -d \
  --name phonebook-app \
  -p 8081:8081 \
  -e DATABASE_URL="jdbc:postgresql://your-server:5432/database?currentSchema=api_phonebook_svc&sslmode=require" \
  -e DATABASE_USERNAME="your-username" \
  -e DATABASE_PASSWORD="your-password" \
  npr04191/api-phonebook-svc:latest
```

---

## 📝 변경 이력

- **v1.0.0** (2024-08-19): 초기 API 구현
  - 전화번호부 CRUD 기능
  - 검색 기능 (이름, 전화번호, 통신사별)
  - 권한 검증
  - Docker 지원
