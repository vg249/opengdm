import {Component, OnInit, ViewChild, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {TreeNode, Message, MenuItem} from "primeng/components/common/api";
import {Tree} from "primeng/components/tree/tree";
import {FileItem} from "../model/file-item";
import {GobiiTreeNode} from "../model/GobiiTreeNode";
import {EntityType, EntitySubType} from "../model/type-entity";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {
    FileModelNode, ExtractorItemType, ExtractorCategoryType,
    CardinalityType
} from "../model/file-model-node";
import {CvFilterType} from "../model/cv-filter-type";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {FileModelTreeEvent} from "../model/file-model-tree-event";
import {ProcessType} from "../model/type-process";


//Documentation of p-tree: http://www.primefaces.org/primeng/#/tree
@Component({
    selector: 'status-display-tree',
    inputs: ['fileItemEventChange', 'gobiiExtractFilterTypeEvent'],
    outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage'],
    template: ` 
                    <p-tree [value]="gobiiTreeNodes" 
                    selectionMode="checkbox" 
                    [(selection)]="selectedGobiiNodes"
                    (onNodeUnselect)="nodeUnselect($event)" ></p-tree>
                    <!--<p-tree [value]="demoTreeNodes" selectionMode="checkbox" [(selection)]="selectedDemoNodes"></p-tree>-->
                    <!--<div>Selected Nodes: <span *ngFor="let file of selectedFiles2">{{file.label}} </span></div>-->
`
})
export class StatusDisplayTreeComponent implements OnInit, OnChanges {

    private onAddMessage: EventEmitter<string> = new EventEmitter();

    private reportMessage(arg) {
        this.onAddMessage.emit(arg);
    }

    constructor(private _fileModelTreeService: FileModelTreeService) {
    }

    ngOnInit() {

        this._fileModelTreeService
            .treeNotifications()
            .subscribe(te => {

                if (te.fileItem.getProcessType() === ProcessType.CREATE) {
                    this.placeNodeInTree(te);
                } else if (te.fileItem.getProcessType() === ProcessType.DELETE) {
                    this.removeNodeFromTree(te);
                }
            });

        // this.makeDemoTreeNodes();
        // this.setUpRequredItems();

    }

// *****************************************************************
// *********************  TREE NODE DATA STRUCTURES AND EVENTS

    demoTreeNodes: TreeNode[] = [];
    selectedDemoNodes: TreeNode[] = [];


    gobiiTreeNodes: GobiiTreeNode[] = [];
    selectedGobiiNodes: GobiiTreeNode[] = [];
    // entityNodeLabels: Map < EntityType, string > = new Map<EntityType,string>();
    // entitySubtypeNodeLabels: Map < EntitySubType, string > = new Map<EntitySubType,string>();
    // cvFilterNodeLabels: Map < CvFilterType, string > = new Map<CvFilterType,string>();
    // extractorFilterTypeLabels: Map < GobiiExtractFilterType, string > = new Map<GobiiExtractFilterType, string>();
    // treeCategoryLabels: Map<ExtractorCategoryType,string> = new Map<ExtractorCategoryType,string>();


    experimentId: string;


    nodeSelect(event) {
        //      this.msgs.push({severity: 'info', summary: 'Node Selected', detail: event.node.label});
    }

    nodeUnselect(event) {


        let unselectedTreeNode: GobiiTreeNode = event.node;

        let fileItem: FileItem = this.makeFileItemFromTreeNode(unselectedTreeNode, false);

        this._fileModelTreeService.put(fileItem).subscribe();
    }

    makeFileItemFromTreeNode(gobiiTreeNode: GobiiTreeNode, checked: boolean): FileItem {

        let returnVal: FileItem = FileItem.build(
            this.gobiiExtractFilterType,
            (checked ? ProcessType.CREATE : ProcessType.DELETE))
            .setEntityType(gobiiTreeNode.entityType)
            .setCvFilterType(gobiiTreeNode.cvFilterType)
            .setItemId(null)
            .setItemName(gobiiTreeNode.label)
            .setChecked(checked)
            .setRequired(null);

        returnVal.setFileItemUniqueId(gobiiTreeNode.fileItemId);

        return returnVal;
    }

    nodeExpandMessage(event) {

    }

    nodeExpand(event) {
        if (event.node) {
            //in a real application, make a call to a remote url to load children of the current node and add the new nodes as children
            //this.nodeService.getLazyFiles().then(nodes => event.node.children = nodes);
        }
    }

    viewFile(file: TreeNode) {
        //      this.msgs.push({severity: 'info', summary: 'Node Selected with Right Click', detail: file.label});
    }

    unselectFile() {
//        this.selectedFile2 = null;
    }

    expandAll() {
        this.gobiiTreeNodes.forEach(node => {
            this.expandRecursive(node, true);
        });
    }

    collapseAll() {
        this.gobiiTreeNodes.forEach(node => {
            this.expandRecursive(node, false);
        });
    }


    expandRecursive(node: TreeNode, isExpand: boolean) {
        node.expanded = isExpand;
        if (node.children) {
            node.children.forEach(childNode => {
                this.expandRecursive(childNode, isExpand);
            });
        }
    }


// ********************************************************************************
// ********************* CHECKBOX/TREE NODE CONVERSION FUNCTIONS

    addEntityIconToNode(entityType: EntityType, cvFilterType: CvFilterType, treeNode: GobiiTreeNode) {

        if (entityType === EntityType.DataSets) {

            treeNode.icon = "fa-database";
            treeNode.expandedIcon = "fa-folder-expanded";
            treeNode.collapsedIcon = "fa-database";

        } else if (entityType === EntityType.Contacts) {

            treeNode.icon = "fa-user-o";
            treeNode.expandedIcon = "fa-user-o";
            treeNode.collapsedIcon = "fa-user-o";

        } else if (entityType === EntityType.Mapsets) {

            treeNode.icon = "fa-map-o";
            treeNode.expandedIcon = "fa-map-o";
            treeNode.collapsedIcon = "fa-map-o";

        } else if (entityType === EntityType.Platforms) {

            treeNode.icon = "fa-calculator";
            treeNode.expandedIcon = "fa-calculator";
            treeNode.collapsedIcon = "fa-calculator";

        } else if (entityType === EntityType.Projects) {

            treeNode.icon = "fa-clipboard";
            treeNode.expandedIcon = "fa-clipboard";
            treeNode.collapsedIcon = "fa-clipboard";

        } else if (entityType === EntityType.CvTerms) {

            if (cvFilterType === CvFilterType.DATASET_TYPE) {
                treeNode.icon = "fa-file-excel-o";
                treeNode.expandedIcon = "fa-file-excel-o";
                treeNode.collapsedIcon = "fa-file-excel-o";
            }

        }
    }


    addIconsToNode(statusTreeTemplate: FileModelNode, treeNode: GobiiTreeNode) {

        // if( fileModelNode.getItemType() == ExtractorItemType.ENTITY ) {

        if (statusTreeTemplate.getEntityType() != null
            && statusTreeTemplate.getEntityType() != EntityType.UNKNOWN) {

            this.addEntityIconToNode(statusTreeTemplate.getEntityType(), statusTreeTemplate.getCvFilterType(), treeNode);

        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.EXPORT_FORMAT) {
            treeNode.icon = "fa-columns";
            treeNode.expandedIcon = "fa-columns";
            treeNode.collapsedIcon = "fa-columns";
        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.SAMPLE_LIST) {
            treeNode.icon = "fa-eyedropper";
            treeNode.expandedIcon = "fa-eyedropper";
            treeNode.collapsedIcon = "fa-eyedropper";
        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.MARKER_LIST) {
            treeNode.icon = "fa-map-marker";
            treeNode.expandedIcon = "fa-map-marker";
            treeNode.collapsedIcon = "fa-map-marker";
        } else {
            //     }
            // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
            treeNode.icon = "fa-folder";
            treeNode.expandedIcon = "fa-folder-expanded";
            treeNode.collapsedIcon = "fa-folder";
        }
    }


    addEntityNameToNode(statusTreeTemplate: FileModelNode, gobiiTreeNode: GobiiTreeNode, fileItemEvent: FileItem) {

        if (statusTreeTemplate.getCategoryType() === ExtractorCategoryType.ENTITY_CONTAINER) {
            gobiiTreeNode.label = fileItemEvent.getItemName();
        } else {
            gobiiTreeNode.label += statusTreeTemplate.getEntityName() + ": " + fileItemEvent.getItemName();
        }
    }

    findTreeNodebyModelNodeId(gobiiTreeNodes: GobiiTreeNode[], fileModelNodeId: String): GobiiTreeNode {

        let foo: string = "bar";
        let returnVal: GobiiTreeNode = null;


        for (let idx: number = 0; (idx < gobiiTreeNodes.length) && (returnVal === null); idx++) {
            let currentTreeNode: GobiiTreeNode = gobiiTreeNodes[idx];

            if (currentTreeNode.fileModelNodeId === fileModelNodeId) {
                returnVal = currentTreeNode;
            } else {

                returnVal = this.findTreeNodebyModelNodeId(currentTreeNode.children, fileModelNodeId);
            }
        } // iterate gobii nodes


        return returnVal;
    }

    findTreeNodebyFileItemIdId(gobiiTreeNodes: GobiiTreeNode[], fileItemId: String): GobiiTreeNode {

        let returnVal: GobiiTreeNode = null;

        gobiiTreeNodes.forEach(currentTreeNode => {

            if (currentTreeNode.fileItemId === fileItemId) {
                returnVal = currentTreeNode;
            } else {

                returnVal = this.findTreeNodebyModelNodeId(currentTreeNode.children, fileItemId);
            }
        });

        return returnVal;
    }

    removeNodeFromTree(fileModelTreeEvent: FileModelTreeEvent) {
        if (fileModelTreeEvent.fileModelNode != null && fileModelTreeEvent.fileItem != null) {

            if (fileModelTreeEvent.fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {

                let gobiiTreeNodeToBeRemoved: GobiiTreeNode = this.findTreeNodebyFileItemIdId(this.gobiiTreeNodes, fileModelTreeEvent.fileItem.getFileItemUniqueId());

                if (gobiiTreeNodeToBeRemoved !== null) {

                    // will need a funciton to do this correctly
                    gobiiTreeNodeToBeRemoved.label = "name reset";


                } else {
                    // error node not found?
                } // if-else we found an existing node for the LEAF node's file item


            } else if (fileModelTreeEvent.fileModelNode.getCategoryType() === ExtractorCategoryType.ENTITY_CONTAINER) {

                // there should not be a file item associated with the model because it's a container -- the file items are just for the children
                let parentTreeNode: GobiiTreeNode = this.findTreeNodebyModelNodeId(this.gobiiTreeNodes, fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId());
                if (parentTreeNode != null) {
                    let nodeToDelete: GobiiTreeNode = parentTreeNode.children.find(n => {
                        return n.fileItemId === fileModelTreeEvent.fileItem.getFileItemUniqueId()
                    });

                    if (nodeToDelete != null) {
                        let idxOfNodeToDelete: number = parentTreeNode.children.indexOf(nodeToDelete);
                        parentTreeNode.children.splice(idxOfNodeToDelete, 1);
                    }

                } else {
                    // error?
                }


                // if (parentTreeNode != null) {
                //
                //     let existingFileModelItem: FileItem = fileModelTreeEvent
                //         .fileModelNode
                //         .getChildFileItems()
                //         .find(item => {
                //             return item.fileItemUniqueId === fileModelTreeEvent.fileItem.fileItemUniqueId
                //         });
                //
                //     if (existingFileModelItem !== null) {
                //
                //
                //         let existingGobiiTreeNodeChild: GobiiTreeNode = this.findTreeNodebyFileItemIdId(this.gobiiTreeNodes, existingFileModelItem.fileItemUniqueId);
                //
                //         if (existingGobiiTreeNodeChild === null) {
                //
                //             let newGobiiTreeNode: GobiiTreeNode =
                //                 new GobiiTreeNode(fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId(),
                //                     fileModelTreeEvent.fileItem.fileItemUniqueId);
                //             newGobiiTreeNode.entityType = fileModelTreeEvent.fileItem.entityType;
                //             this.addEntityIconToNode(fileModelTreeEvent.fileModelNode.getEntityType(), fileModelTreeEvent.fileModelNode.getCvFilterType(), newGobiiTreeNode);
                //             this.addEntityNameToNode(fileModelTreeEvent.fileModelNode, newGobiiTreeNode, fileModelTreeEvent.fileItem);
                //             parentTreeNode.children.push(newGobiiTreeNode);
                //             parentTreeNode.expanded = true;
                //             this.selectedGobiiNodes.push(newGobiiTreeNode);
                //             this.selectedGobiiNodes.push(parentTreeNode);
                //         } else {
                //             // modify existing existingGobiiTreeNodeChild
                //         } // if-else there already exists a corresponding tree node
                //
                //     } else {
                //         // error condition
                //     } // if else we found an existing file item
                //
                // } else {
                //     // error condition
                // } // if-else we found a tree node to serve as parent for the container's item tree nodes

            } // if-else -if on extractor category type

        } else {
            // error condition: invalid event
        } // there i sno file mode node for tree event
    }

    placeNodeInTree(fileModelTreeEvent: FileModelTreeEvent) {

        if (fileModelTreeEvent.fileModelNode != null && fileModelTreeEvent.fileItem != null) {

            if (fileModelTreeEvent.fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {

                let gobiiTreeNodeToBePlaced: GobiiTreeNode = this.findTreeNodebyFileItemIdId(this.gobiiTreeNodes, fileModelTreeEvent.fileItem.getFileItemUniqueId());

                if (gobiiTreeNodeToBePlaced === null) {

                    let newGobiiTreeNode: GobiiTreeNode = new GobiiTreeNode(fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId(),
                        fileModelTreeEvent.fileItem.getFileItemUniqueId());

                    this.addEntityNameToNode(fileModelTreeEvent.fileModelNode, newGobiiTreeNode, fileModelTreeEvent.fileItem);
                    this.addEntityIconToNode(fileModelTreeEvent.fileModelNode.getEntityType(), fileModelTreeEvent.fileModelNode.getCvFilterType(), newGobiiTreeNode);

                    // now we need to add the new tree node to the parent
                    if (fileModelTreeEvent.fileModelNode.getParent() != null) {

                        let fileModelNodeParent: FileModelNode = fileModelTreeEvent.fileModelNode.getParent();
                        let parentTreeNode: GobiiTreeNode = this.findTreeNodebyModelNodeId(this.gobiiTreeNodes, fileModelNodeParent.getFileModelNodeUniqueId());

                        if (parentTreeNode != null) {
                            parentTreeNode.children.push(newGobiiTreeNode);
                        } else {
                            // error condition
                        } // the model tree's parent does not have a corresponding tree node

                    } else {
                        // error condition

                    } // if-else the model node has a parent


                } else {
                    // modify existing node?
                } // if-else we found an existing node for the LEAF node's file item


            } else if (fileModelTreeEvent.fileModelNode.getCategoryType() === ExtractorCategoryType.ENTITY_CONTAINER) {

                // there should not be a file item associated with the model because it's a container -- the file items are just for the children
                let parentTreeNode: GobiiTreeNode = this.findTreeNodebyModelNodeId(this.gobiiTreeNodes, fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId());
                if (parentTreeNode != null) {

                    let existingFileModelItem: FileItem = fileModelTreeEvent
                        .fileModelNode
                        .getChildFileItems()
                        .find(item => {
                            return item.getFileItemUniqueId() === fileModelTreeEvent.fileItem.getFileItemUniqueId()
                        });

                    if (existingFileModelItem !== null) {


                        let existingGobiiTreeNodeChild: GobiiTreeNode = this.findTreeNodebyFileItemIdId(this.gobiiTreeNodes, existingFileModelItem.getFileItemUniqueId());

                        if (existingGobiiTreeNodeChild === null) {

                            let newGobiiTreeNode: GobiiTreeNode =
                                new GobiiTreeNode(fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId(),
                                    fileModelTreeEvent.fileItem.getFileItemUniqueId());
                            newGobiiTreeNode.entityType = fileModelTreeEvent.fileItem.getEntityType();
                            this.addEntityIconToNode(fileModelTreeEvent.fileModelNode.getEntityType(), fileModelTreeEvent.fileModelNode.getCvFilterType(), newGobiiTreeNode);
                            this.addEntityNameToNode(fileModelTreeEvent.fileModelNode, newGobiiTreeNode, fileModelTreeEvent.fileItem);
                            parentTreeNode.children.push(newGobiiTreeNode);
                            parentTreeNode.expanded = true;
                            this.selectedGobiiNodes.push(newGobiiTreeNode);
                            this.selectedGobiiNodes.push(parentTreeNode);
                        } else {
                            // modify existing existingGobiiTreeNodeChild
                        } // if-else there already exists a corresponding tree node

                    } else {
                        // error condition
                    } // if else we found an existing file item

                } else {
                    // error condition
                } // if-else we found a tree node to serve as parent for the container's item tree nodes

            } // if-else -if on extractor category type

        } else {
            // error condition: invalid event
        } // there i sno file mode node for tree event
    } // place node in tree


    setUpRequredItems(gobiiExtractorFilterType: GobiiExtractFilterType) {

        this.gobiiTreeNodes = [];

        let fileModelNodes: FileModelNode[] = [];
        this._fileModelTreeService.get(gobiiExtractorFilterType).subscribe(
            f => {
                fileModelNodes = f;
            }
        );

        fileModelNodes.forEach(
            currentFirstLevelFileModelNode => {

                let currentTreeNode: GobiiTreeNode = this.makeTreeNodeFromTemplate(currentFirstLevelFileModelNode);
                if (currentTreeNode != null) {
                    this.gobiiTreeNodes.push(currentTreeNode);
                }
            }
        );
    }

    makeTreeNodeFromTemplate(fileModelNode: FileModelNode): GobiiTreeNode {

        let returnVal: GobiiTreeNode = null;


        if (fileModelNode.getItemType() === ExtractorItemType.ENTITY) {

            returnVal = new GobiiTreeNode(fileModelNode.getFileModelNodeUniqueId(), null);
            returnVal.entityType = fileModelNode.getEntityType();
            returnVal.label = fileModelNode.getEntityName();


        } else if (fileModelNode.getItemType() === ExtractorItemType.CATEGORY) {

            returnVal = new GobiiTreeNode(fileModelNode.getFileModelNodeUniqueId(), null);

            if (fileModelNode.getEntityType() != null
                && fileModelNode.getEntityType() != EntityType.UNKNOWN) {
                returnVal.entityType = fileModelNode.getEntityType();
            }

            returnVal.label = fileModelNode.getCategoryName();

        } else if (fileModelNode.getItemType() == ExtractorItemType.EXPORT_FORMAT) {

            returnVal = new GobiiTreeNode(fileModelNode.getFileModelNodeUniqueId(), null);
            returnVal.label = fileModelNode.getCategoryName();
        } else if (fileModelNode.getItemType() == ExtractorItemType.SAMPLE_LIST) {

            returnVal = new GobiiTreeNode(fileModelNode.getFileModelNodeUniqueId(), null);
            returnVal.label = fileModelNode.getCategoryName();
        } else if (fileModelNode.getItemType() == ExtractorItemType.MARKER_LIST) {

            returnVal = new GobiiTreeNode(fileModelNode.getFileModelNodeUniqueId(), null);
            returnVal.label = fileModelNode.getCategoryName();
        }


        if (null != returnVal) {

            let debug: string = "debug";
            this.addIconsToNode(fileModelNode, returnVal);

            returnVal.expanded = true;
            fileModelNode.getChildren().forEach(
                stt => {

                    let currentTreeNode: GobiiTreeNode = this.makeTreeNodeFromTemplate(stt);
                    if (null != currentTreeNode) {
                        returnVal.children.push(currentTreeNode);
                    }
                }
            ); // iterate child model node
        } // if we created a tree node

        return returnVal;
    }


// ********************************************************************************
// ********************* CHECKBOX (GOBII-SPECIFIC)  NODE DATA STRUCTURES AND EVENTS


    gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;

    onItemChecked: EventEmitter < FileItem > = new EventEmitter();
    onItemSelected: EventEmitter < FileItem > = new EventEmitter();


    fileModelNodeTree: Map < GobiiExtractFilterType, Array < FileModelNode >> =
        new Map<GobiiExtractFilterType,Array<FileModelNode>>();

    ngOnChanges(changes: {
        [propName: string
            ]: SimpleChange
    }) {

        if (changes['fileItemEventChange'] && changes['fileItemEventChange'].currentValue) {

            let itemChangedEvent: FileItem = changes['fileItemEventChange'].currentValue;


//            this.placeNodeInModel(itemChangedEvent);
            //this.treeNodes.push(treeNode);

            //this.placeNodeInModel(treeNode);

            // this.treeNodes.push(treeNode);
            //


            // if (this.itemChangedEvent) {
            //     let itemToChange:FileItem =
            //         this.fileItemEvents.filter(e => {
            //             return e.id == changes['fileItemEventChange'].currentValue.id;
            //         })[0];
            //
            //     //let indexOfItemToChange:number = this.fileItemEvents.indexOf(arg.currentTarget.name);
            //     if (itemToChange) {
            //         itemToChange.processType = changes['fileItemEventChange'].currentValue.processType;
            //         itemToChange.checked = changes['fileItemEventChange'].currentValue.checked;
            //     }
            // }
        } else if (changes['gobiiExtractFilterTypeEvent']
            && ( changes['gobiiExtractFilterTypeEvent'].currentValue != null )
            && ( changes['gobiiExtractFilterTypeEvent'].currentValue != undefined )) {

            let newGobiiExtractFilterType: GobiiExtractFilterType = changes['gobiiExtractFilterTypeEvent'].currentValue;

            if (newGobiiExtractFilterType !== this.gobiiExtractFilterType) {
                this.gobiiExtractFilterType = changes['gobiiExtractFilterTypeEvent'].currentValue;
                //this.getTemplates(this.gobiiExtractFilterType, true);
                this.setUpRequredItems(newGobiiExtractFilterType);
            }


            // this.setList(changes['nameIdList'].currentValue);

        }
    }


    makeDemoTreeNodes() {

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
                    }, {"label": "Resume.doc", "icon": "fa-file-word-o", "data": "Resume Document"}]
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
                    {"label": "barcelona.jpg", "icon": "fa-file-image-o", "data": "Barcelona Photo"},
                    {"label": "logo.jpg", "icon": "fa-file-image-o", "data": "PrimeFaces Logo"},
                    {"label": "primeui.png", "icon": "fa-file-image-o", "data": "PrimeUI Logo"}]
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
                    }, {"label": "Serpico", "icon": "fa-file-video-o", "data": "Serpico Movie"}]
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


        this.selectedDemoNodes.push(this.demoTreeNodes[1].children[0])
        this.demoTreeNodes[1].partialSelected = true;
        this.demoTreeNodes[1].expanded = true;
    }
}
