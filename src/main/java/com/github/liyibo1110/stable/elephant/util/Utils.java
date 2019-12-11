package com.github.liyibo1110.stable.elephant.util;

import java.sql.Timestamp;

public class Utils {

	public static String getValuePlaceHolderByColumnType(String columnType) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("?");
		switch(columnType) {
			case "int4": {
				sb.append("::int");
				break;
			}
			case "int4[]": {
				sb.append("::int[]");
				break;
			}
			case "int8": {
				sb.append("::bigint");
				break;
			}
			case "int8[]": {
				sb.append("::bigint[]");
				break;
			}
			case "numeric": {
				sb.append("::numeric");
				break;
			}
			case "numeric[]": {
				sb.append("::numeric[]");
				break;
			}
			case "varchar": 
			case "text": 
			case "date": {
				sb.append("::varchar");
				break;
			}
			case "varchar[]": {
				sb.append("::varchar[]");
				break;
			}
			case "bool": {
				sb.append("::bool");
				break;
			}
			case "jsonb": {
				sb.append("::jsonb");
				break;
			}
			case "timestamp": {
				sb.append("::timestamp");
				break;
			}
			default: {
				break;
			}
		}
		sb.append(",");
		return sb.toString();
	}
	
	public static Class<?> getClassByTypeName(String typeName) {
		Class<?> clazz = null;
		switch(typeName) {
			case "int4": {
				clazz = Integer.class;
				break;
			}
			case "int4[]": {
				clazz = Integer[].class;
				break;
			}
			case "int8": {
				clazz = Long.class;
				break;
			}
			case "int8[]": {
				clazz = Long[].class;
				break;
			}
			case "numeric": {
				clazz = Double.class;
				break;
			}
			case "numeric[]": {
				clazz = Double[].class;
				break;
			}
			case "varchar": 
			case "text": 
			case "date": {
				clazz = String.class;
				break;
			}
			case "varchar[]": {
				clazz = String[].class;
				break;
			}
			case "bool": {
				clazz = Boolean.class;
				break;
			}
			case "jsonb": {
				clazz = String.class;
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
