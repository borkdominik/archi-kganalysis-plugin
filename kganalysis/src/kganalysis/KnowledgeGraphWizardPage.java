package kganalysis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.model.IArchimateModel;

public class KnowledgeGraphWizardPage extends WizardPage {
	
	private Text dbFolder;
	private List<Button> modelsButton;
	private IArchimateModel selectedModel;
	
	public KnowledgeGraphWizardPage() {
        super("KnowledgeGraphWizardPage1");
        
        setTitle("Create new Knowledge Graph");
        setDescription("Choose the graph database folder and a model");
        setImageDescriptor(IKGAnalysisImages.ImageFactory.getImageDescriptor(IKGAnalysisImages.WIZARD_LOGO));
    }

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        setPageComplete(false);
        
        // DB Folder
        Composite fieldContainer = new Composite(container, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fieldContainer.setLayout(new GridLayout(3, false));
        Label label = new Label(fieldContainer, SWT.NONE);
        label.setText("Database Location");
        dbFolder = UIUtils.createSingleTextControl(fieldContainer, SWT.BORDER, false);
        dbFolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        dbFolder.setText(ArchiPlugin.INSTANCE.getUserDataFolder().toString());
        dbFolder.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        Button button = new Button(fieldContainer, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                handleBrowse();
            }
        });
        
        // Model Selection
        Group group = new Group(container, SWT.NONE);
        group.setText("Choose an ArchiMate model:");
        group.setLayout(new GridLayout(1, false));
        GridData gdata = new GridData(SWT.FILL, SWT.FILL, true, false); 
        gdata.heightHint = 200;
        group.setLayoutData(gdata);
        
        ScrolledComposite scomposite = new ScrolledComposite(group, SWT.V_SCROLL);
        scomposite.setLayout(new GridLayout(1, false));
        scomposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        Composite composite = new Composite(scomposite, SWT.NONE);
        composite.setLayout(new GridLayout(2, true));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
              
        modelsButton = new ArrayList<Button>();
        
        for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {	
	        Button modelButton = new Button(composite, SWT.RADIO);
	        modelButton.setText(model.getName());
	        modelButton.setData("id", model.getId());
	        modelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        modelButton.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(SelectionEvent e) {
	                validateFields();
	            }
	        });
	        
	        modelsButton.add(modelButton);    
	        
        }
        scomposite.setContent(composite);
        scomposite.setExpandHorizontal(true);
        scomposite.setExpandVertical(true);
        scomposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
        
	}
	
	private void validateFields() {
		Boolean isOK = false;
		
		for (Button button : modelsButton) {
			if (button.getSelection()) {
				String modelId = (String) button.getData("id");
				for (IArchimateModel archiModel : IEditorModelManager.INSTANCE.getModels()) {	
    				if(archiModel.getId() == modelId) {
    					selectedModel = archiModel;
    					isOK = true;
    				}
    			}
			}
		}
		
		if(!isOK) {
			setErrorMessage("Invalid input!");
	        setPageComplete(false);
            return;
        }
		
		setErrorMessage(null);
		setPageComplete(true);
	}
	
	private void handleBrowse() {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setText("Knowledge Graph");
        dialog.setMessage("Select a folder where the graph database will be stored");
        dialog.setFilterPath(dbFolder.getText());
        
        String path = dialog.open();
        if (path != null) {
            dbFolder.setText(path);
        }
    }
	
	public File getDbFolder() {
        return new File(dbFolder.getText());
    }
	
	public IArchimateModel getSelectedModel() {
		return selectedModel;
	}
}
