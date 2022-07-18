package kganalysis.views;

import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.QueryExecutionException;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import kganalysis.IEAKGImages;
import kganalysis.KGPlugin;


/**
 ** Experimental Cypher Editor (work in progress)
 */
public class CypherEditorView extends ViewPart implements ICypherEditorView {

	private Text cypherText;
    private Label statusLabel;
	private Action executeQuery;
	private GraphDatabaseService graphDb;
	
	
	public CypherEditorView() {
		super();
		graphDb = KGPlugin.getDefault().getGraphDb();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
	    cypherText = new Text(sashForm, SWT.WRAP | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		cypherText.addKeyListener(new KeyListener() {
			
			@Override
            public void keyReleased(KeyEvent keyEvent) {
				if (!validate()) {
					return;
				}
			}

			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if (!validate()) {
					return;
				}
			}
		});
		
		statusLabel = new Label(sashForm, SWT.WRAP);
		statusLabel.setText("No query result. Execute a query!");
		
        executeQuery = new Action("Execute Query") {
            @Override
            public void run() {
            	executeCurrentQuery(cypherText.getText());
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return IEAKGImages.ImageFactory.getImageDescriptor(IEAKGImages.ICON_RELOAD);
            }
        };
        executeQuery.setEnabled(true);
	    
	    IToolBarManager manager = getViewSite().getActionBars().getToolBarManager();
	    manager.add(executeQuery);
	}
		
	

	@Override
	public void setFocus() {
		cypherText.setFocus();
	}
	
	private void executeCurrentQuery(String query) {
		// TODO: Properly output result
		String resultText = "";
		try (Transaction tx = graphDb.beginTx(); Result result = tx.execute(query)) {
			while (result.hasNext()) {
				Map<String,Object> row = result.next();
			    for (Entry<String,Object> column : row.entrySet()){
			    	resultText += column.getKey() + ": " + column.getValue() + "; ";
			    }
			    	resultText += "\n";
			}
		} catch (QueryExecutionException ex) {
			resultText = "Invalid query: " + ex.getMessage();
		}
		statusLabel.setText(resultText);
	}
	
	private boolean validate() {
		// TODO: Validate text input
		return true;
	}

}
