export class FileName {

    public static makeUniqueFileId(): string {

        let date: Date = new Date();
        let returnVal: string = date.getFullYear()
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