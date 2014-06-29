package jp.coppermine.gfversions;

import static java.nio.file.StandardOpenOption.READ;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Parser for pom.xml of appserver or nucleus.
 */
public class PomParser {
	
	private PomParser() { }
	
	public static PomParser getParser() {
		return new PomParser();
	}
	
	/**
	 * Parse pom.xml downloaded to some place and obtains the version 
	 * information of components.
	 * 
	 * @param path path to pom.xml
	 * @return list of version information as {@code Version}.
	 * @throws IOException some error occurred when read pom.xml
	 * @throws XMLStreamException some error occurred when parse pom.xml
	 */
	public List<Version> parse(Path path) throws IOException, XMLStreamException {
		List<Version> versions = new ArrayList<>();
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader reader = null;
		
		try (InputStream stream = Files.newInputStream(path, READ)) {
			reader = factory.createXMLEventReader(stream);
			
			String tag = "";
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				
				if (event.isStartElement()) {
					StartElement element = event.asStartElement();
					if (element.getName().getLocalPart().matches(".+\\.version$")) {
						tag = element.getName().getLocalPart().replaceFirst("\\.version$", "");
						tag = tag.equals("javaee") ? "" : tag;
					}
				}
				
				if (event.isCharacters() && !tag.isEmpty()) {
					Characters element = event.asCharacters();
					versions.add(new Version(tag, element.getData()));
				}
				
				if (event.isEndElement()) {
					tag = "";
				}
			}
		} finally {
			reader.close();
		}
		
		return versions.stream().sorted((e1, e2) -> e1.getComponent().compareTo(e2.getComponent())).collect(toList());
	}
}
