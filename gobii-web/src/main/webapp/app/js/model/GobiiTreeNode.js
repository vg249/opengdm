System.register(["./guid", "./type-extractor-filter", "./gobii-file-item-compound-id"], function (exports_1, context_1) {
    "use strict";
    var guid_1, type_extractor_filter_1, gobii_file_item_compound_id_1, ContainerType, GobiiTreeNode;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (guid_1_1) {
                guid_1 = guid_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            }
        ],
        execute: function () {
            (function (ContainerType) {
                ContainerType[ContainerType["NONE"] = 0] = "NONE";
                ContainerType[ContainerType["STRUCTURE"] = 1] = "STRUCTURE";
                ContainerType[ContainerType["DATA"] = 2] = "DATA";
            })(ContainerType || (ContainerType = {}));
            exports_1("ContainerType", ContainerType);
            GobiiTreeNode = class GobiiTreeNode extends gobii_file_item_compound_id_1.GobiiFileItemCompoundId {
                constructor(parent, fileItemId, required) {
                    super();
                    this.children = [];
                    //GOBII UI properties
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.required = false;
                    this.active = false;
                    this.containerType = ContainerType.NONE;
                    this.id = guid_1.Guid.generateUUID();
                    this.parent = parent;
                    this.fileItemId = fileItemId;
                    this.required = required;
                    this.selectable = false; // for now all nodes are not selectable
                    //(TreeNode)
                }
                static build(gobiiExtractFilterType, extractoItemType) {
                    let returnVal = new GobiiTreeNode(null, null, null);
                    returnVal.gobiiExtractFilterType = gobiiExtractFilterType;
                    returnVal.setItemType(extractoItemType);
                    return returnVal;
                } //build
                getId() {
                    return this.id;
                }
                //unique identifiers
                getItemType() {
                    return super.getExtractorItemType();
                }
                setItemType(value) {
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
                setGobiiExtractFilterType(gobiiExtractFilterType) {
                    this.gobiiExtractFilterType = gobiiExtractFilterType;
                    return this;
                }
                getGobiiExtractFilterType() {
                    return this.gobiiExtractFilterType;
                }
                getActive() {
                    return this.active;
                }
                setActive(value) {
                    this.active = value;
                    return this;
                }
                getLabel() {
                    return this.label;
                }
                setLabel(value) {
                    this.label = value;
                    return this;
                }
                getGenericLabel() {
                    return this.genericLabel;
                }
                setGenericLabel(value) {
                    this.genericLabel = value;
                    return this;
                }
                resetLabel() {
                    this.label = this.genericLabel;
                }
                getData() {
                    return this.data;
                }
                setData(value) {
                    this.data = value;
                    return this;
                }
                getIcon() {
                    return this.icon;
                }
                setIcon(value) {
                    this.icon = value;
                    return this;
                }
                getExpandedIcon() {
                    return this.expandedIcon;
                }
                setExpandedIcon(value) {
                    this.expandedIcon = value;
                    return this;
                }
                getCollapsedIcon() {
                    return this.collapsedIcon;
                }
                setCollapsedIcon(value) {
                    this.collapsedIcon = value;
                    return this;
                }
                getChildren() {
                    return this.children;
                }
                setChildren(value) {
                    this.children = value;
                    return this;
                }
                getLeaf() {
                    return this.leaf;
                }
                setLeaf(value) {
                    this.leaf = value;
                    return this;
                }
                getExpanded() {
                    return this.expanded;
                }
                setExpanded(value) {
                    this.expanded = value;
                    return this;
                }
                getType() {
                    return this.type;
                }
                setType(value) {
                    this.type = value;
                    return this;
                }
                getPartialSelected() {
                    return this.partialSelected;
                }
                setPartialSelected(value) {
                    this.partialSelected = value;
                    return this;
                }
                getFileItemId() {
                    return this.fileItemId;
                }
                setFileItemId(value) {
                    this.fileItemId = value;
                    return this;
                }
                getRequired() {
                    return this.required;
                }
                setRequired(value) {
                    this.required = value;
                    return this;
                }
                getContainerType() {
                    return this.containerType;
                }
                setContainerType(value) {
                    this.containerType = value;
                    return this;
                }
            };
            exports_1("GobiiTreeNode", GobiiTreeNode);
        }
    };
});
//# sourceMappingURL=GobiiTreeNode.js.map