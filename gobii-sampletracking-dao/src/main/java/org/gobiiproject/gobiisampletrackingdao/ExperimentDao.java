package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.VendorProtocol;

public interface ExperimentDao {

    List<Experiment> getExperiments(Integer pageSize, Integer rowOffset,
                                    Integer projectId);

	Experiment getExperiment(Integer i) throws Exception;

	VendorProtocol getVendorProtocol(Integer vendorProtocolId);

	Experiment createExperiment(Experiment experiment);

	Experiment updateExperiment(Experiment target) throws Exception;

}
