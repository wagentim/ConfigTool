package de.etas.tef.config.search;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.eclipse.swt.widgets.Display;

import de.etas.tef.config.main.Utils;

public class INIFileContentSearchEngine extends AbstractSearchEngine{
	
	

	public INIFileContentSearchEngine(Display display) {
		super(display);
	}

	@Override
	protected void handleFile(Path file, BasicFileAttributes attrs, String searchWord) {
			
		if (findFile(file, pattern)) {
			try {
				if (containContent(file, searchWord)) {
					
					addResult(file);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean containContent(Path path, String content) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile())));
        String line = null;
        while ((line = br.readLine()) != null) 
        {
        	if(line.startsWith("--") || line.startsWith(";"))
        	{
        		continue;
        	}
        	
        	if(line.trim().equalsIgnoreCase(content))
        	{
        		br.close();
        		return true;
        	}
        	
        	String sourceText = Utils.removeSpace(content);
        	String newLine = Utils.removeSpace(line);
        	
        	if(sourceText.equalsIgnoreCase(newLine))
        	{
        		br.close();
        		return true;
        	}
        }
        
        br.close();
		return false;
	}

}
