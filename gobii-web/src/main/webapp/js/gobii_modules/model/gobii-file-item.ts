import {ProcessType} from "./type-process";
import {TreeNode} from "primeng/components/common/api";
import {Guid} from "./guid";
import {EntityType, EntitySubType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {GobiiUIEventOrigin} from "./type-event-origin";
import {ExtractorItemType} from "./type-extractor-item";

export class GobiiFileItem {

    private _fileItemUniqueId: string;

    private constructor(private _gobiiExtractFilterType: GobiiExtractFilterType,
                        private _processType: ProcessType,
                        private _extractorItemType: ExtractorItemType,
                        private _entityType: EntityType,
                        private _entitySubType: EntitySubType,
                        private _cvFilterType: CvFilterType,
                        private _itemId: string,
                        private _itemName: string,
                        private _selected: boolean,
                        private _required: boolean,
                        private _parentItemId: string,
                        private _parentEntityType: EntityType) {

        this._gobiiExtractFilterType = _gobiiExtractFilterType;
        this._processType = _processType;
        this._entityType = _entityType;
        this._entitySubType = _entitySubType;
        this._extractorItemType = _extractorItemType;
        this._cvFilterType = _cvFilterType;
        this._itemId = _itemId;
        this._itemName = _itemName;
        this._selected = _selected;
        this._required = _required;
        this._parentItemId = _parentItemId;
        this._parentEntityType = _parentEntityType;

        this._fileItemUniqueId = Guid.generateUUID();

        if (this._cvFilterType === null) {
            this._cvFilterType = CvFilterType.UNKNOWN;
        }

        if (this._extractorItemType == null) {
            this._extractorItemType = ExtractorItemType.UNKNOWN;
        }

        if (this._entityType == null) {
            this._entityType = EntityType.UNKNOWN;
        }

        if (this._entitySubType == null) {
            this._entitySubType = EntitySubType.UNKNOWN;
        }

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


    public equals(gobiiFileItemToCompare: GobiiFileItem) {
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
        return this._extractorItemType;
    }

    setExtractorItemType(value: ExtractorItemType): GobiiFileItem {

        if (value != null) {
            this._extractorItemType = value;
        } else {
            this._extractorItemType = ExtractorItemType.UNKNOWN;
        }
        return this;
    }

    getEntityType(): EntityType {
        return this._entityType;
    }

    setEntityType(value: EntityType): GobiiFileItem {

        if (value != null) {
            this._entityType = value;
        } else {
            this._entityType = EntityType.UNKNOWN;
        }
        return this;
    }

    getEntitySubType(): EntitySubType {
        return this._entitySubType;
    }

    setEntitySubType(value: EntitySubType): GobiiFileItem {

        if (value != null) {
            this._entitySubType = value;
        } else {
            this._entitySubType = EntitySubType.UNKNOWN;
        }

        return this;
    }

    getCvFilterType(): CvFilterType {
        return this._cvFilterType;
    }

    setCvFilterType(value: CvFilterType): GobiiFileItem {
        if (value != null) {
            this._cvFilterType = value;
        } else {
            this._cvFilterType = CvFilterType.UNKNOWN;
        }
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
