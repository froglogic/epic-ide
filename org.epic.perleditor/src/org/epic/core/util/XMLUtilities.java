/*
 * Created on Apr 22, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.epic.core.util;

import org.eclipse.core.resources.IProject;

import java.io.*;
import java.util.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

/**
 * @author luelljoc
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class XMLUtilities {

	private static final String INCLUDE_FILE_NAME = ".includepath";
    private static final String CHARSET = "UTF-8";    

	/**
	 * 
	 */
	public XMLUtilities() {
		super();
	}

	public String[] getIncludeEntries(IProject project) {
		List includes = new ArrayList();
		try {

			String fileName =
				project.getLocation().toString()
					+ File.separator
					+ INCLUDE_FILE_NAME;

			File file = new File(fileName);

			if (file.exists()) {

				// No validation
				SAXBuilder builder = new SAXBuilder(false);
				Document doc = builder.build(file);

				// Get root element
				Element root = doc.getRootElement();

				List entries = root.getChildren("includepathentry");

				Iterator iter = entries.iterator();

				while (iter.hasNext()) {
					Element element = (Element) iter.next();
					includes.add(element.getAttributeValue("path"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (String[]) includes.toArray(new String[includes.size()]);
	}

	public void writeIncludeEntries(IProject project, String[] items) throws IOException {
		//Build XML document
		Element root = new Element("includepath");

		for (int i = 0; i < items.length; i++) {
			Element entry = new Element("includepathentry");
			entry.setAttribute(new Attribute("path", items[i]));
			root.addContent(entry);
		}

		// Prepare output
		Document doc = new Document(root);
        String file = project.getLocation().toString()+File.separator+INCLUDE_FILE_NAME;
        
        OutputStream out = null;
		try {
			out = new FileOutputStream(file);
            this.writeOutput(doc, out);
		} finally {
            SafeClose.close(out);
		}
	}

	private void writeOutput(Document doc, OutputStream out) throws IOException {
		XMLOutputter xmlout = new XMLOutputter();

		xmlout.setIndent(true);
		xmlout.setNewlines(true);
		//xmlout.setTextNormalize(true);
		xmlout.setEncoding(CHARSET);
        xmlout.output(doc, out);
	}

}