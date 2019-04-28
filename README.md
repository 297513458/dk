 # pom配置
 先在pom.xml加上插件,
	注意:<repository>docker服务器地址(默认docker.io)/docker注册名称/具体名称</repository>(docker服务器地址的值需要在settings.xml里有对应的配置,否则无法push到docker服务器)
 <plugin>
				<groupId>com.spotify</groupId>
				<artifactId>dockerfile-maven-plugin</artifactId>
				<version>1.4.10</version>
				<executions>
					<execution>
						<id>default</id>
						<goals>
							<goal>build</goal>
							<goal>push</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<useMavenSettingsForAuth>true</useMavenSettingsForAuth>
					<googleContainerRegistryEnabled>false </googleContainerRegistryEnabled>
					<repository>registry.hub.docker.com/${docker.registry.name}/${project.artifactId}</repository>
					<tag>latest</tag>
					<buildArgs>
						<JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
					</buildArgs>
				</configuration>
			</plugin>
   # settings.xml配置
   docker服务器的用户名密码配置
   id和上面的docker服务器地址地址一致
   	 <server>
      <id>docker.io</id>
      <username>297513458</username>
      <password>密码</password>
    </server>
    <server>
      <id>registry.hub.docker.com</id> 
      <username>297513458</username>
      <password>密码</password>
    </server>
在pom.xml同级目录加上Dockerfile
# dockerfile 基础配置
FROM openjdk:8-jre
MAINTAINER wendao
#加载到docker的目录
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} /app/app.jar
WORKDIR /app/
EXPOSE 8080
ENTRYPOINT ["/usr/bin/java","-jar","./app.jar"]
命令,不处理docker:mvn clean package -Ddockerfile.skip
编译mvn clean package dockerfile:build
发布mvn clean package dockerfile:push或mvn clean deploy
				
				#jenkins pipeline
				node {
   stage('拉取git'  ) { // for display purposes
     echo '拉取git'
     git 'https://github.com/297513458/dk.git'
   }
   stage('构建') {
      echo 'mvn clean package'
       sh "'mvn' clean package"
   }
     stage('构建docker') {
      echo '构建docker'
      sh "'docker' build -t 297513458/dk ."
   }
   stage('发布到私服 ') {
      echo 'deploy docker'
      sh "'docker' login -u 297513458 -p Kkk888888"
      sh "'docker' push 297513458/dk"
   }
    stage('触发k8s') {
      echo '触发k8s'
      sh "'kubectl' run k8s --image=297513458/dk --replicas=3 --namespace=app"
      sh "'kubectl expose deployment/k8s --port=8080 --target-port=8080 --type=LoadBalancer --namespace=app"
   }
}
