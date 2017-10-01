import {Component, DoCheck, EventEmitter, KeyValueDiffers, OnChanges, OnInit, SimpleChange} from "@angular/core";
import {NameId} from "../model/name-id";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {NameIdService} from "../services/core/name-id-service";
import {FileItemParams} from "../model/name-id-request-params";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import * as fileAction from '../store/actions/fileitem-action';
import {Observable} from "rxjs/Observable";
import {FileItemService} from "../services/core/file-item-service";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";


@Component({
    selector: 'name-id-list-box',
    inputs: ['fileItems$'],
    outputs: ['onNameIdSelected', 'onError'],
    template: `<select (change)="handleFileItemSelected($event)">
        <option *ngFor="let fileItem of fileItems$ | async"
                [value]="fileItem.getFileItemUniqueId()"
                [selected]="fileItem.getSelected()">{{fileItem.getItemName()}}
        </option>
    </select>
    ` // end template

})


export class NameIdListBoxComponent implements OnInit, OnChanges {


    // keep all of these until we change the interface
    public fileItems$: Observable<GobiiFileItem[]>;
    public onNameIdSelected: EventEmitter<NameId> = new EventEmitter();
    public onError: EventEmitter<HeaderStatusMessage> = new EventEmitter();

    //private uniqueId:string;

    differ: any;


    constructor(private store: Store<fromRoot.State>,
                private differs: KeyValueDiffers) {


        this.differ = differs.find({}).create(null);

    } // ctor


    ngOnInit(): any {

     //   this.fileItems$.subscribe( items => console.log("Items count: " + items.length));

    }

    private handleHeaderStatus(headerStatusMessage: HeaderStatusMessage) {

        this.onError.emit(headerStatusMessage);
    }


    previousSelectedItemId: string = null;

    public handleFileItemSelected(arg) {

        let currentFileItemUniqueId: string = arg.currentTarget.value;

        this.store.select(fromRoot.getAllFileItems)
            .subscribe(all => {
                let selectedFileItem: GobiiFileItem = all.find(fi => fi.getFileItemUniqueId() === currentFileItemUniqueId);
                if (( selectedFileItem.getProcessType() !== ProcessType.DUMMY )
                    && (selectedFileItem.getExtractorItemType() !== ExtractorItemType.LABEL)) {

                    this.previousSelectedItemId = currentFileItemUniqueId;
                    this.store.dispatch(new fileAction.AddToExtractAction(selectedFileItem));

                    this.onNameIdSelected
                        .emit(new NameId(selectedFileItem.getItemId(),
                            selectedFileItem.getItemName(),
                            selectedFileItem.getEntityType()));

                } else {
                    if (this.previousSelectedItemId) {

                        let previousItem: GobiiFileItem = all.find(fi => fi.getFileItemUniqueId() === this.previousSelectedItemId);
                        this.store.dispatch(new fileAction.RemoveFromExtractAction(previousItem));

                        this.onNameIdSelected
                            .emit(new NameId(previousItem.getItemId(),
                                previousItem.getItemName(),
                                previousItem.getEntityType()));
                    }
                }
            }).unsubscribe(); //unsubscribe or else this subscribe() keeps the state collection locked and the app freezes really badly

    }


    ngOnChanges(changes: { [propName: string]: SimpleChange }) {

    } // ngonChanges


} // class
