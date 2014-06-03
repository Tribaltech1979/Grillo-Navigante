package com.schiappa.grillonavigante.KML;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class KmlManager {
	
	private static final String TAG = "kmlManager";
	
	Context mncontext;
	String FileName;
	List<Placemark> Placemarks;
	
	public String getFileName() {
		return mncontext.getFilesDir().toString()+"/"+this.FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public KmlManager(String filename, Context c){
		Placemarks = new ArrayList<Placemark>();
		FileName = filename;
		mncontext = c;
	}
	
	public void addPlacemark(Placemark place){
		
		Placemarks.add(place);
		
	}
	
	public void addPlacemark(String name, String description, String coordinates){
		Placemark place = new Placemark(name, description, coordinates);
		
		this.addPlacemark(place);
	}
	
	public void addPlacemark(String name,String description, Location loc){
		
		String coordinates = loc.getLatitude()+","+loc.getLongitude();
		
		this.addPlacemark(name, description, coordinates);		
	}
	
	public void addPlacemark(String name,String description, String lat, String lon){
		
		String coordinates = lat+","+lon;
		
		this.addPlacemark(name, description, coordinates);		
	}
	
	
	public void GetKmlFromFile(){
		
		this.Placemarks = new ArrayList<Placemark>();
		
		try {
		File fXmlFile = new File(mncontext.getFilesDir(),this.FileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("Placemark");
		
		for (int temp = 0; temp < nList.getLength(); temp++){
			Node nNode = nList.item(temp);
			
			if(nNode.getNodeType() == Node.ELEMENT_NODE ){
				
				Element eElement = (Element) nNode;
				
				Placemark place = new Placemark(eElement.getElementsByTagName("name").item(0).getTextContent(), 
												eElement.getElementsByTagName("description").item(0).getTextContent(),
												eElement.getElementsByTagName("coordinates").item(0).getTextContent());
				
				this.Placemarks.add(place);
				
				}
			
			}
		
		}catch (Exception e) {
			e.printStackTrace();
	    }
		
	}
	
	public void SetKmlToFile(){
		
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("kml");
			doc.appendChild(rootElement);
			
			Attr attr = doc.createAttribute("xmlns");
			attr.setValue("http://www.opengis.net/kml/2.2");
			rootElement.setAttributeNode(attr);
			
			for(int i=0; i<Placemarks.size(); i++){
				Placemark place = Placemarks.get(i);
				
				Element ePlace = doc.createElement("Placemark");
				rootElement.appendChild(ePlace);
				
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(place.getName()));
				ePlace.appendChild(name);
				
				Element descr = doc.createElement("description");
				descr.appendChild(doc.createTextNode(place.Description));
				ePlace.appendChild(descr);
				
				Element point = doc.createElement("Point");
				ePlace.appendChild(point);
				
				Element coordinates = doc.createElement("coordinates");
				coordinates.appendChild(doc.createTextNode(place.getCoordinates()));
				point.appendChild(coordinates);
				
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(mncontext.getFilesDir(),this.FileName));
			
			transformer.transform(source, result);
			
			Log.w(TAG,"Salvato: "+mncontext.getFilesDir().toString()+"/"+this.FileName);
			
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		
	}

}
