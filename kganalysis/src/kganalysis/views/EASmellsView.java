package kganalysis.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimateModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class EASmellsView extends ViewPart {
	
	public static String ID = "kganalysis.eaSmellsView"; //$NON-NLS-1$

	private KGAnalysisViewer fViewer;
	
	private IArchimateModel fModel;
	
	// Actions
	private IAction fActionDetectSmells;
    
	private Label label;
	
	
	public EASmellsView() {
    	
    }
	
	@Override
    public void createPartControl(Composite parent) {	
        Composite treeComp = new Composite(parent, SWT.NULL);
        treeComp.setLayout(new TreeColumnLayout());
        treeComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // TODO: Add JFace Viewer
        fViewer = new KGAnalysisViewer(treeComp, SWT.NULL);
        
        makeLocalActions();
        registerGlobalActions();
        makeLocalToolbar();
        
        // TODO: ContextMenu
	}

	@Override
	public void setFocus() {
		if(fViewer != null) {
            fViewer.getControl().setFocus();
        }	
	}
	
	private void makeLocalActions() {
		// TODO: Detect EA Smells Action
		
		fActionDetectSmells = new Action("Detect Smells") {
            
			@Override
            public void run() {
                detectSmells();
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
            	// Archi Validate icon
            	return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_DIAGRAM);
            }
        };
        fActionDetectSmells.setEnabled(false);
	}
	
	/**
	 * Global Action Handles
	 */
	private void registerGlobalActions() {
        // No global actions
    }
	
	private void makeLocalToolbar() {
		// TODO: Populate toolbar once local actions are set
	}
	
    public void detectSmells() {
        //TODO: Detect EA Smells Action
    }
    
    public KGAnalysisViewer getViewer() {
        return fViewer;
    }
	
	

}
