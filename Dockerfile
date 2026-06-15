# =============================================================================
# 멀티스테이지 Dockerfile
# =============================================================================
# Stage 1 (builder): Gradle로 전체 프로젝트 빌드 → 해당 서비스 JAR 생성
# Stage 2 (runtime): JRE만 포함된 경량 이미지에 JAR 복사 → 실행
#
# 멀티스테이지를 쓰는 이유:
# - builder 스테이지의 Gradle, 소스코드, 빌드 캐시는 최종 이미지에 포함되지 않음
# - 최종 이미지 크기: ~400MB (JDK 포함 시 ~800MB) → 약 50% 경량화
# =============================================================================

# ---------------------------------------------------------------------------
# Stage 1: 빌드
# ---------------------------------------------------------------------------
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Gradle Wrapper와 설정 파일을 먼저 복사
# → 의존성이 안 바뀌면 Docker 레이어 캐시가 적용되어 재빌드가 빨라짐
COPY gradlew settings.gradle build.gradle ./
COPY gradle/ gradle/

# 각 서비스의 build.gradle 복사 (의존성 해석에 필요)
COPY eureka/build.gradle eureka/build.gradle
COPY gateway/build.gradle gateway/build.gradle
COPY user-service/build.gradle user-service/build.gradle
COPY product-service/build.gradle product-service/build.gradle
COPY order-service/build.gradle order-service/build.gradle
COPY payment-service/build.gradle payment-service/build.gradle

# 의존성만 먼저 다운로드 (소스 변경 시에도 이 레이어는 캐시됨)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# 전체 소스 복사 후 빌드
COPY . .
RUN ./gradlew clean build -x test --no-daemon

# ---------------------------------------------------------------------------
# Stage 2: 실행 (JRE만 포함된 경량 이미지)
# ---------------------------------------------------------------------------
FROM eclipse-temurin:21-jre

WORKDIR /app

# ARG로 서비스명을 받아 해당 서비스의 JAR만 복사
# docker build 시 --build-arg SERVICE_NAME=user-service 로 지정
ARG SERVICE_NAME

# builder 스테이지에서 빌드된 JAR 복사
COPY --from=builder /app/${SERVICE_NAME}/build/libs/${SERVICE_NAME}-0.0.1-SNAPSHOT.jar app.jar

# Spring Boot Actuator 헬스체크
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# JAR 실행
# -XX:+UseContainerSupport: 컨테이너 메모리 제한을 JVM이 인식하도록 설정
# -XX:MaxRAMPercentage=75.0: 컨테이너 메모리의 75%를 힙으로 사용
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
