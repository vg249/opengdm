package org.gobiiproject.gobidomain;

import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Utils {

    /**
     * GDM PageTokens are Strings with token part values separated by hyphen(-).
     * This util function will split the page token by hyphen and assign them to respective part
     * keys according to their position
     * @param pageToken
     * @param tokenPartKeys
     * @return
     */
    public static Map<String, Integer> pageTokenParser(String pageToken, String[] tokenPartKeys) {


        Map<String, Integer> returnVal = new HashMap<>();

        String[] tokenPartValues = null;

        try {
            if (pageToken != null) {
                tokenPartValues = pageToken.split("-", tokenPartKeys.length);
            }


            for (int i = 0; i < tokenPartKeys.length; i++) {

                String tokenPartKey = tokenPartKeys[i];

                if (tokenPartValues == null || tokenPartValues.length == tokenPartKeys.length) {
                    returnVal.put(tokenPartKey, null);
                } else {

                    Integer tokenPartValue = null;

                    try {
                        tokenPartValue = Integer.parseInt(tokenPartValues[i]);
                    }
                    catch(NumberFormatException ne) {
                        throw new GobiiDomainException(
                                GobiiStatusLevel.ERROR,
                                GobiiValidationStatusType.BAD_REQUEST,
                                "Invalid page token");

                    }
                    returnVal.put(tokenPartKey, tokenPartValue);
                }
            }

            return returnVal;
        }
        catch (Exception e) {
            throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());
        }
    }
}
