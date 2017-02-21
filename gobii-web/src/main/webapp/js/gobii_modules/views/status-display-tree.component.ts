import {Component, OnInit, ViewChild, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {TreeNode, Message, MenuItem} from "primeng/components/common/api";
import {Tree} from "primeng/components/tree/tree";
import {CheckBoxEvent} from "../model/event-checkbox";


//Documentation of p-tree: http://www.primefaces.org/primeng/#/tree
@Component({
    selector: 'status-display-tree',
    inputs: ['checkBoxEventChange'],
    outputs: ['onItemSelected', 'onItemChecked'],
    template: ` <p-tree [value]="treeNodes" selectionMode="checkbox" [(selection)]="selectedNodes"></p-tree>
                    <!--<div>Selected Nodes: <span *ngFor="let file of selectedFiles2">{{file.label}} </span></div>-->
`
})
export class StatusDisplayTreeComponent implements OnInit, OnChanges {


    constructor() {
    }

    ngOnInit() {


        this.treeNodes = [
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
                        }, {"label": "Untouchables", "icon": "fa-file-video-o", "data": "Untouchables Movie"}]
                    }]
            }
        ];


        this.selectedNodes = [];
        this.selectedNodes.push(this.treeNodes[1].children[0])
        this.treeNodes[1].partialSelected = true;
        this.treeNodes[1].expanded = true;
        let foo: string = "foo";
        // this.nodeService.getFiles().then(files => this.filesTree1 = files);
        // this.nodeService.getFiles().then(files => this.filesTree2 = files);
        // this.nodeService.getFiles().then(files => this.filesTree3 = files);
        // this.nodeService.getFiles().then(files => this.treeNodes = files);
        // this.nodeService.getFiles().then(files => this.filesTree5 = files);
        // this.nodeService.getFiles().then(files => this.filesTree6 = files);
        // this.nodeService.getFiles().then(files => this.filesTree7 = files);

        //     this.filesTree8 = [{
        //         label: 'Root',
        //         children: files
        //     }];
        // });
        //
        // this.nodeService.getLazyFiles().then(files => this.lazyFiles = files);
        //
        // this.items = [
        //     {label: 'View', icon: 'fa-search', command: (event) => this.viewFile(this.selectedFile2)},
        //     {label: 'Unselect', icon: 'fa-close', command: (event) => this.unselectFile()}
        // ];
    }

    // *****************************************************************
    // *********************  TREE NODE DATA STRUCTURES AND EVENTS
    treeNodes: TreeNode[];
    selectedNodes: TreeNode[];

    private experimentId:string;


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
        this.treeNodes.forEach(node => {
            this.expandRecursive(node, true);
        });
    }

    collapseAll() {
        this.treeNodes.forEach(node => {
            this.expandRecursive(node, false);
        });
    }

    private expandRecursive(node: TreeNode, isExpand: boolean) {
        node.expanded = isExpand;
        if (node.children) {
            node.children.forEach(childNode => {
                this.expandRecursive(childNode, isExpand);
            });
        }
    }


    // ********************************************************************************
    // ********************* CHECKBOX/TREE NODE CONVERSION FUNCTIONS


    private makeCbEventFromNode(treeNode: TreeNode): CheckBoxEvent {

        let returnVal: CheckBoxEvent = null;

        return returnVal;

    }


    private makeNodeFromCbEvent(cbEvent: CheckBoxEvent): TreeNode {

        let returnVal: TreeNode = null;

        return returnVal;
    }

    private placeNodeInTree(treeNode: TreeNode) {

    }


    // ********************************************************************************
    // ********************* CHECKBOX (GOBII-SPECIFIC)  NODE DATA STRUCTURES AND EVENTS

    private onItemChecked: EventEmitter<CheckBoxEvent> = new EventEmitter();
    private onItemSelected: EventEmitter<CheckBoxEvent> = new EventEmitter();

    ngOnChanges(changes: {[propName: string]: SimpleChange}) {

        if (changes['checkBoxEventChange'] && changes['checkBoxEventChange'].currentValue) {

            let itemChangedEvent: CheckBoxEvent = changes['checkBoxEventChange'].currentValue;

            let treeNode: TreeNode = this.makeNodeFromCbEvent(itemChangedEvent);

            // this.treeNodes.push(treeNode);
            //
            // this.placeNodeInTree(treeNode);

            // if (this.itemChangedEvent) {
            //     let itemToChange:CheckBoxEvent =
            //         this.checkBoxEvents.filter(e => {
            //             return e.id == changes['checkBoxEventChange'].currentValue.id;
            //         })[0];
            //
            //     //let indexOfItemToChange:number = this.checkBoxEvents.indexOf(arg.currentTarget.name);
            //     if (itemToChange) {
            //         itemToChange.processType = changes['checkBoxEventChange'].currentValue.processType;
            //         itemToChange.checked = changes['checkBoxEventChange'].currentValue.checked;
            //     }
            // }
        } else if (changes['nameIdList'] && changes['nameIdList'].currentValue) {

            // this.setList(changes['nameIdList'].currentValue);

        }
    }

}
