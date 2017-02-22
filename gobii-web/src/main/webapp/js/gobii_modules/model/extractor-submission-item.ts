

import {EntityType} from "./type-entity";
enum ItemType {Category, Entity}
enum CategoryType {Container,MarkerList,SampleList}


export class ExtractorSubmissionItem {

    constructor(public itemType:ItemType,
                public categoryType:CategoryType,
                public entityType:EntityType,
                public childEntityTypes:EntityType[],
                public childCattegoryTypes:CategoryType[]) {
        }
}   