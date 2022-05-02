package kganalysis.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.help.hints.IHintsView;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IArchimateElement;

import kganalysis.IKGAnalysisImages;
import kganalysis.KGPlugin;
import kganalysis.db.SmellDetectionProvider;

public class SmellsView extends ViewPart implements ISmellsView {

    private SmellDetectionProvider provider;
    // private SmellsViewer viewer;
    private TreeViewer tv;

    private IArchimateModel model;

    private IAction detectAction;
    private IAction helpAction;

    public SmellsView() {

    }

    @Override
    public void createPartControl(Composite parent) {
	Composite treeComp = new Composite(parent, SWT.NULL);
	treeComp.setLayout(new GridLayout(1, false));
	tv = new TreeViewer(treeComp, SWT.NULL);
	tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

	//createActions();
	//createToolbar();
	//getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	//getSite().setSelectionProvider(tv);

	//PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
	SmellDetectionProvider provider = new SmellDetectionProvider();
	KGPlugin.INSTANCE.setSmellProvider(provider);
	tv.setContentProvider(provider);
	tv.setInput("root");
	/*
	 * viewer = new SmellsViewer(treeComp, SWT.NULL);
	 * 
	 * 
	 * 
	 * selectionChanged(getSite().getWorkbenchWindow().getPartService().
	 * getActivePart(),
	 * getSite().getWorkbenchWindow().getSelectionService().getSelection());
	 * 
	 * 
	 * 
	 */

	// detectSmells();
    }

   

    private void createToolbar() {
	IActionBars bars = getViewSite().getActionBars();
	IToolBarManager manager = bars.getToolBarManager();
	manager.add(detectAction);
    }

    @Override
    public void detectSmells() {
	BusyIndicator.showWhile(null, new Runnable() {
	    @Override
	    public void run() {
		provider = new SmellDetectionProvider();
		tv.setContentProvider(provider);
		tv.refresh();
	    }
	});
    }

    void selectObjects(IStructuredSelection selection) {
	if (selection != null) {
	    List<IDiagramModel> viewList = new ArrayList<IDiagramModel>();
	    for(Object o : selection.toArray()) {
		if (o instanceof IDiagramModel) {
		    viewList.add(((IDiagramModel) o).getDiagramModel());
		}
		    
	    }
	    
	    if(!viewList.isEmpty()) {
                for(IDiagramModel dm : viewList) {
                    IDiagramModelEditor editor = EditorManager.openDiagramEditor(dm, false);
                    if(editor != null) {
                        getSite().getShell().getDisplay().asyncExec(()-> { 
                            editor.selectObjects(viewList.toArray());
                        });
                    }
                }
            }
	}
    }

    @Override
    public void setFocus() {
	if (tv != null) {
	    tv.getControl().setFocus();
	}
    }

   

}
