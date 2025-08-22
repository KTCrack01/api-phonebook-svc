# api-phonebook-svc

단체문자 발송 편의를 위한 전화번호부 CRUD 전용 서비스입니다. Messaging 서비스는 필요 시 이 API를 호출하여 대상 목록을 가져옵니다.

---

## 기술 스택 및 실행 포트
- Spring Boot 3, Java 21, Gradle
- JPA + PostgreSQL (스키마: `api_phonebook_svc`)
- 기본 컨테이너 포트: `8080` ([ADR-005](../msa-project-hub/docs/adr/ADR-005-service-port-convention.md))

---

## 환경 변수
- `DATABASE_URL` (기본값: `jdbc:postgresql://localhost:5432/postgres`)
- `DATABASE_USERNAME` (기본값: `postgres`)
- `DATABASE_PASSWORD` (기본값: `password`)
- `CORS_ALLOWED_ORIGINS` (기본값: `http://localhost:3000,...`)

application.properties 발췌:
```properties
spring.jpa.properties.hibernate.default_schema=api_phonebook_svc
spring.jpa.hibernate.ddl-auto=${JPA_DDL_AUTO:update}
app.cors.allowed-origins=${CORS_ALLOWED_ORIGINS:...}
```

---

## 빌드 및 실행
```bash
# 로컬 실행
./gradlew bootRun

# 빌드(JAR)
./gradlew bootJar

# Docker 이미지 빌드
docker build -t api-phonebook-svc:local .

# Docker 컨테이너 실행 (예시)
docker run --rm -p 8080:8080 \
  -e DATABASE_URL="jdbc:postgresql://host.docker.internal:5432/postgres" \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=password \
  -e CORS_ALLOWED_ORIGINS="http://localhost:3000" \
  api-phonebook-svc:local
```

---

## API 엔드포인트
베이스 경로: `/api/phonebook`

- 생성: `POST /api/phonebook`
- 소유자 전체 조회: `GET /api/phonebook/owner/{ownerEmail}`
- 단건 조회: `GET /api/phonebook/{id}`
- 수정: `PUT /api/phonebook/{id}?ownerEmail=...`
- 삭제: `DELETE /api/phonebook/{id}?ownerEmail=...`
- 이름 검색: `GET /api/phonebook/search/name?ownerEmail=...&contactName=...`
- 번호 검색: `GET /api/phonebook/search/phone?ownerEmail=...&phoneNumber=...`
- 통신사별 조회: `GET /api/phonebook/carrier/{carrier}?ownerEmail=...`

요청/응답 스키마는 DTO 클래스(`PhonebookCreateRequest`, `PhonebookUpdateRequest`)를 참고하세요.

---

## 참고
- 서비스 분리 배경: [ADR-0015](../msa-project-hub/docs/adr/ADR-0015-phonebook-service-separation.md) (Phonebook Service Separation)
- API 버저닝: [ADR-003](../msa-project-hub/docs/adr/ADR-003-api-versioning-and-base-path.md)
