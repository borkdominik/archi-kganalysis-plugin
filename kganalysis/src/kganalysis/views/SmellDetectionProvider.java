package kganalysis.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.jface.viewers.ITreeContentProvider;
import kganalysis.KGPlugin;
import kganalysis.smells.AbstractSmell;
import kganalysis.smells.ChattyServiceSmell;
import kganalysis.smells.CyclicDependencySmell;
import kganalysis.smells.DeadComponentSmell;
import kganalysis.smells.DocumentationSmell;
import kganalysis.smells.DuplicationSmell;
import kganalysis.smells.MessageChainSmell;
import kganalysis.smells.SharedPersistencySmell;
import kganalysis.smells.StrictLayerViolationSmell;


public class SmellDetectionProvider implements ITreeContentProvider {

	private Map<String, String[]> contentMap = new HashMap<>();

	public SmellDetectionProvider() {
		if (KGPlugin.getDefault().isGraphDbStarted() && KGPlugin.INSTANCE.getExporter() != null) {
			runSmellDetection();
		}
	}

	private void runSmellDetection() {
		List<AbstractSmell> smells = new ArrayList<>();
		String[] elements;

		// add individual smell detectors
		smells.add(new ChattyServiceSmell());
		smells.add(new CyclicDependencySmell());
		smells.add(new DeadComponentSmell());
		smells.add(new DocumentationSmell());
		smells.add(new DuplicationSmell());
		smells.add(new MessageChainSmell());
		smells.add(new SharedPersistencySmell());
		smells.add(new StrictLayerViolationSmell());

		// initialize the tree with smell names
		String[] smellNames = smells
				.stream()
				.map(AbstractSmell::getLabel)
				.toArray(String[]::new);
		
		contentMap.put("smells", smellNames);

		// run the smell detection
		for (AbstractSmell smell : smells) {
			elements = smell.detect();
			contentMap.put(smell.getLabel(), elements);
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return contentMap.get("smells");
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return contentMap.get(parentElement);
	}

	@Override
	public Object getParent(Object element) {
		Set<String> keys = contentMap.keySet();
		for (String key : keys) {
			String[] values = contentMap.get(key);
			for (String val : values) {
				if (val.equals(element)) {
					return key;
				}
			}
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return contentMap.containsKey(element);
	}
}
