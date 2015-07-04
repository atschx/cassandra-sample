package com.atschx.cassandra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;

public class SimpleClient {

	Logger logger = LoggerFactory.getLogger(getClass());
	private Cluster cluster;

	public void connect(String node) {
		cluster = Cluster.builder().addContactPoint(node).build();
		Metadata metadata = cluster.getMetadata();

		logger.info(String.format("Connected to cluster: %s",
				metadata.getClusterName()));
		for (Host host : metadata.getAllHosts()) {
			logger.info(String.format("Datatacenter: %s; Host: %s; Rack: %s",
					host.getDatacenter(), host.getAddress(), host.getRack()));
		}
	}

	public void close() {
		cluster.close();
	}

	public static void main(String[] args) {
		SimpleClient client = new SimpleClient();
		client.connect("127.0.0.1");
		client.close();
	}
}