# 使用最小化的 JRE 运行环境
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制生成的 jar 文件
COPY ./target/*.war app.war

# 设置 JVM 参数（可根据需要调整）
ENV JAVA_OPTS="-Xmx256m -Xms256m"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.war"]