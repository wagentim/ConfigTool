package de.etas.tef.config.search;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import de.etas.tef.config.main.IConstants;
import de.etas.tef.config.main.MainController;
import de.etas.tef.editor.message.MessageManager;

public class BasicSearchEngine 
{
	
	protected final Display display;
	private List<Path> result = new ArrayList<Path>();
	private final FileNameFilter fnf;
	private final FileContentFilter fcf;
	
	public BasicSearchEngine(final Display display, final MainController controller) {
		this.display = display;
		fnf = new FileNameFilter(controller);
		fcf = new FileContentFilter(controller);
	}

	public void search(SearchInfo si) {
		
		Thread worker = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					clearResultList();
					doSearch(si);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		worker.start();
	}
	
	protected void clearResultList() {
		result.clear();
	}
	
	protected void addResult(Path p)
	{
		result.add(p);
	}
	
	protected void searchFile(Path directory, SearchInfo si) throws IOException
	{
		Files.walkFileTree(directory, new FileVisitor<Path>()
		{
			
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
			{
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
			{
				if(!Files.isDirectory(file))
				{
					checkFile(file, si);
				}
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
			{
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
			{
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	private void checkFile(Path file, SearchInfo si)
	{
		boolean fileNameOK = fnf.check(file, si);
		
		if(!fileNameOK)
		{
			return;
		}
		
		boolean fileContentOK = fcf.check(file, si);
		
		if(fileNameOK && fileContentOK)
		{
			addResult(file);
		}
	}

	protected void doSearch(SearchInfo si) throws IOException
	{
		Iterator<Path> it = Files.list(si.getStartPath()).iterator();
		
		List<Path> allFiles = new ArrayList<Path>();
		
		while(it.hasNext())
		{
			allFiles.add(it.next());
		}
		
		showProgressbar();
		
		int counter = 1;
		int total = allFiles.size();
		
		for(Path pth : allFiles)
		{
			if(Files.isDirectory(pth))
			{
				searchFile(pth, si);
			}
			else
			{
				checkFile(pth, si);
			}
			
			double d = (double)counter / total;
			counter++;
			setProgressbar(d);
		}
		
		hideProgressbar();
		sendResult();
	}
	
	protected void showProgressbar()
	{
		display.asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				MessageManager.INSTANCE.sendMessage(IConstants.ACTION_PROGRESS_BAR_DISPLAY, true);
			}
		});
	}
	
	protected void hideProgressbar()
	{
		display.asyncExec(new Runnable()
		{
			
			@Override
			public void run()
			{
				MessageManager.INSTANCE.sendMessage(IConstants.ACTION_PROGRESS_BAR_DISPLAY, false);
			}
		});
	}
	
	protected void setProgressbar(double value)
	{
		display.asyncExec(new Runnable()
		{
			
			@Override
			public void run()
			{
				MessageManager.INSTANCE.sendMessage(IConstants.ACTION_PROGRESS_BAR_UPDATE, value);
			}
		});
	}
	
	protected void sendResult()
	{
		display.asyncExec(new Runnable()
		{
			
			@Override
			public void run()
			{
				MessageManager.INSTANCE.sendMessage(IConstants.ACTION_UPDATE_FILE_LIST, result);
			}
		});
	}
}
