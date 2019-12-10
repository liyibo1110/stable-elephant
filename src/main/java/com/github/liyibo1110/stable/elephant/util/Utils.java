package com.github.liyibo1110.stable.elephant.util;

import java.sql.Timestamp;

public class Utils {

	public static Class<?> getClassByTypeName(String typeName) {
		Class<?> clazz = null;
		switch(typeName) {
			case "int4": {
				clazz = Integer.class;
				break;
			}
			case "int8": {
				clazz = Long.class;
				break;
			}
			case "numeric": {
				clazz = Double.class;
				break;
			}
			case "varchar": 
			case "text": {
				clazz = String.class;
				break;
			}
			case "bool": {
				clazz = Boolean.class;
				break;
			}
			case "timestamp": {
				clazz = Timestamp.class;
				break;
			}
			default: {
				clazz = null;
				break;
			}
		}
		return clazz;
	}
}
