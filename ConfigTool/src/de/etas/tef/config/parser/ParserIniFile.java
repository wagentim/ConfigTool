package de.etas.tef.config.parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import de.etas.tef.config.entity.ConfigBlock;
import de.etas.tef.config.entity.ConfigFile;
import de.etas.tef.config.entity.KeyValuePair;
import de.etas.tef.config.log.IErroReason;
import de.etas.tef.config.log.LoggerHandler;
import de.etas.tef.config.main.IConstants;

public class ParserIniFile implements Runnable
{

	public ConfigFile read(Path filePath)
	{
		ConfigFile configFile = new ConfigFile();
		configFile.setFilePath(filePath);

		try
		{
			Charset charset = Charset.forName("ISO-8859-1");
			List<String> lines = Files.readAllLines(filePath, charset);

			ConfigBlock block = null;
			KeyValuePair pair = null;

			boolean fileStart = true;

			for (String s : lines)
			{
				s = s.trim();
				
				// comment for the whole config file
				if(fileStart)
				{
					if(s.isEmpty())
					{
						continue;
					}
					else if(isLineComment(s))
					{
						configFile.addComments(s);
					}
					else if(s.startsWith(IConstants.SYMBOL_LEFT_BRACKET))
					{
						fileStart = false;
						block = new ConfigBlock();
						block.setBlockName(extractBlockName(s));
						configFile.addConfigBlock(block);
					}
					else
					{
						LoggerHandler.INSTANCE().addIniFileFormatError(filePath, IErroReason.ERROR_INI_FILE_START, s);
					}
				}
				else
				{
					pair = new KeyValuePair();

					if (isLineComment(s))
					{
						pair.setKey(s);
						pair.setType(KeyValuePair.TYPE_COMMENT);
						block.addParameterInLast(pair);
					}
					else if (s.contains(IConstants.SYMBOL_EQUAL))
					{
						extractkeyValue(pair, s, filePath, block);
						block.addParameterInLast(pair);
					}
					else if (s.startsWith(IConstants.SYMBOL_LEFT_BRACKET))
					{
						block = new ConfigBlock();
						block.setBlockName(extractBlockName(s));
						configFile.addConfigBlock(block);
					}
					else
					{
						LoggerHandler.INSTANCE().addIniFileFormatError(filePath, block,
								IErroReason.ERROR_INI_FILE_PARAMETER, s);
					}
				}
			}
				
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return configFile;
	}
	
	protected boolean isLineComment(String line)
	{
		return line.startsWith("--") || line.startsWith(";") || line.startsWith("#") || line.isEmpty();
	}
	
	protected String extractBlockName(String line)
	{
		int index = line.indexOf(IConstants.SYMBOL_RIGHT_BRACKET);
		
		return line.subSequence(1, index).toString();
	}
	
	protected void extractkeyValue(KeyValuePair kvp, String line, Path filePath, ConfigBlock block)
	{
		StringTokenizer st = new StringTokenizer(line, IConstants.SYMBOL_EQUAL);
		
		if(st.countTokens() != 2)
		{
			LoggerHandler.INSTANCE().addIniFileFormatError(filePath, block, IErroReason.ERROR_INI_FILE_PARAMETER, line);
		}
		
		int index = 0;
		StringBuilder sb = new StringBuilder();
		
		while(st.hasMoreTokens())
		{
			String token = st.nextToken();
			
			if(index == 0)
			{
				kvp.setKey(token);
			}
			else if(index == 1)
			{
				sb.append(token);
			}
			else
			{
				sb.append(IConstants.SYMBOL_EQUAL);
				sb.append(token);
			}
			
			index++;
		}
		
		kvp.setValue(sb.toString());
		kvp.setType(KeyValuePair.TYPE_PARA);
	}
	
	protected void printConfigFile(ConfigFile cf)
	{
		if(cf == null)
		{
			System.out.println("Config File is NULL");
			return;
		}
		
		System.out.println("== " + cf.getFilePath().toFile().getAbsolutePath() + " ==");
		
		Iterator<ConfigBlock> it = cf.getConfigBlocks().iterator();
		while(it.hasNext())
		{
			ConfigBlock cb = it.next();
			System.out.println(IConstants.SYMBOL_LEFT_BRACKET + cb.getBlockName() + IConstants.SYMBOL_RIGHT_BRACKET);
			
			Iterator<KeyValuePair> itkvp = cb.getAllParameters().iterator();
			
			while(itkvp.hasNext())
			{
				KeyValuePair kvp = itkvp.next();
				
				int type = kvp.getType();
				
				switch(type)
				{
				case KeyValuePair.TYPE_COMMENT:
					System.out.println(kvp.getKey());
					break;
				case KeyValuePair.TYPE_PARA:
					System.out.println(kvp.getKey() + IConstants.SYMBOL_EQUAL + kvp.getValue());
					break;
				}
			}
			
//			System.out.println();
		}
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void run()
//	{
//		if (filePath == null || !Files.exists(filePath))
//		{
//			return;
//		}
//
//		read();
//	}
//
//	public static void main(String[] args)
//	{
//		String path = "D:\\test\\ES411\\ES411.ini";
//		
//		ParserIniFile load = new ParserIniFile();
//		load.printConfigFile(load.read(Paths.get(path)));
//	}
}
