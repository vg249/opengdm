package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.impl.ConfigSettingsServiceImpl;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.CropsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CropServiceImpl implements CropService {

    Logger LOGGER = LoggerFactory.getLogger(CropServiceImpl.class);

    private ConfigSettings configSettings = new ConfigSettings();

    /**
     * Get all the crops defined in the system.
     * No paging done as there are only very few crops available.
     * @return List of crop names
     * @throws GobiiException
     */
    @Override
    public List<CropsDTO> getCrops() throws GobiiException {

        List<CropsDTO> crops = new ArrayList<>();

        try {

            configSettings.getActiveCropConfigs().forEach(cropConfig -> {
                CropsDTO crop = new CropsDTO();
                crop.setCropType(cropConfig.getGobiiCropType());
                crops.add(crop);
            });

        }
        catch (Exception e) {
            LOGGER.error("Gobii Read Setting Error", e);
            throw new GobiiDomainException(e);
        }

        return crops;
    }
}
