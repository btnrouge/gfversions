package jp.coppermine.gfversions;

import static javafx.concurrent.WorkerStateEvent.WORKER_STATE_SUCCEEDED;
import static org.tmatesoft.svn.core.SVNDepth.FILES;
import static org.tmatesoft.svn.core.wc.SVNRevision.HEAD;
import static org.tmatesoft.svn.core.wc.SVNRevision.UNDEFINED;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

/**
 * Controller of scene GlassFishVersions.
 */
public class GlassFishVersionsController implements Initializable {
	
	/**
	 * Choice box for select to obtain version.
	 */
	@FXML
	private ChoiceBox<Repository> target;
	
	/**
	 * Text field for keep to the revision.
	 */
	@FXML
	private TextField revision;
	
	/**
	 * Button pushed when checkout from the repository.
	 */
	@FXML
	private Button load;
	
	/**
	 * Progress indicator.
	 */
	@FXML
	private ProgressBar progress;
	
	/**
	 * One of pane of accordion, for appserver.
	 */
	@FXML
	private TitledPane appserverPane;
	
	/**
	 * Table of appserver components version information.
	 */
	@FXML
	private TableView<Version> appserverTable;
	
	/**
	 * Table column of appserver components name.
	 */
	@FXML
	private TableColumn<Version, String> appserverComponent;
	
	/**
	 * Table column of appserver components version.
	 */
	@FXML
	private TableColumn<Version, String> appserverVersion;
	
	/**
	 * One of pane of accordion, for nucleus.
	 */
	@FXML
	private TitledPane nucleusPane;
	
	/**
	 * Table of nucleus components version information.
	 */
	@FXML
	private TableView<Version> nucleusTable;
	
	/**
	 * Table column of nucleus components name.
	 */
	@FXML
	private TableColumn<Version, String> nucleusComponent;
	
	/**
	 * Table column of nucleus components version.
	 */
	@FXML
	private TableColumn<Version, String> nucleusVersion;
	
	/**
	 * Contains of table of appserver components version information.
	 */
	private ObservableList<Version> appserverEntries;
	
	/**
	 * Contains of table of nucleus components version information.
	 */
	private ObservableList<Version> nucleusEntries;
	
	/**
	 * Event handler when pushed Load button.
	 * 
	 * @param event action event
	 */
	@FXML
	public void onLoadAction(ActionEvent event) {
		GlassFishVersionsMain.cleanup();
		
		Optional.ofNullable(target.getSelectionModel().getSelectedItem()).ifPresent(repository -> {
			ExecutorService executor = Executors.newCachedThreadPool();
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					SVNURL url = SVNURL.parseURIEncoded(repository.getUrl());
					SVNClientManager clientManager = SVNClientManager.newInstance();
					SVNUpdateClient updateClient = clientManager.getUpdateClient();
					updateClient.setIgnoreExternals(false);
					updateProgress(0.1, 1.0);
					
					SVNRevision svnRevision = revision.getText().isEmpty() ? HEAD : SVNRevision.parse(revision.getText());
					
					updateClient.doCheckout(url.appendPath("appserver", true), GlassFishVersionsMain.workCopy.resolve("appserver").toFile(), UNDEFINED, svnRevision, FILES, false);
					updateProgress(0.4, 1.0);
					
					updateClient.doCheckout(url.appendPath("nucleus", true), GlassFishVersionsMain.workCopy.resolve("nucleus").toFile(), UNDEFINED, svnRevision, FILES, false);
					updateProgress(0.7, 1.0);
					
					PomParser pomParser = PomParser.getParser();
					
					appserverEntries = FXCollections.observableList(pomParser.parse(GlassFishVersionsMain.workCopy.resolve("appserver").resolve("pom.xml")));
					updateProgress(0.8, 1.0);
					
					nucleusEntries = FXCollections.observableList(pomParser.parse(GlassFishVersionsMain.workCopy.resolve("nucleus").resolve("pom.xml")));
					updateProgress(0.9, 1.0);

					appserverTable.setItems(appserverEntries);
					appserverComponent.setCellValueFactory(new PropertyValueFactory<>("component"));
					appserverVersion.setCellValueFactory(new PropertyValueFactory<>("version"));
					updateProgress(0.95, 1.0);
					
					nucleusTable.setItems(nucleusEntries);
					nucleusComponent.setCellValueFactory(new PropertyValueFactory<>("component"));
					nucleusVersion.setCellValueFactory(new PropertyValueFactory<>("version"));
					updateProgress(1.0, 1.0);
					
					return null;
				}
			};
			task.addEventFilter(WORKER_STATE_SUCCEEDED, (t) -> executor.shutdown());
			progress.progressProperty().bind(task.progressProperty());
			executor.submit(task);
		});
	}

	/* (non Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<Repository> repositories = FXCollections.observableArrayList();
		
		try (InputStream stream = getClass().getResourceAsStream("/repository.properties")) {
			Properties properties = new Properties();
			properties.load(stream);
			
			for (String key : properties.stringPropertyNames()) {
				repositories.add(new Repository(key, properties.getProperty(key)));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			Platform.exit();
		}
		
		target.setItems(repositories);
	}
}
