System.register(["@angular/core", "../../model/GobiiTreeNode", "../../model/type-entity", "../../views/entity-labels", "../../model/file-model-node", "../../model/type-extractor-filter", "../../model/cv-filter-type"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, GobiiTreeNode_1, type_entity_1, entity_labels_1, file_model_node_1, type_extractor_filter_1, cv_filter_type_1, TreeStructureService;
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
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            }
        ],
        execute: function () {
            TreeStructureService = (function () {
                function TreeStructureService() {
                    this.treeStructure = null;
                }
                TreeStructureService.prototype.makeCommonNodes = function (gobiiExtractFilterType) {
                    var returnVal = [
                        GobiiTreeNode_1.GobiiTreeNode.build(gobiiExtractFilterType, file_model_node_1.ExtractorItemType.JOB_ID)
                            .setLabel(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.JOB_ID]),
                        GobiiTreeNode_1.GobiiTreeNode.build(gobiiExtractFilterType, file_model_node_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.Contacts)
                            .setEntitySubType(type_entity_1.EntitySubType.CONTACT_SUBMITED_BY)
                            .setLabel(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntitySubType.CONTACT_SUBMITED_BY]),
                        GobiiTreeNode_1.GobiiTreeNode.build(gobiiExtractFilterType, file_model_node_1.ExtractorItemType.EXPORT_FORMAT)
                            .setLabel(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.EXPORT_FORMAT]),
                        GobiiTreeNode_1.GobiiTreeNode.build(gobiiExtractFilterType, file_model_node_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.Mapsets)
                            .setLabel(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Mapsets]),
                    ];
                    return returnVal;
                };
                TreeStructureService.prototype.getInitialTree = function () {
                    if (this.treeStructure === null) {
                        this.treeStructure = this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET).concat([
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, file_model_node_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.DataSets)
                                .setLabel(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.DataSets])
                                .setContainerType(GobiiTreeNode_1.ContainerType.ITEM_NODE)
                        ], this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE), [
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, file_model_node_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.CvTerms)
                                .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                                .setLabel(entity_labels_1.Labels.instance().cvFilterNodeLabels[cv_filter_type_1.CvFilterType.DATASET_TYPE]),
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE)
                                .setLabel(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE]),
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, file_model_node_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.Platforms)
                                .setLabel(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Platforms]),
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, file_model_node_1.ExtractorItemType.TREE_STRUCTURE)
                                .setContainerType(GobiiTreeNode_1.ContainerType.TREE_NODE)
                                .setLabel("Samples Criteria")
                                .setExpanded(true)
                                .setChildren([
                                GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, file_model_node_1.ExtractorItemType.ENTITY)
                                    .setEntityType(type_entity_1.EntityType.Contacts)
                                    .setEntitySubType(type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
                                    .setLabel(entity_labels_1.Labels.instance().entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR]),
                                GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, file_model_node_1.ExtractorItemType.ENTITY)
                                    .setEntityType(type_entity_1.EntityType.Projects)
                                    .setContainerType(GobiiTreeNode_1.ContainerType.ITEM_NODE)
                                    .setLabel(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Projects]),
                                GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, file_model_node_1.ExtractorItemType.SAMPLE_FILE)
                                    .setLabel(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_FILE]),
                                GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE)
                                    .setLabel(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE])
                                    .setContainerType(GobiiTreeNode_1.ContainerType.ITEM_NODE),
                            ])
                        ], this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER), [
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, file_model_node_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.CvTerms)
                                .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                                .setLabel(entity_labels_1.Labels.instance().cvFilterNodeLabels[cv_filter_type_1.CvFilterType.DATASET_TYPE]),
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, file_model_node_1.ExtractorItemType.TREE_STRUCTURE)
                                .setContainerType(GobiiTreeNode_1.ContainerType.TREE_NODE)
                                .setLabel("Markers Criteria")
                                .setExpanded(true)
                                .setChildren([
                                GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, file_model_node_1.ExtractorItemType.ENTITY)
                                    .setEntityType(type_entity_1.EntityType.Platforms)
                                    .setLabel(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.Platforms])
                                    .setContainerType(GobiiTreeNode_1.ContainerType.ITEM_NODE),
                                GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, file_model_node_1.ExtractorItemType.MARKER_FILE)
                                    .setLabel(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_FILE]),
                                GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM)
                                    .setLabel(entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM])
                                    .setContainerType(GobiiTreeNode_1.ContainerType.ITEM_NODE),
                                GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, file_model_node_1.ExtractorItemType.ENTITY)
                                    .setEntityType(type_entity_1.EntityType.MarkerGroups)
                                    .setLabel(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.MarkerGroups])
                                    .setContainerType(GobiiTreeNode_1.ContainerType.ITEM_NODE)
                            ])
                        ]);
                        this.setTreeIcons(this.treeStructure);
                    }
                    return this.treeStructure;
                };
                TreeStructureService.prototype.setTreeIcons = function (treeNodes) {
                    var _this = this;
                    treeNodes.forEach(function (tn) {
                        if ((tn.children === null) || (tn.children.length <= 0)) {
                            _this.addIconsToNode(tn, false);
                        }
                        else {
                            _this.setTreeIcons(tn.children);
                        }
                    });
                };
                TreeStructureService.prototype.addEntityIconToNode = function (entityType, cvFilterType, treeNode) {
                    if (entityType === type_entity_1.EntityType.DataSets) {
                        treeNode.icon = "fa-database";
                        treeNode.expandedIcon = "fa-folder-expanded";
                        treeNode.collapsedIcon = "fa-database";
                    }
                    else if (entityType === type_entity_1.EntityType.Contacts) {
                        treeNode.icon = "fa-user-o";
                        treeNode.expandedIcon = "fa-user-o";
                        treeNode.collapsedIcon = "fa-user-o";
                    }
                    else if (entityType === type_entity_1.EntityType.Mapsets) {
                        treeNode.icon = "fa-map-o";
                        treeNode.expandedIcon = "fa-map-o";
                        treeNode.collapsedIcon = "fa-map-o";
                    }
                    else if (entityType === type_entity_1.EntityType.Platforms) {
                        treeNode.icon = "fa-calculator";
                        treeNode.expandedIcon = "fa-calculator";
                        treeNode.collapsedIcon = "fa-calculator";
                    }
                    else if (entityType === type_entity_1.EntityType.Projects) {
                        treeNode.icon = "fa-clipboard";
                        treeNode.expandedIcon = "fa-clipboard";
                        treeNode.collapsedIcon = "fa-clipboard";
                    }
                    else if (entityType === type_entity_1.EntityType.CvTerms) {
                        if (cvFilterType === cv_filter_type_1.CvFilterType.DATASET_TYPE) {
                            treeNode.icon = "fa-file-excel-o";
                            treeNode.expandedIcon = "fa-file-excel-o";
                            treeNode.collapsedIcon = "fa-file-excel-o";
                        }
                    }
                    else if (entityType === type_entity_1.EntityType.MarkerGroups) {
                        // if (isParent) {
                        treeNode.icon = "fa-pencil";
                        treeNode.expandedIcon = "fa-pencil";
                        treeNode.collapsedIcon = "fa-pencil";
                        // } else {
                        //     treeNode.icon = "fa-map-marker";
                        //     treeNode.expandedIcon = "fa-map-marker";
                        //     treeNode.collapsedIcon = "fa-map-marker";
                        // }
                    }
                };
                TreeStructureService.prototype.addIconsToNode = function (treeNode, isParent) {
                    // if( fileModelNode.getItemType() == ExtractorItemType.ENTITY ) {
                    if (treeNode.getEntityType() != null
                        && treeNode.getEntityType() != type_entity_1.EntityType.UNKNOWN) {
                        this.addEntityIconToNode(treeNode.getEntityType(), treeNode.getCvFilterType(), treeNode);
                    }
                    else if (treeNode.getItemType() === file_model_node_1.ExtractorItemType.EXPORT_FORMAT) {
                        treeNode.icon = "fa-columns";
                        treeNode.expandedIcon = "fa-columns";
                        treeNode.collapsedIcon = "fa-columns";
                    }
                    else if (treeNode.getItemType() === file_model_node_1.ExtractorItemType.SAMPLE_FILE) {
                        treeNode.icon = "fa-file-text-o";
                        treeNode.expandedIcon = "fa-file-text-o";
                        treeNode.collapsedIcon = "fa-file-text-o";
                    }
                    else if (treeNode.getItemType() === file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM) {
                        if (isParent) {
                            treeNode.icon = "fa-list-ul";
                            treeNode.expandedIcon = "fa-list-ul";
                            treeNode.collapsedIcon = "fa-list-ul";
                        }
                        else {
                            treeNode.icon = "fa-eyedropper";
                            treeNode.expandedIcon = "fa-eyedropper";
                            treeNode.collapsedIcon = "fa-eyedropper";
                        }
                    }
                    else if (treeNode.getItemType() === file_model_node_1.ExtractorItemType.MARKER_FILE) {
                        treeNode.icon = "fa-file-text-o";
                        treeNode.expandedIcon = "fa-file-text-o";
                        treeNode.collapsedIcon = "fa-file-text-o";
                    }
                    else if (treeNode.getItemType() === file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM) {
                        if (isParent) {
                            treeNode.icon = "fa-list-ul";
                            treeNode.expandedIcon = "fa-list-ul";
                            treeNode.collapsedIcon = "fa-list-ul";
                        }
                        else {
                            treeNode.icon = "fa-map-marker";
                            treeNode.expandedIcon = "fa-map-marker";
                            treeNode.collapsedIcon = "fa-map-marker";
                        }
                    }
                    else if (treeNode.getItemType() === file_model_node_1.ExtractorItemType.JOB_ID) {
                        treeNode.icon = "fa-info-circle";
                        treeNode.expandedIcon = "fa-info-circle";
                        treeNode.collapsedIcon = "fa-info-circle";
                    }
                    else if (treeNode.getItemType() === file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE) {
                        treeNode.icon = "fa-info-circle";
                        treeNode.expandedIcon = "fa-info-circle";
                        treeNode.collapsedIcon = "fa-info-circle";
                    }
                    else {
                        //     }
                        // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
                        treeNode.icon = "fa-folder";
                        treeNode.expandedIcon = "fa-folder-expanded";
                        treeNode.collapsedIcon = "fa-folder";
                    }
                };
                TreeStructureService = __decorate([
                    core_1.Injectable()
                ], TreeStructureService);
                return TreeStructureService;
            }());
            exports_1("TreeStructureService", TreeStructureService);
        }
    };
});
//# sourceMappingURL=tree-structure-service.js.map