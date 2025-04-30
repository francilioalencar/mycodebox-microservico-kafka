# Estágio de build
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /application

# Copiar os arquivos necessários para o build
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Dar permissão de execução
RUN chmod +x mvnw

# Construir o projeto Maven
RUN ./mvnw clean package -DskipTests

# Extrair o conteúdo do JAR gerado
ARG JAR_FILE=target/*.jar
RUN java -Djarmode=layertools -jar ${JAR_FILE} extract

# Estágio de execução
FROM eclipse-temurin:17-jre-alpine
WORKDIR /application

# Opcional: resolver problemas de bibliotecas
# RUN apk add --no-cache libc6-compat

# Copiar os arquivos extraídos do build
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./

# Definir o ponto de entrada
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
CMD ["--spring.profiles.active=prod"]
