package com.tigerwho.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.tigerwho.constant.CommonConstant;

public class Transformtools {
	public static List<String> transformAll(String[] coordinateArr, int type, int zeroNum) {
		List<String> outList = new ArrayList<>();
		for (String coordinate : coordinateArr) {
			String transformSingle = transformSingle(coordinate, type, zeroNum);
			outList.add(transformSingle);
		}
		return outList;
	}

	/**
	 * 1.1 -> 1°6′0″ 1°6′0″ -> 1.1
	 * 
	 * @param coordinate
	 * @param type
	 * @return
	 */
	public static String transformSingle(String coordinate, int type, int zeroNum) throws NumberFormatException {
		String output = "";
		if (type == 0) {
			if (!"".equals(coordinate)) {
				String regex = "^\\d+([\\.|。]\\d+)?$";
				if (!coordinate.matches(regex)) {
					return "NULL";
				}
				coordinate = coordinate.trim().replace("。", ".");
				Double coordinateDouble = Double.valueOf(coordinate);
				int intValue = coordinateDouble.intValue();
				double doubleValue = coordinateDouble - intValue;

				output += intValue + CommonConstant.DEGREE;

				Double doubleValueSub = doubleValue * 60;
				output += doubleValueSub.intValue() + CommonConstant.MINUTE;

				double doubleValueSub1 = doubleValueSub - doubleValueSub.intValue();
				long round = Math.round(doubleValueSub1 * 60);

				output += round + CommonConstant.SECOND;
			}

		} else {
			if (!"".equals(coordinate)) {
				coordinate = coordinate.trim().replace("’", CommonConstant.MINUTE).replaceAll("'", CommonConstant.MINUTE)
						.replaceAll("“", CommonConstant.SECOND).replaceAll("”", CommonConstant.SECOND)
						.replaceAll("\"", CommonConstant.SECOND).replaceAll("\\s", "");

				String regex = "^\\d+" + CommonConstant.DEGREE + "(\\d+" + CommonConstant.MINUTE + "(\\d+"
						+ CommonConstant.SECOND + ")?)?$";
				if (!coordinate.matches(regex)) {
					return "NULL";
				}

				String degreeFull = "0", minuteFull = "0", secondFull = "0";
				if (coordinate.contains(CommonConstant.MINUTE) && coordinate.contains(CommonConstant.SECOND)) {
					secondFull = coordinate.substring(coordinate.lastIndexOf(CommonConstant.MINUTE) + 1,
							coordinate.lastIndexOf(CommonConstant.SECOND));
				}

				if (coordinate.contains(CommonConstant.DEGREE) && coordinate.contains(CommonConstant.SECOND)) {
					minuteFull = coordinate.substring(coordinate.lastIndexOf(CommonConstant.DEGREE) + 1,
							coordinate.lastIndexOf(CommonConstant.MINUTE));
				}

				double minute = Integer.valueOf(minuteFull) + Double.valueOf(secondFull) / 60;
				degreeFull = coordinate.substring(0, coordinate.indexOf(CommonConstant.DEGREE));
				int prefix = Integer.valueOf(degreeFull) + (int) minute / 60;
				double suffix = minute % 60 / 60;
				double out = prefix + suffix;

				String multiZero = "";
				for (int i = 0; i < zeroNum; i++) {
					multiZero += "0";
				}

				DecimalFormat df = new DecimalFormat("#." + multiZero);
				output = df.format(out+"");
			}

		}

		return output;
	}
}
