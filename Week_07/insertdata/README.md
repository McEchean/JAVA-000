### jdbc 批量插入 100 万数据
1. 时间大概在100s左右
2. 去除掉自动提交

### Hikari 批量插入 100 万数据
1. 时间大概在 80s 左右


### 注意点
* 在数据库连接url上增加了 rewriteBatchedStatements=true 这个参数，会让数据插入性能提升好几倍