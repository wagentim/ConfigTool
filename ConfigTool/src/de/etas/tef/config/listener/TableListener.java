package de.etas.tef.config.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import de.etas.tef.config.entity.CellIndex;
import de.etas.tef.config.main.IConstants;
import de.etas.tef.config.main.MainController;

public class TableListener extends CellEditingListener
{

	private CellIndex cell = null;
	private final MainController controller;
	
	public TableListener(Table table, MainController controller)
	{
		super(table, controller);
		this.controller = controller;
	}
	
	private CellIndex getDoubleClickPosIndex(Point pt)
	{
		int columnCount = getTable().getColumnCount();
		int rowCount = getTable().getItemCount();

		for( int j = 0; j < columnCount; j++)
		{
			for (int i = 0; i < rowCount; i++)
			{
				Rectangle rect = getTable().getItem(i).getBounds(j);
				if (rect.contains(pt))
				{
					return new CellIndex(i, j);
				}
			}
		}
		return null;
	}
	
	private Table getTable()
	{
		return (Table)getComposite();
	}

	@Override
	protected ControlEditor getNewEditor()
	{
		TableEditor editor = new TableEditor(getTable());
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	    editor.minimumWidth = 50;
		return editor;
	}

	@Override
	protected void updateWithNewValue()
	{
	}
	
	protected void disposeEditor()
	{
		super.disposeEditor();
		this.cell = null;
	}

	@Override
	protected Item getSelectedItem(TypedEvent event)
	{
		if(event instanceof MouseEvent)
		{
			MouseEvent mevent =(MouseEvent)event;
			Point pt = new Point(mevent.x, mevent.y);
	        cell = getDoubleClickPosIndex(pt);
	        
	        if( null == cell )
	        {
	        	return null;
	        }
	        
	        final TableItem item = getTable().getItem(cell.getRow());
	        
	        if (item == null)
	        {
	          return null;
	        }
			return item;
		}
		else if(event instanceof KeyEvent)
		{
			int row = getTable().getSelectionIndices()[0];
			
			cell = new CellIndex(row, 1);
			return getTable().getItem(row);
		}
		
		return null;
	}

	@Override
	protected void setNewEditor(Text newEditor, Item item)
	{
		((TableEditor)editor).setEditor(newEditor, (TableItem)item, cell.getColumn());
	}

	@Override
	protected void setNewValueByModify()
	{
		((TableEditor)editor).getItem().setText(cell.getColumn(), newValue);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void widgetSelected(SelectionEvent event)
	{
		if(event.getSource() instanceof MenuItem)
		{
			String text = ((MenuItem)event.getSource()).getText();
			
			if( text.contentEquals(IConstants.TXT_BTN_ADD) )
			{
			}
			else if( text.contentEquals(IConstants.TXT_BTN_DELETE) )
			{
			}
			else if( text.contentEquals(IConstants.TXT_COPY) )
			{
				sendCopyMessage();
			}
			else if( text.contentEquals(IConstants.TXT_PASTE) )
			{
				sendPasteMessage();
			}
		}
		
	}
	
	private void sendCopyMessage()
	{
	}
	
	private void sendPasteMessage()
	{
	}

	@Override
	protected CellIndex getCell()
	{
		return cell;
	}

	@Override
	protected void keyCopyPressed()
	{
		sendCopyMessage();
	}

	@Override
	protected void keyPastePressed()
	{
		sendPasteMessage();
	}

	@Override
	public void receivedAction(int type, Object content)
	{
		// TODO Auto-generated method stub
		
	}

}
