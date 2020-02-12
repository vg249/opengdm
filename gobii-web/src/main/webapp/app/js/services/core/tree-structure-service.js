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
            TreeStructureService = class TreeStructureService {
                constructor(store) {
                    this.store = store;
                }
                makeCommonNodes(gobiiExtractFilterType) {
                    let returnVal = [
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
                }
                // Note that we aren't storing these nodes as state -- we only make them
                // and return them. We rely on the store to make them part of state.
                // For now, this is the only context in which container type is ste on tree nodes;
                // tree nodes that are created dynamically will only ever replace or be stored within
                // one of these original structural nodes. See the tree nodes reducer for how the
                // container types are actually used
                getInitialTree() {
                    let returnVal = [
                        // BY DATASET
                        ...this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET),
                        GobiiTreeNode_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.DATASET)
                            .setContainerType(GobiiTreeNode_1.ContainerType.DATA),
                        // BY SAMPLE
                        ...this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE),
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
                        ]),
                        // BY MARKER
                        ...this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER),
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
                    ];
                    // we know we only have to go one level deep in this case -- no need to recurse
                    returnVal.forEach(function (currentNode, idx, nodes) {
                        currentNode.getChildren().forEach(currentChild => {
                            currentChild.parent = currentNode;
                        });
                    });
                    this.setTreeNodeProperties(returnVal);
                    return returnVal;
                }
                applyLabel(gobiiTreeNode) {
                    let labelValue = null;
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
                }
                setTreeNodeProperties(treeNodes) {
                    treeNodes.forEach(tn => {
                        if ((tn.children === null) || (tn.children.length <= 0)) {
                            this.addIconsToNode(tn, false);
                            this.applyLabel(tn);
                        }
                        else {
                            this.setTreeNodeProperties(tn.children);
                        }
                    });
                }
                getEntityIcon(gobiiFileItemCompoundId) {
                    let icon;
                    let expandedIcon;
                    let collapsedIcon;
                    if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.DATASET) {
                        icon = "fa-database";
                        expandedIcon = "fa-folder-expanded";
                        collapsedIcon = "fa-database";
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.CONTACT) {
                        icon = "fa-user-o";
                        expandedIcon = "fa-user-o";
                        collapsedIcon = "fa-user-o";
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.MAPSET) {
                        icon = "fa-map-o";
                        expandedIcon = "fa-map-o";
                        collapsedIcon = "fa-map-o";
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.PLATFORM) {
                        icon = "fa-calculator";
                        expandedIcon = "fa-calculator";
                        collapsedIcon = "fa-calculator";
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.PROJECT) {
                        icon = "fa-clipboard";
                        expandedIcon = "fa-clipboard";
                        collapsedIcon = "fa-clipboard";
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.CV) {
                        if (gobiiFileItemCompoundId.getCvFilterType() === cv_filter_type_1.CvFilterType.DATASET_TYPE) {
                            icon = "fa-file-excel-o";
                            expandedIcon = "fa-file-excel-o";
                            collapsedIcon = "fa-file-excel-o";
                        }
                    }
                    else if (gobiiFileItemCompoundId.getEntityType() === type_entity_1.EntityType.MARKER_GROUP) {
                        // if (isParent) {
                        icon = "fa-pencil";
                        expandedIcon = "fa-pencil";
                        collapsedIcon = "fa-pencil";
                    }
                    return { icon: icon, expandedIcon: expandedIcon, collapsedIcon: collapsedIcon };
                }
                addEntityIconToNode(entityType, cvFilterType, treeNode) {
                    if (entityType === type_entity_1.EntityType.DATASET) {
                        treeNode.icon = "fa-database";
                        treeNode.expandedIcon = "fa-folder-expanded";
                        treeNode.collapsedIcon = "fa-database";
                    }
                    else if (entityType === type_entity_1.EntityType.CONTACT) {
                        treeNode.icon = "fa-user-o";
                        treeNode.expandedIcon = "fa-user-o";
                        treeNode.collapsedIcon = "fa-user-o";
                    }
                    else if (entityType === type_entity_1.EntityType.MAPSET) {
                        treeNode.icon = "fa-map-o";
                        treeNode.expandedIcon = "fa-map-o";
                        treeNode.collapsedIcon = "fa-map-o";
                    }
                    else if (entityType === type_entity_1.EntityType.PLATFORM) {
                        treeNode.icon = "fa-calculator";
                        treeNode.expandedIcon = "fa-calculator";
                        treeNode.collapsedIcon = "fa-calculator";
                    }
                    else if (entityType === type_entity_1.EntityType.PROJECT) {
                        treeNode.icon = "fa-clipboard";
                        treeNode.expandedIcon = "fa-clipboard";
                        treeNode.collapsedIcon = "fa-clipboard";
                    }
                    else if (entityType === type_entity_1.EntityType.CV) {
                        if (cvFilterType === cv_filter_type_1.CvFilterType.DATASET_TYPE) {
                            treeNode.icon = "fa-file-excel-o";
                            treeNode.expandedIcon = "fa-file-excel-o";
                            treeNode.collapsedIcon = "fa-file-excel-o";
                        }
                    }
                    else if (entityType === type_entity_1.EntityType.MARKER_GROUP) {
                        // if (isParent) {
                        treeNode.icon = "fa-pencil";
                        treeNode.expandedIcon = "fa-pencil";
                        treeNode.collapsedIcon = "fa-pencil";
                    }
                }
                getIcons(gobiiFileItemCompoundId, isParent) {
                    let icon;
                    let expandedIcon;
                    let collapsedIcon;
                    if (gobiiFileItemCompoundId.getEntityType() != null
                        && gobiiFileItemCompoundId.getEntityType() != type_entity_1.EntityType.UNKNOWN) {
                        let entityIcons = this.getEntityIcon(gobiiFileItemCompoundId);
                        icon = entityIcons.icon;
                        expandedIcon = entityIcons.expandedIcon;
                        collapsedIcon = entityIcons.collapsedIcon;
                        //this.addEntityIconToNode(gobiiFileItemCompoundId.getEntityType(), gobiiFileItemCompoundId.getCvFilterType(), treeNode);
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT) {
                        icon = "fa-columns";
                        expandedIcon = "fa-columns";
                        collapsedIcon = "fa-columns";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_FILE) {
                        icon = "fa-file-text-o";
                        expandedIcon = "fa-file-text-o";
                        collapsedIcon = "fa-file-text-o";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM) {
                        if (isParent) {
                            icon = "fa-list-ul";
                            expandedIcon = "fa-list-ul";
                            collapsedIcon = "fa-list-ul";
                        }
                        else {
                            icon = "fa-eyedropper";
                            expandedIcon = "fa-eyedropper";
                            collapsedIcon = "fa-eyedropper";
                        }
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_FILE) {
                        icon = "fa-file-text-o";
                        expandedIcon = "fa-file-text-o";
                        collapsedIcon = "fa-file-text-o";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM) {
                        if (isParent) {
                            icon = "fa-list-ul";
                            expandedIcon = "fa-list-ul";
                            collapsedIcon = "fa-list-ul";
                        }
                        else {
                            icon = "fa-map-marker";
                            expandedIcon = "fa-map-marker";
                            collapsedIcon = "fa-map-marker";
                        }
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.JOB_ID) {
                        icon = "fa-info-circle";
                        expandedIcon = "fa-info-circle";
                        collapsedIcon = "fa-info-circle";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE) {
                        icon = "fa-info-circle";
                        expandedIcon = "fa-info-circle";
                        collapsedIcon = "fa-info-circle";
                    }
                    else {
                        //     }
                        // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
                        icon = "fa-folder";
                        expandedIcon = "fa-folder-expanded";
                        collapsedIcon = "fa-folder";
                    }
                    return { icon: icon, expandedIcon: expandedIcon, collapsedIcon: collapsedIcon };
                }
                addIconsToNode(treeNode, isParent) {
                    let icons = this.getIcons(treeNode, isParent);
                    treeNode.icon = icons.icon;
                    treeNode.expandedIcon = icons.expandedIcon;
                    treeNode.collapsedIcon = icons.collapsedIcon;
                    // if (treeNode.getEntityType() != null
                    //     && treeNode.getEntityType() != EntityType.UNKNOWN) {
                    //
                    //     this.addEntityIconToNode(treeNode.getEntityType(), treeNode.getCvFilterType(), treeNode);
                    //
                    // } else if (treeNode.getItemType() === ExtractorItemType.EXPORT_FORMAT) {
                    //     treeNode.icon = "fa-columns";
                    //     treeNode.expandedIcon = "fa-columns";
                    //     treeNode.collapsedIcon = "fa-columns";
                    // } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_FILE) {
                    //     treeNode.icon = "fa-file-text-o";
                    //     treeNode.expandedIcon = "fa-file-text-o";
                    //     treeNode.collapsedIcon = "fa-file-text-o";
                    // } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {
                    //     if (isParent) {
                    //         treeNode.icon = "fa-list-ul";
                    //         treeNode.expandedIcon = "fa-list-ul";
                    //         treeNode.collapsedIcon = "fa-list-ul";
                    //     } else {
                    //         treeNode.icon = "fa-eyedropper";
                    //         treeNode.expandedIcon = "fa-eyedropper";
                    //         treeNode.collapsedIcon = "fa-eyedropper";
                    //     }
                    // } else if (treeNode.getItemType() === ExtractorItemType.MARKER_FILE) {
                    //     treeNode.icon = "fa-file-text-o";
                    //     treeNode.expandedIcon = "fa-file-text-o";
                    //     treeNode.collapsedIcon = "fa-file-text-o";
                    // } else if (treeNode.getItemType() === ExtractorItemType.MARKER_LIST_ITEM) {
                    //
                    //     if (isParent) {
                    //         treeNode.icon = "fa-list-ul";
                    //         treeNode.expandedIcon = "fa-list-ul";
                    //         treeNode.collapsedIcon = "fa-list-ul";
                    //     } else {
                    //         treeNode.icon = "fa-map-marker";
                    //         treeNode.expandedIcon = "fa-map-marker";
                    //         treeNode.collapsedIcon = "fa-map-marker";
                    //     }
                    // } else if (treeNode.getItemType() === ExtractorItemType.JOB_ID) {
                    //     treeNode.icon = "fa-info-circle";
                    //     treeNode.expandedIcon = "fa-info-circle";
                    //     treeNode.collapsedIcon = "fa-info-circle";
                    // } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_LIST_TYPE) {
                    //     treeNode.icon = "fa-info-circle";
                    //     treeNode.expandedIcon = "fa-info-circle";
                    //     treeNode.collapsedIcon = "fa-info-circle";
                    // } else {
                    //     //     }
                    //     // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
                    //     treeNode.icon = "fa-folder";
                    //     treeNode.expandedIcon = "fa-folder-expanded";
                    //     treeNode.collapsedIcon = "fa-folder";
                    // }
                }
                makeTreeNodeFromFileItem(gobiiFileItem) {
                    let returnVal = GobiiTreeNode_1.GobiiTreeNode
                        .build(gobiiFileItem.getGobiiExtractFilterType(), gobiiFileItem.getExtractorItemType())
                        .setFileItemId(gobiiFileItem.getFileItemUniqueId())
                        .setEntityType(gobiiFileItem.getEntityType())
                        .setEntitySubType(gobiiFileItem.getEntitySubType())
                        .setCvFilterType(gobiiFileItem.getCvFilterType());
                    this.addIconsToNode(returnVal, false);
                    this.applyLabel(returnVal);
                    this.addFileItemNameToNode(returnVal, gobiiFileItem);
                    return returnVal;
                }
                addFileItemNameToNode(gobiiTreeNode, gobiiFileItem) {
                    if (gobiiTreeNode.getContainerType() === GobiiTreeNode_1.ContainerType.DATA) {
                        gobiiTreeNode.label = gobiiFileItem.getItemName();
                    }
                    else { // coves the LEAF node use case
                        if (gobiiFileItem.getExtractorItemType() == type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT) {
                            let gobiiExtractFormat = type_extract_format_1.GobiiExtractFormat[gobiiFileItem.getItemId()];
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
                }
                markTreeItemMissing(gobiiExtractFilterType, gobiiFileItemCompoundId) {
                    //let icon: string = "fa-chevron-circle-right";
                    let icon = "fa-share";
                    //let icon: string = "fa-chevron-right";
                    this.store.dispatch(new treeNodeActions.SetTreeNodeLook({
                        gobiiExtractFilterType: gobiiExtractFilterType,
                        gobiiFileItemCompoundId: gobiiFileItemCompoundId,
                        icon: icon
                    }));
                }
                unMarkTreeItemMissing(gobiiExtractFilterType, gobiiFileItemCompoundId) {
                    let icons = this.getIcons(gobiiFileItemCompoundId, false);
                    this.store.dispatch(new treeNodeActions.SetTreeNodeLook({
                        gobiiExtractFilterType: gobiiExtractFilterType,
                        gobiiFileItemCompoundId: gobiiFileItemCompoundId,
                        icon: icons.icon
                    }));
                }
            };
            TreeStructureService = __decorate([
                core_1.Injectable(),
                __metadata("design:paramtypes", [store_1.Store])
            ], TreeStructureService);
            exports_1("TreeStructureService", TreeStructureService);
        }
    };
});
//# sourceMappingURL=tree-structure-service.js.map