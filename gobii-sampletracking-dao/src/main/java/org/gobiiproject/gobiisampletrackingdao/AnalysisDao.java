package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Cv;

import java.util.List;
import java.util.Set;

public interface AnalysisDao {

    /**
     * Gets analyses for given list of analysis ids
     * @param analysisIds - List of analysis ids for which
     *                    respective analysis need to be fetched
     * @return List of Analysis entity
     */
    List<Analysis> getAnalysesByAnalysisIds(Set<Integer> analysisIds);

	List<Analysis> getAnalyses(Integer offset, Integer pageSize);

	Analysis createAnalysis(Analysis analysis) throws Exception;

}
