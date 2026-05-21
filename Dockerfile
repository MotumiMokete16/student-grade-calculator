# ---- Build Stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first (caches dependencies layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build the WAR
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Run Stage ----
FROM tomcat:10.1-jdk17

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy built WAR into Tomcat
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]