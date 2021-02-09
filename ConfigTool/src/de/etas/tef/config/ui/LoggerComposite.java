package de.etas.tef.config.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import de.etas.tef.config.main.MainController;

public class LoggerComposite extends AbstractComposite
{
	
	private StyledText st;

	public LoggerComposite(Composite parent, int style, MainController controller)
	{
		super(parent, style, controller);
	}
	
	protected void initComposite() 
	{
		super.initComposite();
		st = new StyledText(this, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		st.setLayoutData(gd);
		st.setEditable(false);
	}

	@Override
	public void receivedAction(int type, Object content)
	{

	}

}
