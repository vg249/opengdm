import {NameId} from "./name-id";
import {Vertex} from "./vertex";

export class VertexFilterDTO {

    constructor(public destinationVertex: Vertex,
                public vertices: Vertex[],
                public vertexValues: NameId[],
                public markerCount: number,
                public sampleCount: number) {
    }

    public static fromJson(json:any):VertexFilterDTO {

        let returnVal: VertexFilterDTO = new VertexFilterDTO(
            json.destinationVertexDTO,
            json.vertices,
            json.vertexValues,
            json.markerCount,
            json.sampleCount
        );

        return returnVal;
    }

    public getJson(): any {

        let returnVal: any = {};

        returnVal.destinationVertexDTO = this.destinationVertex;
        returnVal.vertices = this.vertices;
        returnVal.vertexValues = this.vertexValues;
        returnVal.markerCount = this.markerCount;
        returnVal.sampleCount = this.sampleCount;

        return returnVal;
    }

}