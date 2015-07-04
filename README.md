# cassandra-sample

> step by step

```
cqlsh:statistic> desc schema;

CREATE KEYSPACE statistic WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'}  AND durable_writes = true;

CREATE TABLE statistic.protocols (
    uid int,
    sid int,
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

更多内容,需要参考[这里](http://docs.datastax.com/en/developer/java-driver/2.1/java-driver/whatsNew2.html)
