import {EntityFilter} from "./type-entity-filter";
import {EntityType, EntitySubType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {NameIdLabelType} from "./name-id-label-type";
import {ExtractorItemType} from "./type-extractor-item";
import {GobiiFileItemCompoundId} from "./gobii-file-item-compound-id";

/**
 * Created by Phil on 3/9/2017.
 */

export class FileItemParams extends GobiiFileItemCompoundId {

    private constructor(private _queryName: string = null,
                        _entityType: EntityType = EntityType.UNKNOWN,
                        private _entityFilter: EntityFilter = EntityFilter.NONE,
                        private _fkEntityFilterValue: string = null,
                        private _selectedItemId: string = null,
                        _entitySubType: EntitySubType = EntitySubType.UNKNOWN,
                        _cvFilterType: CvFilterType = CvFilterType.UNKNOWN,
                        private _gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN,
                        private _nameIdLabelType: NameIdLabelType,
                        _extractorItemType: ExtractorItemType,
                        private _parentNameIdRequestParams: FileItemParams,
                        private _childNameIdRequestParams: FileItemParams[],
                        private _isDynamicFilterValue:boolean) {

        super(_extractorItemType,_entityType,_entitySubType,_cvFilterType);


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
            [],
            true));
    }

    getQueryName(): string {
        return this._queryName;
    }


    getExtractorItemType(): ExtractorItemType {
        return super.getExtractorItemType();
    }

    setExtractorItemType(value: ExtractorItemType): FileItemParams {

        super.setExtractorItemType(value);
        return this;
    }

    getEntityType(): EntityType {
        return super.getEntityType();
    }

    setEntityType(value: EntityType): FileItemParams {

        super.setEntityType(value);
        return this;
    }

    getEntitySubType(): EntitySubType {
        return super.getEntitySubType();
    }

    setEntitySubType(value: EntitySubType): FileItemParams {

        super.setEntitySubType(value);
        return this;
    }

    getCvFilterType(): CvFilterType {
        return super.getCvFilterType();
    }

    setCvFilterType(value: CvFilterType): FileItemParams {
        super.setCvFilterType(value);
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


    getGobiiExtractFilterType(): GobiiExtractFilterType {
        return this._gobiiExtractFilterType;
    }

    setGobiiExtractFilterType(value: GobiiExtractFilterType): FileItemParams {
        this._gobiiExtractFilterType = value;
        return this;
    }


    setNameIdLabelType(nameIdLabelType: NameIdLabelType) {
        this._nameIdLabelType = nameIdLabelType;
        return this;
    }

    getMameIdLabelType(): NameIdLabelType {
        return this._nameIdLabelType;
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

    setIsDynamicFilterValue(dynamicFilterValue:boolean): FileItemParams {
        this._isDynamicFilterValue = dynamicFilterValue;
        return this;
    }

    getIsDynamicFilterValue(): boolean {
        return this._isDynamicFilterValue;
    }


}