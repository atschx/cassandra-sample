package com.atschx.cassandra.sample;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

	public void createSchema(Session session) {

		// build keyspace
		StringBuffer keyspaceBuff = new StringBuffer();
		keyspaceBuff.append("CREATE KEYSPACE IF NOT EXISTS ").append(
				KEYSPACE_NAME);
		keyspaceBuff.append(" WITH replication ");
		keyspaceBuff
				.append("= {'class':'SimpleStrategy', 'replication_factor':3};");

		// build table
		StringBuffer tableStringBuff = new StringBuffer();
		tableStringBuff.append("CREATE TABLE IF NOT EXISTS ");
		tableStringBuff.append(KEYSPACE_NAME).append(".").append(TABLE_NAME);
		tableStringBuff.append(" (");

		// tableStringBuff.append("id").append(" uuid").append(" PRIMARY KEY").append(",");

		tableStringBuff.append("uid").append(" int").append(",");
		tableStringBuff.append("sid").append(" bigint").append(",");
		tableStringBuff.append("ip_address").append(" inet").append(",");
		tableStringBuff.append("occured_on").append(" timestamp").append(",");

		tableStringBuff.append("seq").append(" int").append(",");
		tableStringBuff.append("channel").append(" int").append(",");
		tableStringBuff.append("tag").append(" int").append(",");
		tableStringBuff.append("type").append(" int").append(",");
		tableStringBuff.append("packet").append(" text").append(",");
		tableStringBuff.append("bytes").append(" int").append(",");

		tableStringBuff.append("client_version").append(" varchar").append(",");
		tableStringBuff.append("client_type").append(" int").append(",");

		//组合主键
//		tableStringBuff.append("PRIMARY KEY").append("(").append("uid")
//				.append(",").append("sid").append(",").append("ip_address")
//				.append(",").append("occured_on").append(",").append("bytes")
//				.append(")");

		tableStringBuff.append("PRIMARY KEY");
		tableStringBuff.append("(");
		tableStringBuff.append("uid").append(",");
		tableStringBuff.append("channel").append(",");
		tableStringBuff.append("bytes").append(",");
		tableStringBuff.append("occured_on");
		tableStringBuff.append(")");
		tableStringBuff.append(" )");
		
//		tableStringBuff
//				.append("WITH CLUSTERING ORDER BY (sid ASC,ip_address DESC,occured_on ASC,bytes DESC)");
		
		tableStringBuff.append("WITH CLUSTERING ORDER BY (channel ASC, bytes DESC, occured_on ASC)");
		

		tableStringBuff.append(";");

		session.execute(keyspaceBuff.toString());
		session.execute(tableStringBuff.toString());

	}

	// 测试数据
	public void loadData(Session session) {

		StringBuffer cqlBuff = new StringBuffer();
		cqlBuff.append("INSERT INTO ");
		cqlBuff.append(KEYSPACE_NAME).append(".").append(TABLE_NAME);
		cqlBuff.append("(uid, sid, ip_address, occured_on,seq,channel,tag,type,packet,bytes,client_version,client_type) ");
		cqlBuff.append("VALUES");
		cqlBuff.append("(?,?,?,?,?,?,?,?,?,?,?,?)");
//		cqlBuff.append(" USING TTL 20");//20S数据过期
		cqlBuff.append(";");

		PreparedStatement statement = session.prepare(cqlBuff.toString());

		List<IMProtocolStatisticModel> protocolStatisticModels = new ArrayList<IMProtocolStatisticModel>();
		for (int i = 0; i < 30; i++) {
			IMProtocolStatisticModel imProtocolStatisticModel = new IMProtocolStatisticModel();

			imProtocolStatisticModel.setUid(13141988);
			imProtocolStatisticModel.setSid(System.currentTimeMillis());
			try {
				imProtocolStatisticModel
						.setIpAddress(InetAddress.getByAddress(new byte[] {
								(byte) 192, (byte) 168, 1, (byte)(i+1) }));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			imProtocolStatisticModel.setOccuredOn(new Date());
			imProtocolStatisticModel.setSeq(1);
			imProtocolStatisticModel.setChannel(i/5);
			imProtocolStatisticModel.setTag(2);
			imProtocolStatisticModel.setType(3);
			imProtocolStatisticModel
					.setPacket("{header:{\"tag\":2,\"type\":3},meta:{}}");
			imProtocolStatisticModel.setBytes(imProtocolStatisticModel
					.getPacket().getBytes().length * (i + 2));

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
					imProtocolStatisticModel.getOccuredOn(),
					imProtocolStatisticModel.getSeq(),
					imProtocolStatisticModel.getChannel(),
					imProtocolStatisticModel.getTag(),
					imProtocolStatisticModel.getType(),
					imProtocolStatisticModel.getPacket(),
					imProtocolStatisticModel.getBytes(),
					imProtocolStatisticModel.getClientVersion(),
					imProtocolStatisticModel.getClientType());
			
			batch.add(boundStatement);
		}

		session.execute(batch);
	}

	public ResultSetFuture getRows(Session session) {
		Select select = QueryBuilder.select().all()
				.from(KEYSPACE_NAME, TABLE_NAME);
		return session.executeAsync(select);
	}

	public static void main(String[] args) {

		Application application = new Application();

		CassandraOperator operator = new CassandraOperator();
		operator.connect("127.0.0.1");

		try {
			application.createSchema(operator.getSession());
			application.loadData(operator.getSession());
		} finally {
			operator.close();
		}
	}

}
