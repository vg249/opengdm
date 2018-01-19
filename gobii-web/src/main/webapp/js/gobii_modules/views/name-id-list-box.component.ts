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
                [selected]="fileItem.getSelected()">{{fileItem.getItemName()}}
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

                    /**
                     * The following two line are annoying. The problem is that when the results of this filter changes,
                     * the default selected item in the drop-down does not change. Say you selected PI name from the list.
                     * So then projects are filtered as you expect. Then you go back to PI and select "All Principle Investigators."
                     * The project list expands to contain all the projects, but the selected one in the list box
                     * is the one that had been at the top of the list when it was filtered. I've checked and there are no
                     * other items in the list for which selected == true. Setting selected to true here
                     * does solve the problem. But it's a bargain with the devil: technically, we are modifying state.
                     * And state should never be modified outside of a reducer. However, the selected property is only
                     * ever used for display. So it is sort of a gray area. For example, it's not used to determine the
                     * content of the instruction file. And any other solution I can think of at the moment would be
                     complex, difficult to manage, and more prone to errors. So this is the solution for now.
                     */
                    if( items[0] ) {
                        items[0].setSelected(true);
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
            filterParamName: this.filterParamName,
            gobiiExtractFilterType: this.gobiiExtractFilterType,
            itemIdCurrentlyInExtract: previousFileItemUniqueId,
            itemIdToReplaceItWith: newFileItemUniqueId
        }));

        this.previousSelectedItemId = newFileItemUniqueId;

    }


} // class
