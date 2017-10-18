import {ProcessType} from "./type-process";
import {Guid} from "./guid";
import {EntitySubType, EntityType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {ExtractorItemType} from "./type-extractor-item";
import {GobiiFileItemCompoundId} from "./gobii-file-item-compound-id";

export class GobiiFileItem extends GobiiFileItemCompoundId {

    private _fileItemUniqueId: string;

    private constructor(private _gobiiExtractFilterType: GobiiExtractFilterType,
                        private _processType: ProcessType,
                        _extractorItemType: ExtractorItemType,
                        _entityType: EntityType,
                        _entitySubType: EntitySubType,
                        _cvFilterType: CvFilterType,
                        private _itemId: string,
                        private _itemName: string,
                        private _selected: boolean,
                        private _required: boolean,
                        private _parentItemId: string,
                        private _parentEntityType: EntityType) {

        super(_extractorItemType,_entityType,_entitySubType,_cvFilterType);


        this._gobiiExtractFilterType = _gobiiExtractFilterType;
        this._processType = _processType;
        this._itemId = _itemId;
        this._itemName = _itemName;
        this._selected = _selected;
        this._required = _required;
        this._parentItemId = _parentItemId;
        this._parentEntityType = _parentEntityType;

        this._fileItemUniqueId = Guid.generateUUID();
    }

    public static build(gobiiExtractFilterType: GobiiExtractFilterType,
                        processType: ProcessType): GobiiFileItem {

        let returnVal: GobiiFileItem = new GobiiFileItem(
            gobiiExtractFilterType,
            processType,
            ExtractorItemType.UNKNOWN,
            EntityType.UNKNOWN,
            EntitySubType.UNKNOWN,
            CvFilterType.UNKNOWN,
            null,
            null,
            null,
            null,
            null,
            EntityType.UNKNOWN
        );


        return returnVal;
    }


    public compoundIdeEquals(gobiiFileItemToCompare: GobiiFileItem) {
        return (
            this.getExtractorItemType() === gobiiFileItemToCompare.getExtractorItemType() &&
            this.getEntityType() === gobiiFileItemToCompare.getEntityType() &&
            this.getEntitySubType() === gobiiFileItemToCompare.getEntitySubType() &&
            this.getCvFilterType() === gobiiFileItemToCompare.getCvFilterType() &&
            this.getItemId() === gobiiFileItemToCompare.getItemId()
        );
    }

    setFileItemUniqueId(fileItemUniqueId: string): GobiiFileItem {
        this._fileItemUniqueId = fileItemUniqueId;
        return this;
    }

    getFileItemUniqueId(): string {
        return this._fileItemUniqueId;
    }

    getGobiiExtractFilterType(): GobiiExtractFilterType {
        return this._gobiiExtractFilterType;
    }

    setGobiiExtractFilterType(value: GobiiExtractFilterType): GobiiFileItem {

        if (value != null) {
            this._gobiiExtractFilterType = value;
        } else {
            this._gobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
        }
        return this;
    }

    getProcessType(): ProcessType {
        return this._processType;
    }

    setProcessType(value: ProcessType): GobiiFileItem {

        if (value != null) {
            this._processType = value;
        } else {
            this._processType = ProcessType.UNKNOWN;
        }

        return this;
    }


    getExtractorItemType(): ExtractorItemType {
        return super.getExtractorItemType();
    }

    setExtractorItemType(value: ExtractorItemType): GobiiFileItem {

        super.setExtractorItemType(value);
        return this;
    }

    getEntityType(): EntityType {
        return super.getEntityType();
    }

    setEntityType(value: EntityType): GobiiFileItem {

        super.setEntityType(value);
        return this;
    }

    getEntitySubType(): EntitySubType {
        return super.getEntitySubType();
    }

    setEntitySubType(value: EntitySubType): GobiiFileItem {

        super.setEntitySubType(value);
        return this;
    }

    getCvFilterType(): CvFilterType {
        return super.getCvFilterType();
    }

    setCvFilterType(value: CvFilterType): GobiiFileItem {
        super.setCvFilterType(value);
        return this;
    }

    getItemId(): string {
        return this._itemId;
    }

    setItemId(value: string): GobiiFileItem {
        this._itemId = value;
        return this;
    }

    getItemName(): string {
        return this._itemName;
    }

    setItemName(value: string): GobiiFileItem {
        this._itemName = value;
        return this;
    }

    getSelected(): boolean {
        return this._selected;
    }

    setSelected(value: boolean): GobiiFileItem {
        this._selected = value;
        return this;
    }

    getRequired(): boolean {
        return this._required;
    }

    setRequired(value: boolean): GobiiFileItem {
        this._required = value;
        return this;
    }

    getParentItemId(): string {
        return this._parentItemId;
    }

    setParentItemId(parentIteIid: string): GobiiFileItem {
        this._parentItemId = parentIteIid;
        return this;
    }

    getParentEntityType(): EntityType {
        return this._parentEntityType;
    }

    setParentEntityType(parentIteIid: EntityType): GobiiFileItem {
        this._parentEntityType = parentIteIid;
        return this;
    }

} // GobiiFileItem()
