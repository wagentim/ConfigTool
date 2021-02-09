package de.etas.tef.config.main;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import de.etas.tef.config.entity.ConfigBlock;
import de.etas.tef.config.entity.ConfigFile;
import de.etas.tef.config.entity.KeyValuePair;
import de.etas.tef.config.parser.ParserIniFile;
import de.etas.tef.config.search.BasicSearchEngine;
import de.etas.tef.config.search.SearchInfo;
import de.etas.tef.config.settings.SettingController;
import de.etas.tef.config.settings.SettingController.Setting;
import de.etas.tef.config.ui.MainScreen;

public class MainController
{

//	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	private final ImageFactory imageFactory;
	private final ColorFactory colorFactory;
	private final INIFileController iniFileController;
	private final SettingController settingController;
	private final Display display;
	private final ParserIniFile parser;
	
	private ConfigBlock copyBlock = null;
	private List<KeyValuePair> copyParameters = null;
	private boolean connected = false;
	
	private BasicSearchEngine searchEngine = null;
	
	public MainController(final Display display)
	{
		this.display = display;
		this.imageFactory = new ImageFactory(display);
		this.colorFactory = new ColorFactory(display);
		this.settingController = new SettingController();
		this.iniFileController = new INIFileController();
		this.searchEngine = new BasicSearchEngine(display, this);
		this.parser = new ParserIniFile();
		
		new MainScreen(display, this);
	}
	
	public ConfigFile parserINIFile(Path filePath)
	{
		ConfigFile cf = new ConfigFile();
		
		try
		{
			iniFileController.readFile(filePath.toString(), cf);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return cf;
	}
	
	public Setting getSetting()
	{
		return this.settingController.getSetting();
	}
	
	public ImageFactory getImageFactory()
	{
		return this.imageFactory;
	}
	
	public ColorFactory getColorFactory()
	{
		return this.colorFactory;
	}
	
	public void setConnected(boolean connected)
	{
		this.connected = connected;
	}
	
	public boolean isConnected()
	{
		return connected;
	}

	public void setCopyBlock(ConfigBlock selectedConfigBlock)
	{
		try
		{
			this.copyBlock = selectedConfigBlock.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
	}
	
	public ConfigBlock getCopyBlock()
	{
		ConfigBlock result = copyBlock;
		copyBlock = null;
		return result;
	}

	public void copyParameters(List<KeyValuePair> result)
	{
		this.copyParameters = result;
	}
	
	public List<KeyValuePair> getCopyParameters()
	{
		List<KeyValuePair> result = this.copyParameters;
		this.copyParameters = Collections.emptyList();
		return result;
	}

	public void replaceText(List<Path> currentList, String[] content) throws IOException 
	{
		if(currentList == null || currentList.isEmpty())
		{
			return;
		}
		
		Thread t = new Thread(new TextReplaceWorker(content, currentList, display));
		t.start();
	}

	public void search(SearchInfo si) 
	{
		searchEngine.search(si);
	}
	
	public ParserIniFile getINIFileParser()
	{
		return parser;
	}
}
