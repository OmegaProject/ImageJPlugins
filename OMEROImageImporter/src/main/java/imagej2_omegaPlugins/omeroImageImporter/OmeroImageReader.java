package imagej2_omegaPlugins.omeroImageImporter;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.omero.OMEROCredentials;
import net.imagej.omero.OMEROService;
import net.imagej.omero.OMEROSession;
import net.imagej.ops.AbstractOp;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.convert.ConvertService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;

@Plugin(type = Command.class, headless = true, menuPath = "Plugins>OmegaToolbox>OmeroImageReader_V16")
public class OmeroImageReader extends AbstractOp {
	
	@Parameter(label = "Hostname")
	private String host = "Localhost";
	@Parameter(label = "Port")
	private int port = 4064;
	@Parameter(label = "Username")
	private String name;
	@Parameter(label = "Password")
	private String psw;
	
	@Parameter(label = "ID of OMERO image")
	private Long id;
	
	//private List<Long> ids;
	
	@Parameter
	private OMEROService ome;
	
	@Parameter
	private ConvertService cs;
	
	@Parameter(type = ItemIO.OUTPUT)
	Dataset dataset;

	@Override
	public void run() {
		OMEROCredentials credentials = new OMEROCredentials();
		credentials.setUser(name);
		credentials.setPassword(psw);
		credentials.setServer(host);
		credentials.setPort(port);
		OMEROSession session = null;
		try {
			session = new OMEROSession(credentials);
		} catch (omero.ServerError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PermissionDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCreateSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(session == null)
			return;
		try {
			session.getClient().createClient(true);
		} catch (omero.ServerError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCreateSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PermissionDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(session.getClient() == null)
			return;
		
		//for(Long id : ids) {
		try {
			dataset = ome.downloadImage(session.getClient(), id);
		} catch (omero.ServerError ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		//}
	}
	
	public static void main(final String... args) {
		// Launch ImageJ as usual.
		final ImageJ ij = net.imagej.Main.launch(args);

		// Launch our "Hello World" command right away.
		ij.command().run(OmeroImageReader.class, true);
		//ij.op().run(OmeroImageReader.class, true);
	}
}
