import {Component, EventEmitter, OnChanges, OnInit, SimpleChange} from "@angular/core";
import {TreeNode} from "primeng/components/common/api";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiTreeNode} from "../model/GobiiTreeNode";
import {EntityType} from "../model/type-entity";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {CvFilterType} from "../model/cv-filter-type";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import * as fromRoot from '../store/reducers';
import {Store} from "@ngrx/store";
import {Observable} from "rxjs/Observable";


//Documentation of p-tree: http://www.primefaces.org/primeng/#/tree
//width of p-tree does not take if it's in the style sheet class; you have to inline it
@Component({
    selector: 'status-display-tree',
    inputs: ['fileItemEventChange', 'gobiiExtractFilterTypeEvent'],
    outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage', 'onTreeReady'],
    template: `
        <p-tree [value]="gobiiTreeNodesFromStore$ | async"
                selectionMode="checkbox"
                propagateSelectionUp="false"
                propagateSelectionDown="false"
                [selection]="gobiiSelectedNodesFromStore$ | async"
                (onNodeUnselect)="nodeUnselect($event)"
                (onNodeSelect)="nodeSelect($event)"
                (onNodeExpand)="nodeExpand($event)"
                (onNodeCollapse)="nodeCollapse($event)"
                [style]="{'width':'100%'}"
                styleClass="criteria-tree"></p-tree>
        <!--<p-tree [value]="demoTreeNodes" selectionMode="checkbox" [(selection)]="selectedDemoNodes"></p-tree>-->
        <!--<div>Selected Nodes: <span *ngFor="let file of selectedFiles2">{{file.label}} </span></div>-->
    `
})
export class StatusDisplayTreeComponent implements OnInit, OnChanges {


    private containerCollapseThreshold = 10;
    private onAddMessage: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private onTreeReady: EventEmitter<HeaderStatusMessage> = new EventEmitter();

    gobiiTreeNodesFromStore$: Observable<GobiiTreeNode[]>;
    gobiiSelectedNodesFromStore$: Observable<GobiiTreeNode[]>;

    constructor(private store: Store<fromRoot.State>) {

        this.gobiiTreeNodesFromStore$ = store
            .select(fromRoot.getGobiiTreeNodesForExtractFilter);

        this.gobiiSelectedNodesFromStore$ = store
            .select(fromRoot.getSelectedGobiiTreeNodes);
    }

    ngOnInit() {

        let foo: string = "bar";


        // this.makeDemoTreeNodes();
        // this.setUpRequredItems();

    }




// *****************************************************************
// *********************  TREE NODE DATA STRUCTURES AND EVENTS

    demoTreeNodes: TreeNode[] = [];
    selectedDemoNodes: TreeNode[] = [];


//    gobiiTreeNodes$: Observable<GobiiTreeNode[]>;
    gobiiTreeNodes$: GobiiTreeNode[];
    selectedGobiiNodes: GobiiTreeNode[] = [];

    experimentId: string;


    nodeSelect(event) {

        // Unless a node already is checked such that it has data, we don't allow checking
        // something because it has no meaning without data in it; these would typically
        // by CONTAINER type nodes: once they have children they're selected, and it which
        // point we deal with check events in nodeUnselect()
        // yes this is a bit of a kludge; version 4 of PrimeNG will add a selectable proeprty
        // to TreeNode which will enable us to approch selectability of nodes in general in
        // a more systematic and elegant way



        let selectedGobiiTreeNode: GobiiTreeNode = event.node;

        selectedGobiiTreeNode.children.forEach(childNode => {
            this.removeItemFromSelectedNodes(childNode);
        })

        this.removeItemFromSelectedNodes(selectedGobiiTreeNode);

    }


    // we need to disable partial selection because when you click
    // a node that's partially selected, you don't get the unselect event
    // which breaks everything
    unsetPartialSelect(gobiiTreeNode: GobiiTreeNode) {

        let thereAreSelectedChildren: boolean = false;
        if (gobiiTreeNode.partialSelected) {

            gobiiTreeNode.partialSelected = false;

            let foo: string = "foo";

            for (let idx: number = 0;
                 (idx < gobiiTreeNode.children.length) && !thereAreSelectedChildren; idx++) {

                let currentTreeNode: GobiiTreeNode = gobiiTreeNode.children[idx];
                thereAreSelectedChildren = this.selectedGobiiNodes.find(fi => {

                    return fi
                        && fi.fileItemId
                        && (fi.fileItemId === currentTreeNode.fileItemId)
                }) != undefined;
            }

            if (thereAreSelectedChildren) {
                this.selectedGobiiNodes.push(gobiiTreeNode);
            }
        }

        if (( gobiiTreeNode.parent !== null )
            && ( gobiiTreeNode.parent !== undefined )) {
            this.unsetPartialSelect(gobiiTreeNode.parent);
        }

    }

    nodeUnselect(event) {

        // this funditonality is nearly working;
        // but it breaks down in the marker criteria section of the
        // tree. There is no more time to work on this. It must just
        // effectively disabled for now: you can only select and deselect
        // from the controls outside the tree
        let unselectedTreeNode: GobiiTreeNode = event.node;
        this.unsetPartialSelect(unselectedTreeNode);
        this.selectedGobiiNodes.push(unselectedTreeNode);
        unselectedTreeNode.children.forEach(tn => {
            this.selectedGobiiNodes.push(tn);
        })

    }


    nodeExpand(event) {
        if (event.node) {
        }
    }

    nodeCollapse(event) {
        if (event.node) {

        }
    }

    addCountToContainerNode(node: TreeNode) {
        let foo: string = "foo";

        let parenPosition: number = node.label.indexOf("(");
        if (parenPosition > 0) {
            node.label = node.label.substring(0, parenPosition);
        }

        if (node.children.length > 0) {

            node.label += " (" + node.children.length + ")";

        }


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

        } else if (entityType === EntityType.MarkerGroups) {

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
    }


    removeItemFromSelectedNodes(gobiiTreeNode: GobiiTreeNode) {

        if (gobiiTreeNode) {

            let idxOfSelectedNodeParentNode: number = this.selectedGobiiNodes.indexOf(gobiiTreeNode);
            if (idxOfSelectedNodeParentNode >= 0) {
                let deleted: GobiiTreeNode[] = this.selectedGobiiNodes.splice(idxOfSelectedNodeParentNode, 1);
                let foo: string = "foo";
            }
        }


    }



    private treeIsInitialized: boolean = false;


// ********************************************************************************
// ********************* CHECKBOX (GOBII-SPECIFIC)  NODE DATA STRUCTURES AND EVENTS


    gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;

    onItemChecked: EventEmitter<GobiiFileItem> = new EventEmitter();
    onItemSelected: EventEmitter<GobiiFileItem> = new EventEmitter();


    ngOnChanges(changes: {
        [propName: string
            ]: SimpleChange
    }) {

        if (changes['fileItemEventChange'] && changes['fileItemEventChange'].currentValue) {

        } else if (changes['gobiiExtractFilterTypeEvent']
            && ( changes['gobiiExtractFilterTypeEvent'].currentValue != null )
            && ( changes['gobiiExtractFilterTypeEvent'].currentValue != undefined )) {


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
