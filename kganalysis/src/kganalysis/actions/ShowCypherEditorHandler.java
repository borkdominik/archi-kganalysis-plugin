package kganalysis.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import com.archimatetool.editor.ui.services.ViewManager;
import kganalysis.KGPlugin;
import kganalysis.views.ICypherEditorView;

public class ShowCypherEditorHandler extends AbstractHandler {
	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ViewManager.toggleViewPart(ICypherEditorView.ID, false);
		/*
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(CypherEditorView.ID);
		} catch (Exception e) {
			Logger.log(IStatus.ERROR, "Could not open Cypher Editor View", e);
			MessageDialog.openError(HandlerUtil.getActiveShell(event), 
					"Error",
					"Could not open Cypher Editor View. Exception: "  + e.getMessage() != null 
						? e.getMessage()
						: e.toString());
			e.printStackTrace();
		}
		*/
		return null;
	}
	
	
	@Override
	public boolean isEnabled() {
		return KGPlugin.getDefault().isGraphDbStarted();
	}
}
