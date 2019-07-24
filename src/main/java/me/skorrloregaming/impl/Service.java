package me.skorrloregaming.impl;

public enum Service {

	PlanetMinecraft(0, "PlanetMinecraft.com", ServicePriority.delay24hour),
	MinecraftMP(1, "Minecraft-MP.com", ServicePriority.delay12hour),
	MinecraftServersOrg(2, "MinecraftServers.org", ServicePriority.midnight),
	MinecraftServersBiz(3, "MinecraftServers.biz", ServicePriority.delay12hour),
	MinecraftServerList(4, "MCSL", ServicePriority.midnightGreenwich),
	MinecraftServerNet(5, "Minecraft-Server.net", ServicePriority.midnight),
	MinecraftList(6, "MinecraftServersList", ServicePriority.delay24hour),
	TopG(7, "TopG.org", ServicePriority.delay12hour),
	Trackyserver(8, "Trackyserver.com", ServicePriority.delay24hour),
	TopMinecraftServers(9, "/Top Minecraft Servers", ServicePriority.delay24hour);

	private int id;
	private String serviceName;
	private ServicePriority priority;

	Service(int id, String serviceName, ServicePriority priority) {
		this.id = id;
		this.serviceName = serviceName;
		this.priority = priority;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return serviceName;
	}

	public ServicePriority getPriority() {
		return priority;
	}

}
