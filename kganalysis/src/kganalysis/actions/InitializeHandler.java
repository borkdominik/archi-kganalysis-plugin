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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import com.archimatetool.csv.export.CSVExporter;
import com.archimatetool.editor.Logger;
import com.archimatetool.model.IArchimateModel;
import kganalysis.KGAnalysisPlugin;
import kganalysis.db.KGDatabase;

public class InitializeHandler extends AbstractHandler {

    private static final String CONFIRM_MESSAGE = "The process creates a new database and can take several minutes.\n"
	    + "Continue?";
    //private static final String INFO_MESSAGE = "Successfully initialized Knowledge Graph.\n Open Visualization?";

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	IWorkbenchPart part = HandlerUtil.getActivePart(event);
        IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;

	if (model == null) {
	    return null;
	}

	boolean result = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event), "Confirm", CONFIRM_MESSAGE);
	if (!result) {
	    // Cancel button pressed, return
	    return null;
	} else {
	    // OK pressed, continue and run initializiation progress async
	    
	    try {
		// TODO: Fix progress indicator (currently freezes UI until operation finished)
		PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {

		    @Override
		    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			try {
			    KGDatabase db = KGAnalysisPlugin.INSTANCE.getKGDatabase();
			    monitor.beginTask("Starting graph database and initializing Knowledge Graph...", IProgressMonitor.UNKNOWN);
			    // Create and start new neo4j database
			    db.createDb();

			    // Export model as CSV (reuse Archi CSVExporter) and copy files to user folder
			    CSVExporter csvExporter = new CSVExporter(model);
			    csvExporter.export(KGAnalysisPlugin.KG_FOLDER);
			    copyFiles();

			    // Transform ExportedArchiMate model to a Knowledge Graph
			    KGAnalysisPlugin.INSTANCE.getExporter().export(model);

			    /* TODO: Does not work anymore
			    boolean openBrowser = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event), "Confirm", INFO_MESSAGE);
			    if (!openBrowser) {
				return;
			    }
			    
			    IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench()
				    .getService(IHandlerService.class);
			    handlerService.executeCommand("kganalysis.command.showKGGraph", null);
			     */
			    monitor.done();
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
	File indexFile = new File(KGAnalysisPlugin.KG_FOLDER, "index.html");
	FileUtils.copyURLToFile(url, indexFile);
	url = new URL("platform:/plugin/kganalysis/files/smells.js");
	File smellsFile = new File(KGAnalysisPlugin.KG_FOLDER, "smells.js");
	FileUtils.copyURLToFile(url, smellsFile);
    }
    

}
