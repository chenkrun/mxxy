package com.mxxy.game.resources;

public interface Item{

	public abstract String getId();

	public abstract String getName();

	public abstract String getType();

	public abstract String getDescription();

	public abstract short getLevel();

	public abstract long getPrice();

}