package com.tencent.dingdang.tvs.message.request.system;

import com.tencent.dingdang.tvs.exception.DirectiveHandlingException.ExceptionType;
import com.tencent.dingdang.tvs.message.Payload;

public class ExceptionEncounteredPayload extends Payload {

    private String unparsedDirective;
    private ErrorStructure error;

    public ExceptionEncounteredPayload(String unparsedDirective, ExceptionType type, String message) {
        this.unparsedDirective = unparsedDirective;
        error = new ErrorStructure(type, message);
    }

    public String getUnparsedDirective() {
        return unparsedDirective;
    }

    public ErrorStructure getError() {
        return error;
    }

    private static class ErrorStructure {
        private ExceptionType type;
        private String message;

        public ErrorStructure(ExceptionType type, String message) {
            this.type = type;
            this.message = message;
        }

        public ExceptionType getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }
    }
}
