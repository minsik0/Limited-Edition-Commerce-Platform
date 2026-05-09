# 🚀 Limited-Edition-Commerce-Platform
> **한정판 상품 선착순 구매를 위한 이벤트 기반 MSA 백엔드 프로젝트**

<br>

## 📄 목차
- [1. 프로젝트 소개](#1-프로젝트-소개)
- [2. 기술 스택](#2-기술-스택)
- [3. 시스템 아키텍처](#3-시스템-아키텍처)
- [4. 성능 개선 및 문제 해결](#4-성능-개선-및-문제-해결)
- [5. 기술적 의사결정](#5-기술적-의사결정-deep-dive)
- [6. 주요 특징](#6-주요-특징-key-features)
- [7. 로컬 인프라 및 실행 방법](#7-로컬-인프라-및-실행-방법)

---

## 1. 프로젝트 소개

Limited-Edition-Commerce-Platform은 특정 시간에 오픈되는 **한정판 상품을 선착순으로 구매**하는 커머스 서비스입니다.

본 프로젝트는 단순한 기능 구현보다는 기본적인 CRUD 기반 서비스 구현 이후, **동시 요청 상황에서 발생하는 문제를 직접 재현**하고 이를 단계적으로 개선하는 과정을 경험하는 것을 목표로 합니다. 

초기 단계에서는 필수적인 기능만 구현하고, 프로젝트 진행 과정에서 트래픽 증가 및 분산 환경을 가정한 개선을 점진적으로 적용하고 있습니다.

● **개발 기간:** 2026.01 ~ 진행 중
● **개발 인원:** 1인 (Backend)

---

## 2. 기술 스택

### **Backend**
● Java 21

● Spring Boot 3.2.1

● Spring Web / Spring Data JPA

● QueryDSL / Spring Security

### **MSA / Infrastructure**
● Spring Cloud Gateway

● Netflix Eureka

● OpenFeign

### **Database & Messaging**
● PostgreSQL (RDB)

● Redis (Cache)

● Apache Kafka (Messaging)

| 분류 | 기술 | 선정 이유 |
| :--- | :--- | :--- |
| Language & Framework | Java 21, Spring Boot | 안정적인 백엔드 생태계 및 높은 생산성 |
| Database | PostgreSQL, JPA | 데이터 정합성 보장 및 객체 중심 설계 |
| Cache & Message | Redis, Apache Kafka | 조회 성능 최적화 및 비동기 서비스 연동 |
| Architecture | MSA, Monorepo | 서비스별 독립적 확장 및 코드 관리 효율화 |
| Test & Infra | JMeter, Docker Compose | 성능 검증 및 로컬 인프라 통합 관리 |

---

## 3. 시스템 아키텍처
*![대체텍스트](./images/Architecture.png)*

> **Gateway를 통한 라우팅 및 Kafka 기반의 비동기 이벤트를 활용한 서비스 간 결합도 해소**

---

## 4. 성능 개선 및 문제 해결

> **JMeter를 활용하여 수치 기반의 성능 개선 결과를 도출했습니다.**

### 📈 1. 상품 조회 성능 최적화: Offset → Cursor 전환
● **문제 상황:** 데이터 양이 증가함에 따라 기존 Offset 방식의 페이징 조회 성능이 급격히 저하됨.

● **해결 방안:**

● **Cursor Pagination:** 불필요한 Offset 스캔을 제거하여 탐색 비용 최소화.

● **복합 인덱스:** `(open_at, product_id)` 인덱스로 정렬 및 탐색 속도 최적화.

● **캐싱 전략:** Redis를 적용하여 DB 접근 부하 감소.

| 성능 지표 | 기존 Offset Paging | Cursor + Index + Redis | 개선 결과 |
| :--- | :---: | :---: | :---: |
| 평균 응답시간 | 2,940ms | **641ms** | **약 4.5배 개선** |
| 최대 응답시간 | 8,820ms | **1,458ms** | **약 6배 개선** |
| 처리량 (TPS) | 215 TPS | **620 TPS** | **약 2.9배 증가** |

### 📈 2. 주문 처리 병목 개선: 비동기 Saga 패턴 도입
● **문제 상황:** 동기(Sync) 재고 차감 요청 시 DB Row Lock 대기로 인한 응답 지연 및 시스템 결합도 심화.

● **해결 방안:**

● **Kafka 기반 Saga 패턴:** 주문 생성과 재고 차감을 비동기로 분리하여 스레드 차단 방지.

● **트랜잭션 정합성:** `@TransactionalEventListener`로 DB 커밋 이후 이벤트 발행 보장.

● **멱등성 보장:** `processed_event` 테이블을 활용한 중복 메시지 처리 방지.

| 성능 지표 | 동기 Feign 처리 | Kafka 비동기 처리 | 개선 결과 |
| :--- | :---: | :---: | :---: |
| 평균 응답시간 | 4,004ms | **90ms** | **약 44배 개선** |
| 최대 응답시간 | 49,607ms | **357ms** | **약 140배 개선** |
| 처리량 (TPS) | 18 TPS | **94 TPS** | **약 5배 증가** |

---

## 5. 기술적 의사결정 (Deep Dive)

### 🛡️ 멱등성(Idempotency) 및 정합성 보장
● **배경:** Kafka의 At Least Once 전달 정책으로 인한 중복 메시지 소비 가능성 방어 필요.

● **해결:** `processed_event` 테이블을 생성하여 소비된 `event_id(UUID)`를 관리.

● **로직:** `existsById(eventId)`를 통해 중복 여부를 체크하며, DB 트랜잭션 내에서 비즈니스 로직과 이벤트 기록을 원자적으로 처리.

● **결과:** 네트워크 장애 상황에서도 **재고 중복 차감 사고 0건** 달성.

### 🔄 Kafka 기반 Saga 패턴 및 최종 일관성

● **배경:** 서비스 간 강한 결합을 해소하고 시스템 전체의 가용성을 높이기 위해 도입.

● **프로세스:**

● 주문 생성(PENDING) → Kafka 이벤트 발행 → 상품 서비스 재고 차감 → 결과 이벤트 발행.

● **보상 트랜잭션:** 재고 부족 시 `stock-result(FAIL)` 발행 → 주문 서비스에서 주문 상태를 `FAILED`로 자동 롤백.

● **이점:** 서비스 장애 시에도 메시지 큐를 통해 요청이 보존되어 데이터의 **최종 일관성** 유지.

---

## 6. 주요 특징 (Key Features)

● **MSA 통신:** OpenFeign을 활용한 선언적 HTTP 통신 및 로드밸런싱 적용.

● **분산 환경 대응:** Kafka 환경을 고려한 멱등성 및 이벤트 발행 보장 설계.

● **보안:** JWT 및 Spring Security를 이용한 Role 기반 접근 제어.

● **인프라:** Docker Compose를 이용한 Kafka / Redis / PostgreSQL 로컬 환경 통합 관리.

---

## 7. 로컬 인프라 및 실행 방법

### 🐳 Infrastructure (Local Dev)

● **Storage:** PostgreSQL (Persistence), Redis (Cache)

● **Messaging:** Apache Kafka, Zookeeper (Local Cluster)

● **Strategy:** `docker-compose.yml`을 통해 원클릭으로 개발 환경 구축 가능. 향후 Prometheus & Grafana 모니터링 확장 예정.

### 🛠 실행 방법
```bash
# 1. 컨테이너 인프라 실행
docker-compose up -d

# 2. 애플리케이션 실행
./gradlew bootRun