package imagej2_omegaPlugins.omeroImageImporter;
import java.io.IOException;
import java.rmi.ServerError;
import java.util.List;
import java.util.Map;

import javax.swing.RootPaneContainer;

import edu.umassmed.omega.omeroCommons.OmeroGateway;
import edu.umassmed.omega.omeroCommons.OmeroImporterUtilities;
import edu.umassmed.omega.omeroCommons.data.OmeroExperimenterWrapper;
import edu.umassmed.omega.omeroCommons.data.OmeroGroupWrapper;
import edu.umassmed.omega.omeroCommons.data.OmeroImageWrapper;
import edu.umassmed.omega.omeroCommons.gui.OmeroPanel;

public class OmeroImageJPanel extends OmeroPanel {
	
	private long imageID;
	private boolean isLoaded;
	
	public OmeroImageJPanel(RootPaneContainer parent,OmeroGateway gateway) {
		super(parent, gateway);
		isLoaded = false;
	}

	private static final long serialVersionUID = -5740459087763362607L;

	@Override
	public void loadData(final boolean hasToSelect)
			throws IOException, ServerError {
		isLoaded = false;
		OmeroGateway gateway = getGateway();
		OmeroExperimenterWrapper expWrapper = OmeroImporterUtilities.getExperimenterWrapper(gateway);
		System.out.println("I'm loading " + expWrapper.getStringRepresentation());
		List<OmeroGroupWrapper> groupWrapperList = OmeroImporterUtilities.getGroupWrapper(gateway);
		System.out.println("I'm loading " + groupWrapperList.size() + " groups");
		Map<OmeroGroupWrapper, List<OmeroExperimenterWrapper>> leaderWrapperMap = OmeroImporterUtilities.getGroupLeaders(groupWrapperList);
		System.out.println("I'm loading leaders for groups");
		for(OmeroImageWrapper imgWrapper : getImageWrapperToBeLoadedList()) {
			imageID = imgWrapper.getPixelsID();
		}
		
		this.getImageWrapperToBeLoadedList().clear();
		this.getProjectPanel().updateLoadedElements(null);
		this.getBrowserPanel().updateLoadedElements(null);
		isLoaded = true;
	}

	@Override
	public void onCloseOperation() {
		// TODO Auto-generated method stub
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}
	
	public long getImageID() {
		isLoaded = false;
		return imageID;
	}
	
//	public ImageStack getImageStack() {
//		isLoaded = false;
//		return is;
//	}
}
