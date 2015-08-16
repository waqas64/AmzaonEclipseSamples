package a2e_autoseller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.FillLayout;

import swing2swt.layout.BorderLayout;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import com.ECS.client.jax.ItemLookupResponse;
import com.a2e.core.AmazonClient;
import com.a2e.core.DataExtractor;
import com.a2e.core.PropertiesReader;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Group;

public class SearchItem extends ViewPart {

	public static final String ID = "A2E_AutoSeller.viewd"; //$NON-NLS-1$
	private Text txt_ItemID;
	private Text txt_RawXML;
	private Browser browser;
	private Browser browser_images;
	DataExtractor ext;
	Tree tree;
	private Text txt_details;

	public SearchItem() {

	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new BorderLayout(0, 0));

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.NORTH);
		composite_1.setLayout(new GridLayout(5, false));

		Label lblEnterItemNumber = new Label(composite_1, SWT.NONE);
		lblEnterItemNumber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblEnterItemNumber.setText("Enter Item ID");

		txt_ItemID = new Text(composite_1, SWT.BORDER);
		txt_ItemID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Button btn_GetData = new Button(composite_1, SWT.NONE);
		btn_GetData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				{
					ext = new DataExtractor();
					ext.setItem(txt_ItemID.getText());
					txt_RawXML.setText(ext.getRawXML());
					txt_RawXML.update();
					if (ext.getURL() != null) {
						browser.setUrl(ext.getURL());
						
					}
if(ext.getItemDetails()!=null)
{
	txt_details.setText(ext.getItemDetails());}
					
					java.util.List<String> imgURLList = ext.getImages();
					tree.removeAll();
					int counter = 0;

					for (String str : imgURLList) {
						TreeItem trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
						trtmNewTreeitem.setText("Image_" + counter);
						trtmNewTreeitem.setData(str);

						counter++;
					}

				}
			}
		});
		btn_GetData.setBounds(0, 0, 75, 25);
		btn_GetData.setText("Get Data");
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);

		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayoutData(BorderLayout.CENTER);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));

		CTabFolder tabFolder = new CTabFolder(composite_2, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tbtmItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmItem.setText("Item Page");

		browser = new Browser(tabFolder, SWT.NONE);
		tbtmItem.setControl(browser);

		CTabItem tbtmRawXml = new CTabItem(tabFolder, SWT.NONE);
		tbtmRawXml.setText("Raw XML");

		txt_RawXML = new Text(tabFolder, SWT.BORDER | SWT.READ_ONLY
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		tbtmRawXml.setControl(txt_RawXML);

		tabFolder.setSelection(0);

		CTabItem tbtmImages = new CTabItem(tabFolder, SWT.NONE);
		tbtmImages.setText("Images");

		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmImages.setControl(composite_3);
		composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setLayout(new FillLayout(SWT.VERTICAL));

		CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(
				composite_4, SWT.BORDER);
		tree = checkboxTreeViewer.getTree();
		// SelectionListener new SelectionListener();
		tree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (tree.getSelection().length > 0) {
					String s = (String) tree.getSelection()[0].getData();
					browser_images.setUrl(s);

				}
			}
		});
		TreeItem trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem.setText("New TreeItem");

		Group group = new Group(composite_4, SWT.NONE);

		Button btnDownload = new Button(group, SWT.NONE);
		btnDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getItems();

				for (int i = 0; i < items.length; i++) {
					TreeItem item = items[i];
					if (item.getChecked()) {
						String s = (String) item.getData();
						// Image image = null;
						try {
							URL url = new URL(s);
							// image = ImageIO.read(url);
							BufferedImage bi = ImageIO.read(url);
							File outputfile = new File(
									"E:\\Development\\amazon\\" + ext.getASIN()
											+ "\\" + item.getText() + ".JPG");
							outputfile.getParentFile().mkdirs();
							ImageIO.write(bi, "JPG", outputfile);
						} catch (IOException e1) {
						}
					}
				}
			}
		});
		btnDownload.setBounds(10, 10, 75, 25);
		btnDownload.setText("Download");

		browser_images = new Browser(composite_3, SWT.NONE);

		CTabItem tbtmDetails = new CTabItem(tabFolder, SWT.NONE);
		tbtmDetails.setText("Details");

		Composite composite_5 = new Composite(tabFolder, SWT.NONE);
		tbtmDetails.setControl(composite_5);
		composite_5.setLayout(new BorderLayout(0, 0));

		txt_details = new Text(composite_5, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txt_details.setLayoutData(BorderLayout.CENTER);

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
}
