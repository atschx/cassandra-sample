package com.atschx.cassandra;

import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

public class AsynchronousExample extends SmartClient {

	public AsynchronousExample() {
	}

	public ResultSetFuture getRows() {

		Select select = QueryBuilder.select().all().from("simplex", "songs");

		return getSession().executeAsync(select);
	}

	public static void main(String[] args) {
		AsynchronousExample client = new AsynchronousExample();
		client.connect("127.0.0.1");
		client.createSchema();
		client.loadData();
		ResultSetFuture results = client.getRows();
		for (Row row : results.getUninterruptibly()) {
			System.out.printf("%s: %s / %s\n", row.getString("artist"),
					row.getString(" "), row.getString("album"));
		}
		client.dropSchema("simplex");
		client.close();
	}
}
