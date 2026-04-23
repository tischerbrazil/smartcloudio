package org.geoazul.view.website;

import org.glassfish.jaxb.runtime.marshaller.NamespacePrefixMapper;

final class NameSpaceBeautyfier extends NamespacePrefixMapper {

    private static final String KML_PREFIX = ""; // DEFAULT NAMESPACE
    private static final String KML_URI= "http://www.opengis.net/kml/2.2";

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if(KML_URI.equals(namespaceUri)) {
            return KML_PREFIX;
        }
        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[] { KML_URI };
    }

    NameSpaceBeautyfier() {
    }
}
