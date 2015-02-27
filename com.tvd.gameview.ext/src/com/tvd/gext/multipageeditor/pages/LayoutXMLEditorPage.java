package com.tvd.gext.multipageeditor.pages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

import com.tvd.gext.multipageeditor.editors.LayoutMultiPageEditor;

public class LayoutXMLEditorPage extends StructuredTextEditor {
	
	public static LayoutXMLEditorPage create(LayoutMultiPageEditor editor) 
			throws PartInitException {
		LayoutXMLEditorPage pRet = new LayoutXMLEditorPage();
		pRet.init(editor);
		
		return pRet;
	}

	public boolean init(LayoutMultiPageEditor editor) 
			throws PartInitException {
		mIndex = editor.addPage(this, editor.getEditorInput());
		editor.setPageText(mIndex, getTitle());
		
		return true;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		
		LayoutEditorInput input = (LayoutEditorInput)getEditorInput();
		input.update();
	}
	
	public int getIndex() {
		return mIndex;
	}
	
	protected int mIndex;
}