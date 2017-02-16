package org.palladiosimulator.transactions.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import de.uka.ipd.sdq.workflow.launchconfig.tabs.TabHelper;
import edu.kit.ipd.sdq.eventsim.modules.AbstractLaunchContribution;

public class TransactionsLaunchContribution extends AbstractLaunchContribution {

	protected Text dataRepositoryLocation;

	protected Text entityTypeRepositoryLocation;

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void createControl(Composite parent) {
		final ModifyListener modifyListener = new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		};

		final Composite container = new Composite(parent, SWT.NONE);
		this.setControl(container);
		container.setLayout(new GridLayout());

		dataRepositoryLocation = new Text(container, SWT.SINGLE | SWT.BORDER);
		TabHelper.createFileInputSection(container, modifyListener, "Data Repository File",
				ConfigurationConstants.DATA_REPOSITORY_EXTENSION, dataRepositoryLocation,
				"Select Data Repository File", getShell(), ConfigurationConstants.NO_DEFAULT_FILE_URI);

		entityTypeRepositoryLocation = new Text(container, SWT.SINGLE | SWT.BORDER);
		TabHelper.createFileInputSection(container, modifyListener, "Entity Type Repository File",
				ConfigurationConstants.ENTITY_TYPE_REPOSITORY_EXTENSION, entityTypeRepositoryLocation,
				"Select Entity Type Repository File", getShell(), ConfigurationConstants.NO_DEFAULT_FILE_URI);

	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(ConfigurationConstants.DATA_REPOSITORY_FILE, "");
		configuration.setAttribute(ConfigurationConstants.ENTITY_TYPES_REPOSITORY_FILE, "");
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			dataRepositoryLocation
					.setText(configuration.getAttribute(ConfigurationConstants.DATA_REPOSITORY_FILE, ""));
			entityTypeRepositoryLocation
					.setText(configuration.getAttribute(ConfigurationConstants.ENTITY_TYPES_REPOSITORY_FILE, ""));
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(ConfigurationConstants.DATA_REPOSITORY_FILE, dataRepositoryLocation.getText());
		configuration.setAttribute(ConfigurationConstants.ENTITY_TYPES_REPOSITORY_FILE,
				entityTypeRepositoryLocation.getText());
	}

	@Override
	public String getName() {
		return "Palladio.TX Extension";
	}
}
