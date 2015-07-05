# cassandra-sample

> cassandra 2.x以后推荐使用native的驱动,性能提升50%.更多内容可参考: datastax的[java-driver](http://docs.datastax.com/en/developer/java-driver/2.1/java-driver/whatsNew2.html)

## try it ?

> 创建这个表时,看上去有模有样,然则针对Cassdanra而言,这其实是灾难.

``` cqlsh
CREATE KEYSPACE statistic 
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'}  AND durable_writes = true;

CREATE TABLE statistic.protocols (
    uid int,
    sid bigint,
    ip_address inet,
    occured_on timestamp,
    bytes int,
    channel int,
    client_type int,
    client_version text,
    packet text,
    seq int,
    tag int,
    type int,
    PRIMARY KEY (uid, sid, ip_address, occured_on, bytes)
) WITH CLUSTERING ORDER BY (sid ASC, ip_address DESC, occured_on ASC, bytes DESC);
```

> PS:示例代码在插入数据时设置了TTL 20S. 

``` java
StringBuffer cqlBuff = new StringBuffer();
cqlBuff.append("INSERT INTO ");
cqlBuff.append(KEYSPACE_NAME).append(".").append(TABLE_NAME);
cqlBuff.append("(uid, sid, ip_address, occured_on,seq,channel,tag,type,packet,bytes,client_version,client_type) ");
cqlBuff.append("VALUES");
cqlBuff.append("(?,?,?,?,?,?,?,?,?,?,?,?)");
cqlBuff.append(" USING TTL 20");//20S数据过期
cqlBuff.append(";");

PreparedStatement statement = session.prepare(cqlBuff.toString());
```

