import {EntityType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";

export enum ExtractorItemType {CATEGORY,
                                ENTITY,
                                MARKER_LIST,
                                SAMPLE_LIST,
                                EXPORT_FORMAT,
                                CROP_TYPE}

export enum ExtractorCategoryType {CONTAINER, LEAF }
export enum CardinalityType {ZERO_OR_ONE, ZERO_OR_MORE, ONE_ONLY, ONE_OR_MORE , MORE_THAN_ONE}


export class StatusTreeTemplate {

    constructor(itemType: ExtractorItemType) {
        this._itemType = itemType;
    }

    private _children: StatusTreeTemplate[] = [];
    private _alternatePeerTypes: EntityType[] = [];
    private _cardinality: CardinalityType = CardinalityType.ZERO_OR_MORE;
    private _itemType: ExtractorItemType = ExtractorItemType.ENTITY;
    private _categoryType: ExtractorCategoryType = ExtractorCategoryType.LEAF;
    private _categoryName: string;
    private _entityType: EntityType = EntityType.UNKNOWN;
    private _entityName: string;
    private _cvFilterType: CvFilterType = CvFilterType.UKNOWN;


    public static build(itemType: ExtractorItemType): StatusTreeTemplate {
        return new StatusTreeTemplate(itemType);
    }


    getAlternatePeerTypes(): EntityType[] {
        return this._alternatePeerTypes;
    }

    setAlternatePeerTypes(value: EntityType[]): StatusTreeTemplate {
        this._alternatePeerTypes = value;
        return this;
    }

    getCardinality(): CardinalityType {
        return this._cardinality;
    }

    setCardinality(value: CardinalityType): StatusTreeTemplate {
        this._cardinality = value;
        return this;
    }

    getChildren(): StatusTreeTemplate[] {
        return this._children;
    }

    addChild(child: StatusTreeTemplate): StatusTreeTemplate {
        this._children.push(child);
        return this;
    }

    getItemType(): ExtractorItemType {
        return this._itemType;
    }

    setItemType(value: ExtractorItemType): StatusTreeTemplate {
        this._itemType = value;
        return this;
    }

    getCategoryType(): ExtractorCategoryType {
        return this._categoryType;
    }

    setCategoryType(value: ExtractorCategoryType): StatusTreeTemplate {
        this._categoryType = value;
        return this;
    }

    getCategoryName(): string {
        return this._categoryName;
    }

    setCategoryName(value: string): StatusTreeTemplate {
        this._categoryName = value;
        return this;
    }

    getEntityType(): EntityType {
        return this._entityType;
    }

    setEntityType(value: EntityType): StatusTreeTemplate {
        this._entityType = value;
        return this;
    }

    getEntityName(): string {
        return this._entityName;
    }

    setEntityName(value: string): StatusTreeTemplate {
        this._entityName = value;
        return this;
    }

    getCvFilterType(): CvFilterType {
        return this._cvFilterType;
    }

    setCvFilterType(value: CvFilterType): StatusTreeTemplate {
        this._cvFilterType = value;
        return this;
    }

}