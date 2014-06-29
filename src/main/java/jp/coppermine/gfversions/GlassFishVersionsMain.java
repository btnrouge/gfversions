package jp.coppermine.gfversions;
	
import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

/**
 * Main class of this application.
 */
public class GlassFishVersionsMain extends Application {
	
	/**
	 * Path to directory of the work copy.
	 */
	public static final Path workCopy = Paths.get(System.getProperty("java.io.tmpdir"), ".gfversions");
	
	/**
	 * Initialize this application.
	 * 
	 * @throws Exception some error is occurred
	 */
	@Override
	public void init() throws Exception {
		if (!Files.exists(workCopy)) {
			Files.createDirectory(workCopy);
		}
	}

	/**
	 * Cleanup this application.
	 * 
	 * @throws Exception some error is occurred
	 */
	@Override
	public void stop() throws Exception {
		cleanup();
	}

	/**
	 * Startup this application.
	 * 
	 * @param stage the primary stage
	 */
	@Override
	public void start(Stage stage) {
		try {
			AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/GlassFishVersions.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/style/application.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle("GlassFish components");
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/gfversions.png")));
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
			Platform.exit();
		}
	}
	
	/**
	 * Cleanup temporary files created by this application.
	 */
	public static void cleanup() {
		FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return CONTINUE;
			}
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return CONTINUE;
			}
		};
		try {
			Files.walkFileTree(workCopy, visitor);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * The main method of this application.
	 * 
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
