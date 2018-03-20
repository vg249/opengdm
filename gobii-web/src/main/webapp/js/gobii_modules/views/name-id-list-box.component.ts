import {Component} from "@angular/core";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import * as fileAction from '../store/actions/fileitem-action';
import * as historyAction from '../store/actions/history-action';
import {Observable} from "rxjs/Observable";
import {FilterParamNames} from "../model/file-item-param-names";
import {FileItemService} from "../services/core/file-item-service";


@Component({
    selector: 'name-id-list-box',
    inputs: ['gobiiExtractFilterType','filterParamName'],
    outputs: [],
    template: `<select (change)="handleFileItemSelected($event)">
        <option *ngFor="let fileItem of fileItems$ | async"
                [value]="fileItem.getFileItemUniqueId()"
                [selected]="fileItem.getSelected()"
                title="{{fileItem.getItemName()}}">
              {{fileItem.getItemName().length < 30 ? fileItem.getItemName().substr(0,29) : fileItem.getItemName().substr(0,24).concat(" . . .")}}
        </option>
    </select>
    ` // end template

})
export class NameIdListBoxComponent  {


    public fileItems$: Observable<GobiiFileItem[]>;

    private gobiiExtractFilterType: GobiiExtractFilterType;

    private filterParamName:FilterParamNames;
    constructor(private store: Store<fromRoot.State>,
                private fileItemService:FileItemService) {


    } // ctor


    ngOnInit(): any {

        this.fileItems$ = this.fileItemService.getForFilter(this.filterParamName)

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

        this.store.dispatch(new fileAction.ReplaceByItemIdAction({
            filterParamName: this.filterParamName,
            gobiiExtractFilterType: this.gobiiExtractFilterType,
            itemIdCurrentlyInExtract: previousFileItemUniqueId,
            itemIdToReplaceItWith: newFileItemUniqueId
        }));

        this.previousSelectedItemId = newFileItemUniqueId;

    }


} // class
