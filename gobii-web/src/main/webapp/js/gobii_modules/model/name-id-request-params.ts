import {EntityFilter} from "./type-entity-filter";
import {EntityType, EntitySubType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {NameIdLabelType} from "./name-id-label-type";
import {ExtractorItemType} from "./file-model-node";

/**
 * Created by Phil on 3/9/2017.
 */

export class FileItemParams {

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
                        private _parentNameIdRequestParams: FileItemParams,
                        private _childNameIdRequestParams: FileItemParams[]) {

    }

    public static build(queryName: string,
                        gobiiExtractFilterType: GobiiExtractFilterType,
                        entityType: EntityType): FileItemParams {
        return ( new FileItemParams(queryName,
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

    setEntityType(value: EntityType): FileItemParams {
        this._entityType = value;
        return this;
    }

    getEntityFilter(): EntityFilter {
        return this._entityFilter;
    }

    setEntityFilter(value: EntityFilter): FileItemParams {
        this._entityFilter = value;
        return this;
    }

    getFkEntityFilterValue(): string {
        return this._fkEntityFilterValue;
    }

    setFkEntityFilterValue(value: string): FileItemParams {
        this._fkEntityFilterValue = value;
        return this;
    }

    getSelectedItemId(): string {
        return this._selectedItemId;
    }

    setSelectedItemId(id: string): FileItemParams {
        this._selectedItemId = id;
        return this;
    }

    getEntitySubType(): EntitySubType {
        return this._entitySubType;
    }

    setEntitySubType(value: EntitySubType): FileItemParams {
        this._entitySubType = value;
        return this;
    }

    getCvFilterType(): CvFilterType {
        return this._cvFilterType;
    }

    setCvFilterType(value: CvFilterType): FileItemParams {
        this._cvFilterType = value;
        return this;
    }

    getGobiiExtractFilterType(): GobiiExtractFilterType {
        return this._gobiiExtractFilterType;
    }

    setGobiiExtractFilterType(value: GobiiExtractFilterType): FileItemParams {
        this._gobiiExtractFilterType = value;
        return this;
    }


    getQueryName(): string {
        return this._queryName;
    }


    setNameIdLabelType(nameIdLabelType: NameIdLabelType) {
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

    setParentNameIdRequestParams(nameIdRequestParams: FileItemParams): FileItemParams {
        this._parentNameIdRequestParams = nameIdRequestParams;
        return this;
    }

    getParentNameIdRequestParams(): FileItemParams {
        return this;
    }

    getChildNameIdRequestParams(): FileItemParams[] {
        return this._childNameIdRequestParams;
    }

    setChildNameIdRequestParams(childNameIdRequestParams: FileItemParams[]): FileItemParams {
        this._childNameIdRequestParams = childNameIdRequestParams;
        return this;
    }


}