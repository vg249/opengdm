package org.gobiiproject.gobiimodel.dto.instructions.loader;

public class InstructionFileProcessingResult {

    private boolean success;
    private boolean sendQc;

    public InstructionFileProcessingResult(boolean success, boolean sendQc) {
        this.sendQc = sendQc;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSendQc() {
        return sendQc;
    }

    public void setSendQc(boolean sendQc) {
        this.sendQc = sendQc;
    }
}
