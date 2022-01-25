package kganalysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import com.archimatetool.csv.export.CSVExporter;
import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.browser.BrowserEditorInput;
import com.archimatetool.editor.browser.IBrowserEditor;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateModel;

import kganalysis.db.KGDatabase;

public class KnowledgeGraph {
	
	public static File KG_FOLDER = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "kg-analysis");
	
	private IProgressMonitor progressMonitor;
	
	private KGDatabase db;
	
	private IArchimateModel archiModel;
	
	static class CancelledException extends IOException {
        public CancelledException(String message) {
            super(message);
        }
    }
	
	public KnowledgeGraph(IArchimateModel model) {
		archiModel = model;
		KG_FOLDER.mkdirs();
	}

	
	public void openBrowser() throws Exception {
		
		
		Exception[] exception = new Exception[1];
		
		IRunnableWithProgress runnable = monitor -> {
			try {
				
				File file = createGraph(monitor);
				
				BrowserEditorInput input = new BrowserEditorInput(file.getPath(), "Knowledge Graph");
				IBrowserEditor editor = (IBrowserEditor)EditorManager.openEditor(input, IBrowserEditor.ID);
				final Browser browser = editor.getBrowser();
				
				if(editor != null && browser != null) {
					browser.refresh();
					addBrowserFunctions(browser);
                }
				
				// editor.getBrowser().execute("alert(\"JavaScript, called from Java\");");
				//editor.getBrowser().execute("document.getElementById('viz').style.width= " + browser.getSize().y + ";");

			} catch(Exception e) {
				exception[0] = e;
			}
		};
		
		try {
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
            dialog.run(false, true, runnable);
        } catch (Exception ex) {
            exception[0] = ex;
        }
		
		if(exception[0] instanceof CancelledException) {
            MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Cancelled", exception[0].getMessage());
        }
        else if(exception[0] != null) {
            throw exception[0];
        }
	}
	
	public File createGraph(IProgressMonitor monitor) throws IOException {
		progressMonitor = monitor;
		
		if(progressMonitor != null) {
            progressMonitor.beginTask("Creating Knowledge Graph", -1);
        }
		
		// 1) Copy HTML and CSS to /kg-analysis folder
		setProgressSubTask("Copying files", true);
		URL url = new URL("platform:/plugin/kganalysis/files/index.html");

		
		InputStream inputStream = url.openConnection().getInputStream();
		File indexFile = new File(KG_FOLDER, "index.html");
		copyInputStreamToFile(inputStream, indexFile);
		
		// 2) Export Archi Model as CSV to /kg-analysis folder
		setProgressSubTask("Preparing Archi Model", true);
		
		// TODO: use CSVExporter

		// 3) Start Neo4j Database	
		setProgressSubTask("Starting Graph Database", true);
		db = new KGDatabase();
		db.createDb();
		
		// 4) Import CSV Archi model to neo4j
		db.importCSVModel(KG_FOLDER);
		
		
		return indexFile;
	}
	
	
	public void exportToCSV() throws IOException {
		
		if (archiModel != null) {
			
			CSVExporter exporter = new CSVExporter(archiModel);
			exporter.export(KG_FOLDER);
			
		} else {
			throw new IOException("No Archi Model!");
		}
		
	}
	
	private void addBrowserFunctions(Browser browser) {
		new BrowserFunction(browser, "setDegree") {
            @Override
            public Object function(final Object[] arguments) {
            	db.setDegree();
            	return null;
            }
        };
        new BrowserFunction(browser, "setPageRank") {
            @Override
            public Object function(final Object[] arguments) {
            	db.setPageRank();
            	return null;
            }
        };
		/*
		new BrowserFunction(browser, "getAll") {
            @Override
            public Object function(final Object[] arguments) {
            	String query = "'MATCH (n)-[r]->(m) RETURN *'";
            	browser.execute("document.getElementById('cypher').value= " + query + ";");
            	browser.execute("document.getElementById('reload').click();");
            	return null;
            }
        };
        
        new BrowserFunction(browser, "cyclicDependency") {
            @Override
            public Object function(final Object[] arguments) {
            	String query = "'MATCH (a)-[r1]->(b)-[r2]->(c)-[]->(a) return a,b,c'";
            	browser.execute("document.getElementById('cypher').value= " + query + ";");
            	browser.execute("document.getElementById('reload').click();");
            	return null;
            }
        };
        */
	}
	

	private static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {

		try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[8192];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
	}
	
	private void updateProgress() throws CancelledException {
        if(progressMonitor != null && PlatformUI.isWorkbenchRunning() && Display.getCurrent() != null) {
            while(Display.getCurrent().readAndDispatch());
            
            if(progressMonitor.isCanceled()) {
                throw new CancelledException("Canceled");
            }
        }
    }
	
	private void setProgressSubTask(String task, boolean doUpdate) throws CancelledException {
        if(progressMonitor != null) {
            progressMonitor.subTask(task);
            if(doUpdate) {
                updateProgress();
            }
        }
    }


	

}
