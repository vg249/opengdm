import {FileItem} from "./file-item";
import {FileModelNode} from "./file-model-node";

export enum FileModelState {
    UNKNOWN,
    NOT_COMPLETE,
    ERROR,
    COMPLETE
}

export class FileModelTreeEvent {

    constructor(public fileItem: FileItem,
                public statusTreeTemplate: FileModelNode,
                public fileModelState: FileModelState,
                public message:string) {

        this.fileItem = fileItem;
        this.statusTreeTemplate = statusTreeTemplate;
        this.fileModelState = fileModelState;

    }

}   