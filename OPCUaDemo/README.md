# 项目需求
读取opc系统里PLC实时监听各个机械的数据值，存储到数据库中
## 操作步骤
要想读取opc系统里的数据，首先要与其建立连接。官方提供了一套opc-ua的连接客户端的依赖。
```java
        <!--OA客户端连接-->
        <dependency>
            <groupId>org.eclipse.milo</groupId>
            <artifactId>sdk-client</artifactId>
            <version>0.3.6</version>
        </dependency>

        <dependency>
            <groupId>org.eclipse.milo</groupId>
            <artifactId>sdk-server</artifactId>
            <version>0.3.6</version>
        </dependency>


        <!-- https://mvnrepository.com/artifact/org.eclipse.milo/stack-core -->
        <dependency>
            <groupId>org.eclipse.milo</groupId>
            <artifactId>stack-core</artifactId>
            <version>0.3.6</version>
        </dependency>

        <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcpkix-jdk15on</artifactId>
            <version>1.57</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
```java
