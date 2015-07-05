# cassandra-sample

> 参考: datastax的[java-driver](http://docs.datastax.com/en/developer/java-driver/2.1/java-driver/whatsNew2.html)


## try it ?

> 创建这个表时,看上去有模有样,然则针对Cassdanra而言,这其实是灾难.

```
cqlsh:statistic> desc schema;

CREATE KEYSPACE statistic WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'}  AND durable_writes = true;

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
) WITH CLUSTERING ORDER BY (sid ASC, ip_address DESC, occured_on ASC, bytes DESC)
    AND bloom_filter_fp_chance = 0.01
    AND caching = '{"keys":"ALL", "rows_per_partition":"NONE"}'
    AND comment = ''
    AND compaction = {'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32'}
    AND compression = {'sstable_compression': 'org.apache.cassandra.io.compress.LZ4Compressor'}
    AND dclocal_read_repair_chance = 0.1
    AND default_time_to_live = 0
    AND gc_grace_seconds = 864000
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0
    AND min_index_interval = 128
    AND read_repair_chance = 0.0
    AND speculative_retry = '99.0PERCENTILE';
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

