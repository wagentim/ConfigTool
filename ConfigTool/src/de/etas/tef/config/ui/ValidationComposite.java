package de.etas.tef.config.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.etas.tef.config.main.IConstants;
import de.etas.tef.config.main.MainController;

public class ValidationComposite extends AbstractComposite
{
	private Button checkDuplicatedSection;

	public ValidationComposite(Composite parent, int style, MainController controller)
	{
		super(parent, style, controller);
	}
	
	protected void initComposite() 
	{
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginBottom = layout.marginLeft = layout.marginRight = layout.marginHeight = layout.marginWidth = 5;
		GridData gd = new GridData(GridData.FILL_VERTICAL);
		this.setLayout(layout);
		gd.widthHint = 200;
		this.setLayoutData(gd);
		this.setBackground(controller.getColorFactory().getColorBackground());
		
		checkDuplicatedSection = new Button(this, SWT.CHECK);
		checkDuplicatedSection.setText(IConstants.TXT_DUPLICATED_SECTION);
		checkDuplicatedSection.setBackground(controller.getColorFactory().getColorBackground());
	}


	@Override
	public void receivedAction(int type, Object content)
	{
		// TODO Auto-generated method stub

	}

}
