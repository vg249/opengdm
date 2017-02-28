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


//Documentation of p-tree: http://www.primefaces.org/primeng/#/tree
@Component({
    selector: 'status-display-tree',
    inputs: ['fileItemEventChange', 'gobiiExtractFilterTypeEvent'],
    outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage'],
    template: ` 
                    <p-tree [value]="gobiiTreeNodes" selectionMode="checkbox" [(selection)]="selectedGobiiNodes"></p-tree>
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
            .subject
            .subscribe(te => {
                this.placeNodeInTree(te);
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
//        this.msgs.push({severity: 'info', summary: 'Node Unselected', detail: event.node.label});
    }

    nodeExpandMessage(event) {
//        this.msgs.push({severity: 'info', summary: 'Node Expanded', detail: event.node.label});
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

    addEntityIconToNode(entityType: EntityType, treeNode: GobiiTreeNode) {

        if (entityType === EntityType.DataSets) {

            treeNode.icon = "fa-database";
            treeNode.expandedIcon = "fa-database";
            treeNode.collapsedIcon = "fa-database";

        } else if (entityType === EntityType.Contacts) {

            treeNode.icon = "fa-address-book-o";
            treeNode.expandedIcon = "fa-address-book-o";
            treeNode.collapsedIcon = "fa-address-book-o";

        } else if (entityType === EntityType.Mapsets) {

            treeNode.icon = "fa-map-o";
            treeNode.expandedIcon = "fa-map-o";
            treeNode.collapsedIcon = "fa-map-o";
        }
    }


    addIconsToNode(statusTreeTemplate: FileModelNode, treeNode: GobiiTreeNode) {

        // if( fileModelNode.getItemType() == ExtractorItemType.ENTITY ) {

        if (statusTreeTemplate.getEntityType() != null
            && statusTreeTemplate.getEntityType() != EntityType.UNKNOWN) {

            this.addEntityIconToNode(statusTreeTemplate.getEntityType(), treeNode);

        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.EXPORT_FORMAT) {
            treeNode.icon = "fa-columns";
            treeNode.expandedIcon = "fa-columns";
            treeNode.collapsedIcon = "fa-columns";
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
            gobiiTreeNode.label = fileItemEvent.itemName;
        } else {
            gobiiTreeNode.label += statusTreeTemplate.getEntityName() + ": " + fileItemEvent.itemName;
        }
    }

    findTreeNodebyId(gobiiTreeNodes: GobiiTreeNode[], uniqueId: String): GobiiTreeNode {

        let returnVal:GobiiTreeNode = null;

        gobiiTreeNodes.forEach(currentTreeNode => {

            if ((currentTreeNode.fileItemId === uniqueId) || (currentTreeNode.fileModelNodeId === uniqueId)) {
                returnVal = currentTreeNode;
            } else {

                returnVal = this.findTreeNodebyId(currentTreeNode.children,uniqueId);
            }
        });

        return returnVal;
    }

    placeNodeInTree(fileModelTreeEvent: FileModelTreeEvent) {

        // if (fileModelTreeEvent.fileModelNode != null) {
        //
        //
        //     if (fileModelTreeEvent.fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {
        //
        //
        //
        //         // let treeNodeId: string = = fileModelTreeEvent
        //         //     .fileModelNode
        //         //     .getFileItems()[0].itemId;
        //
        //         let existingGobiiTreeNode: GobiiTreeNode = this.findTreeNodebyId(this.gobiiTreeNodes, treeNodeId);
        //
        //         this.addEntityNameToNode(fileModelTreeEvent, existingGobiiTreeNode, fileItemEvent);
        //
        //     } else if (fileModelTreeEvent.fileModelNode.getCategoryType() === ExtractorCategoryType.ENTITY_CONTAINER) {
        //
        //         let newGobiiTreeNode = new GobiiTreeNode();
        //         newGobiiTreeNode.entityType = fileItemEvent.entityType;
        //         this.addEntityIconToNode(newGobiiTreeNode.entityType, newGobiiTreeNode);
        //         this.addEntityNameToNode(fileModelTreeEvent, newGobiiTreeNode, fileItemEvent);
        //         fileModelTreeEvent.fileModelNode.getFileItems().children.push(newGobiiTreeNode);
        //         fileModelTreeEvent.fileModelNode.getFileItems().expanded = true;
        //         this.selectedGobiiNodes.push(newGobiiTreeNode);
        //         this.selectedGobiiNodes.push(fileModelTreeEvent.fileModelNode.getFileItems());
        //
        //     } else {
        //         this.reportMessage("The node of category  "
        //             + fileModelTreeEvent.fileModelNode.getCategoryType()
        //             + " for checkbox event " + fileItemEvent.itemName
        //             + " could not be placed in the tree ");
        //     }
        //
        //
        // } else {
        //
        //     this.reportMessage("Could not place checkbox event "
        //         + fileItemEvent.itemName
        //         + " in tree");
        // }

    } //


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
            returnVal.label = "Export Format";
        }

        this.addIconsToNode(fileModelNode, returnVal);

        if (null != returnVal) {

            if (( fileModelNode.getCategoryType() != ExtractorCategoryType.ENTITY_CONTAINER
                && fileModelNode.getChildren() !== null )
                && ( fileModelNode.getChildren().length > 0)) {

                fileModelNode.getChildren().forEach(
                    stt => {

                        let currentTreeNode: GobiiTreeNode = this.makeTreeNodeFromTemplate(stt);
                        if (null != currentTreeNode) {
                            returnVal.children.push(currentTreeNode);
                        }
                    }
                );
            }
        }

        return returnVal;
    }


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


//            this.placeNodeInTree(itemChangedEvent);
            //this.treeNodes.push(treeNode);

            //this.placeNodeInTree(treeNode);

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
