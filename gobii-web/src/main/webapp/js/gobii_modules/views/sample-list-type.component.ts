import {Component, OnInit, EventEmitter, OnChanges, SimpleChanges, SimpleChange} from '@angular/core';
import {EntityFilter} from "../model/type-entity-filter";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {GobiiSampleListType} from "../model/type-extractor-sample-list";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";


@Component({
    selector: 'sample-list-type',
    inputs: ['gobiiExtractFilterType'],
    outputs: ['onSampleListTypeSelected', 'onHeaderStatusMessage'],
    template: `<label class="the-label">Export By:</label><BR>
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="GERMPLASM_NAME" checked="checked">Germplasm Name<BR>
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="EXTERNAL_CODE">External Code<BR>
                  <input type="radio" (change)="handleExportTypeSelected($event)" name="format" value="DNA_SAMPLE">DNA Sample<BR>` // end template
})

export class SampleListTypeComponent implements OnInit, OnChanges {

    constructor(private _fileModelTreeService: FileModelTreeService) {
    } // ctor

    private onSampleListTypeSelected: EventEmitter<GobiiSampleListType> = new EventEmitter();

    private onHeaderStatusMessage: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private gobiiExtractFilterType:GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;

    private handleExportTypeSelected(arg) {
        if (arg.srcElement.checked) {

            let radioValue: string = arg.srcElement.value;

            let gobiiSampleListType: GobiiSampleListType = GobiiSampleListType[radioValue];

            this.submitSampleListTypeToService(gobiiSampleListType);
            this.onSampleListTypeSelected.emit(gobiiSampleListType)

        }
    }

    private submitSampleListTypeToService(gobiiSampleListType: GobiiSampleListType) {
        this._fileModelTreeService
            .put(GobiiFileItem.build(this.gobiiExtractFilterType,ProcessType.CREATE)
                .setExtractorItemType(ExtractorItemType.SAMPLE_LIST_TYPE)
                .setItemName(GobiiSampleListType[gobiiSampleListType])
                .setItemId(GobiiSampleListType[gobiiSampleListType]))
            .subscribe(null,
                he => {
                    this.onHeaderStatusMessage.emit(he)
                });

    }



    ngOnInit():any {
        return null;
    }


    ngOnChanges(changes: {[propName: string]: SimpleChange}) {

        this.submitSampleListTypeToService(GobiiSampleListType.GERMPLASM_NAME);

        let scope$ = this;
        this._fileModelTreeService
            .fileItemNotifications()
            .subscribe(fileItem => {
                if (fileItem.getProcessType() === ProcessType.NOTIFY
                    && fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY) {

                    scope$.submitSampleListTypeToService(GobiiSampleListType.GERMPLASM_NAME);


                }
            });

    }
}
