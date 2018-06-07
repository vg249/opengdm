import {EntitySubType, EntityType} from "./type-entity";
import {VertexType} from "./type-vertex";
import {CvGroup} from "./cv-group";

export class Vertex {

    constructor(public vertexId:number,
                public gobiiVertexType:VertexType,
                public vertexName:string,
                public entityType:EntityType,
                public entitySubType:EntitySubType,
                public cvGroup:CvGroup,
                public cvTerm:string,
                public filterVals:string[]
                ) {
    }
}