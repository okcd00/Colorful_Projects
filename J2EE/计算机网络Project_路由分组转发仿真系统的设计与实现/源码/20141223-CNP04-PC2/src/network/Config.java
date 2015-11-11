package network;

public class Config {

	public String host1IP;
	public String host2IP;
	public String host3IP;
	public int host1Port;
	public int host2Port;
	public int host3Port;
	public int router1Port;
	public int router2Port;
	public int router3Port;
	public Config() {
		this.host1IP="127.0.0.1";
		this.host2IP="127.0.0.1";
		this.host3IP="127.0.0.1";
		this.host1Port=8081;
		this.host2Port=8083;
		this.host3Port=8085;
		this.router1Port=8082;
		this.router2Port=8084;
		this.router3Port=8086;
	}

}
