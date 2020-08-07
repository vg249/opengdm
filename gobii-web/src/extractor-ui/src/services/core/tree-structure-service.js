System.register(["@angular/core", "../../model/GobiiTreeNode", "../../model/type-entity", "../../views/entity-labels", "../../model/type-extractor-item", "../../model/type-extractor-filter", "../../model/cv-filter-type", "../../model/type-extract-format", "../../model/type-process", "../../store/actions/treenode-action", "@ngrx/store"], function (exports_1, context_1) {
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
    var __spreadArrays = (this && this.__spreadArrays) || function () {
        for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
        for (var r = Array(s), k = 0, i = 0; i < il; i++)
            for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
                r[k] = a[j];
        return r;
    };
    var core_1, GobiiTreeNode_1, type_entity_1, entity_labels_1, type_extractor_item_1, type_extractor_filter_1, cv_filter_type_1, type_extract_format_1, type_process_1, treeNodeActions, store_1, TreeStructureService;
    var __moduleName = context_1 && context_1.id;
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
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (treeNodeActions_1) {
                treeNodeActions = treeNodeActions_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            }
        ],
        execute: function () {
            TreeStructureService = /** @class */ (function () {
                function TreeStructureService(store) {
                    this.store = store;
                }
                TreeStructureService.prototype.makeCommonNodes = function (gobiiExtractFilterType) {
                    var returnVal = [
                        GobiiTreeNode_1.GobiiTreeNode.build(gobiiExtractFilterType, type_extractor_item_1.ExtractorItemType.JOB_ID)
                            .setGenericLabel(entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.JOB_ID]),
                        GobiiTreeNode_1.GobiiTreeNode.build(gobiiExtractFilterType, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.CONTACT)
                            .setEntitySubType(type_entity_1.EntitySubType.CONTACT_SUBMITED_BY)
                            .setGenericLabel(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntitySubType.CONTACT_SUBMITED_BY]),
                        GobiiTreeNode_1.GobiiTreeNode.build(gobiiExtractFilterType, type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT)
                            .setGenericLabel(entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT]),
                        GobiiTreeNode_1.GobiiTreeNode.build(gobiiExtractFilterType, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.MAPSET)
                            .setGenericLabel(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.MAPSET]),
                    ];
                    return returnVal;
                };
                // Note that we aren't storing these nodes as state -- we only make them
                // and return them. We rely on the store to make them part of state.
                // For now, this is the only context in which container type is ste on tree nodes;
                // tree nodes that are created dynamically will only ever replace or be stored within
                // one of these original structural nodes. See the tree nodes reducer for how the
                // container types are actually used
                TreeStructureService.prototype.getInitialTree = function () {
                    var returnVal = __spreadArrays(this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET), [
                        GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.DATASET)
                            .setContainerType(GobiiTreeNode_1.ContainerType.DATA)
                    ], this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE), [
                        GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.CV)
                            .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE),
                        GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE),
                        GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.PLATFORM)
                            .setContainerType(GobiiTreeNode_1.ContainerType.DATA),
                        GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.TREE_STRUCTURE)
                            .setContainerType(GobiiTreeNode_1.ContainerType.STRUCTURE)
                            .setLabel("Samples Criteria")
                            .setExpanded(true)
                            .setChildren([
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.CONTACT)
                                .setEntitySubType(type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
                                .setContainerType(GobiiTreeNode_1.ContainerType.NONE),
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.PROJECT)
                                .setContainerType(GobiiTreeNode_1.ContainerType.NONE),
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.SAMPLE_FILE),
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM)
                                .setContainerType(GobiiTreeNode_1.ContainerType.DATA),
                        ])
                    ], this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER), [
                        GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.CV)
                            .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE),
                        GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.TREE_STRUCTURE)
                            .setContainerType(GobiiTreeNode_1.ContainerType.STRUCTURE)
                            .setLabel("Markers Criteria")
                            .setExpanded(true)
                            .setChildren([
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.PLATFORM)
                                .setContainerType(GobiiTreeNode_1.ContainerType.DATA),
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.MARKER_FILE),
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM)
                                .setContainerType(GobiiTreeNode_1.ContainerType.DATA),
                            GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.MARKER_GROUP)
                                .setContainerType(GobiiTreeNode_1.ContainerType.DATA)
                        ])
                    ]);
                    // we know we only have to go one level deep in this case -- no need to recurse
                    returnVal.forEach(function (currentNode, idx, nodes) {
                        currentNode.getChildren().forEach(function (currentChild) {
                            currentChild.parent = currentNode;
                        });
                    });
                    this.setTreeNodeProperties(returnVal);
                    return returnVal;
                };
                TreeStructureService.prototype.applyLabel = function (gobiiTreeNode) {
                    var labelValue = null;
                    if (gobiiTreeNode.getItemType() === type_extractor_item_1.ExtractorItemType.ENTITY) {
                        if (gobiiTreeNode.getEntitySubType() === type_entity_1.EntitySubType.UNKNOWN) {
                            if (gobiiTreeNode.getEntityType() !== type_entity_1.EntityType.CV) {
                                labelValue = entity_labels_1.Labels.instance().entityNodeLabels[gobiiTreeNode.getEntityType()];
                            }
                            else {
                                labelValue = entity_labels_1.Labels.instance().cvFilterNodeLabels[gobiiTreeNode.getCvFilterType()];
                            }
                        }
                        else {
                            labelValue = entity_labels_1.Labels.instance().entitySubtypeNodeLabels[gobiiTreeNode.getEntitySubType()];
                        }
                    }
                    else {
                        labelValue = entity_labels_1.Labels.instance().treeExtractorTypeLabels[gobiiTreeNode.getItemType()];
                    }
                    gobiiTreeNode.setGenericLabel(labelValue);
                    gobiiTreeNode.setLabel(labelValue);
                };
                TreeStructureService.prototype.setTreeNodeProperties = function (treeNodes) {
                    var _this = this;
                    treeNodes.forEach(function (tn) {
                        if ((tn.children === null) || (tn.children.length <= 0)) {
                            _this.addIconsToNode(tn, false);
                            _this.applyLabel(tn);
                        }
                        else {
                            _this.setTreeNodeProperties(tn.children);
                        }
                    });
                };
                TreeStructureService.prototype.getEntityIcon = function (gobiiFileItemCompoundId) {
                    var icon;
                    var expandedIcon;
                    var collapsedIcon;
                    if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.DATASET) {
                        icon = "pi pi-copy";
                        expandedIcon = "pi pi-folder-open";
                        collapsedIcon = "pi pi-copy";
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.CONTACT) {
                        icon = "pi pi-user";
                        expandedIcon = "pi pi-user";
                        collapsedIcon = "pi pi-user";
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.MAPSET) {
                        icon = "pi pi-map-marker";
                        expandedIcon = "pi pi-map-marker";
                        collapsedIcon = "pi pi-map-marker";
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.PLATFORM) {
                        icon = "pi pi-bookmark";
                        expandedIcon = "pi pi-bookmark";
                        collapsedIcon = "pi pi-bookmark";
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.PROJECT) {
                        icon = "pi pi-compass";
                        expandedIcon = "pi pi-compass";
                        collapsedIcon = "pi pi-compass";
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.CV) {
                        if (gobiiFileItemCompoundId.getCvFilterType() === cv_filter_type_1.CvFilterType.DATASET_TYPE) {
                            icon = "pi pi-file-excel";
                            expandedIcon = "pi pi-file-excel";
                            collapsedIcon = "pi pi-file-excel";
                        }
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.MARKER_GROUP) {
                        // if (isParent) {
                        icon = "pi pi-sitemap";
                        expandedIcon = "pi pi-sitemap";
                        collapsedIcon = "pi pi-sitemap";
                    }
                    return { icon: icon, expandedIcon: expandedIcon, collapsedIcon: collapsedIcon };
                };
                TreeStructureService.prototype.addEntityIconToNode = function (entityType, cvFilterType, treeNode) {
                    if (entityType === type_entity_1.EntityType.DATASET) {
                        treeNode.icon = "pi pi-copy";
                        treeNode.expandedIcon = "pi pi-folder-open";
                        treeNode.collapsedIcon = "pi pi-copy";
                    }
                    else if (entityType === type_entity_1.EntityType.CONTACT) {
                        treeNode.icon = "pi pi-user";
                        treeNode.expandedIcon = "pi pi-user";
                        treeNode.collapsedIcon = "pi pi-user";
                    }
                    else if (entityType === type_entity_1.EntityType.MAPSET) {
                        treeNode.icon = "pi pi-map-marker";
                        treeNode.expandedIcon = "pi pi-map-marker";
                        treeNode.collapsedIcon = "pi pi-map-marker";
                    }
                    else if (entityType === type_entity_1.EntityType.PLATFORM) {
                        treeNode.icon = "pi pi-bookmark";
                        treeNode.expandedIcon = "pi pi-bookmark";
                        treeNode.collapsedIcon = "pi pi-bookmark";
                    }
                    else if (entityType === type_entity_1.EntityType.PROJECT) {
                        treeNode.icon = "pi pi-compass";
                        treeNode.expandedIcon = "pi pi-compass";
                        treeNode.collapsedIcon = "pi pi-compass";
                    }
                    else if (entityType === type_entity_1.EntityType.CV) {
                        if (cvFilterType === cv_filter_type_1.CvFilterType.DATASET_TYPE) {
                            treeNode.icon = "pi pi-file-excel";
                            treeNode.expandedIcon = "pi pi-file-excel";
                            treeNode.collapsedIcon = "pi pi-file-excel";
                        }
                    }
                    else if (entityType === type_entity_1.EntityType.MARKER_GROUP) {
                        // if (isParent) {
                        treeNode.icon = "pi pi-sitemap";
                        treeNode.expandedIcon = "pi pi-sitemap";
                        treeNode.collapsedIcon = "pi pi-sitemap";
                    }
                };
                TreeStructureService.prototype.getIcons = function (gobiiFileItemCompoundId, isParent) {
                    var icon;
                    var expandedIcon;
                    var collapsedIcon;
                    if (gobiiFileItemCompoundId.getEntityType() != null
                        && gobiiFileItemCompoundId.getEntityType() != type_entity_1.EntityType.UNKNOWN) {
                        var entityIcons = this.getEntityIcon(gobiiFileItemCompoundId);
                        icon = entityIcons.icon;
                        expandedIcon = entityIcons.expandedIcon;
                        collapsedIcon = entityIcons.collapsedIcon;
                        //this.addEntityIconToNode(gobiiFileItemCompoundId.getEntityType(), gobiiFileItemCompoundId.getCvFilterType(), treeNode);
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT) {
                        icon = "pi pi-pencil";
                        expandedIcon = "pi pi-pencil";
                        collapsedIcon = "pi pi-pencil";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_FILE) {
                        icon = "pi pi-paperclip";
                        expandedIcon = "pi pi-paperclip";
                        collapsedIcon = "pi pi-paperclip";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM) {
                        if (isParent) {
                            icon = "pi pi-list";
                            expandedIcon = "pi pi-list";
                            collapsedIcon = "pi pi-list";
                        }
                        else {
                            icon = "pi pi-list";
                            expandedIcon = "pi pi-list";
                            collapsedIcon = "pi pi-list";
                        }
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_FILE) {
                        icon = "pi pi-paperclip";
                        expandedIcon = "pi pi-paperclip";
                        collapsedIcon = "pi pi-paperclip";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM) {
                        if (isParent) {
                            icon = "pi pi-list";
                            expandedIcon = "pi pi-list";
                            collapsedIcon = "pi pi-list";
                        }
                        else {
                            icon = "pi pi-list";
                            expandedIcon = "pi pi-list";
                            collapsedIcon = "pi pi-list";
                        }
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.JOB_ID) {
                        icon = "pi pi-ticket";
                        expandedIcon = "pi pi-ticket";
                        collapsedIcon = "pi pi-ticket";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE) {
                        icon = "pi pi-list";
                        expandedIcon = "pi pi-list";
                        collapsedIcon = "pi pi-list";
                    }
                    else {
                        //     }
                        // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
                        icon = "pi pi-folder";
                        expandedIcon = "pi pi-folder-open";
                        collapsedIcon = "pi pi-folder";
                    }
                    return { icon: icon, expandedIcon: expandedIcon, collapsedIcon: collapsedIcon };
                };
                TreeStructureService.prototype.addIconsToNode = function (treeNode, isParent) {
                    var icons = this.getIcons(treeNode, isParent);
                    treeNode.icon = icons.icon;
                    treeNode.expandedIcon = icons.expandedIcon;
                    treeNode.collapsedIcon = icons.collapsedIcon;
                    // if (treeNode.getEntityType() != null
                    //     && treeNode.getEntityType() != EntityType.UNKNOWN) {
                    //
                    //     this.addEntityIconToNode(treeNode.getEntityType(), treeNode.getCvFilterType(), treeNode);
                    //
                    // } else if (treeNode.getItemType() === ExtractorItemType.EXPORT_FORMAT) {
                    //     treeNode.icon = "pi pi-pencil";
                    //     treeNode.expandedIcon = "pi pi-pencil";
                    //     treeNode.collapsedIcon = "pi pi-pencil";
                    // } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_FILE) {
                    //     treeNode.icon = "pi pi-paperclip";
                    //     treeNode.expandedIcon = "pi pi-paperclip";
                    //     treeNode.collapsedIcon = "pi pi-paperclip";
                    // } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {
                    //     if (isParent) {
                    //         treeNode.icon = "pi pi-list";
                    //         treeNode.expandedIcon = "pi pi-list";
                    //         treeNode.collapsedIcon = "pi pi-list";
                    //     } else {
                    //         treeNode.icon = "pi pi-list";
                    //         treeNode.expandedIcon = "pi pi-list";
                    //         treeNode.collapsedIcon = "pi pi-list";
                    //     }
                    // } else if (treeNode.getItemType() === ExtractorItemType.MARKER_FILE) {
                    //     treeNode.icon = "pi pi-paperclip";
                    //     treeNode.expandedIcon = "pi pi-paperclip";
                    //     treeNode.collapsedIcon = "pi pi-paperclip";
                    // } else if (treeNode.getItemType() === ExtractorItemType.MARKER_LIST_ITEM) {
                    //
                    //     if (isParent) {
                    //         treeNode.icon = "pi pi-list";
                    //         treeNode.expandedIcon = "pi pi-list";
                    //         treeNode.collapsedIcon = "pi pi-list";
                    //     } else {
                    //         treeNode.icon = "pi pi-list";
                    //         treeNode.expandedIcon = "pi pi-list";
                    //         treeNode.collapsedIcon = "pi pi-list";
                    //     }
                    // } else if (treeNode.getItemType() === ExtractorItemType.JOB_ID) {
                    //     treeNode.icon = "pi pi-list";
                    //     treeNode.expandedIcon = "pi pi-list";
                    //     treeNode.collapsedIcon = "pi pi-list";
                    // } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_LIST_TYPE) {
                    //     treeNode.icon = "pi pi-list";
                    //     treeNode.expandedIcon = "pi pi-list";
                    //     treeNode.collapsedIcon = "pi pi-list";
                    // } else {
                    //     //     }
                    //     // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
                    //     treeNode.icon = "pi pi-folder";
                    //     treeNode.expandedIcon = "pi pi-folder-open";
                    //     treeNode.collapsedIcon = "pi pi-folder";
                    // }
                };
                TreeStructureService.prototype.makeTreeNodeFromFileItem = function (gobiiFileItem) {
                    var returnVal = GobiiTreeNode_1.GobiiTreeNode
                        .build(gobiiFileItem.getGobiiExtractFilterType(), gobiiFileItem.getExtractorItemType())
                        .setFileItemId(gobiiFileItem.getFileItemUniqueId())
                        .setEntityType(gobiiFileItem.getEntityType())
                        .setEntitySubType(gobiiFileItem.getEntitySubType())
                        .setCvFilterType(gobiiFileItem.getCvFilterType());
                    this.addIconsToNode(returnVal, false);
                    this.applyLabel(returnVal);
                    this.addFileItemNameToNode(returnVal, gobiiFileItem);
                    return returnVal;
                };
                TreeStructureService.prototype.addFileItemNameToNode = function (gobiiTreeNode, gobiiFileItem) {
                    if (gobiiTreeNode.getContainerType() === GobiiTreeNode_1.ContainerType.DATA) {
                        gobiiTreeNode.label = gobiiFileItem.getItemName();
                    }
                    else { // coves the LEAF node use case
                        if (gobiiFileItem.getExtractorItemType() == type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT) {
                            var gobiiExtractFormat = type_extract_format_1.GobiiExtractFormat[gobiiFileItem.getItemId()];
                            gobiiTreeNode.label += ": " + entity_labels_1.Labels.instance().extractFormatTypeLabels[gobiiExtractFormat];
                        }
                        else if (gobiiFileItem.getExtractorItemType() == type_extractor_item_1.ExtractorItemType.JOB_ID) {
                            gobiiTreeNode.label = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.JOB_ID]
                                + ": " + gobiiFileItem.getItemId();
                        }
                        else {
                            if (gobiiFileItem.getProcessType() !== type_process_1.ProcessType.DELETE) {
                                gobiiTreeNode.label += ": " + gobiiFileItem.getItemName();
                            }
                            else {
                                gobiiTreeNode.label = gobiiFileItem.getItemName();
                            }
                        }
                    }
                };
                TreeStructureService.prototype.markTreeItemMissing = function (gobiiExtractFilterType, gobiiFileItemCompoundId) {
                    //let icon: string = "fa-chevron-circle-right";
                    var icon = "pi pi-circle-on";
                    //let icon: string = "fa-chevron-right";
                    this.store.dispatch(new treeNodeActions.SetTreeNodeLook({
                        gobiiExtractFilterType: gobiiExtractFilterType,
                        gobiiFileItemCompoundId: gobiiFileItemCompoundId,
                        icon: icon
                    }));
                };
                TreeStructureService.prototype.unMarkTreeItemMissing = function (gobiiExtractFilterType, gobiiFileItemCompoundId) {
                    var icons = this.getIcons(gobiiFileItemCompoundId, false);
                    this.store.dispatch(new treeNodeActions.SetTreeNodeLook({
                        gobiiExtractFilterType: gobiiExtractFilterType,
                        gobiiFileItemCompoundId: gobiiFileItemCompoundId,
                        icon: icons.icon
                    }));
                };
                TreeStructureService = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [store_1.Store])
                ], TreeStructureService);
                return TreeStructureService;
            }());
            exports_1("TreeStructureService", TreeStructureService);
        }
    };
});
//# sourceMappingURL=tree-structure-service.js.map