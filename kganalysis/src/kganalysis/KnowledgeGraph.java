package kganalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import com.archimatetool.csv.export.CSVExporter;
import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.browser.BrowserEditorInput;
import com.archimatetool.editor.browser.IBrowserEditor;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.model.IArchimateModel;

import kganalysis.db.EmbeddedNeo4j;

public class KnowledgeGraph {
	
	public static File KG_FOLDER = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "kg-analysis");
	
	private IProgressMonitor progressMonitor;
	
	private EmbeddedNeo4j db;
	
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
				/*
				//progressMonitor = monitor;
				progressMonitor.beginTask("Creating Knowledge Graph", 100);
				
				
				// 1) Copy HTML template to /kg-analysis folder
				progressMonitor.subTask("Copying files");	
				URL url = new URL("platform:/plugin/kganalysis/files/index.html");
				InputStream inputStream = url.openConnection().getInputStream();
				File file = new File(KG_FOLDER, "index.html");
				copyInputStreamToFile(inputStream, file);
				progressMonitor.worked(20);
				
				
				
				// 2) Export Archi Model as CSV to /kg-analysis folder
				progressMonitor.subTask("Preparing Archi Model");
				// TODO: use CSVExporter
				progressMonitor.worked(20);
				
				
				// 3) Start Neo4j Database	
				progressMonitor.subTask("Starting Graph Database");
				db = new EmbeddedNeo4j();
				db.createDb();
				progressMonitor.worked(20);
				
				
				// 4) Import CSV Archi model to neo4j
				progressMonitor.subTask("Importing Archi model to database");
				db.importCSVModel(KG_FOLDER);
				progressMonitor.worked(20);
				
				// 5) Open browser with copied index.html
				progressMonitor.subTask("Done! Opening Browser...");
				BrowserEditorInput input = new BrowserEditorInput(file.getPath(), "Knowledge Graph");
				progressMonitor.worked(20);
				*/
				File file = createGraph(monitor);
				
				BrowserEditorInput input = new BrowserEditorInput(file.getPath(), "Knowledge Graph");
				IBrowserEditor editor = (IBrowserEditor)EditorManager.openEditor(input, IBrowserEditor.ID);
				final Browser browser = editor.getBrowser();
				
				if(editor != null && browser != null) {
					browser.refresh();
                }
				
				// editor.getBrowser().execute("alert(\"JavaScript, called from Java\");");
				//editor.getBrowser().execute("document.getElementById('viz').style.width= " + browser.getSize().y + ";");
				//editor.getBrowser().execute("document.getElementById('viz').style.height= " + browser.getSize().y + ";");
				
				
				new BrowserFunction(editor.getBrowser(), "getAll") {
		            @Override
		            public Object function(final Object[] arguments) {
		            	String query = "'MATCH (n)-[r]->(m) RETURN *'";
		            	editor.getBrowser().execute("document.getElementById('cypher').value= " + query + ";");
		            	editor.getBrowser().execute("document.getElementById('reload').click();");
		            	return null;
		            }
		        };
		        
		        new BrowserFunction(editor.getBrowser(), "cyclicDependency") {
		            @Override
		            public Object function(final Object[] arguments) {
		            	String query = "'MATCH (a)-[r1]->(b)-[r2]->(c)-[]->(a) return a,b,c'";
		            	editor.getBrowser().execute("document.getElementById('cypher').value= " + query + ";");
		            	editor.getBrowser().execute("document.getElementById('reload').click();");
		            	return null;
		            }
		        };

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
		
		// 1) Copy HTML template to /kg-analysis folder
		setProgressSubTask("Copying files", true);
		URL url = new URL("platform:/plugin/kganalysis/files/index.html");
		InputStream inputStream = url.openConnection().getInputStream();
		File file = new File(KG_FOLDER, "index.html");
		copyInputStreamToFile(inputStream, file);
		
		// 2) Export Archi Model as CSV to /kg-analysis folder
		setProgressSubTask("Preparing Archi Model", true);
		// TODO: use CSVExporter
		
		
		
		// 3) Start Neo4j Database	
		setProgressSubTask("Starting Graph Database", true);
		db = new EmbeddedNeo4j();
		db.createDb();
		
		
		
		// 4) Import CSV Archi model to neo4j
		db.importCSVModel(KG_FOLDER);
		
		
		return file;
	
		
	}
	
	
	public void exportToCSV() throws IOException {
		
		if (archiModel != null) {
			
			CSVExporter exporter = new CSVExporter(archiModel);
			exporter.export(KG_FOLDER);
			
		} else {
			throw new IOException("No Archi Model!");
		}
		
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
