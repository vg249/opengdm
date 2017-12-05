import {FilterType} from "./filter-type";
import {EntityType, EntitySubType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {NameIdLabelType} from "./name-id-label-type";
import {ExtractorItemType} from "./type-extractor-item";
import {GobiiFileItemCompoundId} from "./gobii-file-item-compound-id";
import {GobiiFileItem} from "./gobii-file-item";
import * as fileAction from '../store/actions/fileitem-action';

/**
 * Created by Phil on 3/9/2017.
 */

/***
 * This class is used extensively for the purpose of retrieving and
 * managing the results of queries to the GOBII /names/{entity} service. In this
 * case, in the NameIdService's get() method, values from this class
 * are used to set up the GET request to the /names/{entityResource}. Of particular
 * note is the use fo the _fkEntityFilterValue value for the purpose of retrieving
 * names for a given entity when that entity must be filtered according to a foreign key.
 * For example, when retrieving projects by contact_id (i.e., by principle investigator contact
 * id), the _fkEntityFilterValue will be the value of the PI according to which the project names
 * should be filtered.
 *
 * This class also has more general application for retrieving whole entities (e.g., dataset records.
 *
 * The _parentFileItemParams and _childFileItemParams can be used to create a tree of instances
 * of this class that can be used for hierarchical filtering. That is to say, the parent/child
 * relationships of FilterParams instances corresponds to the primary/foreign key relationships of the
 * tables involved in generating the query. In our example, the PROJECTS_BY_CONTACT FileFilterParams is a
 * child of the CONTACT_PI FileFilterParams. The PROJECTS_BY_CONTACT query will be run along with a
 * contactId value, which will serve to filter the results of the project query. That contactId value
 * will now be the _fkEntityFilterValue of the PROJECTS_BY_CONTACT FilterValues. Moreover, each
 * GobiiFileItem resulting from the PROJECTS_BY_CONTACT query will be assigned the contactId as its
 * parentItemId value. Thus, once the GobiiFileItems have been retrieved from the server, they can
 * subsequently be retrieved from the store such that the GobiiFileItems of EntityType PROJECT are
 * filtered as follows: the current _fkEntityFilterValue of the PROJECTS_BY_CONTACT filter matches
 * the parentItemId of the GobiiFileItems of EntityType PROJECT. Thus, the PROJECTS_BY_CONTACT filter,
 * with an arbitrary _fkEntityFilterValue, can be dispatched to the store at any time and thereby change
 * the set of GobiiFileItems that are filtered in this way. In other words, when we want to get the
 * "currently selected" projects from the store (i.e., the projects filtered for
 * the pi who is currently selected in the UI), the selector returns the file items whose parent id
 * matches current contact ID in state.
 *
 */
export class FilterParams extends GobiiFileItemCompoundId {

    private constructor(_entityType: EntityType = EntityType.UNKNOWN, //first four args are passed to base class ctor
                        _entitySubType: EntitySubType = EntitySubType.UNKNOWN,
                        _cvFilterType: CvFilterType = CvFilterType.UNKNOWN,
                        _cvFilterValue: string = null,
                        _extractorItemType: ExtractorItemType,
                        private _queryName: string = null,
                        private _filterType: FilterType = FilterType.NONE,
                        private _fkEntityFilterValue: string = null,
                        private _gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN,
                        private _nameIdLabelType: NameIdLabelType,
                        private _parentFileItemParams: FilterParams,
                        private _childFileItemParams: FilterParams[],
                        private _isDynamicFilterValue: boolean,
                        private onLoadFilteredItemsAction: (fileItems: GobiiFileItem[], filterValue:string) => any) {

        super(_extractorItemType, _entityType, _entitySubType, _cvFilterType, _cvFilterValue);


    }

    public static build(queryName: string,
                        gobiiExtractFilterType: GobiiExtractFilterType,
                        entityType: EntityType): FilterParams {
        return ( new FilterParams(
            entityType,
            EntitySubType.UNKNOWN,
            CvFilterType.UNKNOWN,
            null,
            ExtractorItemType.ENTITY,
            queryName,
            FilterType.NONE,
            null,
            gobiiExtractFilterType,
            NameIdLabelType.UNKNOWN,
            null,
            [],
            true,
            null));
    }

    getQueryName(): string {
        return this._queryName;
    }


    getExtractorItemType(): ExtractorItemType {
        return super.getExtractorItemType();
    }

    setExtractorItemType(value: ExtractorItemType): FilterParams {

        super.setExtractorItemType(value);
        return this;
    }

    getEntityType(): EntityType {
        return super.getEntityType();
    }

    setEntityType(value: EntityType): FilterParams {

        super.setEntityType(value);
        return this;
    }

    getEntitySubType(): EntitySubType {
        return super.getEntitySubType();
    }

    setEntitySubType(value: EntitySubType): FilterParams {

        super.setEntitySubType(value);
        return this;
    }

    getCvFilterType(): CvFilterType {
        return super.getCvFilterType();
    }

    setCvFilterType(value: CvFilterType): FilterParams {
        super.setCvFilterType(value);
        return this;
    }

    getCvFilterValue(): string {
        return super.getCvFilterValue();
    }

    setCvFilterValue(value: string) {

        super.setCvFilterValue(value);
        return this;
    }


    getFilterType(): FilterType {
        return this._filterType;
    }

    setFilterType(value: FilterType): FilterParams {
        this._filterType = value;
        return this;
    }

    getFkEntityFilterValue(): string {
        return this._fkEntityFilterValue;
    }

    setFkEntityFilterValue(value: string): FilterParams {
        this._fkEntityFilterValue = value;
        return this;
    }

    getGobiiExtractFilterType(): GobiiExtractFilterType {
        return this._gobiiExtractFilterType;
    }

    setGobiiExtractFilterType(value: GobiiExtractFilterType): FilterParams {
        this._gobiiExtractFilterType = value;
        return this;
    }


    setNameIdLabelType(nameIdLabelType: NameIdLabelType) {
        this._nameIdLabelType = nameIdLabelType;
        return this;
    }

    getMameIdLabelType(): NameIdLabelType {
        return this._nameIdLabelType;
    }

    setParentFileItemParams(fileItemParams: FilterParams): FilterParams {
        this._parentFileItemParams = fileItemParams;
        return this;
    }

    getChildFileItemParams(): FilterParams[] {
        return this._childFileItemParams;
    }

    setChildNameIdRequestParams(childNameIdRequestParams: FilterParams[]): FilterParams {
        this._childFileItemParams = childNameIdRequestParams;
        return this;
    }

    setIsDynamicFilterValue(dynamicFilterValue: boolean): FilterParams {
        this._isDynamicFilterValue = dynamicFilterValue;
        return this;
    }

    getIsDynamicFilterValue(): boolean {
        return this._isDynamicFilterValue;
    }

    setOnLoadFilteredItemsAction(initializeTransform: (fileItems: GobiiFileItem[], filterValue:string) => any) {
        this.onLoadFilteredItemsAction = initializeTransform;
        return this;
    }

    getOnLoadFilteredItemsAction(): (fileItems: GobiiFileItem[], filterValue:string) => any {
        return this.onLoadFilteredItemsAction;
    }

}