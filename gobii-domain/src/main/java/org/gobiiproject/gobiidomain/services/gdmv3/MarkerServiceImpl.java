package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction;
import org.gobiiproject.gobiimodel.dto.noaudit.JobDTO;

public class MarkerServiceImpl implements MarkerService {

    @Override
    public JobDTO uploadMarkerFile(
        String markerTemplateId,
        MarkerUploadRequestDTO markerUploadRequestDTO,
        String cropType) throws GobiiDomainException {

        LoaderInstruction loaderInstruction = new LoaderInstruction();


        return new JobDTO();
    }

}
