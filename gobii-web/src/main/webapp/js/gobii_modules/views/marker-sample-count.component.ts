import {Component, EventEmitter, OnChanges, SimpleChange} from "@angular/core";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FilterParamNames} from "../model/file-item-param-names";

@Component({
    selector: 'marker-sample-count',
    outputs: [],
    inputs: ['gobiiExtractFilterType'],
    template: `
        <p-panel *ngIf="displayPanel" header="Estimated Marker/Sample Count"
                 [(toggleable)]="displayPanel"
                 [(collapsed)]="panelCollapsed"
                 (onBeforeToggle)="onBeforeToggle($event)"
                 (onAfterToggle)="onAfterToggle($event)">
            <p-progressSpinner *ngIf="displaySpinner" [style]="{width: '50px', height: '50px'}"
                               strokeWidth="8" fill="#EEEEEE"
                               animationDuration="3s"></p-progressSpinner>
            <div *ngIf="displayCounts">
                <p>Total Markers: {{markerCount}}</p>
                <p>Total Samples: {{sampleCount}}</p>
            </div>
        </p-panel>
    `
})
export class MarkerSampleCountComponent implements OnChanges {

    public gobiiExtractFilterType: GobiiExtractFilterType;
    public displayPanel: boolean = false;
    public displayCounts: boolean = false;
    public panelCollapsed: boolean = true;
    public markerCount: string = "0";
    public sampleCount: string = "0";
    public displaySpinner: boolean = true;

    public onBeforeToggle($event) {
    }

    private initCount() {
        this.displayCounts = false;
        this.displaySpinner = true;
    }

    public onAfterToggle($event) {

        if ($event.collapsed) {

            this.initCount();
        } else {
            setTimeout(() => {
                this.displayCounts = true;
                this.displaySpinner = false;
            }, 3000);
        }
    }

    // gobiiExtractType is not set until you get OnChanges
    ngOnChanges(changes: { [propName: string]: SimpleChange }) {

        if (changes['gobiiExtractFilterType']
            && (changes['gobiiExtractFilterType'].currentValue != null)
            && (changes['gobiiExtractFilterType'].currentValue != undefined)) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                if (this.gobiiExtractFilterType === GobiiExtractFilterType.FLEX_QUERY) {

                    this.displayPanel = true;
                    this.panelCollapsed = true;
                    this.initCount();

                } else {
                    this.displayPanel = false;
                }

            } // if we have a new filter type

        } // if filter type changed


    } // ngonChanges


}