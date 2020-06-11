package com.emiperez.repeson.mapper;

import java.io.IOException;

public interface JsonDataObject {

	<T> T getAttribute(String attrName, Class<T> type) throws IOException;
	
	String getAttributeAsText(String attrName) throws IOException;
}
