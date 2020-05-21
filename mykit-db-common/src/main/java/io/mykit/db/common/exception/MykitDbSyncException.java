/**
 * Copyright 2020-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mykit.db.common.exception;

/**
 * @author binghe
 * @version 1.0.0
 * @description 自定义异常
 */
public class MykitDbSyncException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 异常状态码
     */
    private Integer errorCode;

    /**
     * 异常信息
     */
    private String errorMessage;


    public MykitDbSyncException(Integer errorCode) {
        super("errorCode: " + errorCode);
        this.errorCode = errorCode;
    }

    public MykitDbSyncException(String errorMessage) {
        super("errorMessage: " + errorMessage);
        this.errorMessage = errorMessage;
    }

    public MykitDbSyncException(Integer errorCode, String errorMessage) {
        super("errorCode: " + errorCode + ", errorMessage: " + errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
