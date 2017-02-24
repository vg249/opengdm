System.register(["@angular/core", "../model/GobiiTreeNode", "../model/type-entity", "../model/type-extractor-filter", "../model/extractor-submission-item", "../model/cv-filter-type"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, GobiiTreeNode_1, type_entity_1, type_extractor_filter_1, extractor_submission_item_1, cv_filter_type_1, StatusDisplayTreeComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (GobiiTreeNode_1_1) {
                GobiiTreeNode_1 = GobiiTreeNode_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (extractor_submission_item_1_1) {
                extractor_submission_item_1 = extractor_submission_item_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            }
        ],
        execute: function () {
            StatusDisplayTreeComponent = (function () {
                function StatusDisplayTreeComponent() {
                    this.onAddMessage = new core_1.EventEmitter();
                    // *****************************************************************
                    // *********************  TREE NODE DATA STRUCTURES AND EVENTS
                    this.demoTreeNodes = [];
                    this.selectedDemoNodes = [];
                    this.gobiiTreeNodes = [];
                    this.selectedGobiiNodes = [];
                    this.entityNodeLabels = new Map();
                    this.entitySubtypeNodeLabels = new Map();
                    this.cvFilterNodeLabels = new Map();
                    this.extractorFilterTypeLabels = new Map();
                    this.treeCategoryLabels = new Map();
                    // ********************************************************************************
                    // ********************* CHECKBOX (GOBII-SPECIFIC)  NODE DATA STRUCTURES AND EVENTS
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onItemChecked = new core_1.EventEmitter();
                    this.onItemSelected = new core_1.EventEmitter();
                    this.templates = new Map();
                }
                StatusDisplayTreeComponent.prototype.reportMessage = function (arg) {
                    this.onAddMessage.emit(arg);
                };
                StatusDisplayTreeComponent.prototype.ngOnInit = function () {
                    this.makeDemoTreeNodes();
                    this.setUpRequredItems();
                };
                StatusDisplayTreeComponent.prototype.getTemplates = function (gobiiExtractFilterType, forceReset) {
                    if (this.templates.size === 0 || forceReset === true) {
                        this.entityNodeLabels[type_entity_1.EntityType.DataSets] = "Data Sets";
                        this.entityNodeLabels[type_entity_1.EntityType.Platforms] = "Platforms";
                        this.entityNodeLabels[type_entity_1.EntityType.Mapsets] = "Mapsets";
                        this.cvFilterNodeLabels[cv_filter_type_1.CvFilterType.DATASET_TYPE] = "Dataset Type";
                        this.entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR] = "Principle Investigator";
                        this.entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_SUBMITED_BY] = "Submitted By";
                        this.extractorFilterTypeLabels[type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET] = "Extract by Dataset";
                        this.extractorFilterTypeLabels[type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE] = "Extract by Sample";
                        this.extractorFilterTypeLabels[type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER] = "Extract by Marker";
                        // **** FOR ALL EXTRACTION TYPES
                        var submissionItemsForAll = [];
                        submissionItemsForAll.push(extractor_submission_item_1.StatusTreeTemplate.build(extractor_submission_item_1.ExtractorItemType.ENTITY)
                            .setCategoryType(extractor_submission_item_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.Contacts)
                            .setEntityName(this.entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_SUBMITED_BY])
                            .setCardinality(extractor_submission_item_1.CardinalityType.ONE_ONLY));
                        submissionItemsForAll.push(extractor_submission_item_1.StatusTreeTemplate.build(extractor_submission_item_1.ExtractorItemType.EXPORT_FORMAT)
                            .setCategoryType(extractor_submission_item_1.ExtractorCategoryType.LEAF)
                            .setEntityName("Export Formats")
                            .setCardinality(extractor_submission_item_1.CardinalityType.ONE_ONLY));
                        submissionItemsForAll.push(extractor_submission_item_1.StatusTreeTemplate.build(extractor_submission_item_1.ExtractorItemType.ENTITY)
                            .setCategoryType(extractor_submission_item_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.Mapsets)
                            .setEntityName(this.entityNodeLabels[type_entity_1.EntityType.Mapsets])
                            .setCardinality(extractor_submission_item_1.CardinalityType.ZERO_OR_ONE));
                        // ******** SET UP extract by dataset
                        // -- Data set type
                        var submissionItemsForDataSet = [];
                        submissionItemsForDataSet = submissionItemsForDataSet.concat(submissionItemsForAll);
                        submissionItemsForDataSet.push(extractor_submission_item_1.StatusTreeTemplate.build(extractor_submission_item_1.ExtractorItemType.CATEGORY)
                            .setCategoryType(extractor_submission_item_1.ExtractorCategoryType.ENTITY_CONTAINER)
                            .setEntityType(type_entity_1.EntityType.DataSets)
                            .setCategoryName(this.entityNodeLabels[type_entity_1.EntityType.DataSets]));
                        // .addChild(
                        //     StatusTreeTemplate.build(ExtractorItemType.ENTITY)
                        //         .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                        //         .setEntityType(EntityType.DataSets)
                        //         .setEntityName(this.entityNodeLabels[EntityType.DataSets])
                        //         .setCardinality(CardinalityType.ONE_OR_MORE))
                        //            );
                        this.templates.set(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, submissionItemsForDataSet);
                        // ******** SET UP extract by samples
                        // -- Data set type
                        var submissionItemsForBySample = [];
                        submissionItemsForBySample = submissionItemsForBySample.concat(submissionItemsForAll);
                        submissionItemsForBySample.push(extractor_submission_item_1.StatusTreeTemplate.build(extractor_submission_item_1.ExtractorItemType.ENTITY)
                            .setCategoryType(extractor_submission_item_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.CvTerms)
                            .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                            .setEntityName(this.cvFilterNodeLabels[cv_filter_type_1.CvFilterType.DATASET_TYPE])
                            .setCardinality(extractor_submission_item_1.CardinalityType.ONE_ONLY));
                        // -- Platforms
                        submissionItemsForBySample.push(extractor_submission_item_1.StatusTreeTemplate.build(extractor_submission_item_1.ExtractorItemType.CATEGORY)
                            .setCategoryType(extractor_submission_item_1.ExtractorCategoryType.ENTITY_CONTAINER)
                            .setCategoryName(this.entityNodeLabels[type_entity_1.EntityType.Platforms])
                            .setCardinality(extractor_submission_item_1.CardinalityType.ZERO_OR_MORE)
                            .addChild(extractor_submission_item_1.StatusTreeTemplate.build(extractor_submission_item_1.ExtractorItemType.ENTITY)
                            .setCategoryType(extractor_submission_item_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.Platforms)
                            .setEntityName(this.entityNodeLabels[type_entity_1.EntityType.Platforms])
                            .setCardinality(extractor_submission_item_1.CardinalityType.ZERO_OR_MORE)));
                        // -- Samples
                        submissionItemsForBySample.push(extractor_submission_item_1.StatusTreeTemplate.build(extractor_submission_item_1.ExtractorItemType.CATEGORY)
                            .setCategoryType(extractor_submission_item_1.ExtractorCategoryType.CONTAINER)
                            .setCategoryName("Sample Crieria")
                            .setCardinality(extractor_submission_item_1.CardinalityType.ONE_OR_MORE)
                            .setAlternatePeerTypes([type_entity_1.EntityType.Projects, type_entity_1.EntityType.Contacts])
                            .addChild(extractor_submission_item_1.StatusTreeTemplate.build(extractor_submission_item_1.ExtractorItemType.ENTITY)
                            .setCategoryType(extractor_submission_item_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.Contacts)
                            .setEntityName("Principle Investigator")
                            .setCardinality(extractor_submission_item_1.CardinalityType.ZERO_OR_ONE))
                            .addChild(extractor_submission_item_1.StatusTreeTemplate.build(extractor_submission_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.Projects)
                            .setEntityName(this.entityNodeLabels[type_entity_1.EntityType.Projects])
                            .setCardinality(extractor_submission_item_1.CardinalityType.ZERO_OR_MORE))
                            .addChild(extractor_submission_item_1.StatusTreeTemplate.build(extractor_submission_item_1.ExtractorItemType.SAMPLE_LIST)
                            .setEntityName("Sample List")
                            .setCategoryName(this.entityNodeLabels[type_entity_1.EntityType.Platforms])
                            .setCardinality(extractor_submission_item_1.CardinalityType.ZERO_OR_MORE)));
                        this.templates.set(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, submissionItemsForBySample);
                    }
                    return this.templates.get(gobiiExtractFilterType);
                };
                StatusDisplayTreeComponent.prototype.nodeSelect = function (event) {
                    //      this.msgs.push({severity: 'info', summary: 'Node Selected', detail: event.node.label});
                };
                StatusDisplayTreeComponent.prototype.nodeUnselect = function (event) {
                    //        this.msgs.push({severity: 'info', summary: 'Node Unselected', detail: event.node.label});
                };
                StatusDisplayTreeComponent.prototype.nodeExpandMessage = function (event) {
                    //        this.msgs.push({severity: 'info', summary: 'Node Expanded', detail: event.node.label});
                };
                StatusDisplayTreeComponent.prototype.nodeExpand = function (event) {
                    if (event.node) {
                    }
                };
                StatusDisplayTreeComponent.prototype.viewFile = function (file) {
                    //      this.msgs.push({severity: 'info', summary: 'Node Selected with Right Click', detail: file.label});
                };
                StatusDisplayTreeComponent.prototype.unselectFile = function () {
                    //        this.selectedFile2 = null;
                };
                StatusDisplayTreeComponent.prototype.expandAll = function () {
                    var _this = this;
                    this.gobiiTreeNodes.forEach(function (node) {
                        _this.expandRecursive(node, true);
                    });
                };
                StatusDisplayTreeComponent.prototype.collapseAll = function () {
                    var _this = this;
                    this.gobiiTreeNodes.forEach(function (node) {
                        _this.expandRecursive(node, false);
                    });
                };
                StatusDisplayTreeComponent.prototype.expandRecursive = function (node, isExpand) {
                    var _this = this;
                    node.expanded = isExpand;
                    if (node.children) {
                        node.children.forEach(function (childNode) {
                            _this.expandRecursive(childNode, isExpand);
                        });
                    }
                };
                // ********************************************************************************
                // ********************* CHECKBOX/TREE NODE CONVERSION FUNCTIONS
                StatusDisplayTreeComponent.prototype.addEntityIconToNode = function (entityType, treeNode) {
                    if (entityType === type_entity_1.EntityType.DataSets) {
                        treeNode.icon = "fa-database";
                        treeNode.expandedIcon = "fa-database";
                        treeNode.collapsedIcon = "fa-database";
                    }
                    else if (entityType === type_entity_1.EntityType.Contacts) {
                        treeNode.icon = "fa-address-book-o";
                        treeNode.expandedIcon = "fa-address-book-o";
                        treeNode.collapsedIcon = "fa-address-book-o";
                    }
                    else if (entityType === type_entity_1.EntityType.Mapsets) {
                        treeNode.icon = "fa-map-o";
                        treeNode.expandedIcon = "fa-map-o";
                        treeNode.collapsedIcon = "fa-map-o";
                    }
                };
                StatusDisplayTreeComponent.prototype.addIconsToNode = function (statusTreeTemplate, treeNode) {
                    // if( statusTreeTemplate.getItemType() == ExtractorItemType.ENTITY ) {
                    if (statusTreeTemplate.getEntityType() != null
                        && statusTreeTemplate.getEntityType() != type_entity_1.EntityType.UNKNOWN) {
                        this.addEntityIconToNode(statusTreeTemplate.getEntityType(), treeNode);
                    }
                    else if (statusTreeTemplate.getItemType() === extractor_submission_item_1.ExtractorItemType.EXPORT_FORMAT) {
                        treeNode.icon = "fa-columns";
                        treeNode.expandedIcon = "fa-columns";
                        treeNode.collapsedIcon = "fa-columns";
                    }
                    else {
                        //     }
                        // } else if (statusTreeTemplate.getItemType() == ExtractorItemType.CATEGORY ) {
                        treeNode.icon = "fa-folder";
                        treeNode.expandedIcon = "fa-folder-expanded";
                        treeNode.collapsedIcon = "fa-folder";
                    }
                };
                StatusDisplayTreeComponent.prototype.makeCbEventFromNode = function (treeNode) {
                    var returnVal = null;
                    return returnVal;
                };
                StatusDisplayTreeComponent.prototype.findTemplate = function (extractorItemType, entityType) {
                    var returnVal = null;
                    var statusTreeTemplates = this.getTemplates(this.gobiiExtractFilterType, false);
                    for (var idx = 0; (idx < statusTreeTemplates.length) && (returnVal == null); idx++) {
                        var currentTemplate = statusTreeTemplates[idx];
                        returnVal = this.findTemplateByCriteria(currentTemplate, extractorItemType, entityType);
                    }
                    return returnVal;
                };
                StatusDisplayTreeComponent.prototype.findTemplateByCriteria = function (statusTreeTemplate, extractorItemType, entityType) {
                    var returnVal = null;
                    if (statusTreeTemplate.getChildren() != null) {
                        for (var idx = 0; (idx < statusTreeTemplate.getChildren().length) && (returnVal == null); idx++) {
                            var currentTemplate = statusTreeTemplate.getChildren()[idx];
                            returnVal = this.findTemplateByCriteria(currentTemplate, extractorItemType, entityType);
                        }
                    }
                    if (returnVal === null) {
                        if (extractorItemType == statusTreeTemplate.getItemType()
                            && entityType == statusTreeTemplate.getEntityType()) {
                            returnVal = statusTreeTemplate;
                        }
                    }
                    return returnVal;
                };
                StatusDisplayTreeComponent.prototype.addEntityNameToNode = function (statusTreeTemplate, gobiiTreeNode, checkBoxEvent) {
                    if (statusTreeTemplate.getCategoryType() === extractor_submission_item_1.ExtractorCategoryType.ENTITY_CONTAINER) {
                        gobiiTreeNode.label = checkBoxEvent.name;
                    }
                    else {
                        gobiiTreeNode.label += statusTreeTemplate.getEntityName() + ": " + checkBoxEvent.name;
                    }
                };
                StatusDisplayTreeComponent.prototype.placeNodeInTree = function (checkBoxEvent) {
                    var statusTreeTemplate = this.findTemplate(extractor_submission_item_1.ExtractorItemType.CATEGORY, checkBoxEvent.entityType);
                    if (statusTreeTemplate != null) {
                        if (statusTreeTemplate.getCategoryType() === extractor_submission_item_1.ExtractorCategoryType.LEAF) {
                            var existingGobiiTreeNode = statusTreeTemplate.getGobiiTreeNode();
                            this.addEntityNameToNode(statusTreeTemplate, existingGobiiTreeNode, checkBoxEvent);
                        }
                        else if (statusTreeTemplate.getCategoryType() === extractor_submission_item_1.ExtractorCategoryType.ENTITY_CONTAINER) {
                            var newGobiiTreeNode = new GobiiTreeNode_1.GobiiTreeNode();
                            newGobiiTreeNode.entityType = checkBoxEvent.entityType;
                            this.addEntityIconToNode(newGobiiTreeNode.entityType, newGobiiTreeNode);
                            this.addEntityNameToNode(statusTreeTemplate, newGobiiTreeNode, checkBoxEvent);
                            statusTreeTemplate.getGobiiTreeNode().children.push(newGobiiTreeNode);
                            statusTreeTemplate.getGobiiTreeNode().expanded = true;
                            this.selectedGobiiNodes.push(newGobiiTreeNode);
                            this.selectedGobiiNodes.push(statusTreeTemplate.getGobiiTreeNode());
                        }
                        else {
                            this.reportMessage("The node of category  "
                                + statusTreeTemplate.getCategoryType()
                                + " for checkbox event " + checkBoxEvent.name
                                + " could not be placed in the tree ");
                        }
                    }
                    else {
                        this.reportMessage("Could not place checkbox event "
                            + checkBoxEvent.name
                            + " in tree");
                    }
                }; //
                StatusDisplayTreeComponent.prototype.makeTreeNodeFromTemplate = function (statusTreeTemplate) {
                    var _this = this;
                    var returnVal = null;
                    if (statusTreeTemplate.getItemType() === extractor_submission_item_1.ExtractorItemType.ENTITY) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode();
                        returnVal.entityType = statusTreeTemplate.getEntityType();
                        returnVal.label = statusTreeTemplate.getEntityName();
                    }
                    else if (statusTreeTemplate.getItemType() === extractor_submission_item_1.ExtractorItemType.CATEGORY) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode();
                        if (statusTreeTemplate.getEntityType() != null
                            && statusTreeTemplate.getEntityType() != type_entity_1.EntityType.UNKNOWN) {
                            returnVal.entityType = statusTreeTemplate.getEntityType();
                        }
                        returnVal.label = statusTreeTemplate.getCategoryName();
                    }
                    else if (statusTreeTemplate.getItemType() == extractor_submission_item_1.ExtractorItemType.EXPORT_FORMAT) {
                        returnVal = new GobiiTreeNode_1.GobiiTreeNode();
                        returnVal.label = "Export Format";
                    }
                    this.addIconsToNode(statusTreeTemplate, returnVal);
                    if (null != returnVal) {
                        if ((statusTreeTemplate.getCategoryType() != extractor_submission_item_1.ExtractorCategoryType.ENTITY_CONTAINER
                            && statusTreeTemplate.getChildren() !== null)
                            && (statusTreeTemplate.getChildren().length > 0)) {
                            statusTreeTemplate.getChildren().forEach(function (stt) {
                                var currentTreeNode = _this.makeTreeNodeFromTemplate(stt);
                                if (null != currentTreeNode) {
                                    returnVal.children.push(currentTreeNode);
                                }
                            });
                        }
                    }
                    statusTreeTemplate.setGobiiTreeNode(returnVal);
                    return returnVal;
                };
                StatusDisplayTreeComponent.prototype.setUpRequredItems = function () {
                    var _this = this;
                    this.gobiiTreeNodes = [];
                    var statusTreeTemplates = this.getTemplates(this.gobiiExtractFilterType, false);
                    var mainNode = new GobiiTreeNode_1.GobiiTreeNode();
                    mainNode.label = this.extractorFilterTypeLabels[this.gobiiExtractFilterType];
                    mainNode.icon = "fa-folder";
                    mainNode.expandedIcon = "fa-folder";
                    mainNode.expanded = true;
                    statusTreeTemplates.forEach(function (currentFirstLevelTemplate) {
                        var currentTreeNode = _this.makeTreeNodeFromTemplate(currentFirstLevelTemplate);
                        if (currentTreeNode != null) {
                            mainNode.children.push(currentTreeNode);
                        }
                    });
                    this.gobiiTreeNodes.push(mainNode);
                };
                StatusDisplayTreeComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['checkBoxEventChange'] && changes['checkBoxEventChange'].currentValue) {
                        var itemChangedEvent = changes['checkBoxEventChange'].currentValue;
                        this.placeNodeInTree(itemChangedEvent);
                    }
                    else if (changes['gobiiExtractFilterTypeEvent']
                        && (changes['gobiiExtractFilterTypeEvent'].currentValue != null)
                        && (changes['gobiiExtractFilterTypeEvent'].currentValue != undefined)) {
                        var newGobiiExtractFilterType = changes['gobiiExtractFilterTypeEvent'].currentValue;
                        if (newGobiiExtractFilterType !== this.gobiiExtractFilterType) {
                            this.gobiiExtractFilterType = changes['gobiiExtractFilterTypeEvent'].currentValue;
                            this.getTemplates(this.gobiiExtractFilterType, true);
                            this.setUpRequredItems();
                        }
                    }
                };
                StatusDisplayTreeComponent.prototype.makeDemoTreeNodes = function () {
                    this.demoTreeNodes = [
                        {
                            "label": "Documents",
                            "data": "Documents Folder",
                            "expandedIcon": "fa-folder-open",
                            "collapsedIcon": "fa-folder",
                            "children": [{
                                    "label": "Work",
                                    "data": "Work Folder",
                                    "expandedIcon": "fa-folder-open",
                                    "collapsedIcon": "fa-folder",
                                    "children": [{
                                            "label": "Expenses.doc",
                                            "icon": "fa-file-word-o",
                                            "data": "Expenses Document"
                                        }, { "label": "Resume.doc", "icon": "fa-file-word-o", "data": "Resume Document" }]
                                },
                                {
                                    "label": "Home",
                                    "data": "Home Folder",
                                    "expandedIcon": "fa-folder-open",
                                    "collapsedIcon": "fa-folder",
                                    "children": [{
                                            "label": "Invoices.txt",
                                            "icon": "fa-file-word-o",
                                            "data": "Invoices for this month"
                                        }]
                                }]
                        },
                        {
                            "label": "Pictures",
                            "data": "Pictures Folder",
                            "expandedIcon": "fa-folder-open",
                            "collapsedIcon": "fa-folder",
                            "children": [
                                { "label": "barcelona.jpg", "icon": "fa-file-image-o", "data": "Barcelona Photo" },
                                { "label": "logo.jpg", "icon": "fa-file-image-o", "data": "PrimeFaces Logo" },
                                { "label": "primeui.png", "icon": "fa-file-image-o", "data": "PrimeUI Logo" }
                            ]
                        },
                        {
                            "label": "Movies",
                            "data": "Movies Folder",
                            "expandedIcon": "fa-folder-open",
                            "collapsedIcon": "fa-folder",
                            "children": [{
                                    "label": "Al Pacino",
                                    "data": "Pacino Movies",
                                    "children": [{
                                            "label": "Scarface",
                                            "icon": "fa-file-video-o",
                                            "data": "Scarface Movie"
                                        }, { "label": "Serpico", "icon": "fa-file-video-o", "data": "Serpico Movie" }]
                                },
                                {
                                    "label": "Robert De Niro",
                                    "data": "De Niro Movies",
                                    "children": [{
                                            "label": "Goodfellas",
                                            "icon": "fa-file-video-o",
                                            "data": "Goodfellas Movie"
                                        }, {
                                            "label": "Untouchables",
                                            "icon": "fa-file-video-o",
                                            "data": "Untouchables Movie"
                                        }]
                                }]
                        }
                    ];
                    this.selectedDemoNodes.push(this.demoTreeNodes[1].children[0]);
                    this.demoTreeNodes[1].partialSelected = true;
                    this.demoTreeNodes[1].expanded = true;
                };
                return StatusDisplayTreeComponent;
            }());
            StatusDisplayTreeComponent = __decorate([
                core_1.Component({
                    selector: 'status-display-tree',
                    inputs: ['checkBoxEventChange', 'gobiiExtractFilterTypeEvent'],
                    outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage'],
                    template: " \n                    <p-tree [value]=\"gobiiTreeNodes\" selectionMode=\"checkbox\" [(selection)]=\"selectedGobiiNodes\"></p-tree>\n                    <!--<p-tree [value]=\"demoTreeNodes\" selectionMode=\"checkbox\" [(selection)]=\"selectedDemoNodes\"></p-tree>-->\n                    <!--<div>Selected Nodes: <span *ngFor=\"let file of selectedFiles2\">{{file.label}} </span></div>-->\n"
                }),
                __metadata("design:paramtypes", [])
            ], StatusDisplayTreeComponent);
            exports_1("StatusDisplayTreeComponent", StatusDisplayTreeComponent);
        }
    };
});
//# sourceMappingURL=status-display-tree.component.js.map