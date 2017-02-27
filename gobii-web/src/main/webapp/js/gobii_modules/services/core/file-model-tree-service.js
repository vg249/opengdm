System.register(["@angular/core", "../../model/file-model-tree-event", "../../model/file-model-node", "../../model/type-extractor-filter", "../../model/type-entity", "../../model/cv-filter-type", "rxjs/Subject", "rxjs/Observable"], function (exports_1, context_1) {
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
    var core_1, file_model_tree_event_1, file_model_node_1, type_extractor_filter_1, type_entity_1, cv_filter_type_1, Subject_1, Observable_1, FileModelTreeService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (file_model_tree_event_1_1) {
                file_model_tree_event_1 = file_model_tree_event_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (Subject_1_1) {
                Subject_1 = Subject_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            }
        ],
        execute: function () {
            FileModelTreeService = (function () {
                function FileModelTreeService() {
                    this.fileModelNodeTree = new Map();
                    this.entityNodeLabels = new Map();
                    this.entitySubtypeNodeLabels = new Map();
                    this.cvFilterNodeLabels = new Map();
                    this.extractorFilterTypeLabels = new Map();
                    this.treeCategoryLabels = new Map();
                    this.subject = new Subject_1.Subject();
                }
                FileModelTreeService.prototype.getFileModelNodes = function (gobiiExtractFilterType) {
                    if (this.fileModelNodeTree.size === 0) {
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
                        submissionItemsForAll.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.Contacts)
                            .setEntityName(this.entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_SUBMITED_BY])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY));
                        submissionItemsForAll.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.EXPORT_FORMAT)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityName("Export Formats")
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY));
                        submissionItemsForAll.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.Mapsets)
                            .setEntityName(this.entityNodeLabels[type_entity_1.EntityType.Mapsets])
                            .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_ONE));
                        // ******** SET UP extract by dataset
                        // -- Data set type
                        var submissionItemsForDataSet = [];
                        submissionItemsForDataSet = submissionItemsForDataSet.concat(submissionItemsForAll);
                        submissionItemsForDataSet.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.CATEGORY)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER)
                            .setEntityType(type_entity_1.EntityType.DataSets)
                            .setCategoryName(this.entityNodeLabels[type_entity_1.EntityType.DataSets]));
                        this.fileModelNodeTree.set(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, submissionItemsForDataSet);
                        // ******** SET UP extract by samples
                        // -- Data set type
                        var submissionItemsForBySample = [];
                        submissionItemsForBySample = submissionItemsForBySample.concat(submissionItemsForAll);
                        submissionItemsForBySample.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.CvTerms)
                            .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                            .setEntityName(this.cvFilterNodeLabels[cv_filter_type_1.CvFilterType.DATASET_TYPE])
                            .setCardinality(file_model_node_1.CardinalityType.ONE_ONLY));
                        // -- Platforms
                        submissionItemsForBySample.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.CATEGORY)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER)
                            .setCategoryName(this.entityNodeLabels[type_entity_1.EntityType.Platforms])
                            .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_MORE)
                            .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.Platforms)
                            .setEntityName(this.entityNodeLabels[type_entity_1.EntityType.Platforms])
                            .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_MORE)));
                        // -- Samples
                        submissionItemsForBySample.push(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.CATEGORY)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.CONTAINER)
                            .setCategoryName("Sample Crieria")
                            .setCardinality(file_model_node_1.CardinalityType.ONE_OR_MORE)
                            .setAlternatePeerTypes([type_entity_1.EntityType.Projects, type_entity_1.EntityType.Contacts])
                            .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY)
                            .setCategoryType(file_model_node_1.ExtractorCategoryType.LEAF)
                            .setEntityType(type_entity_1.EntityType.Contacts)
                            .setEntityName("Principle Investigator")
                            .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_ONE))
                            .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.Projects)
                            .setEntityName(this.entityNodeLabels[type_entity_1.EntityType.Projects])
                            .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_MORE))
                            .addChild(file_model_node_1.FileModelNode.build(file_model_node_1.ExtractorItemType.SAMPLE_LIST)
                            .setEntityName("Sample List")
                            .setCategoryName(this.entityNodeLabels[type_entity_1.EntityType.Platforms])
                            .setCardinality(file_model_node_1.CardinalityType.ZERO_OR_MORE)));
                        this.fileModelNodeTree.set(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, submissionItemsForBySample);
                    }
                    return this.fileModelNodeTree.get(gobiiExtractFilterType);
                };
                FileModelTreeService.prototype.mutate = function (fileItem) {
                    var returnVal = null;
                    if (fileItem.gobiiExtractFilterType != type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN) {
                        var fileModelNodes = this.getFileModelNodes(fileItem.gobiiExtractFilterType);
                        var fileModelNodeForFileItem = this.placeNodeInTree(fileModelNodes, fileItem);
                        returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, fileModelNodeForFileItem, file_model_tree_event_1.FileModelState.NOT_COMPLETE, null);
                    }
                    else {
                        returnVal = new file_model_tree_event_1.FileModelTreeEvent(fileItem, null, file_model_tree_event_1.FileModelState.ERROR, "An invalid extract filter type was specified");
                    }
                    return returnVal;
                };
                FileModelTreeService.prototype.findFileModelNode = function (fileModelNodes, extractorItemType, entityType) {
                    var returnVal = null;
                    for (var idx = 0; (idx < fileModelNodes.length) && (returnVal == null); idx++) {
                        var currentTemplate = fileModelNodes[idx];
                        returnVal = this.findTemplateByCriteria(currentTemplate, extractorItemType, entityType);
                    }
                    return returnVal;
                };
                FileModelTreeService.prototype.findTemplateByCriteria = function (statusTreeTemplate, extractorItemType, entityType) {
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
                FileModelTreeService.prototype.placeNodeInTree = function (fileModelNodes, fileItemEvent) {
                    var fileModelNode = this.findFileModelNode(fileModelNodes, file_model_node_1.ExtractorItemType.CATEGORY, fileItemEvent.entityType);
                    if (fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.LEAF) {
                        if (fileModelNode.getFileItems().length === 0) {
                            fileModelNode.getFileItems().push(fileItemEvent);
                        }
                        else {
                            fileModelNode.getFileItems()[0] = fileItemEvent;
                        }
                    }
                    else if (fileModelNode.getCategoryType() === file_model_node_1.ExtractorCategoryType.ENTITY_CONTAINER) {
                        var existingItems = fileModelNode.getFileItems().filter(function (item) {
                            return item.fileItemUniqueId === fileItemEvent.fileItemUniqueId;
                        });
                        if (existingItems.length === 0) {
                            fileModelNode.getFileItems().push(fileItemEvent);
                        }
                        else {
                            var idx = fileModelNode.getFileItems().indexOf(existingItems[0]);
                            fileModelNode.getFileItems()[idx] = fileItemEvent;
                        }
                    }
                    else {
                    }
                    return fileModelNode;
                }; //
                FileModelTreeService.prototype.put = function (fileItem) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        var fileTreeEvent = _this.mutate(fileItem);
                        observer.next(fileTreeEvent);
                        observer.complete();
                        _this.subject.next(fileTreeEvent);
                    });
                };
                FileModelTreeService.prototype.get = function (gobiiExtractFilterType) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        var nodesForFilterType = _this.getFileModelNodes(gobiiExtractFilterType);
                        observer.next(nodesForFilterType);
                        observer.complete();
                    });
                };
                return FileModelTreeService;
            }());
            FileModelTreeService = __decorate([
                core_1.Injectable(),
                __metadata("design:paramtypes", [])
            ], FileModelTreeService);
            exports_1("FileModelTreeService", FileModelTreeService);
        }
    };
});
//# sourceMappingURL=file-model-tree-service.js.map