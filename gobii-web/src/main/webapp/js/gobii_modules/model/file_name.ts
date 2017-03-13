export class FileName {

    public static makeUnique(): string {

        let date: Date = new Date();
        let returnVal: string = "extractor_"
            + date.getFullYear()
            + "_"
            + (date.getMonth() + 1)
            + "_"
            + date.getDay()
            + "_"
            + date.getHours()
            + "_"
            + date.getMinutes()
            + "_"
            + date.getSeconds();


        return returnVal;

    };
}   