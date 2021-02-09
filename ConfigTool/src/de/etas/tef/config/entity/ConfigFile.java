package de.etas.tef.config.entity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ConfigFile
{
	private Path filePath = null;
	private List<ConfigBlock> configBlocks = Collections.emptyList();
	private List<String> comments = Collections.emptyList();
	
	public ConfigFile()
	{
		configBlocks = new ArrayList<ConfigBlock>();
	}
	
	public Path getFilePath()
	{
		return filePath;
	}
	public void setFilePath(Path filePath)
	{
		this.filePath = filePath;
	}
	public List<ConfigBlock> getConfigBlocks()
	{
		return configBlocks;
	}
	public void setConfigBlocks(List<ConfigBlock> configBlocks)
	{
		this.configBlocks = configBlocks;
	}
	public List<String> getComments()
	{
		return comments;
	}
	public void addComments(String comment)
	{
		if(comments == null)
		{
			comments = new ArrayList<String>();
		}
		
		comments.add(comment);
	}
	
	public void addConfigBlock(ConfigBlock cb)
	{
		if( null == cb )
		{
			return;
		}
		
		getConfigBlocks().add(cb);
	}
	
	public ConfigFile clone() throws CloneNotSupportedException
	{
		ConfigFile cf = new ConfigFile();
		
		cf.setFilePath(this.getFilePath());
		
		List<ConfigBlock> newList = new ArrayList<ConfigBlock>();
		List<String> commentList = new ArrayList<String>();
		
		Iterator<ConfigBlock> itcb = configBlocks.iterator();
		
		while(itcb.hasNext())
		{
			newList.add(itcb.next().clone());
		}
		
		if(comments != null)
		{
			Iterator<String> itc = comments.iterator();
			
			while(itc.hasNext())
			{
				commentList.add(itc.next());
			}
		}
		
		return cf;
	}
	
}
