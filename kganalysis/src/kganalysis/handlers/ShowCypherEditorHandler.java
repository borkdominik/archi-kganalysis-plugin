package kganalysis.handlers;

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
		return null;
	}
	
	
	@Override
	public boolean isEnabled() {
		return KGPlugin.getDefault().isGraphDbStarted();
	}
}
