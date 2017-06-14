package org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices;

import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearchItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapAlleleMatrices {

    private List<BrapiResponseAlleleMatricesItem> getBrapiJsonResponseAlleleMatricesItems() {

        List<BrapiResponseAlleleMatricesItem> returnVal = new ArrayList<>();

        for( Integer idx = 0 ; idx < 11 ; idx++) {

            BrapiResponseAlleleMatricesItem brapiResponseAlleleMatricesItem;
            brapiResponseAlleleMatricesItem = getBrapiResponseAlleleMatricesItem(idx);

            returnVal.add(brapiResponseAlleleMatricesItem);
        }

        return returnVal;
    }

    private BrapiResponseAlleleMatricesItem getBrapiResponseAlleleMatricesItem(Integer idx) {
        BrapiResponseAlleleMatricesItem brapiResponseAlleleMatricesItem;
        brapiResponseAlleleMatricesItem = new BrapiResponseAlleleMatricesItem();
        brapiResponseAlleleMatricesItem.setName("Dataset " + idx.toString());
        brapiResponseAlleleMatricesItem.setLastUpdated(new Date().toString());
        brapiResponseAlleleMatricesItem.setMatrixDbId(idx.toString());
        brapiResponseAlleleMatricesItem.setStudyDbId("10");
        brapiResponseAlleleMatricesItem.setDescription("Dummy dataset number " + idx.toString());
        return brapiResponseAlleleMatricesItem;
    }

    private List<BrapiResponseAlleleMatricesItem> getBrapiJsonResponseAlleleMatricesItemsByStudyDbId(Integer studyDbId) {

        List<BrapiResponseAlleleMatricesItem> returnVal = new ArrayList<>();
            returnVal.add(getBrapiResponseAlleleMatricesItem(studyDbId));
        return returnVal;
    }



    public BrapiResponseAlleleMatrices getBrapiResponseAlleleMatricesItemsByStudyDbId(Integer studyDbId) {
        BrapiResponseAlleleMatrices returnVal = new BrapiResponseAlleleMatrices();

        List<BrapiResponseAlleleMatricesItem> searchItems = getBrapiJsonResponseAlleleMatricesItemsByStudyDbId(studyDbId);

        returnVal.setData(searchItems);

        return returnVal ;

    }

    public BrapiResponseAlleleMatrices getBrapiResponseAlleleMatrices() {
        BrapiResponseAlleleMatrices returnVal = new BrapiResponseAlleleMatrices();

        List<BrapiResponseAlleleMatricesItem> searchItems = getBrapiJsonResponseAlleleMatricesItems();

        returnVal.setData(searchItems);

        return returnVal ;

    }

}
