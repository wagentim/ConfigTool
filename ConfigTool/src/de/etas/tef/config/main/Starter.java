package de.etas.tef.config.main;

import org.eclipse.swt.widgets.Display;

/**
 * Handle start process
 * 
 * @author UIH9FE
 *
 */
public final class Starter
{
	
	public static void main(String[] args)
	{
		Display display = new Display();
		new MainController(display);
	}
}
