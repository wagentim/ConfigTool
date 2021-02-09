package de.etas.tef.config.search;

import java.nio.file.Path;
import java.util.Iterator;

import de.etas.tef.config.entity.ConfigBlock;
import de.etas.tef.config.entity.ConfigFile;
import de.etas.tef.config.entity.KeyValuePair;
import de.etas.tef.config.main.MainController;
import de.etas.tef.config.main.Utils;

public class FileContentFilter extends AbstractFilter
{
	public FileContentFilter(MainController controller)
	{
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean check(Path file, SearchInfo si)
	{
		String section = si.getSectionName();
		String key = si.getKeyName();
		String value = si.getValueName();
		
		boolean isSectionOK = false;
		boolean isKeyOK = false;
		boolean isValueOK = false;
		
		// case 1: no filter defined
		if((section == null || section.isEmpty())
				&& (key == null || key.isEmpty())
				&& (value == null || value.isEmpty())
				)
		{
			return true;
		}
		
//		LoadIniFileV3 parser = new LoadIniFileV3(file);
//		parser.read();
//		ConfigFile cf = parser.getConfigFile();
		
		ConfigFile cf = controller.getINIFileParser().read(file);
		
		if(cf == null)
		{
			System.out.println(file.toFile().getAbsolutePath());
			return true;
		}
		
		Iterator<ConfigBlock> it = cf.getConfigBlocks().iterator();
		while(it.hasNext())
		{
			ConfigBlock cb = it.next();
			
			if(section == null || section.isEmpty())
			{
				isSectionOK = true;
			}
			else
			{
				isSectionOK = Utils.isContentSame(section, cb.getBlockName());
			}
			
			if(!isSectionOK)
			{
				continue;
			}
			
			Iterator<KeyValuePair> itp = cb.getAllParameters().iterator();
			
			while(itp.hasNext())
			{
				KeyValuePair kvp = itp.next();
				
				if(key == null || key.isEmpty())
				{
					isKeyOK = true;
				}
				else
				{
					isKeyOK = Utils.isContentSame(key, kvp.getKey());
				}
				
				if(value == null || value.isEmpty())
				{
					isValueOK = true;
				}
				else
				{
					isValueOK = Utils.isContentSame(value, kvp.getValue());
				}

				if(isSectionOK && isKeyOK && isValueOK)
				{
					return true;
				}
			}
		}
		
		return false;
	}
}
