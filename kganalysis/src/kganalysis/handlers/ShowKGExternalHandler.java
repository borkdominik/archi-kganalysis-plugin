package kganalysis.handlers;

import java.io.File;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.handlers.HandlerUtil;
import com.archimatetool.editor.Logger;
import kganalysis.KGPlugin;


public class ShowKGExternalHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			File file = new File(KGPlugin.KG_FOLDER, "index.html");
			IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
			IWebBrowser browser = support.getExternalBrowser();
			browser.openURL(file.toURI().toURL());
		} catch (Exception ex) {
			Logger.log(IStatus.ERROR, "Error opening external Browser", ex);
			MessageDialog.openError(HandlerUtil.getActiveShell(event), "Knowledge Graph Preview",
					(ex.getMessage() == null ? ex.toString() : ex.getMessage()));
		}

		return null;
	}

	@Override
	public boolean isEnabled() {
		return KGPlugin.getDefault().isGraphDbStarted();
	}
	
}
