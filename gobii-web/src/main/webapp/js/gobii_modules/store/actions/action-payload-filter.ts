import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {Pagination} from "../../model/payload/pagination";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {Payload} from "../../model/payload/payload";

export class PayloadFilter {
    constructor(public gobiiExtractFilterType: GobiiExtractFilterType,
                public targetEntityUniqueId:GobiiFileItemCompoundId,
                public relatedEntityUniqueId:GobiiFileItemCompoundId,
                public relatedEntityFilterValue: string,
                public targetEntityFilterValue: string,
                public entityLasteUpdated: Date,
                public pagination: Pagination) {

    }

    static fromPayloadFilter(payloadFilterToCopy:PayloadFilter) : PayloadFilter {

        return new PayloadFilter(
            payloadFilterToCopy.gobiiExtractFilterType,
            payloadFilterToCopy.targetEntityUniqueId,
            payloadFilterToCopy.relatedEntityUniqueId,
            payloadFilterToCopy.relatedEntityFilterValue,
            payloadFilterToCopy.targetEntityFilterValue,
            payloadFilterToCopy.entityLasteUpdated,
            payloadFilterToCopy.pagination
        );
    }
}

/**
 * bug/GSD-557
 * Interface for JobType:JobStatus key-value pair.
 * Added to define jobStatusFilterValues parameter in
 * class ExtractReadyPayloadFilter.
 * @typedef {Object.<string, Array<string>|null>}
 * example: {
 *     "extract" : "pending",
 *     ...
 * }
 */
export interface JobTypeFilters {
    [key: string]: string[];
}



/**
 * bug/GSD-557
 * @extends PayloadFilter
 * Existing PayloadFilter does not have provision for filters which requires
 * multiple conditions to be satisfied.
 * For Extract ready filter, two jobTypes with different values needs to
 * checked.
 * @param jobStatusFilterValues
 */

export class ExtractReadyPayloadFilter extends PayloadFilter{
    constructor(public gobiiExtractFilterType: GobiiExtractFilterType,
        public targetEntityUniqueId:GobiiFileItemCompoundId,
        public relatedEntityUniqueId:GobiiFileItemCompoundId,
        public relatedEntityFilterValue: string,
        public targetEntityFilterValue: string,
        public entityLasteUpdated: Date,
        public pagination: Pagination,
        public jobStatusFilterValues: JobTypeFilters
    ) {
        super(
            gobiiExtractFilterType, targetEntityUniqueId, relatedEntityUniqueId,
            relatedEntityFilterValue, targetEntityFilterValue, entityLasteUpdated,
            pagination
        );

    }
}

