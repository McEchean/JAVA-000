学习笔记
### 注意
* id的设计,因为设置了unsigned，导致雪花算法产生的id无法插入
* 因为表名设置成了 order， 和mysql的关键字冲突了，导致ss-jdbc在解析sql的时候报错，需要在 sql中将order用``框起来


### 2020.12.09, 因为一直没时间，先写了一题，后面一题后面补上

### 问题记录
1. 配置proto生成java code的时候一直报错, 命令 `mvn compile`
    ```java
   [INFO] BUILD FAILURE
   [INFO] ------------------------------------------------------------------------
   [INFO] Total time:  2.010 s
   [INFO] Finished at: 2020-12-13T10:00:20+08:00
   [INFO] ------------------------------------------------------------------------
   [ERROR] Failed to execute goal org.xolstice.maven.plugins:protobuf-maven-plugin:0.6.1:compile (default) on project tcc-demo-common: protoc did not exit cleanly. Review output for more information. -> [Help 1]
   [ERROR] 
   [ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
   [ERROR] Re-run Maven using the -X switch to enable full debug logging.
   [ERROR] 
   [ERROR] For more information about the errors and possible solutions, please read the following articles:
   [ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
    ```
   一直以为是 protobuf-maven-plugin 的版本问题，没有往上翻错误信息，其实是因为两个proto里面有相同的 message 了
   ```java
   [ERROR] /Users/wujiajia/IdeaProjects/homework120501/tcc-demo-common/src/main/proto/node.proto [0:0]: chain.proto:23:9: "OpNodeResponse.Code" is already defined in file "node.proto".
   chain.proto:24:10: "OpNodeResponse.Data" is already defined in file "node.proto".
   chain.proto:25:10: "OpNodeResponse.Err" is already defined in file "node.proto".
   chain.proto:21:9: "OpNodeResponse" is already defined in file "node.proto".
   chain.proto:8:41: "OpNodeResponse" seems to be defined in "node.proto", which is not imported by "chain.proto".  To use it here, please add the necessary import.
   
   [ERROR] /Users/wujiajia/IdeaProjects/homework120501/tcc-demo-common/src/main/proto/chain.proto [0:0]: chain.proto:23:9: "OpNodeResponse.Code" is already defined in file "node.proto".
   chain.proto:24:10: "OpNodeResponse.Data" is already defined in file "node.proto".
   chain.proto:25:10: "OpNodeResponse.Err" is already defined in file "node.proto".
   chain.proto:21:9: "OpNodeResponse" is already defined in file "node.proto".
   chain.proto:8:41: "OpNodeResponse" seems to be defined in "node.proto", which is not imported by "chain.proto".  To use it here, please add the necessary import.
   ```

2. 配置maven生成不可执行的jar包，需要配置spring-boot-maven-plugin的configuration.layout为NONE
   