import {FileItem} from "./file-item";
import {StatusTreeTemplate} from "./extractor-submission-item";

export enum FileModelState {
    UNKNOWN,
    NOT_COMPLETE,
    COMPLETE
}

export class FileModelTreeEvent {

    constructor(public fileItem: FileItem,
                public statusTreeTemplate: StatusTreeTemplate,
                public fileModelState: FileModelState) {

        this.fileItem = fileItem;
        this.statusTreeTemplate = statusTreeTemplate
        this.fileModelState = fileModelState;
    }
}   