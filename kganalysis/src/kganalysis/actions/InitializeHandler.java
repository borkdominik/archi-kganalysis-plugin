package kganalysis.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.csv.export.CSVExporter;
import com.archimatetool.editor.Logger;
import com.archimatetool.model.IArchimateModel;
import kganalysis.KGPlugin;
import kganalysis.db.KGDatabase;


/**
 * NOT USED!!!
 * Old handler to create a new Knowledge Graph, new one { @see CreateGraphHandler }
 */
public class InitializeHandler extends AbstractHandler {
	
	private static final String CONFIRM_TITLE = "Confirm";
	private static final String CONFIRM_MESSAGE = "The process creates a new database and can take several minutes.\n"
			+ "Continue?";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// get current ArchiMate model
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;
		
		if (model == null) {
			return null;
		}
		
		boolean result = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event), CONFIRM_TITLE, CONFIRM_MESSAGE);
		
		// Cancel button pressed -> return
		if (!result) {
			return null;
		// OK pressed -> run initialization progress
		} else {
			IStatusLineManager manager = null;
			if (part.getSite() instanceof IViewSite) {
	            manager = ((IViewSite) (part.getSite())).getActionBars().getStatusLineManager();
	        } else {
	            manager = ((IEditorSite) (part.getSite())).getActionBars().getStatusLineManager();
	        }
			manager.setMessage("Starting Graph Database...");
			
			try {
				PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {

					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

						try {
							KGDatabase db = KGPlugin.INSTANCE.getKGDatabase();
							monitor.beginTask("Starting graph database and initializing Knowledge Graph...", IProgressMonitor.UNKNOWN);
							
							// Create and start new neo4j database
							db.createDb();

							// Export model as CSV (reuse Archi CSVExporter) and copy files to user folder
							CSVExporter csvExporter = new CSVExporter(model);
							csvExporter.export(KGPlugin.KG_FOLDER);
							copyFiles();

							// Transform ExportedArchiMate model to a Knowledge Graph
							KGPlugin.INSTANCE.getExporter().export(model);
							monitor.done();
							
							/* TODO: Open Browser when initialization is finished (commented code below does not work anymore)
							boolean openBrowser = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event), "Confirm", "Open Knowledge Graph?");
							if (!openBrowser) { 
								return; 
							}
							IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
							handlerService.executeCommand("kganalysis.command.showKGGraph", null);
							*/
						} catch (Exception ex) {
							Logger.log(IStatus.ERROR, "Error - Exception in initialization thread", ex);
							ex.printStackTrace();
						}
					}
				});
			} catch (Exception ex) {
				Logger.log(IStatus.ERROR, "Error - Failed to initialize Knowledge Graph", ex);
				MessageDialog.openError(HandlerUtil.getActiveShell(event), "ERROR - KGAnalysis Plugin",
						"Knowledge Graph Initialization failed. \n Exception: " + ex.getMessage() != null
								? ex.getMessage()
								: ex.toString());
			}
		}

		return null;

	}

	private void copyFiles() throws IOException {
		URL url = new URL("platform:/plugin/kganalysis/files/index.html");
		File indexFile = new File(KGPlugin.KG_FOLDER, "index.html");
		FileUtils.copyURLToFile(url, indexFile);
		url = new URL("platform:/plugin/kganalysis/files/smells.js");
		File smellsFile = new File(KGPlugin.KG_FOLDER, "smells.js");
		FileUtils.copyURLToFile(url, smellsFile);
		url = new URL("platform:/plugin/kganalysis/files/index.js");
		File jsFile = new File(KGPlugin.KG_FOLDER, "index.js");
		FileUtils.copyURLToFile(url, jsFile);
		url = new URL("platform:/plugin/kganalysis/files/styles.css");
		File cssFile = new File(KGPlugin.KG_FOLDER, "styles.css");
		FileUtils.copyURLToFile(url, cssFile);
	}

}
