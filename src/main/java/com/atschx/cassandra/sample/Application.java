package com.atschx.cassandra.sample;

import com.atschx.cassandra.CassandraOperator;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class Application {

	private static final String KEYSPACE_NAME = "statistic";
	private static final String TABLE_NAME = "protocols";

	public void createSchema(Session session) {

		//build keyspace
		StringBuffer keyspaceBuff = new StringBuffer();
		keyspaceBuff.append("CREATE KEYSPACE IF NOT EXISTS ").append(KEYSPACE_NAME);
		keyspaceBuff.append(" WITH replication ");
		keyspaceBuff.append("= {'class':'SimpleStrategy', 'replication_factor':3};");
		
		//build table
		StringBuffer tableStringBuff = new StringBuffer();
		tableStringBuff.append("CREATE TABLE IF NOT EXISTS ");
		tableStringBuff.append(KEYSPACE_NAME).append(".").append(TABLE_NAME);
		tableStringBuff.append(" (");
		
		//tableStringBuff.append("id").append(" uuid").append(" PRIMARY KEY").append(",");
		
		tableStringBuff.append("uid").append(" int").append(",");
		tableStringBuff.append("sid").append(" int").append(",");
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
		
		tableStringBuff.append("PRIMARY KEY")
		.append("(")
		.append("uid").append(",")
		.append("sid").append(",")
		.append("ip_address").append(",")
		.append("occured_on").append(",")
		.append("bytes")
		.append(")");
		
		tableStringBuff.append(" )");
		tableStringBuff.append("WITH CLUSTERING ORDER BY (sid ASC,ip_address DESC,occured_on ASC,bytes DESC)");
		tableStringBuff.append(";");

		session.execute(keyspaceBuff.toString());
		session.execute(tableStringBuff.toString());
		
	}

	public void loadData(Session session) {
		PreparedStatement statement = session.prepare("INSERT INTO "
				+ KEYSPACE_NAME + "." + TABLE_NAME + " "
				+ "(id, title, album, artist, tags) "
				+ "VALUES (?, ?, ?, ?, ?);");
		

	}

	public static void main(String[] args) {

		Application application = new Application();

		CassandraOperator operator = new CassandraOperator();

		operator.connect("127.0.0.1");

		application.createSchema(operator.getSession());

		operator.close();
	}

}
