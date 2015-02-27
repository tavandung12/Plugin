/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.tvd.gext.multipageeditor.pages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.part.PluginTransferData;

import com.tvd.cocos2dx.popup.creator.model.Cell;
import com.tvd.cocos2dx.popup.creator.model.ItemGroup;
import com.tvd.cocos2dx.popup.creator.model.Label;
import com.tvd.cocos2dx.popup.creator.model.Menu;
import com.tvd.cocos2dx.popup.creator.model.MenuItem;
import com.tvd.cocos2dx.popup.creator.model.Progressbar;
import com.tvd.cocos2dx.popup.creator.model.Sprite;
import com.tvd.cocos2dx.popup.creator.model.Table;
import com.tvd.cocos2dx.popup.creator.model.View;
import com.tvd.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tvd.gameview.ext.GameViewSdk;
import com.tvd.gext.multipageeditor.editors.constant.Img;
import com.tvd.gext.multipageeditor.elements.pages.LayoutCellLayout;
import com.tvd.gext.multipageeditor.elements.pages.LayoutElementPage;
import com.tvd.gext.multipageeditor.elements.pages.LayoutGroupPage;
import com.tvd.gext.multipageeditor.elements.pages.LayoutMenuItemPage;
import com.tvd.gext.multipageeditor.elements.pages.LayoutMenuPage;
import com.tvd.gext.multipageeditor.elements.pages.LayoutProgressbarPage;
import com.tvd.gext.multipageeditor.elements.pages.LayoutSpritePage;
import com.tvd.gext.multipageeditor.elements.pages.LayoutTablePage;
import com.tvd.gext.multipageeditor.elements.pages.LayoutViewPage;
/**
 *
 */
public class LayoutScrolledPropertiesBlock extends MasterDetailsBlock {
	
	public LayoutScrolledPropertiesBlock(LayoutDetailsPage page) {
		this.mFormPage = page;
	}
	/**
	 * @param id
	 * @param title
	 */
	class MasterContentProvider implements ITreeContentProvider {
		
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof LayoutEditorInput) {
				LayoutEditorInput input = (LayoutEditorInput) mFormPage
						.getEditor().getEditorInput();

				return new Object[] {
					input.getView(),	
				};
			}
			return new Object[0];
		}
		
		public void dispose() {
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, 
				Object newInput) {
		}

		@Override
		public Object[] getChildren(Object pParentElement) {
			if(pParentElement instanceof CommonObject) {
				return ((CommonObject)pParentElement).getAllGroups().toArray();
			}
			else if(pParentElement instanceof ItemGroup) {
				return ((ItemGroup)pParentElement).getItems().toArray();
			}
			
			return new Object[0];
		}

		@Override
		public Object getParent(Object pElement) {
			if(pElement instanceof View) {
				return null;
			}
			else if(pElement instanceof CommonObject) {
				return ((CommonObject)pElement).getParent();
			}
 			else if(pElement instanceof ItemGroup) {
 				return ((ItemGroup)pElement).getContainer();
 			}
			
			return null;
		}

		@Override
		public boolean hasChildren(Object pElement) {
			if(pElement instanceof CommonObject) {
				return ((CommonObject)pElement).hasChildren();
			}
			else if(pElement instanceof ItemGroup) {
				return ((ItemGroup)pElement).hasChildren();
			}
			return false;
		}
	}
	class MasterLabelProvider extends LabelProvider {
		
		@Override
		public Image getImage(Object obj) {
			if (obj instanceof CommonObject) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_TOOL_REDO);
			}
			if (obj instanceof ItemGroup) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_TOOL_REDO);
			}
			return null;
		}
		
		@Override
		public String getText(Object element) {
			if(element instanceof CommonObject) {
				CommonObject obj = ((CommonObject)element);
				return new StringBuilder()
						.append(obj.getXmlPositionName())
						.append(" (" + obj.getPositionString() + ")")
						.toString();
			}
			else if(element instanceof ItemGroup) {
				ItemGroup itemGroup = ((ItemGroup)element);
				return new StringBuilder()
						.append(itemGroup.toString())
						.append(" (" + itemGroup.numberOfItems() + " elements)")
						.toString();
			}
			
			return "unknown";
		}
		
		@Override
		public void dispose() {
			super.dispose();
		}
	}
	
	@Override
	protected void createMasterPart(final IManagedForm managedForm,
			final Composite parent) {
		//final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, 
				Section.DESCRIPTION | Section.TITLE_BAR);
		section.setText(text("name")); //$NON-NLS-1$
		section.setDescription(text("desc")); //$NON-NLS-1$
		section.marginWidth = 10;
		section.marginHeight = 5;
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);
		Tree tree = toolkit.createTree(client, SWT.NULL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 20;
		gd.widthHint = 100;
		tree.setLayoutData(gd);
		toolkit.paintBordersFor(client);
		Button b = toolkit.createButton(client, text("button.add"), 
				SWT.PUSH); //$NON-NLS-1$
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		b.setLayoutData(gd);
		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		
		TreeViewer treeViewer = new TreeViewer(tree);
		treeViewer.setContentProvider(new MasterContentProvider());
		treeViewer.setLabelProvider(new MasterLabelProvider());
		treeViewer.setInput(mFormPage.getEditor().getEditorInput());
		
		treeViewer.addDragSupport(DND.DROP_MOVE | DND.DROP_COPY, 
				new Transfer[]{LayoutPropertiesDragTransfer.getInstance(),
					PluginTransfer.getInstance()}, 
				new TreeViewDragListener(treeViewer));
		treeViewer.addDropSupport(DND.DROP_MOVE | DND.DROP_COPY, 
				new Transfer[]{LayoutPropertiesDragTransfer.getInstance(),
					PluginTransfer.getInstance()}, 
				new LayoutPropertiesDropListener(treeViewer, mFormPage));
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		
		treeViewer.expandAll();
		mTreeViewer = treeViewer;
	}
	
	protected void createToolBarActions(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		Action haction = new Action("hor", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.HORIZONTAL);
				form.reflow(true);
			}
		};
		haction.setChecked(true);
		haction.setToolTipText(text("horizontal")); //$NON-NLS-1$
		haction.setImageDescriptor(GameViewSdk.getDefault()
				.getImageRegistry()
				.getDescriptor(Img.LAYOUT_HORIZONTAL));
		Action vaction = new Action("ver", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		vaction.setChecked(false);
		vaction.setToolTipText(text("vertical")); //$NON-NLS-1$
		vaction.setImageDescriptor(GameViewSdk.getDefault()
				.getImageRegistry().getDescriptor(Img.LAYOUT_VERTICAL));
		form.getToolBarManager().add(haction);
		form.getToolBarManager().add(vaction);
	}
	
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(View.class, new LayoutViewPage(mFormPage));
		detailsPart.registerPage(ItemGroup.class, new LayoutGroupPage());
		detailsPart.registerPage(Sprite.class, new LayoutSpritePage(mFormPage));
		detailsPart.registerPage(Label.class, new LayoutSpritePage(mFormPage));
		detailsPart.registerPage(Menu.class, new LayoutMenuPage(mFormPage));
		detailsPart.registerPage(MenuItem.class, new LayoutMenuItemPage(mFormPage));
		detailsPart.registerPage(Table.class, new LayoutTablePage(mFormPage));
		detailsPart.registerPage(Cell.class, new LayoutCellLayout(mFormPage));
		detailsPart.registerPage(Progressbar.class, new LayoutProgressbarPage(mFormPage));
		detailsPart.registerPage(CommonObject.class, new LayoutElementPage(mFormPage));
	}
	
	protected String text(String key) {
		String className = getClass().getSimpleName();
		return Messages.getString(className + "." + key);
	}
	
	public void update() {
		if(mTreeViewer != null) {
			mTreeViewer.refresh();
			mTreeViewer.expandAll();
		}
	}
	
	private LayoutDetailsPage mFormPage;
	private TreeViewer mTreeViewer;
}

/**
 * Drag Listener
*/

class TreeViewDragListener implements DragSourceListener {
	
	public TreeViewDragListener(TreeViewer viewer) {
		this.mViewer = viewer;
	}

	@Override
	public void dragStart(DragSourceEvent pEvent) {
		pEvent.doit = !mViewer.getSelection().isEmpty();
		System.out.println("Start Drag doit = " + pEvent.doit);
	}

	@Override
	public void dragSetData(DragSourceEvent pEvent) {
		System.out.println("Drag set data");
		IStructuredSelection selection = 
				(IStructuredSelection)mViewer.getSelection();
		Object[] objects = selection.toList().toArray();
		if(LayoutPropertiesDragTransfer.getInstance().isSupportedType(pEvent.dataType)) {
			pEvent.data = objects;
		}
		else if(PluginTransfer.getInstance().isSupportedType(pEvent.dataType)) {
			byte[] data = LayoutPropertiesDragTransfer.getInstance().toByteArray(objects);
			pEvent.data = new PluginTransferData(
					LayoutPropertiesDropListener.class.getName(), 
					data);
		}
	}

	@Override
	public void dragFinished(DragSourceEvent pEvent) {
		System.out.println("Finshed Drag");
	}
	
	protected TreeViewer mViewer;
}
