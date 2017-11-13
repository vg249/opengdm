import {OnInit} from "@angular/core";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import * as historyAction from '../store/actions/history-action';

export class DatasetDatatable implements OnInit {

    //cars: Car[];

    constructor(private store: Store<fromRoot.State>,) {
    }

    ngOnInit() {
        //this.carService.getCarsSmall().then(cars => this.cars = cars);
    }
}