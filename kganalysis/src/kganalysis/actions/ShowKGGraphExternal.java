package kganalysis.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import com.archimatetool.editor.actions.AbstractModelSelectionHandler;
import com.archimatetool.model.IArchimateModel;

import kganalysis.GraphExternal;

public class ShowKGGraphExternal extends AbstractModelSelectionHandler {
	
	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            try {
                GraphExternal exporter = new GraphExternal(model);
                exporter.showExternalBrowser();
            }
            catch(Exception ex) {
                MessageDialog.openError(workbenchWindow.getShell(),
                        "Knowledge Graph Preview",
                        (ex.getMessage() == null ? ex.toString() : ex.getMessage()) );
            }
        }

        return null;
    }
	
	
}
