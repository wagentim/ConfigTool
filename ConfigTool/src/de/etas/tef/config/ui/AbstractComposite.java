package de.etas.tef.config.ui;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import de.etas.tef.config.listener.IMessageListener;
import de.etas.tef.config.main.MainController;
import de.etas.tef.editor.message.MessageManager;

public abstract class AbstractComposite extends Composite implements IMessageListener
{
	
	protected final MainController controller;
	protected final Composite parent;

	public AbstractComposite(Composite parent, int style, MainController controller)
	{
		super(parent, style);
		this.controller = controller;
		this.parent = parent;
		MessageManager.INSTANCE.addMessageListener(this);
		initComposite();
	}
	
	protected void initComposite() 
	{
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginBottom = layout.marginLeft = layout.marginRight = layout.marginHeight = layout.marginWidth = layout.horizontalSpacing = layout.verticalSpacing = 0;
		GridData gd = new GridData(GridData.FILL_BOTH);
		this.setLayout(layout);
		this.setLayoutData(gd);
		this.setBackground(controller.getColorFactory().getColorBackground());
	}

}
