package com.atschx.cassandra.sample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.atschx.cassandra.CassandraOperator;
import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

public class Application {

	private static final String KEYSPACE_NAME = "statistic";
	private static final String TABLE_NAME = "protocols";
	private static final String TABLE_NAME_ORDER_BY_BYTES = "protocols_order_by_bytes";

	public void createSchema(Session session) {

		// 00.drop schema
		session.execute("DROP KEYSPACE IF EXISTS " + KEYSPACE_NAME);

		// 01.build keyspace
		StringBuffer keyspaceBuff = new StringBuffer();
		keyspaceBuff.append("CREATE KEYSPACE IF NOT EXISTS ").append(KEYSPACE_NAME);
		keyspaceBuff.append(" WITH replication ");
		keyspaceBuff.append("= {'class':'SimpleStrategy', 'replication_factor':2};");
		session.execute(keyspaceBuff.toString());

		// build table：基于uid加日期进行分区
		{
			StringBuffer tableStringBuff = new StringBuffer();
			tableStringBuff.append("CREATE TABLE IF NOT EXISTS ");
			tableStringBuff.append(KEYSPACE_NAME).append(".").append(TABLE_NAME);
			tableStringBuff.append(" (");

			tableStringBuff.append("uid").append(" int").append(",");
			tableStringBuff.append("sid").append(" bigint").append(",");
			tableStringBuff.append("ip_address").append(" text").append(",");
			tableStringBuff.append("occured_on").append(" text").append(",");
			tableStringBuff.append("occured_at").append(" timestamp").append(",");

			tableStringBuff.append("seq").append(" int").append(",");
			tableStringBuff.append("channel").append(" int").append(",");
			tableStringBuff.append("tag").append(" int").append(",");
			tableStringBuff.append("type").append(" int").append(",");
			tableStringBuff.append("packet").append(" text").append(",");
			tableStringBuff.append("bytes").append(" int").append(",");

			tableStringBuff.append("client_version").append(" varchar").append(",");
			tableStringBuff.append("client_type").append(" int").append(",");

			// 麻利的给出结果吧.
			// PRIMARY KEY ((uid,occured_on,ip_address),sid,occured_at,channel)
			tableStringBuff.append("PRIMARY KEY");
			tableStringBuff.append("(");
			tableStringBuff.append("(").append("uid").append(",").append("occured_on").append(",").append("ip_address").append(")").append(",");
			
			//提取用户一次会话的所有交互报文
			tableStringBuff.append("sid").append(",");
			tableStringBuff.append("occured_at").append(",");
			tableStringBuff.append("channel");
			tableStringBuff.append(")");
			tableStringBuff.append(" );");

			session.execute(tableStringBuff.toString());
		}

		// build table：基于服务器IP及发生日期（ip_address,occured_on）分区，按报文大小排序：uid,channel,tag,occured_at,
		{
			StringBuffer tableOrderStrBuff = new StringBuffer();
			tableOrderStrBuff.append("CREATE TABLE IF NOT EXISTS ");
			tableOrderStrBuff.append(KEYSPACE_NAME).append(".").append(TABLE_NAME_ORDER_BY_BYTES);
			tableOrderStrBuff.append(" (");
			tableOrderStrBuff.append("uid").append(" int").append(",");
			tableOrderStrBuff.append("sid").append(" bigint").append(",");
			tableOrderStrBuff.append("ip_address").append(" text").append(",");
			tableOrderStrBuff.append("occured_on").append(" text").append(",");
			tableOrderStrBuff.append("occured_at").append(" timestamp").append(",");

			tableOrderStrBuff.append("seq").append(" int").append(",");
			tableOrderStrBuff.append("channel").append(" int").append(",");
			tableOrderStrBuff.append("tag").append(" int").append(",");
			tableOrderStrBuff.append("type").append(" int").append(",");
			tableOrderStrBuff.append("bytes").append(" int").append(",");

			tableOrderStrBuff.append("client_version").append(" varchar").append(",");
			tableOrderStrBuff.append("client_type").append(" int").append(",");

			// PRIMARY KEY ((ip_address,occured_on),bytes)
			tableOrderStrBuff.append("PRIMARY KEY");
			tableOrderStrBuff.append("(");
			tableOrderStrBuff.append("(").append("ip_address").append(",").append("occured_on").append(")").append(",");
			tableOrderStrBuff.append("bytes");
			tableOrderStrBuff.append(")");
			tableOrderStrBuff.append(" )WITH CLUSTERING ORDER BY (bytes DESC);");

			session.execute(tableOrderStrBuff.toString());
		}

	}

	// 测试数据
	public void loadData(Session session) {

		StringBuffer cqlBuff = new StringBuffer();
		cqlBuff.append("INSERT INTO ");
		cqlBuff.append(KEYSPACE_NAME).append(".").append(TABLE_NAME);
		cqlBuff.append("(uid, sid, ip_address,occured_on, occured_at,seq,channel,tag,type,packet,bytes,client_version,client_type) ");
		cqlBuff.append("VALUES");
		cqlBuff.append("(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		// cqlBuff.append(" USING TTL 20");//20S数据过期
		cqlBuff.append(";");

		PreparedStatement statement = session.prepare(cqlBuff.toString());

		List<IMProtocolStatisticModel> protocolStatisticModels = new ArrayList<IMProtocolStatisticModel>();
		for (int i = 0; i < 30; i++) {
			IMProtocolStatisticModel imProtocolStatisticModel = new IMProtocolStatisticModel();

			imProtocolStatisticModel.setUid(13141988);
			imProtocolStatisticModel.setSid(System.currentTimeMillis());
			imProtocolStatisticModel.setIpAddress("127.0.0.1");
			imProtocolStatisticModel.setOccuredOn(new Date());
			imProtocolStatisticModel.setSeq(1);
			imProtocolStatisticModel.setChannel(i / 5);
			imProtocolStatisticModel.setTag(2);
			imProtocolStatisticModel.setType(3);
			imProtocolStatisticModel.setPacket("{header:{\"tag\":2,\"type\":3},meta:{}}");
			imProtocolStatisticModel.setBytes(imProtocolStatisticModel.getPacket().getBytes().length * (i + 2));

			imProtocolStatisticModel.setClientVersion("1.0.0");
			imProtocolStatisticModel.setClientType(2);

			protocolStatisticModels.add(imProtocolStatisticModel);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		BatchStatement batch = new BatchStatement();
		for (IMProtocolStatisticModel imProtocolStatisticModel : protocolStatisticModels) {
			BoundStatement boundStatement = new BoundStatement(statement);

			boundStatement.bind(
					imProtocolStatisticModel.getUid(),
					imProtocolStatisticModel.getSid(),
					imProtocolStatisticModel.getIpAddress(),
					//
					"2015-07-05", imProtocolStatisticModel.getOccuredOn(), imProtocolStatisticModel.getSeq(),
					imProtocolStatisticModel.getChannel(), imProtocolStatisticModel.getTag(),
					imProtocolStatisticModel.getType(), imProtocolStatisticModel.getPacket(),
					imProtocolStatisticModel.getBytes(), imProtocolStatisticModel.getClientVersion(),
					imProtocolStatisticModel.getClientType());

			batch.add(boundStatement);
		}

		session.execute(batch);
	}

	public ResultSetFuture getRows(Session session) {
		Select select = QueryBuilder.select().all().from(KEYSPACE_NAME, TABLE_NAME);
		return session.executeAsync(select);
	}

	public static void main(String[] args) {

		Application application = new Application();

		CassandraOperator operator = new CassandraOperator();
		operator.connect("127.0.0.1");

		try {
			application.createSchema(operator.getSession());
			//application.loadData(operator.getSession());
		} finally {
			operator.close();
		}
	}

}
