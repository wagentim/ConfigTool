package de.etas.tef.config.log;

import java.nio.file.Path;

import de.etas.tef.config.entity.ConfigBlock;

public final class LoggerHandler
{
	public static final LoggerHandler handler = new LoggerHandler();
	
	public static LoggerHandler INSTANCE()
	{
		return handler;
	}
	
	public void addModifyInfo(Path file, String oldValue, String newValue)
	{
		
	}
	
	public void error(String errorInfo)
	{
		
	}
	
	public void info(String info)
	{
		
	}
	
	public void addIniFileFormatError(Path file, String reason, String text)
	{
		System.out.println("INI Start: " + text);
	}
	
	public void addIniFileFormatError(Path file, ConfigBlock cb, String reason, String text)
	{
		System.out.println("INI Paras: " + text);
	}
}
