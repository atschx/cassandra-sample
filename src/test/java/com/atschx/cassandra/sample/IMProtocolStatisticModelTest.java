package com.atschx.cassandra.sample;

import java.util.Date;

import org.junit.Test;

public class IMProtocolStatisticModelTest {

	@Test
	public void test() {
		
		IMProtocolStatisticModel imProtocolStatisticModel = new IMProtocolStatisticModel();

		imProtocolStatisticModel.setUid(13141988);
		imProtocolStatisticModel.setSid(1292L);
		imProtocolStatisticModel.setIpAddress("192.168.1.1");
//		try {
//			imProtocolStatisticModel.setIpAddress(InetAddress.getByAddress(new byte[]{(byte) 192,(byte) 168,1,1}));
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
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
