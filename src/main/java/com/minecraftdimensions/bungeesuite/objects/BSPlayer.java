package com.minecraftdimensions.bungeesuite.objects;

import java.util.ArrayList;
import java.util.HashMap;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import com.minecraftdimensions.bungeesuite.managers.ChatManager;
import com.minecraftdimensions.bungeesuite.objects.Channel;
import com.minecraftdimensions.bungeesuite.objects.Home;
import com.minecraftdimensions.bungeesuite.objects.Location;
import com.minecraftdimensions.bungeesuite.objects.Messages;


public class BSPlayer {
	private String playername;
	private String channel;
	private boolean muted;
	private String nickname = null;
	private String tempname;
	private boolean chatspying;
	private boolean dnd;
	private boolean afk;
	private boolean acceptingTeleports;
	private ArrayList<String> ignores = new ArrayList<String>();
	private ArrayList<Channel> channels = new ArrayList<Channel>();
	private HashMap<String, Home> homes = new HashMap<String, Home>();
	private Location deathBackLocation;
	private Location teleportBackLocation;
	private boolean lastBack; //true = death false = teleport
	private String replyPlayer;
	
	public BSPlayer(String name, String nickname, String channel, boolean muted, boolean chatspying, boolean dnd, boolean tps){
		this.playername = name;
		this.nickname = nickname;
		this.channel = channel;
		this.muted=muted;
		this.chatspying = chatspying;
		this.dnd = dnd;
		this.acceptingTeleports = tps;
	}
	
	public BSPlayer(String serialised){
		String data[] =serialised.split("~");
		playername = data[0];
		channel = data[1];
		muted = Boolean.parseBoolean(data[2]);
		nickname = data[3];
		tempname = data[4];
		chatspying = Boolean.parseBoolean(data[5]);
		dnd = Boolean.parseBoolean(data[7]);
		afk = Boolean.parseBoolean(data[8]);
		acceptingTeleports = Boolean.parseBoolean(data[9]);
		lastBack =  Boolean.parseBoolean(data[10]);
		if(nickname.equals("null")){
			nickname = null;
		}
		if(tempname.endsWith("null")){
			tempname = null;
		}
	}

	public String serialise(){
		return playername+"~"+channel+"~"+muted+"~"+nickname+"~"+tempname+"~"+chatspying+"~"+dnd+"~"+afk+"~"+acceptingTeleports+"~"+lastBack;
	}
	
	public String getName(){
		return playername;
	}
	public void setPlayerName(String name){
		this.playername=name;
	}
	public ProxiedPlayer getProxiedPlayer(){
		return ProxyServer.getInstance().getPlayer(playername);
	}
	public void sendMessage(String message){
		for (String line : message.split("\n")) {
			getProxiedPlayer().sendMessage(line);
		}
	}
	public String getChannel(){
		return channel;
	}
	public void setChannel(String channel){
		this.channel = channel;
	}
	public boolean isMuted(){
		return muted;
	}
	public void setMute(boolean mute){
		this.muted = mute;
		updatePlayer();
	}
	public boolean hasNickname(){
		return nickname!=null;
	}
	public String getNickname(){
		return nickname;
	}
	public void setNickname(String nick){
		this.nickname = nick;
	}
	public boolean isChatSpying(){
		return chatspying;
	}
	public void setChatSpying(boolean spy){
		this.chatspying= spy;
		updatePlayer();
	}
	public boolean isDND(){
		return dnd;
	}
	public void setDND(boolean dnd){
		this.dnd = dnd;
	}
	public boolean acceptingTeleports(){
		return this.acceptingTeleports;
	}
	public void setAcceptingTeleports(boolean tp){
		this.acceptingTeleports=tp;
	}
	public void addIgnore(String player){
		this.ignores.add(player);
	}
	public void removeIgnore(String player){
		this.ignores.remove(player);
	}
	public boolean ignoringPlayer(String player){
		return ignores.contains(player);
	}
	public void joinChannel(Channel channel){
		this.channels.add(channel);
	}
	public void leaveChannel(Channel channel){
		this.channels.remove(channel);
	}
	public boolean isInChannel(Channel channel){
		return this.channels.contains(channel);	
	}
	public void joinChannel(String channel){
		this.channels.add(ChatManager.getChannel(channel));
	}
	public void leaveChannel(String channel){
		this.channels.remove(ChatManager.getChannel(channel));
	}
	public boolean isInChannel(String channel){
		return this.channels.contains(this.channels.add(ChatManager.getChannel(channel)));	
	}
	public boolean homeExists(String home){
		return this.homes.containsKey(home);
	}
	public void addHome(Home home){
		this.homes.put(home.getName(), home);
	}
	public void removeHome(String home){
		this.homes.remove(home);
	}
	public int getHomesCountOnServer(String server){
		int count = 0;
		for(Home h :homes.values()){
			if(h.getServer().equals(server)){
				count++;
			}
		}
		return count;
	}
	public Home getHomeSimilar(String home){
		for(String h: homes.keySet()){
			if(h.contains(home)){
				return homes.get(h);
			}
		}
		return null;
	}
	
	public Channel getPlayersChannel(){
		return ChatManager.getChannel(channel);
	}
	public ArrayList<Channel> getPlayersChannels(){
		return channels;
	}
	public Channel getPlayersSimilarChannel(String channel){
		for(Channel chan:channels){
			if(chan.getName().contains(channel)){
				return chan;
			}
		}
		return null;
	}
	
	public void teleportPlayerToLocation(Location loc){
		//TODO
	}
	
	public void teleportPlayerToPlayer(String player){
		//TODO
	}
	public void teleportPlayerToPlayer(BSPlayer player){
		//TODO
	}
	
	public void setDeathBackLocation(Location loc){
		deathBackLocation = loc;
		lastBack = true;
	}
	
	public boolean hasDeathBackLocation(){
		return deathBackLocation!=null;
	}
	public void setTeleportBackLocation(Location loc){
		teleportBackLocation = loc;
		lastBack = false;
	}
	
	public Location getLastBackLocation(){
		if(lastBack){
			return deathBackLocation;
		}else{
			return teleportBackLocation;
		}
	}
	
	public ServerData getServerData(){
		return ChatManager.getServerData(getServer());
	}
	
	public boolean hasTeleportBackLocation(){
		return teleportBackLocation!=null;
	}
	
	public Location getDeathBackLocation(){
		return deathBackLocation;
	}
	public Location getTeleportBackLocation(){
		return teleportBackLocation;
	}
	
	public boolean hasReply(){
		return replyPlayer!=null;
	}
	
	public String getReplyPlayer(){
		return replyPlayer;
	}
	public boolean isAFK(){
		return afk;
	}
	public void setAFK(boolean afk){
		this.afk = afk;
	}
	
	public void updateDisplayName() {
		String name = getDisplayingName();
		if(name.length()>16){
		name =getDisplayingName().substring(0,16);	
		}
		ProxyServer.getInstance().getPlayer(playername).setDisplayName(name);
	}
	
	public String getDisplayingName(){
		if(tempname!=null){
			return tempname;
		}else
		if(getNickname()!=null){
			return getNickname();
		}else{
			return getName();
		}
	}
	public void setTempName(String name){
		tempname = name;
		updatePlayer();
		updateDisplayName();
	}
	
	public void revertName(){
		tempname = null;
		updatePlayer();
		updateDisplayName();
	}

	public void updatePlayer() {
		ChatManager.sendPlayer(playername, getServer());
	}

	public void sendMessageToPlayer(BSPlayer target, String message){
		target.sendMessage(Messages.PRIVATE_MESSAGE_RECEIVE.replace("{player}", getDisplayingName()).replace("{message}", message));
	}
	
	public HashMap<String, ArrayList<String>> getHomesList() {
		HashMap<String,ArrayList<String>> homesList = new HashMap<String,ArrayList<String>>();
		for(Home h: homes.values()){
			if(!homesList.containsKey(h.getServer().getName())){
				ArrayList<String>serverHomes = new ArrayList<String>();
				serverHomes.add(h.getName());
				homesList.put(h.getServer().getName(), serverHomes);
			}else{
				homesList.get(h.getServer()).add(h.getName());
			}
		}
		return homesList;
	}

	public void sendToServer(String targetName) {
		getProxiedPlayer().connect(ProxyServer.getInstance().getServerInfo(targetName));
	}

	public Server getServer() {
		return ProxyServer.getInstance().getPlayer(playername).getServer();
	}

	public boolean isIgnoring(String ignore) {
		return ignores.contains(ignore);
	}

	public ArrayList<String> getIgnores() {
		return ignores;
	}

	public boolean hasIgnores() {
		return !ignores.isEmpty();
	}

	public void setReplyPlayer(String name) {
		replyPlayer = name;
	}
}
