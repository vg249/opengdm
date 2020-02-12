System.register(["./type-process", "./guid", "./type-entity", "./cv-filter-type", "./type-extractor-filter", "./type-extractor-item", "./gobii-file-item-compound-id", "./gobii-file-item-entity-relation"], function (exports_1, context_1) {
    "use strict";
    var type_process_1, guid_1, type_entity_1, cv_filter_type_1, type_extractor_filter_1, type_extractor_item_1, gobii_file_item_compound_id_1, gobii_file_item_entity_relation_1, GobiiFileItem;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (guid_1_1) {
                guid_1 = guid_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            },
            function (gobii_file_item_entity_relation_1_1) {
                gobii_file_item_entity_relation_1 = gobii_file_item_entity_relation_1_1;
            }
        ],
        execute: function () {
            GobiiFileItem = class GobiiFileItem extends gobii_file_item_compound_id_1.GobiiFileItemCompoundId {
                constructor(_gobiiExtractFilterType, _processType, _extractorItemType, _entityType, _entitySubType, _cvFilterType, _cvFilterValue, _itemId, _itemName, _selected, _required, _parentItemId, _entity = null, _entityRelations = [], _hasEntity = false, _pageNumber, _isEphemeral = false) {
                    super(_extractorItemType, _entityType, _entitySubType, _cvFilterType, _cvFilterValue);
                    this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    this._processType = _processType;
                    this._itemId = _itemId;
                    this._itemName = _itemName;
                    this._selected = _selected;
                    this._required = _required;
                    this._parentItemId = _parentItemId;
                    this._entity = _entity;
                    this._entityRelations = _entityRelations;
                    this._hasEntity = _hasEntity;
                    this._pageNumber = _pageNumber;
                    this._isEphemeral = _isEphemeral;
                    this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    this._processType = _processType;
                    this._itemId = _itemId;
                    this._itemName = _itemName;
                    this._selected = _selected;
                    this._required = _required;
                    this._parentItemId = _parentItemId;
                    this._entityRelations = _entityRelations;
                    this._entity = _entity;
                    this._hasEntity = _hasEntity;
                    this._pageNumber = _pageNumber;
                    this._isEphemeral = _isEphemeral;
                    this._fileItemUniqueId = guid_1.Guid.generateUUID();
                }
                static build(gobiiExtractFilterType, processType) {
                    let returnVal = new GobiiFileItem(gobiiExtractFilterType, processType, type_extractor_item_1.ExtractorItemType.UNKNOWN, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null, null, null, null, null, null, [], [], false, 0, false);
                    return returnVal;
                }
                setFileItemUniqueId(fileItemUniqueId) {
                    this._fileItemUniqueId = fileItemUniqueId;
                    return this;
                }
                getFileItemUniqueId() {
                    return this._fileItemUniqueId;
                }
                getGobiiExtractFilterType() {
                    return this._gobiiExtractFilterType;
                }
                setGobiiExtractFilterType(value) {
                    if (value != null) {
                        this._gobiiExtractFilterType = value;
                    }
                    else {
                        this._gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    }
                    return this;
                }
                getProcessType() {
                    return this._processType;
                }
                setProcessType(value) {
                    if (value != null) {
                        this._processType = value;
                    }
                    else {
                        this._processType = type_process_1.ProcessType.UNKNOWN;
                    }
                    return this;
                }
                getExtractorItemType() {
                    return super.getExtractorItemType();
                }
                setExtractorItemType(value) {
                    super.setExtractorItemType(value);
                    return this;
                }
                getEntityType() {
                    return super.getEntityType();
                }
                setEntityType(value) {
                    super.setEntityType(value);
                    return this;
                }
                getEntitySubType() {
                    return super.getEntitySubType();
                }
                setEntitySubType(value) {
                    super.setEntitySubType(value);
                    return this;
                }
                getCvFilterType() {
                    return super.getCvFilterType();
                }
                setCvFilterType(value) {
                    super.setCvFilterType(value);
                    return this;
                }
                getCvFilterValue() {
                    return super.getCvFilterValue();
                }
                setCvFilterValue(value) {
                    super.setCvFilterValue(value);
                    return this;
                }
                getIsExtractCriterion() {
                    return super.getIsExtractCriterion();
                }
                setIsExtractCriterion(value) {
                    super.setIsExtractCriterion(value);
                    return this;
                }
                getItemId() {
                    return this._itemId;
                }
                setItemId(value) {
                    this._itemId = value;
                    return this;
                }
                getItemName() {
                    return this._itemName;
                }
                setItemName(value) {
                    this._itemName = value;
                    return this;
                }
                getSelected() {
                    return this._selected;
                }
                setSelected(value) {
                    this._selected = value;
                    return this;
                }
                getRequired() {
                    return this._required;
                }
                setRequired(value) {
                    this._required = value;
                    return this;
                }
                getParentItemId() {
                    return this._parentItemId;
                }
                setParentItemId(parentIteIid) {
                    this._parentItemId = parentIteIid;
                    return this;
                }
                setEntity(entity) {
                    this._entity = entity;
                    this._hasEntity = true;
                    return this;
                }
                getEntity() {
                    return this._entity;
                }
                hasEntity() {
                    return this._hasEntity;
                }
                withRelatedEntityValue(gobiiFileItemEntityRelation, relatedId) {
                    let existingGobiiFileItemEntityRelation = this._entityRelations.find(er => er.compoundIdeEquals(gobiiFileItemEntityRelation));
                    if (existingGobiiFileItemEntityRelation) {
                        existingGobiiFileItemEntityRelation.setRelatedEntityId(relatedId);
                    }
                    else {
                        this._entityRelations.push((new gobii_file_item_entity_relation_1.GobiiFileItemEntityRelation).setRelatedEntityId(relatedId));
                    }
                    return this;
                }
                withRelatedEntity(newGobiiFileItemEntityRelation) {
                    let existingGobiiFileItemEntityRelation = this._entityRelations.find(er => er.compoundIdeEquals(newGobiiFileItemEntityRelation));
                    if (existingGobiiFileItemEntityRelation) {
                        existingGobiiFileItemEntityRelation.setRelatedEntityId(newGobiiFileItemEntityRelation.getRelatedEntityId());
                    }
                    else {
                        this._entityRelations.push(newGobiiFileItemEntityRelation);
                    }
                    return this;
                }
                getRelatedEntityFilterValue(compoundUniqueId) {
                    let returnVal = null;
                    let gobiiFileItemEntityRelation = this._entityRelations.find(er => er.compoundIdeEquals(compoundUniqueId));
                    if (gobiiFileItemEntityRelation) {
                        returnVal = gobiiFileItemEntityRelation.getRelatedEntityId();
                    }
                    return returnVal;
                }
                getRelatedEntities() {
                    return this._entityRelations;
                }
                setPageNumber(value) {
                    this._pageNumber = value;
                    return this;
                }
                getPageNumber() {
                    return this._pageNumber;
                }
                getIsEphemeral() {
                    return this._isEphemeral;
                }
                setIsEphemeral(value) {
                    this._isEphemeral = value;
                    return this;
                }
            }; // GobiiFileItem()
            exports_1("GobiiFileItem", GobiiFileItem);
        }
    };
});
//# sourceMappingURL=gobii-file-item.js.map