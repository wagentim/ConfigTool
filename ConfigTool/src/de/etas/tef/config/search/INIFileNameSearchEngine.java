package de.etas.tef.config.search;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.eclipse.swt.widgets.Display;

import de.etas.tef.config.main.IConstants;

public class INIFileNameSearchEngine extends AbstractSearchEngine {

	public INIFileNameSearchEngine(Display display) {
		super(display);
	}

	@Override
	protected void handleFile(Path file, BasicFileAttributes attrs, String searchWord) {
		
		if(searchWord.trim().equalsIgnoreCase(IConstants.EMPTY_STRING))
		{
			if(findFile(file, pattern))
			{
				addResult(file);
			}
		}
		else
		{
			if(findFile(file, pattern) && file.getFileName().toString().toLowerCase().contains(searchWord.toLowerCase().trim()))
			{
				addResult(file);
			}
		}
		
	}

}
