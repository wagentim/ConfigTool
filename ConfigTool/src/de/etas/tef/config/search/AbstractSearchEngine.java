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
import de.etas.tef.editor.message.MessageManager;

public abstract class AbstractSearchEngine implements ISearchEngine {
	
	protected final Display display;
	private List<Path> result = new ArrayList<Path>();
	protected final String[] pattern = { "ini" };
	
	public AbstractSearchEngine(final Display display) {
		this.display = display;
	}

	@Override
	public void search(Path directory, String searchWord) {
		
		Thread worker = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					clearResultList();
					doSearch(directory, searchWord);
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
	
	protected abstract void handleFile(Path file, BasicFileAttributes attrs, String searchWord);
	
	protected void searchFile(Path directory, String searchWord) throws IOException
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
					handleFile(file, attrs, searchWord);
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

	protected void doSearch(Path directory, String searchWord) throws IOException
	{
		Iterator<Path> it = Files.list(directory).iterator();
		
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
				searchFile(pth, searchWord);
			}
			else
			{
				handleFile(pth, Files.readAttributes(pth, BasicFileAttributes.class), searchWord);
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
	
	protected boolean findFile(Path path, String[] pattern)
	{
		String file = path.getFileName().toString();
		
		for(String pa : pattern)
		{
			if(file.endsWith(pa))
			{
				return true;
			}
		}
		
		return false;
	}
}
