import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {Pagination} from "../../model/payload/pagination";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";

export class PayloadFilter {
    constructor(public gobiiExtractFilterType: GobiiExtractFilterType,
                public targetEntityUniqueId:GobiiFileItemCompoundId,
                public relatedEntityUniqueId:GobiiFileItemCompoundId,
                public relatedEntityFilterValue: string,
                public targetEntityFilterValue: string,
                public entityLasteUpdated: Date,
                public pagination: Pagination) {

    }
}