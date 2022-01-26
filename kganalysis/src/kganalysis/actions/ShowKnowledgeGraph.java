package kganalysis.actions;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.browser.Browser;
import com.archimatetool.editor.browser.BrowserEditorInput;
import com.archimatetool.editor.browser.IBrowserEditor;
import com.archimatetool.editor.ui.services.EditorManager;
import kganalysis.KGAnalysisPlugin;
import kganalysis.db.KGDatabase;


public class ShowKnowledgeGraph extends AbstractHandler {
	
	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
		File file = new File(KGAnalysisPlugin.KG_FOLDER, "index.html");
    	BrowserEditorInput input = new BrowserEditorInput(file.getPath(), "Knowledge Graph");
    	IBrowserEditor editor = (IBrowserEditor)EditorManager.openEditor(input, IBrowserEditor.ID);
    	final Browser browser = editor.getBrowser();
    		
    	if(editor != null && browser != null) {
    		browser.refresh();
    		//TODO: addBrowserFunctions(browser);
    	}	

        return null;
    }
	
	@Override
    public boolean isEnabled() {
		KGDatabase db = KGAnalysisPlugin.INSTANCE.getKGDatabase();
		if(db == null || db.isStarted() == false) {
			return false;
		}
		return true;
    }
}
