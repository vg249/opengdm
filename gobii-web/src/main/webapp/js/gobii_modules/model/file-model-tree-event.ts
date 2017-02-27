import {FileItem} from "./file-item";
import {FileModelNode} from "./file-model-node";

export enum FileModelState {
    UNKNOWN,
    NOT_COMPLETE,
    COMPLETE
}

export class FileModelTreeEvent {

    constructor(public fileItem: FileItem,
                public statusTreeTemplate: FileModelNode,
                public fileModelState: FileModelState) {

        this.fileItem = fileItem;
        this.statusTreeTemplate = statusTreeTemplate
        this.fileModelState = fileModelState;
    }
}   