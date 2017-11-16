import {Injectable} from "@angular/core";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvFilters, CvFilterType} from "../../model/cv-filter-type";
import {FilterParams} from "../../model/file-item-params";
import * as historyAction from '../../store/actions/history-action';
import * as fromRoot from '../../store/reducers';
import {Store} from "@ngrx/store";
import {NameIdLabelType} from "../../model/name-id-label-type";
import {FilterType} from "../../model/filter-type";
import {FilterParamNames} from "../../model/file-item-param-names";
import "rxjs/add/operator/expand"

@Injectable()
export class FilterParamsColl {

    filterParams: FilterParams[] = [];

    private addFilter(filterParamsToAdd: FilterParams) {

        let existingFilterParams = this.filterParams
            .find(ffp =>
                ffp.getQueryName() === filterParamsToAdd.getQueryName()
                && ffp.getGobiiExtractFilterType() === filterParamsToAdd.getGobiiExtractFilterType()
            );

        if (!existingFilterParams) {
            this.filterParams.push(filterParamsToAdd);
        } else {
            this.store.dispatch(new historyAction.AddStatusMessageAction("The query "
                + filterParamsToAdd.getQueryName()
                + " because there is already a filter by that name for this extract type "
                + GobiiExtractFilterType[filterParamsToAdd.getQueryName()]));

        }
    }

    public getFilter(nameIdFilterParamTypes: FilterParamNames, gobiiExtractFilterType: GobiiExtractFilterType): FilterParams {

        return this.filterParams.find(ffp =>
            ffp.getQueryName() === nameIdFilterParamTypes &&
            ffp.getGobiiExtractFilterType() === gobiiExtractFilterType
        )
    }

    constructor(private store: Store<fromRoot.State>) {

        // For non-hierarchically filtered request params, we just create them simply
        // as we add them to the flat map
        this.addFilter(
            FilterParams
                .build(FilterParamNames.CV_DATATYPE,
                    GobiiExtractFilterType.BY_SAMPLE,
                    EntityType.CV)
                .setIsDynamicFilterValue(false)
                .setCvFilterType(CvFilterType.DATASET_TYPE)
                .setFilterType(FilterType.NAMES_BY_TYPE_NAME)
                .setFkEntityFilterValue(CvFilters.get(CvFilterType.DATASET_TYPE))
                .setNameIdLabelType(NameIdLabelType.SELECT_A)
        );


        this.addFilter(
            FilterParams
                .build(FilterParamNames.CV_DATATYPE,
                    GobiiExtractFilterType.BY_MARKER,
                    EntityType.CV)
                .setIsDynamicFilterValue(false)
                .setCvFilterType(CvFilterType.DATASET_TYPE)
                .setFilterType(FilterType.NAMES_BY_TYPE_NAME)
                .setFkEntityFilterValue(CvFilters.get(CvFilterType.DATASET_TYPE))
                .setNameIdLabelType(NameIdLabelType.SELECT_A)
        );

        this.addFilter(
            FilterParams
                .build(FilterParamNames.CV_JOB_STATUS,
                    GobiiExtractFilterType.WHOLE_DATASET,
                    EntityType.CV)
                .setIsDynamicFilterValue(false)
                .setCvFilterType(CvFilterType.JOB_STATUS)
                .setFilterType(FilterType.NAMES_BY_TYPE_NAME)
                .setFkEntityFilterValue(CvFilters.get(CvFilterType.JOB_STATUS))
                .setNameIdLabelType(NameIdLabelType.ALL)
        );

        this.addFilter(
            FilterParams
                .build(FilterParamNames.MAPSETS,
                    GobiiExtractFilterType.WHOLE_DATASET,
                    EntityType.MAPSET)
                .setIsDynamicFilterValue(false)
                .setNameIdLabelType(NameIdLabelType.NO));

        this.addFilter(
            FilterParams
                .build(FilterParamNames.MAPSETS,
                    GobiiExtractFilterType.BY_SAMPLE,
                    EntityType.MAPSET)
                .setIsDynamicFilterValue(false)
                .setNameIdLabelType(NameIdLabelType.NO));

        this.addFilter(
            FilterParams
                .build(FilterParamNames.MAPSETS,
                    GobiiExtractFilterType.BY_MARKER,
                    EntityType.MAPSET)
                .setIsDynamicFilterValue(false)
                .setNameIdLabelType(NameIdLabelType.NO));

        this.addFilter(
            FilterParams
                .build(FilterParamNames.PLATFORMS,
                    GobiiExtractFilterType.BY_SAMPLE,
                    EntityType.PLATFORM)
                .setIsDynamicFilterValue(false)
        );

        this.addFilter(
            FilterParams
                .build(FilterParamNames.PLATFORMS,
                    GobiiExtractFilterType.BY_MARKER,
                    EntityType.PLATFORM)
                .setIsDynamicFilterValue(false)
        );

        this.addFilter(
            FilterParams
                .build(FilterParamNames.MARKER_GROUPS,
                    GobiiExtractFilterType.BY_MARKER,
                    EntityType.MARKER_GROUP)
                .setIsDynamicFilterValue(false)
        );

        this.addFilter(
            FilterParams
                .build(FilterParamNames.PROJECTS,
                    GobiiExtractFilterType.BY_SAMPLE,
                    EntityType.PROJECT)
                .setIsDynamicFilterValue(false)
                .setNameIdLabelType(NameIdLabelType.ALL));

        this.addFilter(
            FilterParams
                .build(FilterParamNames.CONTACT_PI,
                    GobiiExtractFilterType.BY_SAMPLE,
                    EntityType.CONTACT)
                .setIsDynamicFilterValue(false)
                .setEntitySubType(EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR));


        this.addFilter(FilterParams
            .build(FilterParamNames.DATASETS,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.DATASET)
            .setFilterType(FilterType.ENTITY_LIST));


        //for hierarchical items, we need to crate the nameid requests separately from the
        //flat map: they _will_ need to be in the flat map; however, they all need to be
        //useed to set up the filtering hierarchy
        let nameIdRequestParamsContactsPi: FilterParams = FilterParams
            .build(FilterParamNames.CONTACT_PI,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.CONTACT)
            .setIsDynamicFilterValue(true)
            .setEntitySubType(EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR);


        let nameIdRequestParamsProjectByPiContact: FilterParams = FilterParams
            .build(FilterParamNames.PROJECTS_BY_CONTACT,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.PROJECT)
            .setIsDynamicFilterValue(true)
            .setFilterType(FilterType.NAMES_BY_TYPEID);

        let nameIdRequestParamsExperiments: FilterParams = FilterParams
            .build(FilterParamNames.EXPERIMENTS_BY_PROJECT,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.EXPERIMENT)
            .setIsDynamicFilterValue(true)
            .setFilterType(FilterType.NAMES_BY_TYPEID);

        let nameIdRequestParamsDatasets: FilterParams = FilterParams
            .build(FilterParamNames.DATASETS_BY_EXPERIMENT,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.DATASET)
            .setIsDynamicFilterValue(true)
            .setFilterType(FilterType.NAMES_BY_TYPEID);

        //add the individual requests to the map
        this.addFilter(nameIdRequestParamsContactsPi);
        this.addFilter(nameIdRequestParamsProjectByPiContact);
        this.addFilter(nameIdRequestParamsExperiments);
        this.addFilter(nameIdRequestParamsDatasets);

        //build the parent-child request params graph
        nameIdRequestParamsContactsPi
            .setChildNameIdRequestParams(
                [nameIdRequestParamsProjectByPiContact
                    .setParentFileItemParams(nameIdRequestParamsContactsPi)
                    .setChildNameIdRequestParams([nameIdRequestParamsExperiments
                        .setParentFileItemParams(nameIdRequestParamsProjectByPiContact)
                        .setChildNameIdRequestParams([nameIdRequestParamsDatasets
                            .setParentFileItemParams(nameIdRequestParamsExperiments)
                        ])
                    ])
                ]);

    } // constructor
}
