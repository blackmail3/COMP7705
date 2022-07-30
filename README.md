# COMP7705 Everyone can NLP: A User-defined Well Visualized NLP Training Platform


## Dev Environment

The backend is written in Java (JDK11), and uses Maven as build tool. We recommend IntelliJ IDEA as IDE.

## Configuration

Everything in `/src/main/resources/application.yml`, including user info, project and database configs, etc.

## Packages

> **common.dto:** dto classes  
> **common.Exception:** Exception classes  
> **config:** Config classes  
> **entity:** Entity classes    
> **service:** Service classes  
> **utils:** Utils classes  
> **Mapper:** Mapper Interfaces

## Run the project

The installation of jdk11, MySQL and Redis is required before run.

Install command completes the compilation, unit testing, and packaging of the project, and deploys the executable .jar packages to the local Maven repository.

`mvn install`

A build project using Maven produces a target folder, but after you change the code, you need to clean the target and regenerate it.

`mvn clean package -Dmaven.test.skip=true`

The default name of the package is `${artifactId}-${version}.${packaging}`

pom.xml is configured as follows:

```
<artifactId>proj</artifactId>
<version>0.0.1-SNAPSHOT</version>
<packaging>jar</packaging>
```

After uploading the .jar package to the cloud server, you can start the project with `nohup java -jar proj-0.0.1-SNAPSHOT.jar >>run.log`. The runtime log will be recorded in run.log.

## Acknowledgements

- [MybatisPlus](https://github.com/baomidou/mybatis-plus)
- [Swagger](https://github.com/swagger-api)
- [Lombok](https://github.com/rzwitserloot)
