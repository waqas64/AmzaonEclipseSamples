package com.a2e.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.a2e.core.AmazonClient.documentToString;




//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
import org.dom4j.Document;
import org.dom4j.Element;

import com.ECS.client.jax.ImageSet;
import com.ECS.client.jax.Item.ImageSets;
import com.ECS.client.jax.ItemLookupResponse;

public class DataExtractor {

	AmazonClient client;
	Document doc;
	ItemLookupResponse r;

	public DataExtractor() {
		PropertiesReader props = PropertiesReader.getInstance();
		client = new AmazonClient(props.getAccessKeyId(), props.getSecretKey(),
				props.getAssociateTag());

	}

	public boolean setItem(String itemID) {
		final Map<String, String> params = new HashMap<String, String>(3);
		params.put(AmazonClient.Op.PARAM_OPERATION,
				AmazonClient.OPERATION_ITEM_LOOKUP);
		params.put("ItemId", itemID);
		params.put("ResponseGroup", "Large");
		r = client.lookup().execute(params);
		doc = client.getXml(params);
		return true;
	}

	public String getRawXML() {
		String returValue = null;
		if (doc != null) {
			returValue = documentToString(doc);
		}
		return returValue;
	}

	public String getURL() {
		String returValue = null;

		if (r != null && r.getItems() != null && r.getItems().size() > 0) {
			if (r.getItems().get(0).getItem().size() > 0) {
				returValue = r.getItems().get(0).getItem().get(0)
						.getDetailPageURL();
			}
		}

		return returValue;

	}

	public List<String> getImages() {
		String returValue = null;
		List<String> list = new ArrayList<String>();
		if (r != null && r.getItems() != null && r.getItems().size() > 0) {
			if (r.getItems().get(0).getItem().size() > 0) {
				List<ImageSets> imageList = r.getItems().get(0).getItem()
						.get(0).getImageSets();

				for (ImageSets sets : imageList) {
					List<ImageSet> set = sets.getImageSet();

					for (ImageSet img : set) {
						list.add(img.getLargeImage().getURL());
					}
				}
			}
		}
		return list;
	}
	
	public String getASIN()
	{
		String returValue = null;

		if (r != null && r.getItems() != null && r.getItems().size() > 0) {
			if (r.getItems().get(0).getItem().size() > 0) {
				returValue = r.getItems().get(0).getItem().get(0).getASIN();
			}
		}
		
		return returValue;
	}
	
	public String getItemDetails()
	{
		 String returValue = null;

		if (r != null && r.getItems() != null && r.getItems().size() > 0) {
			if (r.getItems().get(0).getItem().size() > 0) {
				//String Title = r.getItems().get(0).getItem().get(0).getItemAttributes().getTitle();
				//String Brand = r.getItems().get(0).getItem().get(0).getItemAttributes().getBrand();
				Element root = doc.getRootElement();//selectSingleNode("ItemAttributes").;
		        
				//for()
				for ( Iterator i = root.elementIterator( "Items" ); i.hasNext(); ) {
		        	//root.elementByID(arg0)
		        	
		        	Element Items = (Element) i.next();
		            
		        	for ( Iterator j = Items.elementIterator("Item"); j.hasNext(); ) {
		                Element item = (Element) j.next();
		                
			        	for ( Iterator k = item.elementIterator("ItemAttributes"); k.hasNext(); ) {
			                Element element = (Element) k.next();
			                
			                for (Object ele: element.elements() )
			                {
			                	Element curr = (Element) ele;
			                	String s = curr.getName() + " :   " +curr.getStringValue() + System.lineSeparator();
			                	
			                	if(returValue==null) 
			                	{
			                		returValue = s;
			                	}else
			                	{
			                		returValue += s;
			                	}
			                	
			                }
			                //element.
			               
			                // do something
			            }
		                //element.
		                //returValue += element.getName() + " :   " +element.getText();
		                // do something
		            }

		            // do something
		        }
				//return Title +System.lineSeparator()+Brand;
			}
		}
		
		return returValue;
	}
}
