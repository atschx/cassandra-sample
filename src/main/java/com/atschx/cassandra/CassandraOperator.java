package com.atschx.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraOperator {

	private Cluster cluster;
	private Session session;

	public Session getSession() {
		return this.session;
	}

	public void connect(String node) {
		cluster = Cluster.builder().addContactPoint(node).build();
		session = cluster.connect();
	}

	public void close() {
		session.close();
		cluster.close();
	}
}
