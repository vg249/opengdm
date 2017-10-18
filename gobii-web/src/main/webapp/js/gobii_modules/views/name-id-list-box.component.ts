import {Component} from "@angular/core";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import * as fileAction from '../store/actions/fileitem-action';
import * as historyAction from '../store/actions/history-action';
import {Observable} from "rxjs/Observable";


@Component({
    selector: 'name-id-list-box',
    inputs: ['fileItems$','gobiiExtractFilterType'],
    outputs: [],
    template: `<select (change)="handleFileItemSelected($event)">
        <option *ngFor="let fileItem of fileItems$ | async"
                [value]="fileItem.getFileItemUniqueId()"
                [selected]="fileItem.getSelected()">{{fileItem.getItemName()}}
        </option>
    </select>
    ` // end template

})


export class NameIdListBoxComponent  {


    public fileItems$: Observable<GobiiFileItem[]>;

    private gobiiExtractFilterType: GobiiExtractFilterType;

    constructor(private store: Store<fromRoot.State>) {


    } // ctor


    ngOnInit(): any {

        this
            .fileItems$
            .subscribe(items => {
                    if (this.previousSelectedItemId === null && items && items.length > 0) {
                        this.previousSelectedItemId = items[0].getFileItemUniqueId()
                    }
                },
                error => {
                    this.store.dispatch(new historyAction.AddStatusMessageAction(error))
                });

    }

    previousSelectedItemId: string = null;

    public handleFileItemSelected(arg) {

        if( ! this.gobiiExtractFilterType ) {
            this.store.dispatch(new historyAction.AddStatusMessageAction("The gobiiExtractFilterType property is not set"))
        }


        let newFileItemUniqueId: string = arg.currentTarget.value;
        let previousFileItemUniqueId: string = this.previousSelectedItemId;

        this.store.dispatch(new fileAction.ReplaceInExtractByItemIdAction({
            gobiiExtractFilterType: this.gobiiExtractFilterType,
            itemIdCurrentlyInExtract: previousFileItemUniqueId,
            itemIdToReplaceItWith: newFileItemUniqueId
        }));

        this.previousSelectedItemId = newFileItemUniqueId;

    }


} // class
