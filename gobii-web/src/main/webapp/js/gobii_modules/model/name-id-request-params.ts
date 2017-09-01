import {EntityFilter} from "./type-entity-filter";
import {EntityType, EntitySubType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {NameIdLabelType} from "./name-id-label-type";
import {ExtractorItemType} from "./file-model-node";

/**
 * Created by Phil on 3/9/2017.
 */

export class NameIdRequestParams {

    private constructor(private _queryName: string = null,
                        private _entityType: EntityType = EntityType.UNKNOWN,
                        private _entityFilter: EntityFilter = EntityFilter.NONE,
                        private _fkEntityFilterValue: string = null,
                        private _selectedItemId: string = null,
                        private _entitySubType: EntitySubType = EntitySubType.UNKNOWN,
                        private _cvFilterType: CvFilterType = CvFilterType.UNKNOWN,
                        private _gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN,
                        private _nameIdLabelType: NameIdLabelType,
                        private _extractorItemType: ExtractorItemType,
                        private _parentNameIdRequestParams: NameIdRequestParams,
                        private _childNameIdRequestParams: NameIdRequestParams[]) {

    }

    public static build(queryName: string,
                        gobiiExtractFilterType: GobiiExtractFilterType,
                        entityType: EntityType): NameIdRequestParams {
        return ( new NameIdRequestParams(queryName,
            entityType,
            EntityFilter.NONE,
            null,
            null,
            EntitySubType.UNKNOWN,
            CvFilterType.UNKNOWN,
            gobiiExtractFilterType,
            NameIdLabelType.UNKNOWN,
            ExtractorItemType.ENTITY,
            null,
            []));
    }


    getEntityType(): EntityType {
        return this._entityType;
    }

    setEntityType(value: EntityType): NameIdRequestParams {
        this._entityType = value;
        return this;
    }

    getEntityFilter(): EntityFilter {
        return this._entityFilter;
    }

    setEntityFilter(value: EntityFilter): NameIdRequestParams {
        this._entityFilter = value;
        return this;
    }

    getFkEntityFilterValue(): string {
        return this._fkEntityFilterValue;
    }

    setFkEntityFilterValue(value: string): NameIdRequestParams {
        this._fkEntityFilterValue = value;
        return this;
    }

    getSelectedItemId(): string {
        return this._selectedItemId;
    }

    setSelectedItemId(id:string): NameIdRequestParams {
        this._selectedItemId = id;
        return this;
    }

    getEntitySubType(): EntitySubType {
        return this._entitySubType;
    }

    setEntitySubType(value: EntitySubType): NameIdRequestParams {
        this._entitySubType = value;
        return this;
    }

    getCvFilterType(): CvFilterType {
        return this._cvFilterType;
    }

    setCvFilterType(value: CvFilterType): NameIdRequestParams {
        this._cvFilterType = value;
        return this;
    }

    getGobiiExtractFilterType(): GobiiExtractFilterType {
        return this._gobiiExtractFilterType;
    }

    setGobiiExtractFilterType(value: GobiiExtractFilterType): NameIdRequestParams {
        this._gobiiExtractFilterType = value;
        return this;
    }


    getQueryName(): string {
        return this._queryName;
    }


    setMameIdLabelType(nameIdLabelType: NameIdLabelType) {
        this._nameIdLabelType = nameIdLabelType;
        return this;
    }

    getMameIdLabelType(): NameIdLabelType {
        return this._nameIdLabelType;
    }

    setExtractorItemType(extractorItemType: ExtractorItemType) {
        this._extractorItemType = extractorItemType;
        return this;
    }

    getExtractorItemType(): ExtractorItemType {
        return this._extractorItemType;
    }

    setParentNameIdRequestParams(nameIdRequestParams:NameIdRequestParams):NameIdRequestParams {
        this._parentNameIdRequestParams = nameIdRequestParams;
        return this;
    }

    getParentNameIdRequestParams():NameIdRequestParams {
        return this;
    }

    getChildNameIdRequestParams(): NameIdRequestParams[] {
        return this._childNameIdRequestParams;
    }

    setChildNameIdRequestParams(childNameIdRequestParams: NameIdRequestParams[]): NameIdRequestParams {
        this._childNameIdRequestParams = childNameIdRequestParams;
        return this;
    }


}