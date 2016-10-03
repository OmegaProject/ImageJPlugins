package imagej2_omegaPlugins.omeroImageImporter;
import java.io.IOException;
import java.rmi.ServerError;
import java.util.List;
import java.util.Map;

import javax.swing.RootPaneContainer;

import edu.umassmed.omega.omero.commons.OmeroGateway;
import edu.umassmed.omega.omero.commons.OmeroImporterUtilities;
import edu.umassmed.omega.omero.commons.data.OmeroExperimenterWrapper;
import edu.umassmed.omega.omero.commons.data.OmeroGroupWrapper;
import edu.umassmed.omega.omero.commons.data.OmeroImageWrapper;
import edu.umassmed.omega.omero.commons.gui.OmeroPanel;

public class OmeroImageJPanel2 extends OmeroPanel {
	
	private long imageID;
	//private boolean isLoaded;
	private OmeroImageBrowser2 browser;
	
	public OmeroImageJPanel2(RootPaneContainer parent,OmeroGateway gateway, OmeroImageBrowser2 browser) {
		super(parent, gateway);
		this.browser = browser;
		//isLoaded = false;
	}

	private static final long serialVersionUID = -5740459087763362607L;

	@Override
	public void loadData(final boolean hasToSelect)
			throws IOException, ServerError {
		OmeroGateway gateway = getGateway();
		OmeroExperimenterWrapper expWrapper = OmeroImporterUtilities.getExperimenterWrapper(gateway);
		//System.out.println("I'm loading " + expWrapper.getStringRepresentation());
		List<OmeroGroupWrapper> groupWrapperList = OmeroImporterUtilities.getGroupWrapper(gateway);
		//System.out.println("I'm loading " + groupWrapperList.size() + " groups");
		Map<OmeroGroupWrapper, List<OmeroExperimenterWrapper>> leaderWrapperMap = OmeroImporterUtilities.getGroupLeaders(groupWrapperList);
		//System.out.println("I'm loading leaders for groups");
		for(OmeroImageWrapper imgWrapper : getImageWrapperToBeLoadedList()) {
			imageID = imgWrapper.getPixelsID();
		}
		
		this.getImageWrapperToBeLoadedList().clear();
		this.getProjectPanel().updateLoadedElements(null);
		this.getBrowserPanel().updateLoadedElements(null);
		
		//System.out.println("Imhere");
		browser.loadImage(imageID);
	}

	@Override
	public void onCloseOperation() {
		// TODO Auto-generated method stub
	}
	
//	public boolean isLoaded() {
//		return isLoaded;
//	}
//	
//	public long getImageID() {
//		isLoaded = false;
//		return imageID;
//	}
	
//	public ImageStack getImageStack() {
//		isLoaded = false;
//		return is;
//	}
}
