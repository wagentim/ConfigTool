package de.etas.tef.config.search;

import de.etas.tef.config.main.MainController;

public abstract class AbstractFilter implements IFilter
{
	protected final MainController controller;
	
	public AbstractFilter(final MainController controller)
	{
		this.controller = controller;
	}
}
