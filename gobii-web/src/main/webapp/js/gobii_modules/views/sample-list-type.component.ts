import {Component, OnInit, EventEmitter, OnChanges, SimpleChanges, SimpleChange} from '@angular/core';
import {EntityFilter} from "../model/type-entity-filter";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {GobiiSampleListType} from "../model/type-extractor-sample-list";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";
import {Observable} from "rxjs/Observable";
import * as fromRoot from '../store/reducers';
import * as historyAction from '../store/actions/history-action';
import * as treeAction from '../store/actions/treenode-action';
import {Store} from "@ngrx/store";
import {FileItemService} from "../services/core/file-item-service";
import {TreeStructureService} from "../services/core/tree-structure-service";


@Component({
    selector: 'sample-list-type',
    inputs: ['gobiiExtractFilterType'],
    outputs: ['onHeaderStatusMessage'],
    template: `
        <form>
            <label class="the-legend">List Item Type:&nbsp;</label>
            <BR><BR><input type="radio"
                       (ngModelChange)="handleSampleTypeSelected($event)"
                       [ngModel]="(sampleListType$ | async).getItemId()"
                       name="listType"
                       value="GERMPLASM_NAME">
            <label for="GERMPLASM_NAME" class="the-legend">Germplasm Name</label>
            <BR><BR><input type="radio"
                       (ngModelChange)="handleSampleTypeSelected($event)"
                       [ngModel]="(sampleListType$ | async).getItemId()"
                       name="listType"
                       value="EXTERNAL_CODE">
            <label for="EXTERNAL_CODE" class="the-legend">External Code</label>
            <BR><BR><input type="radio"
                       (ngModelChange)="handleSampleTypeSelected($event)"
                       [ngModel]="(sampleListType$ | async).getItemId()"
                       name="listType"
                       value="DNA_SAMPLE">
            <label for="DNA_SAMPLE" class="the-legend">DNA Sample</label>
        </form>` // end template
})

export class SampleListTypeComponent implements OnInit, OnChanges {

    constructor(private store: Store<fromRoot.State>,
                private fileItemService: FileItemService) {
    } // ctor

    private onHeaderStatusMessage: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    public sampleListType$: Observable<GobiiFileItem> = this.store.select(fromRoot.getSelectedSampleType);

    public handleSampleTypeSelected(radioValue: string) {

        let gobiiSampleListType: GobiiSampleListType = GobiiSampleListType[radioValue];

        this.fileItemService
            .loadFileItem(GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                    .setExtractorItemType(ExtractorItemType.SAMPLE_LIST_TYPE)
                    .setItemName(GobiiSampleListType[gobiiSampleListType])
                    .setItemId(GobiiSampleListType[gobiiSampleListType]),
                true);

    }


    ngOnInit(): any {
    }


    ngOnChanges(changes: { [propName: string]: SimpleChange }) {
    }
}
