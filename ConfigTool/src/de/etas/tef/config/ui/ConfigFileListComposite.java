package de.etas.tef.config.ui;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.etas.tef.config.listener.FileListMouseListener;
import de.etas.tef.config.listener.FileListTableKeyListener;
import de.etas.tef.config.listener.OpenFileSelectionListener;
import de.etas.tef.config.listener.OpenLocationSelectionListener;
import de.etas.tef.config.main.IConstants;
import de.etas.tef.config.main.IImageConstants;
import de.etas.tef.config.main.MainController;
import de.etas.tef.editor.message.MessageManager;

public class ConfigFileListComposite extends AbstractComposite {

	private Table configFileList;

	public ConfigFileListComposite(Composite parent, int style, MainController controller) {
		super(parent, style, controller);
	}

	protected void initComposite() {
		super.initComposite();

//		new ConfigFileListToolbar(this, SWT.NONE, controller);

		configFileList = new Table(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		configFileList.setLayoutData(gd);
		configFileList.setHeaderVisible(false);
		configFileList.setLinesVisible(false);
		new TableColumn(configFileList, SWT.NONE);
		new TableColumn(configFileList, SWT.NONE);
		configFileList.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent event) {
				adjustColumnSize();
			}

		});

		configFileList.addKeyListener(new FileListTableKeyListener(configFileList));

		configFileList.addMouseListener(new FileListMouseListener());

		createRightMenu();
	}

	protected Menu createRightMenu() {
		Menu rightClickMenu = new Menu(configFileList);
		configFileList.setMenu(rightClickMenu);

		rightClickMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				MenuItem[] items = rightClickMenu.getItems();

				for (int i = 0; i < items.length; i++) {
					items[i].dispose();
				}

				TableItem[] selections = configFileList.getSelection();
				
				if(selections == null || selections.length < 1)
				{
					return;
				}
				
				TableItem item = selections[0];

				if (null == item) {
					return;
				}

				MenuItem openFileLocation = new MenuItem(rightClickMenu, SWT.NONE);
				openFileLocation.setText("Open File Location");
				openFileLocation.addSelectionListener(
						new OpenLocationSelectionListener((Path) item.getData()));

				MenuItem openFile = new MenuItem(rightClickMenu, SWT.NONE);
				openFile.setText("Open File");
				openFile.addSelectionListener(new OpenFileSelectionListener((Path) item.getData()));

				MenuItem deleteItem = new MenuItem(rightClickMenu, SWT.NONE);
				deleteItem.setText("Delete Item");
				deleteItem.addSelectionListener(new DeleteItemSelectionListener());
			}
		});

		return rightClickMenu;
	}

	class DeleteItemSelectionListener implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {

		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			TableItem item = configFileList.getSelection()[0];

			if (null == item) {
				return;
			}

			configFileList.remove(configFileList.getSelectionIndex());
		}

	}

	private void updateList(java.util.List<Path> files) {
		configFileList.removeAll();

		if (files == null || files.isEmpty()) {
			return;
		}

		configFileList.setData(files);

		for (Path p : files) {
			TableItem ti = new TableItem(configFileList, SWT.NONE);
			ti.setImage(controller.getImageFactory().getImage(IImageConstants.IMAGE_FILE));
			ti.setText(0, getDisplayString(p));
			ti.setText(1, IConstants.DATE_TIME_FORMAT.format(new Date(p.toFile().lastModified())));
			ti.setData(p);

			int bg = configFileList.getItemCount() % 2;
			if(bg == 1)
			{
				ti.setBackground(controller.getColorFactory().getColorLightBlue());
			}
		}

		adjustColumnSize();
		MessageManager.INSTANCE.sendMessage(IConstants.ACTION_UPDATE_FILE_LIST_NUM, files);
	}

	private String getDisplayString(Path p) {

		return p.toString();
	}

	private void adjustColumnSize() {
		Rectangle rect = configFileList.getClientArea();
		configFileList.getColumn(0).setWidth((int)(rect.width*0.7));
		configFileList.getColumn(1).setWidth((int)(rect.width*0.3));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void receivedAction(int type, Object content) {
		if (type == IConstants.ACTION_SELECTED_SEARCH_PATH) {
			

		} else if (type == IConstants.ACTION_UPDATE_FILE_LIST) {
			List<Path> list = (List<Path>)content;
			updateList(list);
			
		} else if (type == IConstants.ACTION_SEARCH_CONTENT) {
		} else if (type == IConstants.ACTION_REPLACE_TEXT) {
		}
	}

}
