package imagej2_omegaPlugins.omeroImageImporter;

import java.awt.BorderLayout;
import java.io.IOException;
import java.rmi.ServerError;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import edu.umassmed.omega.commons.eventSystem.events.OmegaMessageEvent;
import edu.umassmed.omega.omero.commons.OmeroGateway;
import edu.umassmed.omega.omero.commons.OmeroImporterUtilities;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.omero.OMEROService;
import omero.client;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.convert.ConvertService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

@Plugin(type = Command.class, headless = true, menuPath = "Plugins>OmegaToolbox>OmeroImageBrowser_V11.1")
public class OmeroImageBrowser implements Command {

	@Parameter(label = "Hostname")
	private String host = "Localhost";
	@Parameter(label = "Port")
	private int port = 4064;
	@Parameter(label = "Username")
	private String name;
	@Parameter(label = "Password")
	private String psw;
	
	@Parameter
	private OMEROService ome;
	
	@Parameter
	private UIService ui;
	
	@Parameter
	private ConvertService cs;

	@Parameter(type = ItemIO.OUTPUT)
	private String status;
	
	private OmeroImageJPanel panel;

	@Override
	public void run() {
		
		//TODO explore OmeroService and see what I can reuse from there
		//TODO explore tutorials to see how to call command from my gui
		//TODO extrapolate all functionalities from gui classes
		//1 command should initialize the gui, the connection and a keep alive thread.
		//1 command should load the images
		OmeroGateway gateway = new OmeroGateway();
		
		JFrame frame = new JFrame();
		
		frame.setTitle("OMERO image importer");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		
		panel = new OmeroImageJPanel(frame, gateway);
		
		JMenuBar menuBar = panel.getMenu();
		frame.setJMenuBar(menuBar);
		
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
		
		panel.updateStatus("Waiting for connection...");
		
		//TODO launch this on another thread and update the status in meanwhile we are waiting	
		OmeroImporterUtilities.connectToGateway(frame, gateway, host, port, name, psw);
		boolean connected = gateway.isConnected();
		
		if(!connected) {
			//TODO Throw error dialog
			return;
		}
		
		client client = gateway.getClient();
		panel.updateStatus("Connection estabilished!");
		
		try {
			panel.updateVisualizationMenu();
		} catch (ServerError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		while(gateway.isConnected()) {
			while(!panel.isLoaded()) {
				
			}
			System.out.println("Image loaded");
			long imageID = panel.getImageID();
			Dataset dataset;
			try {
				dataset = ome.downloadImage(client, imageID);
				//Should create imagej context
				//Should use DefaultConvertService
				//ImagePlus ip = cs.convert(dataset, ImagePlus.class);
				ui.show(dataset);
			} catch (omero.ServerError ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		status = "Omero Gateway is connected: " + connected;
	}
	
	public static void main(final String... args) {
		// Launch ImageJ as usual.
		final ImageJ ij = net.imagej.Main.launch(args);

		// Launch our "Hello World" command right away.
		ij.command().run(OmeroImageBrowser.class, true);
	}
}
