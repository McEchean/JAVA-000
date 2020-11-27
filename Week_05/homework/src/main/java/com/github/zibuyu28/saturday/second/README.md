### maven 的 profile 机制
* 用途：用于根据命令打包不同环境的配置文件等
* 示例：
    ```xml
    <profiles>
        <profile>
            <!-- 开发环境 -->
            <id>develop</id>
            <properties>
                <profiles.active>develop</profiles.active>
            </properties>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
        <profile>
            <!-- 测试环境 -->
            <id>test</id>
            <properties>
                <profiles.active>test</profiles.active>
            </properties>
        </profile>
        <profile>
            <!-- 生产环境 -->
            <id>product</id>
            <properties>
                <profiles.active>product</profiles.active>
            </properties>
        </profile>
    </profiles>
    ```
  * 这个示例配置了三个环境的 profile
  * id 标签即为`mvn package -P <参数>`参数字段， 类似`mvn package -P test`
  * 选择了哪个profile之后，`profiles.active` 这个属性的值便是传入的参数值
  * 还可以根据参数，直接配置需要打包进jar包的配置文件
  ```xml
    <build>
        <finalName>${artifactId}</finalName>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <!--resource的filtering属性用来表示资源文件中的占位符是否需要被替换，true为需要替换-->
                <filtering>true</filtering>
                <!--引入资源文件信息-->
                <includes>
                    <include>application.properties</include>
                    <include>log4j.properties</include>
                    <include>banner.txt</include>
                    <include>application-${profiles.active}.properties</include>
                </includes>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <!-- 引入Maven插件重写manifest -->
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <!-- 指定该Main Class为全局的唯一入口 -->
                    <!--第五步，编写程序运行额主入口信息，该信息必须位于web模块下的main方法要放到目录外层，根据约定哦，-->
                    <mainClass>com.handu.hapm.config.CoreApplication</mainClass>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal><!--可以把依赖的包都打包到生成的Jar包中-->
                            <goal>build-info</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ```

### SpringBoot 中配合 maven profile 使用
* 一般 SpringBoot 会有一个application.properties,这个是针对所有环境的公共配置
* 全局Profile配置使用application-{profile}.properties，如application-product.properties，通过在application.properties中设置spring.profiles.active=product来指定活动的Profile。
* 在application.properties公共配置文件中设置：spring.profiles.active=@profiles.active@， 这个 profile.active 便会替换为上面maven中的参数 profile.active 的值，例如：test
* spring boot就会激活application-test.properties的配置文件了。