package kganalysis.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.archimatetool.editor.ui.UIUtils;

import org.eclipse.ui.IWorkbench;
import kganalysis.KGAnalysisPlugin;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class KGAnalysisPreferencePage
	extends PreferencePage
	implements IWorkbenchPreferencePage, IPreferenceConstants {
	
    private Button externalMode;
    
    private Text serverUrl;
	private Text username;
	private Text password;

	public KGAnalysisPreferencePage() {
		setPreferenceStore(KGAnalysisPlugin.INSTANCE.getPreferenceStore());
		setDescription("Keep the default embedded mode or connect to an existing remote database");
	}
	
	@Override
	public void init(IWorkbench workbench) {
	}
	
	@Override
	protected Control createContents(Composite parent) {
		
		Composite preferenceComp = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = 0;
        preferenceComp.setLayout(layout);
        
        // Connection - Group
        Group connectionGroup = new Group(preferenceComp, SWT.NULL);
        
        // TODO: Choose between Bolt/HTTPS
        
        
        // Group title
        connectionGroup.setText("External Mode");
        connectionGroup.setLayout(new GridLayout(2, false));
        connectionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Mode selection
        Label label = new Label(connectionGroup, SWT.NULL);
        label.setText("Connect to a remote database");
        externalMode = new Button(connectionGroup, SWT.CHECK);
        externalMode.addSelectionListener(new SelectionAdapter() {
            
        	@Override
            public void widgetSelected(SelectionEvent e) {
            	updateMode();
            }
        });
        
        // Server URL "bolt://localhost:7687"
        label = new Label(connectionGroup, SWT.NULL);
        label.setText("Server URL");
        serverUrl = UIUtils.createSingleTextControl(connectionGroup, SWT.BORDER, false);
        serverUrl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        serverUrl.setEnabled(false);
        
        
        // Username "neo4j"
        label = new Label(connectionGroup, SWT.NULL);
        label.setText("Username");
        username = UIUtils.createSingleTextControl(connectionGroup, SWT.BORDER, false);
        username.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        username.setEnabled(false);
        
        // Password "neo4j"
        label = new Label(connectionGroup, SWT.NULL);
        label.setText("Password");
        password = UIUtils.createSingleTextControl(connectionGroup, SWT.BORDER | SWT.PASSWORD, false);
        password.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        password.setEnabled(false);
        
        setPreferences();
        
        return preferenceComp;
	}
	
	private void setPreferences() {
		externalMode.setSelection(getPreferenceStore().getBoolean(P_EXTERNAL_MODE));
		serverUrl.setText(getPreferenceStore().getString(P_SERVER_URL));
		username.setText(getPreferenceStore().getString(P_SERVER_USERNAME));
		password.setText(getPreferenceStore().getString(P_SERVER_PASSWORD));
		
		updateMode();
	}
	
	private void updateMode() {
		serverUrl.setEnabled(externalMode.getSelection());
		username.setEnabled(externalMode.getSelection());
		password.setEnabled(externalMode.getSelection());
	}

	
	/**
	 * Save the preferences
	 */
	@Override
	public boolean performOk() {
		getPreferenceStore().setValue(P_EXTERNAL_MODE, externalMode.getSelection());
		
		if(externalMode.getSelection()) {
			getPreferenceStore().setValue(P_SERVER_URL, serverUrl.getText());
			getPreferenceStore().setValue(P_SERVER_USERNAME, username.getText());
			getPreferenceStore().setValue(P_SERVER_PASSWORD, password.getText());
		}

		return true;

	}
	
	protected void performDefaults() {
		// Choose embedded mode as default
		externalMode.setSelection(getPreferenceStore().getDefaultBoolean(P_EXTERNAL_MODE));

		serverUrl.setText(getPreferenceStore().getDefaultString(P_SERVER_URL));
		username.setText(getPreferenceStore().getDefaultString(P_SERVER_USERNAME));
		password.setText(getPreferenceStore().getDefaultString(P_SERVER_PASSWORD));
		
		updateMode();
		
	}
	

	

	

	
	
}