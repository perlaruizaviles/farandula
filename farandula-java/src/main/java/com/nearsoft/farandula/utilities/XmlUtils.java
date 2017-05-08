package com.nearsoft.farandula.utilities;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class XmlUtils {
    public static Node getNode(String tagName, NodeList nodes) {
        for (int x = 0; x < nodes.getLength(); x++) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }

        return null;
    }

    public static List<Node> getNodeList(String tagName, NodeList nodes) {
        List<Node> results = new ArrayList<>();
        for (int x = 0; x < nodes.getLength(); x++) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                results.add( node );
            }
        }

        return results;
    }

    /**
     * Get textual content of a node(for text nodes only).
     *
     * @param node a text node.
     * @return node's textual content.
     */
    public static String getNodeValue(Node node ) {
        NodeList childNodes = node.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++ ) {
            Node data = childNodes.item(x);
            if ( data.getNodeType() == Node.TEXT_NODE )
                return data.getTextContent();
        }
        return "";
    }

    public static String getAttrByName(Node node, String attrName) {
        if (node.hasAttributes()){
            NamedNodeMap attr = node.getAttributes();
            Node attrNode = attr.getNamedItem(attrName);
            if (attrNode != null) {
                return attrNode.getNodeValue();
            }
        }
        return "";
    }
}
