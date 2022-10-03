package kganalysis.handlers;

import java.io.File;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.browser.BrowserEditorInput;
import com.archimatetool.editor.browser.IBrowserEditor;
import com.archimatetool.editor.browser.IBrowserEditorInput;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.utils.PlatformUtils;

import kganalysis.KGPlugin;


public class ShowKGHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (PlatformUtils.isWindows() && !ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.EDGE_BROWSER)) {
			MessageDialog.openError(HandlerUtil.getActiveShell(event), "Error opening Visualization", "Please enable the 'Use MS Edge' option in the Preferences to open the Knowledge Graph Visualization");
			return null;
		}
		
		File file = new File(KGPlugin.KG_FOLDER, "index.html");
		IBrowserEditorInput input = new BrowserEditorInput("file:///" + file.getPath(), "Knowledge Graph");
		IBrowserEditor editor = (IBrowserEditor) EditorManager.openEditor(input, IBrowserEditor.ID);

		if (editor != null && editor.getBrowser() != null) {
			editor.getBrowser().refresh();
		}

		return null;
	}

	@Override
	public boolean isEnabled() {
		return KGPlugin.getDefault().isGraphDbStarted();
	}
}
