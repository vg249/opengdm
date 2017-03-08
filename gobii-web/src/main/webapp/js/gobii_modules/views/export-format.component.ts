import {Component, OnInit, EventEmitter, OnChanges, SimpleChange} from '@angular/core';
import {GobiiExtractFormat} from "../model/type-extract-format";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {FileItem} from "../model/file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {Header} from "../model/payload/header";


@Component({
    selector: 'export-format',
    outputs: ['onFormatSelected','onError'],
    inputs: ['gobiiExtractFilterType'],
    //directives: [RADIO_GROUP_DIRECTIVES]
//  directives: [Alert]
    template: `
    		  <label class="the-label">Select Format:</label><BR>
              &nbsp;&nbsp;&nbsp;<input type="radio" (change)="handleFormatSelected($event)" name="format" value="HAPMAP" checked="checked">Hapmap<br>
              &nbsp;&nbsp;&nbsp;<input type="radio" (change)="handleFormatSelected($event)" name="format" value="FLAPJACK">FlapJack<br>
              &nbsp;&nbsp;&nbsp;<input type="radio" (change)="handleFormatSelected($event)" name="format" value="META_DATA_ONLY">Dataset Metadata Only<br>
	` // end template
})

export class ExportFormatComponent implements OnInit, OnChanges {

    constructor(private _fileModelTreeService: FileModelTreeService) {
    } // ctor

    ngOnInit() {
        //this.updateTreeService(GobiiExtractFormat.HAPMAP);
    }

    private handleResponseHeader(header: Header) {

        this.onError.emit(header);
    }


    private gobiiExtractFilterType:GobiiExtractFilterType;
    private onFormatSelected: EventEmitter<GobiiExtractFormat> = new EventEmitter();
    private onError: EventEmitter<Header> = new EventEmitter();

    private handleFormatSelected(arg) {
        if (arg.srcElement.checked) {

            let radioValue: string = arg.srcElement.value;
            this.selectedExtractFormat = GobiiExtractFormat[radioValue];

            this.updateTreeService(this.selectedExtractFormat);

        }
        let foo = arg;
        //this.onContactSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    }

    private selectedExtractFormat: GobiiExtractFormat = GobiiExtractFormat.HAPMAP;
    private updateTreeService(arg: GobiiExtractFormat) {

        this.selectedExtractFormat = arg;

        let extractFilterTypeFileItem: FileItem = FileItem
            .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
            .setExtractorItemType(ExtractorItemType.EXPORT_FORMAT)
            .setItemId(GobiiExtractFormat[arg])
            .setItemName(GobiiExtractFormat[GobiiExtractFormat[arg]]);

        this._fileModelTreeService.put(extractFilterTypeFileItem)
            .subscribe(
                null,
                headerResponse => {
                    this.handleResponseHeader(headerResponse)
                });

        //console.log("selected contact itemId:" + arg);
    }

    ngOnChanges(changes: {[propName: string]: SimpleChange}) {

        if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                this.updateTreeService(GobiiExtractFormat.HAPMAP);

            } // if we have a new filter type

        } // if filter type changed

    } // ngonChanges



}
