package com.atschx.cassandra;


import org.junit.Assert;
import org.junit.Test;

public class CassandraOperatorTest {
	

	@Test
	public void testConnect() {
		CassandraOperator cassandraOperator = new CassandraOperator();
		cassandraOperator.connect("127.0.0.1");
		Assert.assertNotNull(cassandraOperator.getSession());
		cassandraOperator.close();
	}

}
