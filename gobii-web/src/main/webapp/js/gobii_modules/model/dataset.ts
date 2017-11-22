export class DataSet {

    constructor(public  id:number,
                public  piContactId:number,
                public  projectId:number,
                public  experimentId:number,
                public  name:string,
                public  callingAnalysisId:number,
                public  dataTable:string,
                public  dataFile:string,
                public  qualityTable:string,
                public  qualityFile:string,
                public  status:number,
                public  typeId:number,
                public analysesIds:number[],
                public createdDate:Date,
                public jobStatusId:number,
                public jobStatusName,
                public jobTypeId:number,
                public jobTypeName,
                public jobSubmittedDate:Date) {
    }
}