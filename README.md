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

개발 기간: 2025.01 ~ 2025.02

개발 인원: 1인

## 🛠 기술 스택
Backend
● Java 17
● Spring Boot 3.2.1
● Spring Web
● Spring Data JPA
● QueryDSL
● Spring Security

MSA / Infrastructure
● Spring Cloud Gateway
● Netflix Eureka

Database & Cache
● MySQL
● Redis

Messaging
● Kafka

