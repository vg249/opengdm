import {EntitySubType, EntityType} from "./type-entity";
import {VertexType} from "./type-vertex";
import {CvGroup} from "./cv-group";
import {VertexNameType} from "./type-vertex-name";

export class Vertex {

    constructor(public vertexId:number,
                public vertexNameType:VertexNameType,
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