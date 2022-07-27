package kganalysis.handlers;

import java.io.File;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.browser.Browser;
import com.archimatetool.editor.browser.BrowserEditorInput;
import com.archimatetool.editor.browser.IBrowserEditor;
import com.archimatetool.editor.ui.services.EditorManager;
import kganalysis.KGPlugin;


public class ShowKGHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		File file = new File(KGPlugin.KG_FOLDER, "index.html");
		BrowserEditorInput input = new BrowserEditorInput(file.getPath(), "Knowledge Graph");
		IBrowserEditor editor = (IBrowserEditor) EditorManager.openEditor(input, IBrowserEditor.ID);
		final Browser browser = editor.getBrowser();

		if (editor != null && browser != null) {
			browser.refresh();
		}

		return null;
	}

	@Override
	public boolean isEnabled() {
		return KGPlugin.getDefault().isGraphDbStarted();
	}
}
