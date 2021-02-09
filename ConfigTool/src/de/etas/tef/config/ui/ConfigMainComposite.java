package de.etas.tef.config.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import de.etas.tef.config.main.IConstants;
import de.etas.tef.config.main.MainController;

public class ConfigMainComposite extends AbstractComposite
{
	private SashForm sfAbove;
	private SashForm svBelow;
	
	public ConfigMainComposite(Composite composite, int style, MainController controller)
	{
		super(composite, style, controller);
	}
	
	protected void initComposite()
	{
		// setting for the composite
		super.initComposite();
		
//		new FunctionComposite(this, SWT.NONE, controller);
		svBelow = new SashForm(this, SWT.VERTICAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		svBelow.setLayoutData(gd);
		
		sfAbove = new SashForm(svBelow, SWT.HORIZONTAL);
		gd = new GridData(GridData.FILL_BOTH);
		sfAbove.setLayoutData(gd);
		
		new ConfigBlockTree(sfAbove, SWT.NONE, controller);
		new ConfigTextEditor(sfAbove, SWT.NONE, controller);
		
		sfAbove.setWeights(new int[]{1,2});
		
		new CommentComposite(svBelow, SWT.NONE, controller);
		svBelow.setWeights(new int[]{1,0});		
		
		initDropFunction(this);
	}
	
	protected void initDropFunction(Composite composite)
	{
		DropTarget dt = new DropTarget(composite, DND.DROP_DEFAULT | DND.DROP_MOVE);
		dt.setTransfer(new Transfer[]{ FileTransfer.getInstance() });
		dt.addDropListener(new DropTargetAdapter()
		{
			public void drop(DropTargetEvent event)
			{
				String fileList[] = null;
				
				FileTransfer ft = FileTransfer.getInstance();
				
				if (ft.isSupportedType(event.currentDataType))
				{
					fileList = (String[]) event.data;
				}
				else
				{
					return;
				}
				
				String filePath = fileList[0];
				
				if(!filePath.toLowerCase().trim().endsWith(".ini"))
				{
					return;
				}
				
			}
		});
	}

	@Override
	public void receivedAction(int type, Object content)
	{
		if(type == IConstants.ACTION_OPEN_INI_FILE)
		{
			if(content == null)
			{
				return;
			}
			
//			Path filePath = (Path)content;
//			
//			Thread t = new Thread(new LoadIniFileV2(filePath, this.getDisplay()));
//			t.start();
		}
	}
}
