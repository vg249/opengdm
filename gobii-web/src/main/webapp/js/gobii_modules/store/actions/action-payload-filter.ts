import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {Pagination} from "../../model/payload/pagination";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";

export class PayloadFilter {
    constructor(public gobiiExtractFilterType: GobiiExtractFilterType,
                public gobiiCompoundUniqueId:GobiiFileItemCompoundId,
                public filterValue: string,
                public entityLasteUpdated: Date,
                public pagination: Pagination) {

    }
}