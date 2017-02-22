import {EntityType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";

export enum ExtractorItemType {Category, Entity}
export enum ExtractorCategoryType {Container, MarkerList, SampleList}


export class ExtractorSubmissionItem {

    constructor(itemType: ExtractorItemType) {
        this._itemType= itemType;
    }

    private _itemType: ExtractorItemType;
    private _categoryType: ExtractorCategoryType;
    private _categoryName: string;
    private _entityType: EntityType;
    private _cvFilterType: CvFilterType;
    private _childEntityTypes: EntityType[];
    private _childCattegoryTypes: ExtractorCategoryType[];
    private _required: boolean;

    public static build(itemType: ExtractorItemType): ExtractorSubmissionItem {
        return new ExtractorSubmissionItem(itemType);
    }

    getItemType(): ExtractorItemType {
        return this._itemType;
    }

    setItemType(value: ExtractorItemType): ExtractorSubmissionItem {
        this._itemType = value;
        return this;
    }

    getCategoryType(): ExtractorCategoryType {
        return this._categoryType;
    }

    setCategoryType(value: ExtractorCategoryType): ExtractorSubmissionItem {
        this._categoryType = value;
        return this;
    }

    getCategoryName(): string {
        return this._categoryName;
    }

    setCategoryName(value: string): ExtractorSubmissionItem {
        this._categoryName = value;
        return this;
    }

    getEntityType(): EntityType {
        return this._entityType;
    }

    setEntityType(value: EntityType): ExtractorSubmissionItem {
        this._entityType = value;
        return this;
    }

    getCvFilterType(): CvFilterType {
        return this._cvFilterType;
    }

    setCvFilterType(value: CvFilterType): ExtractorSubmissionItem {
        this._cvFilterType = value;
        return this;
    }

    getChildEntityTypes(): EntityType[] {
        return this._childEntityTypes;
    }

    setChildEntityTypes(value: EntityType[]): ExtractorSubmissionItem {
        this._childEntityTypes = value;
        return this;
    }

    getChildCattegoryTypes(): ExtractorCategoryType[] {
        return this._childCattegoryTypes;
    }

    setChildCattegoryTypes(value: ExtractorCategoryType[]): ExtractorSubmissionItem {
        this._childCattegoryTypes = value;
        return this;
    }

    getRequired(): boolean {
        return this._required;
    }

    setRequired(value: boolean): ExtractorSubmissionItem {
        this._required = value;
        return this;
    }
}   