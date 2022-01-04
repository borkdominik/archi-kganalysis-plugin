package kganalysis;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;


import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.browser.BrowserEditorInput;
import com.archimatetool.editor.browser.IBrowserEditor;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateModel;

import kganalysis.db.EmbeddedNeo4j;


public class GraphExternal {

	//private static final Path databaseDirectory = Path.of("target/neo4j-hello-db");

	private IArchimateModel fModel;
	


	public static File PREVIEW_FOLDER = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "kg-preview");
	
	public String greeting;




	public GraphExternal(IArchimateModel model) {
		fModel = model;
	}

	public void showExternalBrowser() throws Exception {
		PREVIEW_FOLDER.mkdirs();
		// createKG(PREVIEW_FOLDER);

		try {
			//createDb();
	        
			IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
			IWebBrowser browser = support.getExternalBrowser();
			// This method supports network URLs
			browser.openURL(new URL("file", null, "/Users/philipp/" + "index.html"));
			 //embeddedDb = new EmbeddedNeo4j();
			 
			EmbeddedNeo4j db = new EmbeddedNeo4j();
			db.createDb();
			
			
			
			 //
			 //db.removeData();
			 //db.shutDown();
			/*
			 * BrowserEditorInput input = new BrowserEditorInput("file:///Users/philipp/" +
			 * "index.html"); IBrowserEditor editor =
			 * (IBrowserEditor)EditorManager.openEditor(input, IBrowserEditor.ID); if(editor
			 * != null && editor.getBrowser() != null) { editor.getBrowser().refresh(); }
			 */
		} catch (Exception ex) { // Catch OOM and SWT exceptions
			System.err.println("Exception");
		}
		
		


	}

	public void showInternalBrowser() throws Exception {
		PREVIEW_FOLDER.mkdirs();
		// createKG(PREVIEW_FOLDER);

		try {

			BrowserEditorInput input = new BrowserEditorInput("file:///Users/philipp/" + "index.html");
			IBrowserEditor editor = (IBrowserEditor) EditorManager.openEditor(input, IBrowserEditor.ID);
			if (editor != null && editor.getBrowser() != null) {
				editor.getBrowser().refresh();
			}

		} catch (Exception ex) { // Catch OOM and SWT exceptions
			System.err.println("Exception");
		}

	}

	public void createKG(File targetFolder) throws IOException {
		// FileUtils.copyFolder(srcDir, targetFolder);

	}
	
	/*
	void createDb() throws IOException {
		 FileUtils.deleteDirectory( databaseDirectory );
		 managementService = new DatabaseManagementServiceBuilder( databaseDirectory ).build();
	     graphDb = managementService.database( DEFAULT_DATABASE_NAME );
	     registerShutdownHook( managementService );



	        try ( Transaction tx = graphDb.beginTx() )
	        {
	            // Database operations go here
	            // end::transaction[]
	            // tag::addData[]
	            firstNode = tx.createNode();
	            firstNode.setProperty( "message", "Hello, " );
	            secondNode = tx.createNode();
	            secondNode.setProperty( "message", "World!" );

	            relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
	            relationship.setProperty( "message", "brave Neo4j " );
	            // end::addData[]

	            // tag::readData[]
	            System.out.print( firstNode.getProperty( "message" ) );
	            System.out.print( relationship.getProperty( "message" ) );
	            System.out.print( secondNode.getProperty( "message" ) );
	            // end::readData[]

	            greeting = ( (String) firstNode.getProperty( "message" ) )
	                       + ( (String) relationship.getProperty( "message" ) )
	                       + ( (String) secondNode.getProperty( "message" ) );

	            // tag::transaction[]
	            tx.commit();
	        }
	 }

	void removeData() {
		try (Transaction tx = graphDb.beginTx()) {
			// tag::removingData[]
			// let's remove the data
			firstNode = tx.getNodeById(firstNode.getId());
			secondNode = tx.getNodeById(secondNode.getId());
			firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();
			firstNode.delete();
			secondNode.delete();
			// end::removingData[]

			tx.commit();
		}
	}

	void shutDown() {
		System.out.println();
		System.out.println("Shutting down database ...");
		// tag::shutdownServer[]
		managementService.shutdown();
		// end::shutdownServer[]
	}
	
	private static void registerShutdownHook( final DatabaseManagementService managementService )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                managementService.shutdown();
            }
        } );
    }*/

}
