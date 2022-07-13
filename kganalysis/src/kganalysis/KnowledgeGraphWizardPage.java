package kganalysis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.model.IArchimateModel;

public class KnowledgeGraphWizardPage extends WizardPage {
	
	private Text dbFolder;
	private List<Button> radioButtons;
	private IArchimateModel selectedModel;
	
	public KnowledgeGraphWizardPage() {
        super("KnowledgeGraphWizardPage1");
        setTitle("New Knowledge Graph");
        setDescription("Choose an ArchiMate model:");
        setImageDescriptor(IKGAnalysisImages.ImageFactory.getImageDescriptor(IKGAnalysisImages.WIZARD_LOGO));
    }

	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NULL);
        comp.setLayout(new GridLayout());
        setControl(comp);
        setPageComplete(false);
        
        // DB Folder (currently not synchronized with anything and only serves as info)
        Composite fieldContainer = new Composite(comp, SWT.NULL);
        fieldContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fieldContainer.setLayout(new GridLayout(3, false));
        Label label = new Label(fieldContainer, SWT.NONE);
        label.setText("Database Location");
        dbFolder = UIUtils.createSingleTextControl(fieldContainer, SWT.BORDER, false);
        dbFolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        dbFolder.setText(ArchiPlugin.INSTANCE.getUserDataFolder().toString());
        dbFolder.setEnabled(false);
        
        // Horizontal line seperator
        Label line = new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL);
        line.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Model Selection
        Group group = new Group(comp, SWT.NONE);
        group.setText("Select ArchiMate model");
        group.setLayout(new GridLayout(1, false));
        GridData gdata = new GridData(SWT.FILL, SWT.FILL, true, false); 
        gdata.heightHint = 200;
        group.setLayoutData(gdata);
        
        ScrolledComposite scrolledComp = new ScrolledComposite(group, SWT.V_SCROLL);
        scrolledComp.setLayout(new GridLayout(1, false));
        scrolledComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        Composite innerComp = new Composite(scrolledComp, SWT.NONE);
        innerComp.setLayout(new GridLayout(1, false));
        innerComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
              
        radioButtons = new ArrayList<Button>();
        
        for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {	
	        Button modelButton = new Button(innerComp, SWT.RADIO);
	        modelButton.setText(model.getName());
	        modelButton.setData("id", model.getId());
	        modelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        modelButton.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(SelectionEvent e) {
	                validateFields();
	            }
	        });
	        radioButtons.add(modelButton);    
        }
        scrolledComp.setContent(innerComp);
        scrolledComp.setExpandHorizontal(true);
        scrolledComp.setExpandVertical(true);
        scrolledComp.setMinSize(innerComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
        
	}
	
	private void validateFields() {
		Boolean isOK = false;
		
		for (Button button : radioButtons) {
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
	
	public File getDbFolder() {
        return new File(dbFolder.getText());
    }
	
	public IArchimateModel getSelectedModel() {
		return selectedModel;
	}
}
