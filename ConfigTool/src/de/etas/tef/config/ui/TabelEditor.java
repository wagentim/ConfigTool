package de.etas.tef.config.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import de.etas.tef.config.main.MainController;

public class TabelEditor extends AbstractComposite 
{
	
	private Table table;

	public TabelEditor(Composite parent, int style, MainController controller) {
		super(parent, style, controller);
		// TODO Auto-generated constructor stub
	}
	
	protected void initComposite() 
	{
		table = new Table(this, SWT.NONE);
	}

	@Override
	public void receivedAction(int type, Object content) {
		// TODO Auto-generated method stub

	}

}
