package org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices;

import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapAlleleMatrices {

    private List<BrapiResponseAlleleMatricesItem> getBrapiJsonResponseAlleleMatricestems(Integer studyDbId) {

        List<BrapiResponseAlleleMatricesItem> returnVal = new ArrayList<>();

//        returnVal.add(brapiResponseStudiesSearchItem);


        return returnVal;
    }


    public BrapiResponseAlleleMatrices getBrapiResponseAlleleMatrices(Integer studyDbId) {

        BrapiResponseAlleleMatrices returnVal = new BrapiResponseAlleleMatrices();

        List<BrapiResponseAlleleMatricesItem> searchItems = getBrapiJsonResponseAlleleMatricestems(studyDbId);

        returnVal.setData(searchItems );

        return returnVal ;

    }

}
