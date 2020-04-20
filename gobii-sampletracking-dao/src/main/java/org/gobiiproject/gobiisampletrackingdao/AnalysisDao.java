package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Analysis;

import java.util.List;
import java.util.Set;

public interface AnalysisDao {

    Analysis createAnalysis(Analysis analysisToCreate);

    /**
     * Gets analyses for given list of analysis ids
     * @param analysisIds - List of analysis ids for which
     *                    respective analysis need to be fetched
     * @return List of Analysis entity
     */
    List<Analysis> getAnalysesByAnalysisIds(Set<Integer> analysisIds);

    void deleteAnalysis(Integer analysisId) throws Exception;

}
