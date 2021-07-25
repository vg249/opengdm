package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import lombok.Data;

@Data
public class FileHeader {
   private int headerLineNumber;
   private String[] headers;

   public int getHeaderColumnIndex(String headerColumnToIndex) {
      if(headers == null || headers.length == 0) {
         return -1;
      }
      for(int i = 0; i < headers.length; i++) {
         if (headers[i].equals(headerColumnToIndex)) {
            return i;
         }
      }

      return -1;
   }
}
