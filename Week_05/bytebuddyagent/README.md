### 注意
1. pom 文件里面打包的插件需要改成jar-plugin，并且需要指定premain的类
2. 使用agent的时候，在VM参数里面增加 -javaagent:<agent.jar的路径>