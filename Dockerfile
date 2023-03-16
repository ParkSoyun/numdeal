# open jdk java 11 버전의 환경 구성
FROM openjdk:11-jdk

# 한국시간으로 설정
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Seoul
RUN apt-get install -y tzdata

# build가 되는 시점에 JAR_FILE이라는 변수명에 build/libs/*.jar 표현식 선언
# bild/libs 경로는 gradle로 빌드했을 때 jar 파일이 생성되는 경로
ARG JAR_FILE=build/libs/numdeal-0.0.1-SNAPSHOT.jar

# JAR_FILE을 app.jar로 복사
COPY ${JAR_FILE} app.jar

# jar 파일을 실행하는 명령어 (java -jar xxx.jar)
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
