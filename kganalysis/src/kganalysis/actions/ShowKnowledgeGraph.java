package kganalysis.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.actions.AbstractModelSelectionHandler;
import com.archimatetool.model.IArchimateModel;

import kganalysis.KnowledgeGraph;

public class ShowKnowledgeGraph extends AbstractModelSelectionHandler {
	
	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        
		IArchimateModel model = getActiveArchimateModel();
		
        if (model != null) {
            
        	try {
            	KnowledgeGraph knowledgeGraph = new KnowledgeGraph(model);
            	knowledgeGraph.exportToCSV();
            	knowledgeGraph.openBrowser();
            
            } catch (Exception ex) {
            	
            	Logger.log(IStatus.ERROR, "Error opening Knowledge Graph", ex); //$NON-NLS-1$
            	MessageDialog.openError(HandlerUtil.getActiveShell(event),
                        "Knowledge Graph Preview",
                        (ex.getMessage() == null ? ex.toString() : ex.getMessage()) );
            }
        }

        return null;
    }
}
