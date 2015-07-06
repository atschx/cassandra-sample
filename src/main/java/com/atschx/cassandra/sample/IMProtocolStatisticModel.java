package com.atschx.cassandra.sample;

import java.net.InetAddress;
import java.util.Date;

public class IMProtocolStatisticModel {

	private Integer uid;
	private Long sid;
	private String ipAddress;
	private Date occuredOn;

	private Integer seq;
	private Integer channel;
	private Integer tag;
	private Integer type;
	private String packet;
	private Integer bytes;

	private String clientVersion;
	private Integer clientType;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getOccuredOn() {
		return occuredOn;
	}

	public void setOccuredOn(Date occuredOn) {
		this.occuredOn = occuredOn;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPacket() {
		return packet;
	}

	public void setPacket(String packet) {
		this.packet = packet;
	}

	public Integer getBytes() {
		return bytes;
	}

	public void setBytes(Integer bytes) {
		this.bytes = bytes;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	@Override
	public String toString() {
		return "IMProtocolStatisticModel [ uid=" + uid + ", sid=" + sid + ", ipAddress=" + ipAddress + ", occuredOn="
				+ occuredOn + ", seq=" + seq + ", channel=" + channel + ", tag=" + tag + ", type=" + type + ", packet="
				+ packet + ", bytes=" + bytes + ", clientVersion=" + clientVersion + ", clientType=" + clientType + "]";
	}

}
