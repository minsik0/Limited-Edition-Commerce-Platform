# Limited-Edition-Commerce-Platform
(한정판 상품 선착순 구매를 위한 이벤트 기반 MSA 백엔드)

## 📄 목차
- [프로젝트 소개](#프로젝트-소개)
- [기술 스택](#기술-스택)
- [시스템 아키텍처](#시스템-아키텍처)
- [기술적 의사결정](#기술적-의사결정)
- [특징](#특징)
- [성능 개선](#성능-개선)
- [트러블 슈팅](#트러블-슈팅)

## 📦 프로젝트 소개

Limited-Edition-Commerce-Platform은
특정 시간에 오픈되는 한정판 상품을 선착순으로 구매하는 커머스 서비스입니다.

본 프로젝트는 단순한 기능 구현보다는
기본적인 CRUD 기반 서비스 구현 이후
동시 요청 상황에서 발생하는 문제를 직접 재현하고, 이를 단계적으로 개선하는 과정을 경험하는 것을 목표로 합니다.

초기 단계에서는 필수적인 기능만 구현하고,
프로젝트 진행 과정에서 트래픽 증가 및 분산 환경을 가정한 개선을 점진적으로 적용할 예정입니다.

개발 기간:

개발 인원: 1인

## 🛠 기술 스택
Backend
● Java 21
● Spring Boot 3.2.1
● Spring Web
● Spring Data JPA
● QueryDSL
● Spring Security

MSA / Infrastructure
● Spring Cloud Gateway
● Netflix Eureka

Database & Cache
● PostgresSQL
● Redis

Messaging
● Kafka

분류,기술,선정 이유
Language & Framework,"Java 17, Spring Boot",안정적인 백엔드 생태계 및 높은 생산성
Database,"PostgreSQL, JPA",데이터 정합성 보장 및 객체 중심 설계
Cache & Message,"Redis, Apache Kafka",조회 성능 최적화 및 비동기 서비스 연동
Architecture,"MSA, Monorepo",서비스별 독립적 확장 및 코드 관리 효율화
Test & Infra,"JMeter, Docker Compose",성능 검증 및 로컬 인프라 통합 관리

System Architecture

성능 개선 및 문제 해결

1. 상품 조회 성능 최적화: Offset → Cursor 기반 전환
   문제 상황: 데이터 양이 증가함에 따라 기존 Offset 방식의 페이징 조회 속도가 급격히 저하되었습니다.
   해결 방안: Cursor Pagination: 불필요한 Offset 스캔을 제거하여 전체 탐색 비용 최소화.
             복합 인덱스: (open_at, product_id) 인덱스를 통해 정렬 및 탐색 속도 최적화.
             캐싱 전략: Redis를 적용하여 반복적인 DB 접근을 방지.

성능 지표,기존 Offset Paging,Cursor + Index + Redis,개선 결과
평균 응답시간,"2,940ms",641ms,약 4.5배 개선
최대 응답시간,"8,820ms","1,458ms",약 6배 개선
처리량 (TPS),215 TPS,620 TPS,약 2.9배 증가

2. 주문 처리 병목 개선: 비동기 Saga 패턴 도입
   문제 상황: Feign Client를 이용한 동기(Sync) 재고 차감 요청 시, DB Row Lock 대기로 인해 응답 시간이 지연되고 시스템 결합도가 높아지는 문제가 발생했습니다.
   해결 방안: Kafka 기반 Saga 패턴: 주문 생성과 재고 차감을 비동기적으로 분리하여 스레드 차단 방지.
             트랜잭션 정합성: @TransactionalEventListener를 사용하여 DB 커밋 이후에만 이벤트를 발행하도록 보장.
             멱등성 보장: processed_event 테이블을 활용하여 중복 메시지 유입 시에도 데이터 일관성 유지.

성능 지표,동기 Feign 처리,Kafka 비동기 처리,개선 결과
평균 응답시간,"4,004ms",90ms,약 44배 개선
최대 응답시간,"49,607ms",357ms,약 140배 개선
처리량 (TPS),18 TPS,94 TPS,약 5배 증가

Key Features
MSA 통신: OpenFeign을 활용한 선언적 HTTP 통신 및 로드밸런싱.

분산 환경 대응: Kafka의 At Least Once 환경을 고려한 멱등성 설계.

보안: JWT 및 Spring Security를 이용한 Role 기반 접근 제어.

로컬 인프라: docker-compose를 통한 개발 환경 통합 구축.

실행 방법

🧱 멱등성(Idempotency) 및 정합성 보장
"분산 환경에서의 데이터 무결성을 위한 설계"

배경: Kafka의 At Least Once 전달 정책으로 인해 발생할 수 있는 중복 메시지 소비 문제를 방지해야 했습니다.

해결책 (Event Tracking):

processed_event 테이블을 생성하여 소비된 event_id(UUID)를 관리합니다.

Logic: existsById(eventId)를 통해 중복 여부를 체크하며, DB 트랜잭션 내에서 비즈니스 로직과 이벤트 기록을 원자적으로 처리합니다.

결과: 네트워크 장애나 Consumer 재시작으로 인한 재전달 상황에서도 재고 중복 차감 사고를 0건으로 방어할 수 있도록 설계했습니다.

🔄 Kafka 기반 Saga 패턴 및 최종 일관성
"분산 트랜잭션 없이 달성하는 최종 일관성(Eventual Consistency)"

주문과 재고 서비스 간의 강한 결합을 해소하고, 시스템 전체의 가용성을 높이기 위해 Choreography Saga 패턴을 도입했습니다.

주문 생성 (PENDING): 주문 서비스에서 주문 정보를 PENDING 상태로 저장 후 이벤트를 발행합니다.

재고 검증 및 차감: 상품 서비스에서 이벤트를 수신하여 재고를 확인합니다.

상태 확정 및 보상 처리:

성공 시: stock-result(SUCCESS) 발행 → 주문 서비스에서 주문 상태를 CREATED로 업데이트.

실패 시 (재고 부족 등): stock-result(FAIL) 발행 → 주문 서비스에서 주문 상태를 FAILED로 변경하는 보상 트랜잭션 수행.

이점: 특정 서비스에 장애가 발생해도 메시지 큐를 통해 요청이 보존되며, 최종적으로 데이터의 정합성이 맞추어집니다.

🐳 Infrastructure (Local Dev)
"Docker Compose를 활용한 인프라 자동화"

로컬 개발 및 테스트의 일관성을 위해 전체 인프라를 컨테이너화하여 관리합니다.

Storage: PostgreSQL (Persistence), Redis (Cache)

Messaging: Apache Kafka, Zookeeper (Local Cluster)

Strategy: 서비스 기동 시 인프라 환경이 즉시 구축되도록 docker-compose.yml을 구성하였으며, 향후 Prometheus & Grafana를 추가하여 Consumer Lag 및 처리량을 모니터링할 예정입니다.