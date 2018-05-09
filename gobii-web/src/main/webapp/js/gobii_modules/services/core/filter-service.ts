import {Injectable} from "@angular/core";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {NameIdService} from "./name-id-service";
import {FilterParams} from "../../model/filter-params";
import * as historyAction from '../../store/actions/history-action';
import * as fileItemActions from '../../store/actions/fileitem-action'
import * as fromRoot from '../../store/reducers';

import {Store} from "@ngrx/store";
import {FilterParamNames} from "../../model/file-item-param-names";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/expand"
import "rxjs/add/operator/concat"
import {EntityStats} from "../../model/entity-stats";
import {DtoRequestService} from "./dto-request.service";
import {FilterParamsColl} from "./filter-params-coll";
import {PayloadFilter} from "../../store/actions/action-payload-filter";

@Injectable()
export class FilterService {

    constructor(private store: Store<fromRoot.State>,
                private filterParamsColl: FilterParamsColl) {

    } // constructor

    public loadFilter(gobiiExtractFilterType: GobiiExtractFilterType, filterParamsName: FilterParamNames, filterValue: string) {

        let filterParams: FilterParams = this.filterParamsColl.getFilter(filterParamsName, gobiiExtractFilterType);

        if (filterParams) {

            let loadAction: fileItemActions.LoadFilterAction = new fileItemActions.LoadFilterAction(
                {
                    filterId: filterParams.getQueryName(),
                    filter: new PayloadFilter(
                        gobiiExtractFilterType,
                        filterParams.getTargetEtityUniqueId(),
                        filterParams.getRelatedEntityUniqueId(),
                        filterValue,
                        null,
                        null,
                        null
                    )
                }
            );

            this.store.dispatch(loadAction)

        } else {
            this.store.dispatch(new historyAction.AddStatusMessageAction("Error loading filter: there is no query params object for query "
                + filterParamsName
                + " with extract filter type "
                + GobiiExtractFilterType[gobiiExtractFilterType]));
        }
    }

    public getForFilter(filterParamName: FilterParamNames): Observable<GobiiFileItem[]> {

        //Wrapping an Observable around the select functions just doesn't work. So at leaset this
        //function can encapsualte getting the correct selector for the filter
        let returnVal: Observable<GobiiFileItem[]>;

        switch (filterParamName) {

            case FilterParamNames.MARKER_GROUPS:
                returnVal = this.store.select(fromRoot.getMarkerGroups);
                break;

            case FilterParamNames.PROJECTS:
                returnVal = this.store.select(fromRoot.getProjects);
                break;

            case FilterParamNames.PROJECTS_BY_CONTACT:
                returnVal = this.store.select(fromRoot.getProjectsByPI);
                break;

            case FilterParamNames.EXPERIMENTS_BY_PROJECT:
                returnVal = this.store.select(fromRoot.getExperimentsByProject);
                break;

            case FilterParamNames.EXPERIMENTS:
                returnVal = this.store.select(fromRoot.getExperiments);
                break;

            case FilterParamNames.DATASETS_BY_EXPERIMENT:
                returnVal = this.store.select(fromRoot.getDatasetsByExperiment);
                break;

            case FilterParamNames.PLATFORMS:
                returnVal = this.store.select(fromRoot.getPlatforms);
                break;

            case FilterParamNames.CV_DATATYPE:
                returnVal = this.store.select(fromRoot.getCvTermsDataType);
                break;

            case FilterParamNames.CV_JOB_STATUS:
                returnVal = this.store.select(fromRoot.getCvTermsJobStatus);
                break;

            case FilterParamNames.MAPSETS:
                returnVal = this.store.select(fromRoot.getMapsets);
                break;

            case FilterParamNames.CONTACT_PI_HIERARCHY_ROOT:
                returnVal = this.store.select(fromRoot.getPiContacts);
                break;

            case FilterParamNames.CONTACT_PI_FILTER_OPTIONAL:
                returnVal = this.store.select(fromRoot.getPiContactsFilterOptional);
                break;

            case FilterParamNames.PROJECT_FILTER_OPTIONAL:
                returnVal = this.store.select(fromRoot.getProjectsFilterOptional);
                break;

            case FilterParamNames.EXPERIMENT_FILTER_OPTIONAL:
                returnVal = this.store.select(fromRoot.getExperimentsFilterOptional);
                break;

            //***************************
            // the FQ filters are for now just placeholders

            //------- F1 --------------------------------------
            case FilterParamNames.FQ_F1_VERTICES:
                returnVal = this.store.select(fromRoot.getFqF1Vertices);
                break;

            case FilterParamNames.FQ_F1_VERTEX_VALUES:
                returnVal = this.store.select(fromRoot.getPlatforms);
                break;

            //------- F2 --------------------------------------
            case FilterParamNames.FQ_F2_VERTICES:
                returnVal = this.store.select(fromRoot.getFqF2Vertices);
                break;

            case FilterParamNames.FQ_F2_VERTEX_VALUES:
                returnVal = this.store.select(fromRoot.getPlatforms);
                break;

            //------- F3 --------------------------------------
            case FilterParamNames.FQ_F3_VERTICES:
                returnVal = this.store.select(fromRoot.getFqF3Vertices);
                break;

            case FilterParamNames.FQ_F3_VERTEX_VALUES:
                returnVal = this.store.select(fromRoot.getPlatforms);
                break;

            //------- F4 --------------------------------------
            case FilterParamNames.FQ_F4_VERTICES:
                returnVal = this.store.select(fromRoot.getFqF4Vertices);
                break;

            case FilterParamNames.FQ_F4_VERTEX_VALUES:
                returnVal = this.store.select(fromRoot.getPlatforms);
                break;

            default:
                this.store.dispatch(new historyAction.AddStatusMessageAction("There is no selector for filter "
                    + filterParamName));
                break;

        }

        return returnVal;

    }

}
