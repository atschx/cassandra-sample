package com.atschx.cassandra.sample;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;


public class IMProtocolStatisticModelTest {

	@Test
	public void test() {
		
		IMProtocolStatisticModel imProtocolStatisticModel = new IMProtocolStatisticModel();

		imProtocolStatisticModel.setId(UUID.randomUUID());
		
		imProtocolStatisticModel.setUid(13141988);
		imProtocolStatisticModel.setSid(1292L);
		try {
			imProtocolStatisticModel.setIpAddress(InetAddress.getByAddress(new byte[]{(byte) 192,(byte) 168,1,1}));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		imProtocolStatisticModel.setOccuredOn(new Date());
		imProtocolStatisticModel.setSeq(1);
		imProtocolStatisticModel.setChannel(1);
		imProtocolStatisticModel.setTag(2);
		imProtocolStatisticModel.setType(3);
		imProtocolStatisticModel.setPacket("{header:{\"tag\":2,\"type\":3},meta:{}}");
		imProtocolStatisticModel.setBytes(imProtocolStatisticModel.getPacket().getBytes().length);
		
		imProtocolStatisticModel.setClientVersion("1.0.0");
		imProtocolStatisticModel.setClientType(2);
		
		System.out.println(imProtocolStatisticModel);
	}

}
