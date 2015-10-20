package imagej2_omegaPlugins.omeroImageImporter;
/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

import java.awt.BorderLayout;
import java.io.IOException;
import java.rmi.ServerError;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

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

import edu.umassmed.omega.commons.eventSystem.events.OmegaMessageEvent;
import edu.umassmed.omega.omeroCommons.OmeroGateway;
import edu.umassmed.omega.omeroCommons.OmeroImporterUtilities;

/**
 * A very simple plugin.
 * <p>
 * The annotation {@code @Plugin} lets ImageJ know that this is a plugin. There
 * are a vast number of possible plugins; {@code Command} plugins are the most
 * common one: they take inputs and produce outputs.
 * </p>
 * <p>
 * A {@link Command} is most useful when it is bound to a menu item; that is
 * what the {@code menuPath} parameter of the {@code @Plugin} annotation does.
 * </p>
 * <p>
 * Each input to the command is specified as a field with the {@code @Parameter}
 * annotation. Each output is specified the same way, but with a
 * {@code @Parameter(type = ItemIO.OUTPUT)} annotation.
 * </p>
 * 
 * @author Johannes Schindelin
 * @author Curtis Rueden
 */
@Plugin(type = Command.class, headless = true, menuPath = "Plugins>OmegaToolbox>OmeroImageImporter")
public class OmeroImageImporter implements Command {

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

	/**
	 * Produces an output with the well-known "Hello, World!" message. The
	 * {@code run()} method of every {@link Command} is the entry point for
	 * ImageJ: this is what will be called when the user clicks the menu entry,
	 * after the inputs are populated.
	 */
	@Override
	public void run() {
		
		//TODO explore OmeroService and see what I can reuse from there
		//TODO explore tutorials to see how to call command from my gui
		//TODO explore extrapolate all functionalities from gui classes
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
		
		panel.updateMessageStatus(new OmegaMessageEvent("Waiting for connection..."));
		
		//TODO launch this on another thread and update the status in meanwhile we are waiting	
		OmeroImporterUtilities.connectToGateway(frame, gateway, host, port, name, psw);
		boolean connected = gateway.isConnected();
		
		if(!connected) {
			//TODO Throw error dialog
			return;
		}
		
		client client = gateway.getClient();
		panel.updateMessageStatus(new OmegaMessageEvent("Connection estabilished!"));
		
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
	
	/**
	 * A {@code main()} method for testing.
	 * <p>
	 * When developing a plugin in an Integrated Development Environment (such as
	 * Eclipse or NetBeans), it is most convenient to provide a simple
	 * {@code main()} method that creates an ImageJ context and calls the plugin.
	 * </p>
	 * <p>
	 * In particular, this comes in handy when one needs to debug the plugin:
	 * after setting one or more breakpoints and populating the inputs (e.g. by
	 * calling something like
	 * {@code ij.command().run(MyPlugin.class, "inputImage", myImage)} where
	 * {@code inputImage} is the name of the field specifying the input) debugging
	 * becomes a breeze.
	 * </p>
	 * 
	 * @param args unused
	 */
	public static void main(final String... args) {
		// Launch ImageJ as usual.
		final ImageJ ij = net.imagej.Main.launch(args);

		// Launch our "Hello World" command right away.
		ij.command().run(OmeroImageImporter.class, true);
	}
}
