package kganalysis.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import com.archimatetool.editor.ui.UIUtils;


public class SmellsView extends ViewPart implements ISmellsView {

	private SmellDetectionProvider smellProvider;
	private TreeViewer tv;


	public SmellsView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite treeComp = new Composite(parent, SWT.NULL);
		treeComp.setLayout(new GridLayout(1, false));
		
		tv = new TreeViewer(treeComp, SWT.NULL);
		tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		UIUtils.fixMacSiliconItemHeight(tv.getTree());
		
		smellProvider = new SmellDetectionProvider();
		tv.setContentProvider(smellProvider);
		tv.setInput("root");
	}

	@Override
	public void setFocus() {
		if (tv != null) {
			tv.getControl().setFocus();
		}
	}

}
